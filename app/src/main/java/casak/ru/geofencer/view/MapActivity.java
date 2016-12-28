package casak.ru.geofencer.view;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.ParcelUuid;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.SupportMapFragment;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.UUID;

import casak.ru.geofencer.Constants;
import casak.ru.geofencer.R;
import casak.ru.geofencer.presenter.MapPresenter;
import casak.ru.geofencer.presenter.interfaces.IMapPresenter;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MapActivity extends AppCompatActivity {

    private static final String TAG = MapActivity.class.getSimpleName();

    //TODO
    private WeakReference<Context> contextWeakReference;

    //TODO Inject
    private IMapPresenter mapPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO alert "NO GPlay Services". Cases too
        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getApplicationContext())
                != ConnectionResult.SUCCESS)
            return;

        mapPresenter = new MapPresenter(this);

        setContentView(R.layout.activity_map);


        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.myFAB);
        myFab.setOnClickListener(mapPresenter.getOnClickListener());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(mapPresenter);

        //TODO Move somewhere bluetooth antenna code. Also need to add broadcast to retrieve turning off
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //TODO Normal handling
        if (bluetoothAdapter == null)
            Log.d(TAG, "You don`t have bluetooth!");
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, Constants.BLUETOOTH_REQUEST_CODE);
        }

 /*       Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        BluetoothDevice antenna = null;
        if (pairedDevices.size() > 0) {
            Log.d(TAG, "There are paired devices. Get the name and address of each paired device");
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                Log.d(TAG, "deviceName = " + deviceName + "; deviceHardwareAddress = " +
                        deviceHardwareAddress + ";");
                if (deviceHardwareAddress.equals(Constants.BLUETOOTH_GPS_ANTENNA_HARDWARE_ADDRESS)) {
                    antenna = device;
                    Log.d(TAG, "Antenna is already paired!");
                }
            }
        }

        if(antenna == null) {
        */

        final BroadcastReceiver mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    Log.d(TAG, "ACTION_FOUND");
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    String deviceHardwareAddress = device.getAddress();
                    ParcelUuid[] uuids = device.getUuids();
                    for(ParcelUuid uuid : uuids){
                        Log.d(TAG, "Antenna`s UUID is : " + uuid.getUuid());
                    }
                    if (deviceHardwareAddress.equals(Constants.BLUETOOTH_GPS_ANTENNA_HARDWARE_ADDRESS)) {
                        bluetoothAdapter.cancelDiscovery();
                        Log.d(TAG, "Antenna is discovered");
                        ConnectThread thread = new ConnectThread(device);
                        thread.start();
                    }
                }
            }
        };

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        //TODO !!!! Unregister receiver
        registerReceiver(mReceiver, filter);
        bluetoothAdapter.startDiscovery();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mapPresenter.onStart();
    }

    @Override
    protected void onStop() {
        mapPresenter.onStop();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mapPresenter.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        //TODO move that bluetooth shit out of here
        if (requestCode == Constants.BLUETOOTH_REQUEST_CODE && resultCode == RESULT_OK)
            Log.d(TAG, "Bluetooth have been successfully enabled");
    }

    private void manageMyConnectedSocket(BluetoothSocket socket) {
        BluetoothDataTransferringThread thread = new BluetoothDataTransferringThread(socket);
        thread.start();
    }


    private class BluetoothDataTransferringThread extends Thread {
        private BluetoothSocket socket;
        private byte[] buffer;
        private InputStream in;

        public BluetoothDataTransferringThread(BluetoothSocket bluetoothSocket) {
            Log.d(TAG, "In BluetoothDataTransferringThread");
            socket = bluetoothSocket;
            buffer = new byte[1024];
            try {
                in = socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Log.d(TAG, "Read from GPS antenna: " + in.read(buffer));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }


    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(Constants.ANTENNA_UUID));
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            Log.d(TAG, "In ConnectThread.run()");
            try {
                mmSocket.connect();
            } catch (IOException connectException) {
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }
            manageMyConnectedSocket(mmSocket);
        }
    }
}



