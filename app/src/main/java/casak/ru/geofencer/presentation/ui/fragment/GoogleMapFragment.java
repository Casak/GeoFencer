package casak.ru.geofencer.presentation.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import casak.ru.geofencer.AndroidApplication;
import casak.ru.geofencer.R;
import casak.ru.geofencer.injector.components.DaggerMapComponent;
import casak.ru.geofencer.injector.components.MapComponent;
import casak.ru.geofencer.injector.modules.ActivityModule;
import casak.ru.geofencer.injector.modules.MapModule;
import casak.ru.geofencer.presentation.presenters.GoogleMapPresenter;

/**
 * Created on 08.02.2017.
 */

public class GoogleMapFragment extends Fragment implements GoogleMapPresenter.View {

    @Inject
    GoogleMapPresenter presenter;

    MapView mMapView;
    private GoogleMap googleMap;



    private MapComponent mapComponent;

    public MapComponent component() {
        if (mapComponent == null) {
            mapComponent = DaggerMapComponent.builder()
                    .appComponent(AndroidApplication.getComponent())
                    .mapModule(new MapModule(this))
                    .activityModule(new ActivityModule(getActivity()))
                    .build();
        }
        return mapComponent;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                LatLng geoCentrUkraine = new LatLng(48.9592699d, 32.8723257d);
                googleMap = mMap;
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(geoCentrUkraine, 6f));
            }
        });

        component().inject(this);
        presenter.injected();

        return rootView;
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
}
