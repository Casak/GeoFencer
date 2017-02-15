package casak.ru.geofencer.presentation.presenters.impl;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import casak.ru.geofencer.domain.interactors.CreateFieldInteractor;
import casak.ru.geofencer.domain.model.Arrow;
import casak.ru.geofencer.domain.model.Field;
import casak.ru.geofencer.domain.model.Route;
import casak.ru.geofencer.injector.scopes.ActivityScope;
import casak.ru.geofencer.presentation.presenters.GoogleMapPresenter;
import casak.ru.geofencer.presentation.ui.fragment.GoogleMapFragment;

/**
 * Created on 09.02.2017.
 */

@ActivityScope
public class GoogleMapPresenterImpl implements GoogleMapPresenter {
    private static final String TAG = GoogleMapPresenterImpl.class.getSimpleName();

    private boolean isFieldBuilding;

    private GoogleMapPresenter.View mapView;
    private LocationSource locationSource;
    private CreateFieldInteractor interactor;

    @Inject
    public GoogleMapPresenterImpl(GoogleMapPresenter.View mapView, LocationSource locationSource) {
        this.mapView = mapView;
        this.locationSource = locationSource;

        isFieldBuilding = false;
    }

    @Override
    public void startBuildField() {
        isFieldBuilding = true;

        if (interactor == null) {
            interactor = GoogleMapFragment.getMapComponent().getCreateFieldInteractor();
        }
        interactor.execute();
        interactor.onStartCreatingRouteClick();
    }

    @Override
    public void finishBuildField() {
        isFieldBuilding = false;
        interactor.onFinishCreatingRouteClick();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setLocationSource(locationSource);
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        LatLng geoCentreUkraine = new LatLng(48.9592699, 32.8723257);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(geoCentreUkraine, 6f));
        mapView.setMap(googleMap);
    }

    @Override
    public boolean isFieldBuilding() {
        return isFieldBuilding;
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

    @Override
    public void showArrow(Arrow model) {

    }

    @Override
    public void hideArrow(Arrow model) {

    }

    @Override
    public void showField(Field model) {

    }

    @Override
    public void hideField(Field model) {

    }

    @Override
    public void showRoute(Route model) {

    }

    @Override
    public void hideRoute(Route model) {

    }

    @Override
    public void onZoomMore() {
        mapView.changeCamera(CameraUpdateFactory.zoomIn());
    }

    @Override
    public void onZoomLess() {
        mapView.changeCamera(CameraUpdateFactory.zoomOut());
    }

    @Override
    public void onTiltMore() {
        float currentTilt = getCurrentCameraTilt();
        float newTilt = currentTilt + 10;

        newTilt = (newTilt > 90) ? 90 : newTilt;

        changeTilt(newTilt);
    }

    @Override
    public void onTiltLess() {
        float currentTilt = getCurrentCameraTilt();
        float newTilt = currentTilt - 10;

        newTilt = (newTilt > 0) ? newTilt : 0;

        changeTilt(newTilt);
    }

    @Override
    public void changeTilt(float tilt) {
        CameraPosition currentCameraPosition = mapView.getCurrentCameraPosition();

        CameraPosition cameraPosition = new CameraPosition.Builder(currentCameraPosition)
                .tilt(tilt).build();

        mapView.changeCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void changeMapType() {
        int current = mapView.getCurrentMapType();

        switch (current) {
            case GoogleMap.MAP_TYPE_NONE:
                mapView.changeMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case GoogleMap.MAP_TYPE_NORMAL:
                mapView.changeMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case GoogleMap.MAP_TYPE_SATELLITE:
                mapView.changeMapType(GoogleMap.MAP_TYPE_NONE);
                break;
            default:
                mapView.changeMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
    }

    private float getCurrentCameraTilt() {
        return mapView.getCurrentCameraPosition().tilt;
    }
}
