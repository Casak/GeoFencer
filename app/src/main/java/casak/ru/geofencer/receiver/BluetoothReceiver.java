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
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import casak.ru.geofencer.BluetoothAntennaLocationSource;
import casak.ru.geofencer.db.Contract;
import casak.ru.geofencer.domain.Constants;
import casak.ru.geofencer.presentation.ui.activities.MapActivity;

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
        else if("android.bluetooth.device.action.DISAPPEARED".equals(action)){

        }
    }

    private void manageMyConnectedSocket(BluetoothSocket socket) {
        BluetoothDataTransferringThread thread = new BluetoothDataTransferringThread(socket);
        thread.start();
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;

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
                connected = true;
            } catch (IOException connectException) {
                Log.e(TAG, "Could not connect to the client socket", connectException);
                try {
                    sleep(500);
                    mmSocket.close();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
            }
            if (connected)
                manageMyConnectedSocket(mmSocket);
        }
    }
    private class BluetoothDataTransferringThread extends Thread {
        private BluetoothSocket socket;
        private InputStream in;

        public BluetoothDataTransferringThread(BluetoothSocket bluetoothSocket) {
            Log.d(TAG, "In BluetoothDataTransferringThread");
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

                ContentResolver resolver = context.getContentResolver();

                while ((length = in.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                    Log.d(TAG, "Read from GPS antenna: " + result.toString("UTF-8"));
                    Location newLocation = parse(result.toString("UTF-8"));

                    if (newLocation != null) {
                        BluetoothAntennaLocationSource.getListener().onLocationChanged(newLocation);
                        ContentValues value = new ContentValues();
                        value.put(Contract.CoordEntry.COLUMN_LAT, newLocation.getLatitude());
                        value.put(Contract.CoordEntry.COLUMN_LNG, newLocation.getLongitude());
                        value.put(Contract.CoordEntry.COLUMN_ALT, newLocation.getAltitude());
                        resolver.insert(Contract.CoordEntry.CONTENT_URI, value);
                        ((MapActivity)context).showToast("Location with latitude: " + newLocation.getLatitude() +
                                "; longitude: " + newLocation.getLongitude() +
                                "; INSERTED");
                    }

                    result.reset();
                    sleep(1000);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
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
                    if (locationData.length >= 12) {
                        String time = locationData[1];

                        String tmp = locationData[2];
                        int deg = Integer.parseInt(tmp.substring(0, 2));
                        double min = Double.parseDouble(tmp.substring(2));
                        String latitude = (deg + (min / 60)) + "";

                        String latitudeAngle = locationData[3];

                        tmp = locationData[4];
                        deg = Integer.parseInt(tmp.substring(0, 3));
                        min = Double.parseDouble(tmp.substring(3));
                        String longitude = (deg + (min / 60)) + "";

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
