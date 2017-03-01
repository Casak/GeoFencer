package casak.ru.geofencer.presentation.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import casak.ru.geofencer.AndroidApplication;
import casak.ru.geofencer.R;
import casak.ru.geofencer.di.components.DaggerMapComponent;
import casak.ru.geofencer.di.components.MapComponent;
import casak.ru.geofencer.di.modules.MapModule;
import casak.ru.geofencer.presentation.presenters.GoogleMapPresenter;
import casak.ru.geofencer.presentation.ui.base.BaseActivity;

/**
 * Created on 08.02.2017.
 */

public class GoogleMapFragment extends Fragment implements GoogleMapPresenter.View {
    private static final String TAG = GoogleMapFragment.class.getSimpleName();

    private static MapComponent mMapComponent;
    private static MapModule mMapModule;

    private GoogleMap mMap;

    @Inject
    GoogleMapPresenter mGoogleMapPresenter;
    @BindView(R.id.mapView)
    MapView mMapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mMapModule == null)
            mMapModule = new MapModule(this);

        if (mMapComponent == null)
            mMapComponent = DaggerMapComponent.builder()
                    .appComponent(AndroidApplication.getComponent())
                    .activityModule(BaseActivity.getActivityModule())
                    .mapModule(getMapModule())
                    .build();

        getMapComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        ButterKnife.bind(this, rootView);

        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setOnPolylineClickListener(mGoogleMapPresenter);
            }
        });

        return rootView;
    }

    @OnClick(R.id.temp_button)
    public void buildField() {
        if (!mGoogleMapPresenter.isFieldBuilding()) {
            mGoogleMapPresenter.startBuildField();
        } else {
            mGoogleMapPresenter.finishBuildField();
        }
    }

    @Override
    public void changeCamera(CameraUpdate update) {
        checkMapReady();
        mMap.animateCamera(update);
    }

    @Override
    public void changeMapType(int type) {
        checkMapReady();
        mMap.setMapType(type);
    }

    @Override
    public Polyline showPolyline(PolylineOptions options) {
        checkMapReady();
        return mMap.addPolyline(options);
    }

    @Override
    public Polygon showPolygon(PolygonOptions options) {
        checkMapReady();
        return mMap.addPolygon(options);
    }

    @Override
    public CameraPosition getCurrentCameraPosition() {
        checkMapReady();
        return mMap.getCameraPosition();
    }

    @Override
    public int getCurrentMapType() {
        checkMapReady();
        return mMap.getMapType();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public static MapModule getMapModule() {
        return mMapModule;
    }

    public static MapComponent getMapComponent() {
        return mMapComponent;
    }

    private boolean checkMapReady() {
        if (mMap == null) {
            throw new NullPointerException("Map is not ready!");
        }
        return true;
    }

    //For debugging
    @Override
    public Circle showCircle(CircleOptions options) {
        checkMapReady();
        return mMap.addCircle(options);
    }
}
