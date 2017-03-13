package casak.ru.geofencer.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import casak.ru.geofencer.AndroidApplication;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.domain.model.Point;

/**
 * Created on 17.02.2017.
 */

public class AntennaDataProvider {
    private static final String TAG = AntennaDataProvider.class.getSimpleName();

    public static final int STATE_NONE = 0;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_CONNECTED = 2;

    public static final String BLUETOOTH_GPS_ANTENNA_HARDWARE_ADDRESS = "98:D3:31:80:3A:26";

    private static final String ANTENNA_UUID = "00001101-0000-1000-8000-00805f9b34fb";

    private static final int ONE_SECOND = 1000;

    private final BluetoothAdapter mAdapter;
    private final AntennaDataObservable mObservable;
    private ConnectThread mConnectThread;
    private AntennaDataTransferThread mAntennaDataTransferThread;
    private int mState;

    public AntennaDataProvider(AntennaDataObservable observable) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mObservable = observable;
        mState = STATE_NONE;
    }

    public synchronized int getState() {
        return mState;
    }

    public synchronized void connect(BluetoothDevice device) {
        Log.d(TAG, "connecting to antenna. Name: " + device.getName());

        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }

        if (mAntennaDataTransferThread != null) {
            mAntennaDataTransferThread.cancel();
            mAntennaDataTransferThread = null;
        }

        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
    }

    private synchronized void connected(BluetoothSocket socket) {
        Log.d(TAG, "connected to antenna");

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mAntennaDataTransferThread != null) {
            mAntennaDataTransferThread.cancel();
            mAntennaDataTransferThread = null;
        }

        mAntennaDataTransferThread = new AntennaDataTransferThread(socket);
        mAntennaDataTransferThread.start();
    }

    private void connectionFailed() {
        mState = STATE_NONE;
        mAdapter.startDiscovery();
    }

    private void connectionLost() {
        mState = STATE_NONE;
        mAdapter.startDiscovery();
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private boolean mCanceled;
        private boolean mConnected;

        ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mCanceled = false;
            mConnected = false;

            try {
                tmp = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString(ANTENNA_UUID));
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;

            mState = STATE_CONNECTING;
        }

        public void run() {
            setName("AntennaConnectThread");

            if (mAdapter.isDiscovering()) {
                mAdapter.cancelDiscovery();
            }

            int i = 0;
            while (!mCanceled && !mConnected && i < 30) {
                i++;
                try {
                    mmSocket.connect();
                    mConnected = true;
                } catch (IOException connectException) {
                    Log.e(TAG, "Could not connect to the antenna socket", connectException);
                    try {
                        sleep(ONE_SECOND);
                        mmSocket.close();
                    } catch (IOException closeException) {
                        Log.e(TAG, "Could not close the antenna socket", closeException);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            synchronized (AntennaDataProvider.this) {
                mConnectThread = null;
                if (mConnected) {
                    connected(mmSocket);
                } else {
                    connectionFailed();
                }
            }
        }

        void cancel() {
            mCanceled = true;
        }
    }

    //TODO Refactor this shit, man, and double check
    private class AntennaDataTransferThread extends Thread {
        private BluetoothSocket socket;
        private InputStream in;
        private MainThread mainThread;

        AntennaDataTransferThread(BluetoothSocket bluetoothSocket) {
            mainThread = AndroidApplication.getComponent().getMainThread();
            socket = bluetoothSocket;
            try {
                in = socket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "Can`t obtain InputStream from socket", e);
            }

            mState = STATE_CONNECTED;
        }

        @Override
        public void run() {
            setName("AntennaDataTransferThread");
            mAdapter.cancelDiscovery();

            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[4800];
            int length;

            while (mState == STATE_CONNECTED) {
                try {
                    if ((length = in.read(buffer)) != -1) {
                        result.write(buffer, 0, length);
                        Log.d(TAG, "Read from GPS antenna: " + result.toString("UTF-8"));
                        final Point point = parse(result.toString("UTF-8"));

                        if (point != null) {
                            mainThread.post(new Runnable() {
                                @Override
                                public void run() {
//                                    mObservable.passLocation(point);
                                }
                            });
                        }

                        result.reset();
                        sleep(ONE_SECOND);
                    }
                } catch (IOException | InterruptedException e) {
                    Log.e(TAG, "Bluetooth socket is closed!", e);
                    connectionLost();
                }
            }
        }

        @Nullable
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

        void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }
}
