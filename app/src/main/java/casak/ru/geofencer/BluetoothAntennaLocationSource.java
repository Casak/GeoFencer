package casak.ru.geofencer;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.util.Log;

import com.google.android.gms.maps.LocationSource;


/**
 * Created by User on 29.12.2016.
 */

public class BluetoothAntennaLocationSource implements LocationSource {
    private static final String TAG = BluetoothAntennaLocationSource.class.getSimpleName();

    private static OnLocationChangedListener locationChangedListener;


    public BluetoothAntennaLocationSource() {


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
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        Log.d(TAG, "Activating BluetoothAntennaLocationSource");
        locationChangedListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        Log.d(TAG, "Deactivating BluetoothAntennaLocationSource");
    }

    static public OnLocationChangedListener getListener(){
        return locationChangedListener;
    }
}
