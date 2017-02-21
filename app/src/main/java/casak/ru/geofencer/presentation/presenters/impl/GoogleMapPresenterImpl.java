package casak.ru.geofencer.presentation.presenters.impl;

import android.content.SharedPreferences;
import android.util.LongSparseArray;
import android.util.SparseArray;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import casak.ru.geofencer.AndroidApplication;
import casak.ru.geofencer.bluetooth.AntennaDataProvider;
import casak.ru.geofencer.domain.executor.Executor;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.domain.interactors.CreateFieldInteractor;
import casak.ru.geofencer.domain.interactors.LoadFieldInteractor;
import casak.ru.geofencer.domain.interactors.LocationInteractor;
import casak.ru.geofencer.domain.model.Arrow;
import casak.ru.geofencer.domain.model.Field;
import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.model.Route;
import casak.ru.geofencer.di.scopes.ActivityScope;
import casak.ru.geofencer.domain.repository.FieldRepository;
import casak.ru.geofencer.presentation.converters.ArrowConverter;
import casak.ru.geofencer.presentation.converters.FieldConverter;
import casak.ru.geofencer.presentation.converters.RouteConverter;
import casak.ru.geofencer.presentation.presenters.GoogleMapPresenter;
import casak.ru.geofencer.presentation.presenters.base.AbstractPresenter;
import casak.ru.geofencer.presentation.ui.fragment.GoogleMapFragment;

/**
 * Created on 09.02.2017.
 */

@ActivityScope
public class GoogleMapPresenterImpl extends AbstractPresenter implements GoogleMapPresenter {
    private static final String TAG = GoogleMapPresenterImpl.class.getSimpleName();

    private boolean isFieldBuilding;

    private GoogleMapPresenter.View mMapView;
    private FieldRepository mFieldRepository;
    private CreateFieldInteractor mCreateFieldInteractor;
    private LocationInteractor mLocationInteractor;
    private LoadFieldInteractor mLoadFieldInteractor;

    private AntennaDataProvider mDataProvider;

    @Inject
    public GoogleMapPresenterImpl(Executor executor, MainThread mainThread,
                                  CreateFieldInteractor createFieldInteractor,
                                  LocationInteractor locationInteractor,
                                  GoogleMapPresenter.View mapView,
                                  FieldRepository fieldRepository,
                                  AntennaDataProvider provider) {
        super(executor, mainThread);

        mCreateFieldInteractor = createFieldInteractor;
        mLocationInteractor = locationInteractor;
        mMapView = mapView;
        mFieldRepository = fieldRepository;
        mDataProvider = provider;

        isFieldBuilding = false;
        mLocationInteractor.init(this);
        mDataProvider.registerObserver(mLocationInteractor.getListener());
        mLocationInteractor.execute();
    }

    @Override
    public void startBuildField() {
        isFieldBuilding = true;

        SharedPreferences preferences = AndroidApplication.getComponent().getSharedPreferences();
        int width = Integer.parseInt(preferences.getString("pref_machinery _width", null));

        mCreateFieldInteractor.init(this, width);

        mDataProvider.registerObserver(mCreateFieldInteractor.getOnLocationChangedListener());

        mCreateFieldInteractor.execute();
        mCreateFieldInteractor.onStartCreatingRouteClick();
    }

    @Override
    public void finishBuildField() {
        isFieldBuilding = false;
        mDataProvider.removeObserver(mCreateFieldInteractor.getOnLocationChangedListener());
        mCreateFieldInteractor.onFinishCreatingRouteClick();
    }

    @Override
    public void onPolylineClick(Polyline polyline) {
        for (Map.Entry<Arrow, Polyline> e : arrowsMap.entrySet()) {
            Arrow key = e.getKey();
            Polyline value = e.getValue();
            if (value.equals(polyline)) {
                mCreateFieldInteractor.onArrowClick(key);
                break;
            }
        }
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

    //TODO Refactor and check incoming data
    Map<Arrow, Polyline> arrowsMap = new HashMap<>();

    @Override
    public void showArrow(Arrow model) {
        PolylineOptions arrowOptions = ArrowConverter.convertToPresentationModel(model);
        Polyline polyline = mMapView.showPolyline(arrowOptions);
        arrowsMap.put(model, polyline);
    }

    //TODO Check
    @Override
    public void hideArrow(Arrow model) {
        Polyline polyline = arrowsMap.get(model);
        polyline.setVisible(false);
        polyline.remove();
        arrowsMap.remove(model);
    }

    SparseArray<Polygon> fields = new SparseArray<>();

    @Override
    public void showField(Field model) {
        PolygonOptions fieldOptions = FieldConverter.convertToPresentation(model);

        Polygon polygon = mMapView.showPolygon(fieldOptions);

        fields.append(model.getId(), polygon);
    }

    @Override
    public void hideField(Field model) {
        int fieldId = model.getId();
        Polygon polygon = fields.get(fieldId);

        polygon.setVisible(false);
        polygon.remove();

        fields.remove(fieldId);
    }

    LongSparseArray<Polyline> routes = new LongSparseArray<>();

    @Override
    public void showRoute(Route model) {
        PolylineOptions routeOptions = RouteConverter.convertToPresentation(model);

        Polyline polyline = mMapView.showPolyline(routeOptions);

        routes.append(model.getId(), polyline);
    }

    @Override
    public void hideRoute(Route model) {
        long routeId = model.getId();
        Polyline polyline = routes.get(routeId);

        polyline.setVisible(false);
        polyline.remove();

        routes.remove(routeId);
    }

    @Override
    public void onZoomMore() {
        mMapView.changeCamera(CameraUpdateFactory.zoomIn());
    }

    @Override
    public void onZoomLess() {
        mMapView.changeCamera(CameraUpdateFactory.zoomOut());
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
        CameraPosition currentCameraPosition = mMapView.getCurrentCameraPosition();

        CameraPosition cameraPosition = new CameraPosition.Builder(currentCameraPosition)
                .tilt(tilt).build();

        mMapView.changeCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void changeMapType() {
        int current = mMapView.getCurrentMapType();

        switch (current) {
            case GoogleMap.MAP_TYPE_NONE:
                mMapView.changeMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case GoogleMap.MAP_TYPE_NORMAL:
                mMapView.changeMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case GoogleMap.MAP_TYPE_SATELLITE:
                mMapView.changeMapType(GoogleMap.MAP_TYPE_NONE);
                break;
            default:
                mMapView.changeMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
    }

    @Override
    public void onFieldLoad(int fieldId) {
        if (mLoadFieldInteractor == null) {
            mLoadFieldInteractor = GoogleMapFragment.getMapComponent().getLoadFieldInteractor();
        }
        mLoadFieldInteractor.init(mFieldRepository, this, fieldId);
        mLoadFieldInteractor.execute();

        //TODO Delete
        mDataProvider.startHarvesting();
    }

    Route sessionRoute = new Route(1, 1011, Route.Type.BASE);

    @Override
    public void addToSessionRoute(Point point) {
        if (routes.get(sessionRoute.getId()) != null) {
            hideRoute(sessionRoute);
        }
        sessionRoute.addRoutePoint(point);
        showRoute(sessionRoute);
    }

    private float getCurrentCameraTilt() {
        return mMapView.getCurrentCameraPosition().tilt;
    }
}
