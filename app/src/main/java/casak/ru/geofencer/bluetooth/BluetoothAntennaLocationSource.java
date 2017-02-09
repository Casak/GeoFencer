package casak.ru.geofencer.bluetooth;

import android.util.Log;

import com.google.android.gms.maps.LocationSource;


/**
 * Created by User on 29.12.2016.
 */

public class BluetoothAntennaLocationSource implements LocationSource {
    private static final String TAG = BluetoothAntennaLocationSource.class.getSimpleName();

    private static OnLocationChangedListener locationChangedListener;

    public BluetoothAntennaLocationSource() {
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
