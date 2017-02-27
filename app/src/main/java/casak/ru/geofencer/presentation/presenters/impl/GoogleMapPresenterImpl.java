package casak.ru.geofencer.presentation.presenters.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.LongSparseArray;
import android.util.SparseArray;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import casak.ru.geofencer.AndroidApplication;
import casak.ru.geofencer.R;
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
import casak.ru.geofencer.presentation.converters.ArrowConverter;
import casak.ru.geofencer.presentation.converters.FieldConverter;
import casak.ru.geofencer.presentation.converters.LatLngConverter;
import casak.ru.geofencer.presentation.converters.RouteConverter;
import casak.ru.geofencer.presentation.presenters.CameraPresenter;
import casak.ru.geofencer.presentation.presenters.GoogleMapPresenter;
import casak.ru.geofencer.presentation.presenters.base.AbstractPresenter;

/**
 * Created on 09.02.2017.
 */

@ActivityScope
public class GoogleMapPresenterImpl extends AbstractPresenter implements GoogleMapPresenter {
    private static final String TAG = GoogleMapPresenterImpl.class.getSimpleName();

    private boolean isFieldBuilding;

    private GoogleMapPresenter.View mMapView;
    private CreateFieldInteractor mCreateFieldInteractor;
    private LocationInteractor mLocationInteractor;
    private LoadFieldInteractor mLoadFieldInteractor;

    private AntennaDataProvider mDataProvider;

    @Inject
    public GoogleMapPresenterImpl(Executor executor, MainThread mainThread,
                                  CreateFieldInteractor createFieldInteractor,
                                  LoadFieldInteractor loadFieldInteractor,
                                  LocationInteractor locationInteractor,
                                  GoogleMapPresenter.View mapView,
                                  AntennaDataProvider provider,
                                  CameraPresenter cameraPresenter) {
        super(executor, mainThread);

        mCreateFieldInteractor = createFieldInteractor;
        mLoadFieldInteractor = loadFieldInteractor;
        mLocationInteractor = locationInteractor;
        mMapView = mapView;
        mDataProvider = provider;
        mCameraPresenter = cameraPresenter;

        isFieldBuilding = false;
        mLocationInteractor.init(this);
        mDataProvider.registerObserver(mLocationInteractor.getListener());
        mLocationInteractor.execute();
    }

    boolean firstBuild = true;

    @Override
    public void startBuildField() {
        isFieldBuilding = true;
        if (firstBuild) {
            firstBuild = false;

            SharedPreferences preferences = AndroidApplication.getComponent().getSharedPreferences();
            Context context = AndroidApplication.getComponent().getContext();

            String defaultWidth = context.getString(R.string.default_machinery_width);
            String key = context.getString(R.string.machinery_width_key);
            int width = Integer.parseInt(preferences.getString(key, defaultWidth));

            mCreateFieldInteractor.init(this, width);

            mDataProvider.registerObserver(mCreateFieldInteractor.getOnLocationChangedListener());

            mCreateFieldInteractor.execute();
            mCreateFieldInteractor.onStartCreatingRoute();

            //TODO Delete
            mDataProvider.startPassingRouteBuildingPoints();
        }
    }

    @Override
    public void finishBuildField() {
        isFieldBuilding = false;
        mDataProvider.removeObserver(mCreateFieldInteractor.getOnLocationChangedListener());
        mCreateFieldInteractor.onFinishCreatingRoute();
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
        mLoadFieldInteractor.init(this, fieldId);
        mLoadFieldInteractor.execute();

        //TODO Delete
        mDataProvider.startHarvesting();
    }

    //TODO Get normal IDs
    Route sessionRoute = new Route(1, 1011, Route.Type.BASE);

    CameraPresenter mCameraPresenter;

    @Override
    public void addToSessionRoute(Point point) {
        if (routes.get(sessionRoute.getId()) == null) {
            showRoute(sessionRoute);
        }

        Polyline polyline = routes.get(sessionRoute.getId());

        List<LatLng> points = polyline.getPoints();
        points.add(LatLngConverter.convertToLatLng(point));
        polyline.setPoints(points);

        mCameraPresenter.onLocationChanged(point);
    }

    private float getCurrentCameraTilt() {
        return mMapView.getCurrentCameraPosition().tilt;
    }
}
