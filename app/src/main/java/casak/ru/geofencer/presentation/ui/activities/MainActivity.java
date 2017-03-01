package casak.ru.geofencer.presentation.ui.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import casak.ru.geofencer.R;
import casak.ru.geofencer.presentation.ui.base.BaseActivity;

/**
 * Created on 08.02.2017.
 */

public class MainActivity extends BaseActivity {
    private final static int REQUEST_CODE = 802;

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
                bluetoothAdapter.startDiscovery();
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                BluetoothAdapter.getDefaultAdapter().startDiscovery();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(
                        this,
                        getResources().getText(R.string.bluetooth_disabled),
                        Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}