package com.smartagrodriver.core.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.smartagrodriver.core.AndroidApplication;
import com.smartagrodriver.core.R;
import com.smartagrodriver.core.presentation.ui.base.BaseActivity;

import static android.bluetooth.BluetoothDevice.ACTION_FOUND;
import static android.bluetooth.BluetoothDevice.ACTION_ACL_DISCONNECTED;
import static android.bluetooth.BluetoothAdapter.ACTION_STATE_CHANGED;
import static android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_FINISHED;

/**
 * Created on 01.03.2017.
 */

public class BluetoothReceiver extends BroadcastReceiver {
    public final static int REQUEST_CODE = 802;

    private static final String TAG = BluetoothReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        final AntennaDataProvider antennaDataProvider = AndroidApplication.getComponent()
                .getAntennaDataProvider();
        final String action = intent.getAction();
        final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

        Log.d(TAG, "onReceive()! Discovering: " + adapter.isDiscovering());
        Log.d(TAG, "onReceive()! Action: " + action);

        switch (action) {
            case ACTION_ACL_DISCONNECTED:
                if (!adapter.isDiscovering()) {
                    adapter.startDiscovery();
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
                        if (!adapter.isDiscovering()) {
                            adapter.startDiscovery();
                        }
                        Log.d(TAG, "STATE_ON!");
                        break;
                    case BluetoothAdapter.ERROR:
                        Log.d(TAG, "ERROR!");
                }
                break;
            case ACTION_FOUND:
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (isAntenna(device) &&
                        antennaDataProvider.getState() == AntennaDataProvider.STATE_NONE) {
                    antennaDataProvider.connect(device);
                }
                break;
            case ACTION_DISCOVERY_FINISHED:
                if (antennaDataProvider.getState() == AntennaDataProvider.STATE_NONE) {
                    adapter.startDiscovery();
                }
        }
    }

    private boolean isAntenna(BluetoothDevice device) {
        return device.getAddress().equals(AntennaDataProvider.BLUETOOTH_GPS_ANTENNA_HARDWARE_ADDRESS);
    }
}