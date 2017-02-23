package casak.ru.geofencer.presentation.presenters.impl;

import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;

import javax.inject.Inject;

import casak.ru.geofencer.domain.executor.Executor;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.presentation.converters.LatLngConverter;
import casak.ru.geofencer.presentation.presenters.CameraPresenter;
import casak.ru.geofencer.presentation.presenters.GoogleMapPresenter;
import casak.ru.geofencer.presentation.presenters.base.AbstractPresenter;

/**
 * Created on 21.02.2017.
 */

public class CameraPresenterImpl extends AbstractPresenter implements CameraPresenter {

    private boolean followUser;
    private GoogleMapPresenter.View mView;

    @Inject
    public CameraPresenterImpl(Executor executor, MainThread mainThread, GoogleMapPresenter.View view) {
        super(executor, mainThread);

        mView = view;
        followUser = true;
    }

    @Override
    public void onLocationChanged(Point point) {
        if (followUser) {
            CameraPosition oldCameraPosition = mView.getCurrentCameraPosition();
            CameraPosition cameraPosition = CameraPosition.builder()
                    .target(LatLngConverter.convertToLatLng(point))
                    .bearing(Float.parseFloat(point.getBearing() + ""))
                    .tilt(oldCameraPosition.tilt)
                    .zoom(oldCameraPosition.zoom)
                    .build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            mView.changeCamera(cameraUpdate);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            followUser = false;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            followUser = true;
        }

        return false;
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
