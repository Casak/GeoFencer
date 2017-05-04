package com.smartagrodriver.core.presentation.presenters.impl;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;

import javax.inject.Inject;

import com.smartagrodriver.core.threading.Executor;
import com.smartagrodriver.core.threading.MainThread;
import com.smartagrodriver.core.domain.interactors.PointerInteractor;
import com.smartagrodriver.core.domain.interactors.impl.MapUtils;
import com.smartagrodriver.core.domain.model.Point;
import com.smartagrodriver.core.presentation.converters.LatLngConverter;
import com.smartagrodriver.core.presentation.presenters.MapCameraPresenter;
import com.smartagrodriver.core.presentation.presenters.GoogleMapPresenter;
import com.smartagrodriver.core.presentation.presenters.base.AbstractPresenter;
import com.smartagrodriver.core.presentation.ui.fragment.MapPointerFragment;

/**
 * Created on 21.02.2017.
 */

public class MapCameraPresenterImpl extends AbstractPresenter implements MapCameraPresenter {

    private FollowType mFollowType;
    private GoogleMapPresenter.View mView;
    private PointerInteractor mPointerInteractor;
    private Point mPointPrevious;

    @Inject
    public MapCameraPresenterImpl(Executor executor, MainThread mainThread, GoogleMapPresenter.View view) {
        super(executor, mainThread);

        mView = view;
        mFollowType = FollowType.NON_FOLLOW;
    }

    @Override
    public void onChange(Point point) {
        if (mPointPrevious == null) {
            mPointPrevious = point;
        }
        CameraUpdate result;
        CameraPosition cameraPosition;
        CameraPosition oldCameraPosition = mView.getCurrentCameraPosition();
        //TODO Normal initialization
        if (mPointerInteractor == null) {
            mPointerInteractor = MapPointerFragment.getPointerComponent().getPointerInteractor();
        }

        switch (mFollowType) {
            case FOLLOW_POINT:
                cameraPosition = CameraPosition.builder()
                        .target(LatLngConverter.convertToLatLng(point))
                        .bearing((float) MapUtils.computeHeading(mPointPrevious, point))
                        .tilt(oldCameraPosition.tilt)
                        .zoom(oldCameraPosition.zoom)
                        .build();
                result = CameraUpdateFactory.newCameraPosition(cameraPosition);
                break;
            case FOLLOW_ROUTE:
                if (mPointerInteractor == null) {
                    return;
                }

                Point nearPoint = mPointerInteractor.getNearestPoint();
                if (nearPoint != null) {
                    point = nearPoint;
                }

                cameraPosition = CameraPosition.builder()
                        .target(LatLngConverter.convertToLatLng(point))
                        .bearing((float) mPointerInteractor.getCurrentRouteBearing())
                        .tilt(oldCameraPosition.tilt)
                        .zoom(oldCameraPosition.zoom)
                        .build();
                result = CameraUpdateFactory.newCameraPosition(cameraPosition);
                break;
            case NON_FOLLOW:
            default:
                return;
        }

        mView.changeCamera(result);
        mPointPrevious = point;
    }

    @Override
    public void setFollowType(FollowType followType) {
        mFollowType = followType;
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
