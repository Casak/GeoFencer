package casak.ru.geofencer.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import casak.ru.geofencer.R;
import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.presentation.ui.base.BaseActivity;

import static android.bluetooth.BluetoothDevice.ACTION_FOUND;
import static android.bluetooth.BluetoothDevice.ACTION_ACL_DISCONNECTED;
import static android.bluetooth.BluetoothAdapter.ACTION_STATE_CHANGED;

/**
 * Created on 01.03.2017.
 */

public class BluetoothReceiver extends BroadcastReceiver {
    public final static int REQUEST_CODE = 802;

    private static final String TAG = BluetoothReceiver.class.getSimpleName();
    private static final String BLUETOOTH_GPS_ANTENNA_HARDWARE_ADDRESS = "98:D3:31:80:3A:26";
    private static final String ANTENNA_UUID = "00001101-0000-1000-8000-00805f9b34fb";
    private static final int ONE_SECOND = 1000;

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        Log.d(TAG, "onReceive()! Discovering: " + BluetoothAdapter.getDefaultAdapter().isDiscovering());
        Log.d(TAG, "onReceive()! Action: " + action);

        switch (action) {
            case ACTION_ACL_DISCONNECTED:
                if (BluetoothAdapter.getDefaultAdapter().isDiscovering()) {
                    BluetoothAdapter.getDefaultAdapter().startDiscovery();
                }
                Toast.makeText(context, context.getText(R.string.antenna_lost), Toast.LENGTH_LONG)
                        .show();
                break;
            case ACTION_STATE_CHANGED:
                final int state = intent
                        .getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        BaseActivity.getAbstractActivityComponent().getActivityContext()
                                .startActivityForResult(enableBtIntent, REQUEST_CODE);
                        Log.d(TAG, "STATE_TURNING_OFF!");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        if (BluetoothAdapter.getDefaultAdapter().isDiscovering()) {
                            BluetoothAdapter.getDefaultAdapter().startDiscovery();
                        }
                        Log.d(TAG, "STATE_ON!");
                        break;
                    case BluetoothAdapter.ERROR:
                        Log.d(TAG, "ERROR!");
                }
                break;
            case ACTION_FOUND:
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (isAntenna(device)) {
                    connect(device);
                }
                break;
        }
    }

    private boolean isAntenna(BluetoothDevice device) {
        return device.getAddress().equals(BLUETOOTH_GPS_ANTENNA_HARDWARE_ADDRESS);
    }

    private void connect(BluetoothDevice device) {
        new ConnectThread(device).start();
    }

    private void startDataTransfer(BluetoothSocket socket) {
        new AntennaDataTransferThread(socket).start();
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;

        ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;

            try {
                tmp = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString(ANTENNA_UUID));
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            boolean connected = false;
            while (!connected) {
                try {
                    mmSocket.connect();
                    connected = true;
                    sleep(ONE_SECOND);
                } catch (IOException connectException) {
                    Log.e(TAG, "Could not connect to the antenna socket", connectException);
                    try {
                        mmSocket.close();
                    } catch (IOException closeException) {
                        Log.e(TAG, "Could not close the antenna socket", closeException);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            startDataTransfer(mmSocket);
        }
    }

    //TODO Refactor this shit, man, and double check
    private class AntennaDataTransferThread extends Thread {
        private BluetoothSocket socket;
        private InputStream in;

        AntennaDataTransferThread(BluetoothSocket bluetoothSocket) {
            socket = bluetoothSocket;
            try {
                in = socket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "Can`t obtain InputStream from socket", e);
            }
            if (BluetoothAdapter.getDefaultAdapter().isDiscovering()) {
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
            }
        }

        @Override
        public void run() {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[4800];
            int length;
            try {
                while (socket.isConnected()) {
                    if ((length = in.read(buffer)) != -1) {
                        result.write(buffer, 0, length);
                        Log.d(TAG, "Read from GPS antenna: " + result.toString("UTF-8"));
                        final Point newLocation = parse(result.toString("UTF-8"));

                        result.reset();
                        sleep(1000);
                    }
                }
            } catch (IOException | InterruptedException e) {
                Log.e(TAG, "Bluetooth socket is closed!", e);
            }

            BluetoothAdapter.getDefaultAdapter().startDiscovery();
        }

        private Point parse(String data) {
            if (!data.startsWith("$"))
                return null;
            Point result = new Point();

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
            return result.getLatitude() != 0.0 ? result : null;
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