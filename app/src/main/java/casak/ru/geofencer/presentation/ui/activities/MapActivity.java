package casak.ru.geofencer.presentation.ui.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.SupportMapFragment;

import java.lang.ref.WeakReference;

import android.os.Handler;

import casak.ru.geofencer.R;
import casak.ru.geofencer.domain.Constants;
import casak.ru.geofencer.domain.executor.impl.ThreadExecutor;
import casak.ru.geofencer.presentation.presenters.IMapPresenter;
import casak.ru.geofencer.presentation.presenters.impl.MapPresenter;
import casak.ru.geofencer.presentation.ui.fragment.DeltaFragment;
import casak.ru.geofencer.threading.MainThreadImpl;

public class MapActivity extends FragmentActivity {

    private static final String TAG = MapActivity.class.getSimpleName();

    //TODO
    private WeakReference<Context> contextWeakReference;

    //TODO Inject; delete static
    public static MapPresenter mapPresenter;

    private DeltaFragment deltaFragment;

    private static Handler h;

    public static void updateCTE(String string) {
        Message msg = new Message();
        Bundle b = new Bundle();
        b.putString("key", string);
        msg.setData(b);
        h.sendMessage(msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                deltaFragment.mTextViewCrossTrackError.setText(msg.getData().getString("key"));
            }

            ;
        };
        //TODO alert "NO GPlay Services". Cases too
        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getApplicationContext())
                != ConnectionResult.SUCCESS)
            return;


        //TODO Move somewhere bluetooth antenna code. Also need to add broadcast to retrieve turning off
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //TODO Normal handling
        if (bluetoothAdapter == null)
            Log.d(TAG, "You don`t have bluetooth!");
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, Constants.BLUETOOTH_REQUEST_CODE);
        }
        bluetoothAdapter.startDiscovery();

        setContentView(R.layout.activity_map);

        mapPresenter = new MapPresenter(this, ThreadExecutor.getInstance(), MainThreadImpl.getInstance());

        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.myFAB);
        myFab.setOnClickListener(mapPresenter.getOnClickListener());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);

        deltaFragment = (DeltaFragment) getSupportFragmentManager().findFragmentById(R.id.delta_fragment);

        mapFragment.getMapAsync(mapPresenter);
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
}



