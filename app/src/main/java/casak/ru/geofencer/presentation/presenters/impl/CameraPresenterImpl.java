package casak.ru.geofencer.presentation.presenters.impl;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;

import javax.inject.Inject;

import casak.ru.geofencer.domain.executor.Executor;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.domain.interactors.PointerInteractor;
import casak.ru.geofencer.domain.interactors.impl.MapUtils;
import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.presentation.converters.LatLngConverter;
import casak.ru.geofencer.presentation.presenters.CameraPresenter;
import casak.ru.geofencer.presentation.presenters.GoogleMapPresenter;
import casak.ru.geofencer.presentation.presenters.base.AbstractPresenter;
import casak.ru.geofencer.presentation.ui.fragment.MapPointerFragment;

/**
 * Created on 21.02.2017.
 */

public class CameraPresenterImpl extends AbstractPresenter implements CameraPresenter {

    private FollowType mFollowType;
    private GoogleMapPresenter.View mView;
    private PointerInteractor mPointerInteractor;
    private Point mPointPrevious;

    @Inject
    public CameraPresenterImpl(Executor executor, MainThread mainThread, GoogleMapPresenter.View view) {
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
