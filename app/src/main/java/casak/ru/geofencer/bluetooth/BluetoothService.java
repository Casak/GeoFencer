package casak.ru.geofencer.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

import javax.inject.Inject;

import casak.ru.geofencer.domain.Constants;

/**
 * Created on 09.02.2017.
 */

public class BluetoothService {
    private static final String TAG = BluetoothService.class.getSimpleName();

    private final BluetoothAdapter mAdapter;

    @Inject
    public BluetoothService(Activity activity) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mAdapter == null) {
            throw new RuntimeException("There is no bluetooth in device!");
        }

        if (!mAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, Constants.BLUETOOTH_REQUEST_CODE);
        }

        mAdapter.startDiscovery();
    }
}
