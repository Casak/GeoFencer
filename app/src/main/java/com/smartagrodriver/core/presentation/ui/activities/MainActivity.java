package com.smartagrodriver.core.presentation.ui.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.smartagrodriver.core.R;
import com.smartagrodriver.core.presentation.ui.base.BaseActivity;

import static com.smartagrodriver.core.bluetooth.BluetoothReceiver.REQUEST_CODE;

/**
 * Created on 08.02.2017.
 */

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, getResources().getText(R.string.no_bluetooth), Toast.LENGTH_LONG)
                    .show();
            finish();
        } else {
            if (bluetoothAdapter.isEnabled()) {
                if (bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.cancelDiscovery();
                }
                bluetoothAdapter.startDiscovery();
            } else {
                enableBluetooth();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
                if (btAdapter.isDiscovering()) {
                    btAdapter.cancelDiscovery();
                }
                btAdapter.startDiscovery();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(
                        this,
                        getResources().getText(R.string.bluetooth_disabled),
                        Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void enableBluetooth() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_CODE);
    }
}