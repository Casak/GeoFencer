package casak.ru.geofencer.view;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.SupportMapFragment;

import java.lang.ref.WeakReference;
import android.os.Handler;

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

    private Handler h;

    public void showToast(String string){
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
                Toast.makeText(getApplicationContext(), msg.getData().getString("key"), Toast.LENGTH_SHORT)
                        .show();
            };
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


        mapPresenter = new MapPresenter(this);

        setContentView(R.layout.activity_map);

        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.myFAB);
        myFab.setOnClickListener(mapPresenter.getOnClickListener());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
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



