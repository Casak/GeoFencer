package casak.ru.geofencer.view;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.SupportMapFragment;

import java.lang.ref.WeakReference;

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

    }
}
