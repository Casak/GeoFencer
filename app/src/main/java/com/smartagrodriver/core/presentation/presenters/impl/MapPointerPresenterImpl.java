package com.smartagrodriver.core.presentation.presenters.impl;

import android.content.Context;
import android.content.res.Resources;
import android.util.LongSparseArray;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

import javax.inject.Inject;

import com.smartagrodriver.core.R;
import com.smartagrodriver.core.bluetooth.AntennaDataObservable;
import com.smartagrodriver.core.presentation.presenters.MapPresenter;
import com.smartagrodriver.core.presentation.ui.fragment.MapFragment;
import com.smartagrodriver.core.threading.Executor;
import com.smartagrodriver.core.threading.MainThread;
import com.smartagrodriver.core.domain.interactors.PointerInteractor;
import com.smartagrodriver.core.domain.model.Point;
import com.smartagrodriver.core.presentation.presenters.MapPointerPresenter;
import com.smartagrodriver.core.presentation.presenters.base.AbstractPresenter;
import com.smartagrodriver.core.presentation.ui.activities.MainActivity;
import com.smartagrodriver.core.presentation.ui.fragment.MapPointerFragment;

/**
 * Created on 21.02.2017.
 */

public class MapPointerPresenterImpl extends AbstractPresenter implements MapPointerPresenter {
    private static final String TAG = MapPointerPresenterImpl.class.getSimpleName();

    private PointerInteractor mInteractor;
    private MapPresenter mMapPresenter;
    private AntennaDataObservable mAntennaDataObservable;
    private MapPointerPresenter.View mView;

    @Inject
    public MapPointerPresenterImpl(Executor executor, MainThread mainThread,
                                   PointerInteractor interactor, AntennaDataObservable observable) {
        super(executor, mainThread);
        mInteractor = interactor;
        mAntennaDataObservable = observable;
        //TODO Refactor
        Context context = MainActivity.getAbstractActivityComponent().getActivityContext();
        mResources = context.getResources();
    }

    @Override
    public void showPointer(double value) {
        if (mView == null) {
            mView = MapPointerFragment.getPointerComponent().getPointerView();
        }

        View.Type side = value > 0 ? View.Type.LEFT : View.Type.RIGHT;

        //TODO Refactor
        int centimeterValue = (int) Math.abs(value * 100);

        if (centimeterValue > parseInt(R.string.pointer_red)) {
            mView.moveAnchor(side, View.RED);
        } else if (centimeterValue > parseInt(R.string.pointer_dark_orange)) {
            mView.moveAnchor(side, View.ORANGE_DARK);
        } else if (centimeterValue > parseInt(R.string.pointer_orange)) {
            mView.moveAnchor(side, View.ORANGE);
        } else if (centimeterValue > parseInt(R.string.pointer_dark_yellow)) {
            mView.moveAnchor(side, View.YELLOW_DARK);
        } else if (centimeterValue > parseInt(R.string.pointer_yellow)) {
            mView.moveAnchor(side, View.YELLOW);
        } else if (centimeterValue > parseInt(R.string.pointer_dark_green)) {
            mView.moveAnchor(side, View.GREEN_DARK);
        } else if (centimeterValue > parseInt(R.string.pointer_green)) {
            mView.moveAnchor(side, View.GREEN);
        } else {
            mView.moveAnchor(side, View.NONE);
        }

        //Debug
        updatePointerVisualization();
    }

    @Override
    public void resume() {
        if (mMapPresenter == null) {
            mMapPresenter = MapFragment.getMapComponent().getGoogleMapPresenter();
        } else if (mView == null) {
            mView = MapPointerFragment.getPointerComponent().getPointerView();
        }

        mInteractor.init(this, mMapPresenter.getCurrentFieldId());
        mAntennaDataObservable.registerObserver(mInteractor);
        mInteractor.execute();
    }

    @Override
    public void pause() {
        mAntennaDataObservable.removeObserver(mInteractor);
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

    private int parseInt(int id) {
        return Integer.parseInt(mResources.getString(id));
    }


    //Pointer work visualization
    private PointerInteractor mPointerInteractor;
    private MapPresenter.View mGoogleMapPresenterView;
    private Resources mResources;
    private long routeId;

    private void updatePointerVisualization() {
        if (mMapPresenter == null) {
            mMapPresenter = MapFragment.getMapComponent().getGoogleMapPresenter();
        } else if (mGoogleMapPresenterView == null) {
            mGoogleMapPresenterView = MapFragment.getMapComponent().getGoogleMapPresenterView();
        } else if (mPointerInteractor == null) {
            mPointerInteractor = MapPointerFragment.getPointerComponent().getPointerInteractor();
        } else if (mResources == null) {
            Context context = MainActivity.getAbstractActivityComponent().getActivityContext();
            mResources = context.getResources();
        }

        if (mPointerInteractor == null || mMapPresenter == null ||
                mGoogleMapPresenterView == null || mResources == null) {
            return;
        }

        if (mPointerInteractor.getCurrentRouteId() != routeId) {
            routeId = mPointerInteractor.getCurrentRouteId();
            updateRoutes(routeId);
        }

        updateCircles();
    }

    private void updateRoutes(long currentRouteId) {
        LongSparseArray<Polyline> routes = mMapPresenter.getRoutes();
        for (int i = 0; i < routes.size(); i++) {
            Polyline route = routes.valueAt(i);

            if (route.getColor() == mResources.getColor(R.color.debug_route_current) &&
                    routes.keyAt(i) != currentRouteId) {
                route.setColor(mResources.getColor(R.color.debug_route_used));
            } else if (routes.keyAt(i) == currentRouteId) {
                route.setColor(mResources.getColor(R.color.debug_route_current));
            }
        }
    }

    private Circle currentDot;
    private Point currentPoint;

    private void updateCircles() {
        Point current = mPointerInteractor.getNearestPoint();
        if (currentPoint == current) {
            return;
        }

        currentPoint = current;

        if (currentDot != null) {
            currentDot.remove();
        }

        currentDot = showCircle();
    }

    private Circle showCircle() {
        CircleOptions options = new CircleOptions()
                .center(new LatLng(currentPoint.getLatitude(), currentPoint.getLongitude()))
                .radius(0.5)
                .fillColor(mResources.getColor(R.color.debug_point_current));

        return mGoogleMapPresenterView.showCircle(options);
    }
}
