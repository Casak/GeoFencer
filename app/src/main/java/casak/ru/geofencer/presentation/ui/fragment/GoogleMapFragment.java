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

    private static MapComponent mapComponent;
    private static MapModule mapModule;

    private GoogleMap map;

    @Inject
    GoogleMapPresenter presenter;
    @BindView(R.id.mapView)
    MapView mapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mapModule == null)
            mapModule = new MapModule(this);

        if (mapComponent == null)
            mapComponent = DaggerMapComponent.builder()
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

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                map.setOnPolylineClickListener(presenter);
            }
        });

        return rootView;
    }

    @OnClick(R.id.temp_button)
    public void buildField() {
        if (!presenter.isFieldBuilding()) {
            presenter.startBuildField();
        } else {
            presenter.finishBuildField();
        }
    }

    @Override
    public void changeCamera(CameraUpdate update) {
        checkMapReady();
        map.moveCamera(update);
    }

    @Override
    public void changeMapType(int type) {
        checkMapReady();
        map.setMapType(type);
    }

    @Override
    public Polyline showPolyline(PolylineOptions options) {
        checkMapReady();
        return map.addPolyline(options);
    }

    @Override
    public Polygon showPolygon(PolygonOptions options) {
        checkMapReady();
        return map.addPolygon(options);
    }

    @Override
    public CameraPosition getCurrentCameraPosition() {
        checkMapReady();
        return map.getCameraPosition();
    }

    @Override
    public int getCurrentMapType() {
        checkMapReady();
        return map.getMapType();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public static MapModule getMapModule() {
        return mapModule;
    }

    public static MapComponent getMapComponent() {
        return mapComponent;
    }

    private boolean checkMapReady() {
        if (map == null) {
            throw new NullPointerException("Map is not ready!");
        }
        return true;
    }
}
