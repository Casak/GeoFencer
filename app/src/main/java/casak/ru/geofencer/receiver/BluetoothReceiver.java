package casak.ru.geofencer.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import casak.ru.geofencer.BluetoothAntennaLocationSource;
import casak.ru.geofencer.domain.Constants;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.storage.db.Contract;
import casak.ru.geofencer.presentation.presenters.impl.MapPresenter;
import casak.ru.geofencer.presentation.ui.activities.MapActivity;
import casak.ru.geofencer.threading.MainThreadImpl;

public class BluetoothReceiver extends BroadcastReceiver {
    private static final String TAG = BluetoothReceiver.class.getSimpleName();

    private Context context;


    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            Log.d(TAG, "ACTION_FOUND");
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            String deviceHardwareAddress = device.getAddress();
            String deviceName = device.getName();
            Log.d(TAG, "Device name is:" + deviceName);
            if (deviceHardwareAddress.equals(Constants.BLUETOOTH_GPS_ANTENNA_HARDWARE_ADDRESS)) {
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                Log.d(TAG, "Antenna is discovered");
                ConnectThread thread = new ConnectThread(device);
                thread.start();
            }
        }
        //TODO Implement + take action from BluetoothDevice.ACTION_DISAPPEARED
        else if ("android.bluetooth.device.action.DISAPPEARED".equals(action)) {

        }
    }

    private void manageMyConnectedSocket(BluetoothSocket socket) {
        BluetoothDataTransferringThread thread = new BluetoothDataTransferringThread(socket);
        thread.start();
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;

            try {
                tmp = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString(Constants.ANTENNA_UUID));
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            Log.d(TAG, "In ConnectThread.run()");
            boolean connected = false;
            try {
                mmSocket.connect();
                Log.d(TAG, "mmSocket.isConnected() = " + mmSocket.isConnected());
                connected = true;
            } catch (IOException connectException) {
                Log.e(TAG, "Could not connect to the client socket", connectException);
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
            }
            if (connected)
                manageMyConnectedSocket(mmSocket);
        }
    }

    private class BluetoothDataTransferringThread extends Thread {
        private ContentResolver resolver;
        private BluetoothSocket socket;
        private InputStream in;
        private List<Location> lastLocations;

        public BluetoothDataTransferringThread(BluetoothSocket bluetoothSocket) {
            Log.d(TAG, "In BluetoothDataTransferringThread");
            resolver = context.getContentResolver();

            lastLocations = new LinkedList<>();

            if (!bluetoothSocket.isConnected())
                return;
            socket = bluetoothSocket;
            try {
                in = socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[4800];
            int length;
            try {
                Location location = new Location(LocationManager.GPS_PROVIDER);
                location.setLatitude(50.077209d);
                location.setLongitude(30.041981d);
                BluetoothAntennaLocationSource.getListener().onLocationChanged(location);


                MainThread mainThread = new MainThreadImpl();
                MapPresenter presenter = MapActivity.mapPresenter;
                final LocationListener listener;
                LocationListener tmpListener = null;

                if (presenter != null)
                    tmpListener = presenter.getLocationListener();
                if(tmpListener != null)
                    listener = tmpListener;
                else listener = null;

                while ((length = in.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                    Log.d(TAG, "Read from GPS antenna: " + result.toString("UTF-8"));
                    final Location newLocation = parse(result.toString("UTF-8"));

                    if (newLocation != null) {
                        if(tmpListener != null)
                            mainThread.post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onLocationChanged(newLocation);
                                }
                            });
                        BluetoothAntennaLocationSource.getListener().onLocationChanged(newLocation);
                        insertDataToProvider(newLocation, Contract.CoordEntry.CONTENT_URI);
                        if (haveToInsert(location))
                            insertDataToProvider(newLocation, Contract.FilteredCoordEntry.CONTENT_URI);
                    }

                    result.reset();
                    sleep(1000);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        //TODO Move this filter somewhere
        private boolean haveToInsert(Location location) {
            lastLocations.add(location);
            if (lastLocations.size() == 1) {
                return true;
            }

            Location first = lastLocations.get(0);
            if (lastLocations.size() == 2) {
                double bearing = SphericalUtil.computeHeading(
                        convertLocationToLatLng(first),
                        convertLocationToLatLng(location));
                first.setBearing((float) bearing);
                return true;
            }
            if (lastLocations.size() > 2) {
                Location previousPoint = lastLocations.get(lastLocations.size() - 2);

                double bearing = SphericalUtil.computeHeading(
                        convertLocationToLatLng(previousPoint),
                        convertLocationToLatLng(location));
                previousPoint.setBearing((float) bearing);

                if (isBearingDifferent(first.getBearing(), previousPoint.getBearing())) {
                    lastLocations.clear();
                    return true;
                }
            }
            return false;
        }


        private boolean isBearingDifferent(float first, float last) {
            return Math.abs(first - last) > Constants.FILTER_HEADING_DIFFERENCE;
        }

        private LatLng convertLocationToLatLng(Location location) {
            return new LatLng(location.getLatitude(), location.getLongitude());
        }

        private void insertDataToProvider(Location location, Uri contentUri) {
            ContentValues value = new ContentValues();
            value.put(Contract.CoordEntry.COLUMN_LAT, location.getLatitude());
            value.put(Contract.CoordEntry.COLUMN_LNG, location.getLongitude());
            value.put(Contract.CoordEntry.COLUMN_ALT, location.getAltitude());
            resolver.insert(contentUri, value);
        }

        @Nullable
        private Location parse(String data) {
            if (!data.startsWith("$"))
                return null;
            Location result = new Location(LocationManager.GPS_PROVIDER);
            String[] messages = data.split("\r\n");
            for (String line : messages) {
                if (line.startsWith("$GPGSA")) {
                    int startIndex = line.indexOf(",");
                    int endIndex = line.indexOf("*");
//                    String[] locationData = line.substring(startIndex, endIndex).split(",");
                }
                if (line.startsWith("$GPGGA")) {
                    String[] locationData = line.split(",");
                    if (locationData.length > 12) {
                        String time = locationData[1];

                        String tmp = locationData[2];
                        String latitude = "".intern();
                        if (tmp.length() > 5) {
                            int deg = Integer.parseInt(tmp.substring(0, 2));
                            double min = Double.parseDouble(tmp.substring(2));
                            latitude = (deg + (min / 60)) + "";
                        }

                        String latitudeAngle = locationData[3];

                        tmp = locationData[4];
                        String longitude = "".intern();
                        if (tmp.length() > 5) {
                            int deg = Integer.parseInt(tmp.substring(0, 3));
                            double min = Double.parseDouble(tmp.substring(3));
                            longitude = (deg + (min / 60)) + "";
                        }

                        String longitudeAngle = locationData[5];
                        String fixQuality = locationData[6];
                        String satellites = locationData[7];
                        String dilution = locationData[8];
                        String altitude = locationData[9];
                        String altitudeMeasure = locationData[10];
                        String seaLevel = locationData[11].concat(locationData[12]);
                        result.setLatitude(Double.parseDouble(latitude.isEmpty() ? "0" : latitude));
                        result.setLongitude(Double.parseDouble(longitude.isEmpty() ? "0" : longitude));
                        result.setAltitude(Double.parseDouble(altitude.isEmpty() ? "0" : altitude));
                    }
                }
            }
            return result.getAltitude() != 0.0 ? result : null;
        }

        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }
}
