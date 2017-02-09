package casak.ru.geofencer.presentation.presenters.impl;

import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import casak.ru.geofencer.domain.executor.Executor;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.injector.scopes.ActivityScope;
import casak.ru.geofencer.presentation.presenters.GoogleMapPresenter;

/**
 * Created on 09.02.2017.
 */

@ActivityScope
public class GoogleMapPresenterImpl implements GoogleMapPresenter {
    private static final String TAG = GoogleMapPresenterImpl.class.getSimpleName();

    private boolean isFieldBuilding;

    GoogleMapPresenter.View mapView;

    @Inject
    LocationSource locationSource;
    @Inject
    Executor threadExecutor;
    @Inject
    MainThread mainThread;

    @Inject
    public GoogleMapPresenterImpl(GoogleMapPresenter.View view){
        mapView = view;
        isFieldBuilding = false;
    }

    @Override
    public boolean isFieldBuilding() {
        return isFieldBuilding;
    }

    @Override
    public void startBuildField() {

    }

    @Override
    public void finishBuildField() {

    }

    public void injected(){
        Log.d(TAG, "INJECTED!!");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setLocationSource(locationSource);
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        LatLng geoCentreUkraine = new LatLng(48.9592699, 32.8723257);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(geoCentreUkraine, 6f));
        mapView.setMap(googleMap);
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void onError(String message) {

    }
}
