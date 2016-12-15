package casak.ru.geofencer.model;

import android.support.annotation.Nullable;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import java.util.LinkedList;
import java.util.List;

import casak.ru.geofencer.R;
import casak.ru.geofencer.presenter.MapPresenter;

public class HarvesterModel {
    private boolean isMoving;

    private LatLng previousLocation;
    private LatLng currentLocation;

    private List<LatLng> sessionRoute;
    private List<LatLng> fieldBuildRoute;

    private Marker positionMarker;

    private MapPresenter mapPresenter;

    private boolean isCreatingFieldRoute;

    public HarvesterModel(MapPresenter presenter, @Nullable LatLng lastKnownLocation) {
        mapPresenter = presenter;
        isCreatingFieldRoute = false;

        if (lastKnownLocation != null) {
            currentLocation = lastKnownLocation;
            updatePositionOnMap();
        }

        sessionRoute = new LinkedList<>();
        fieldBuildRoute = new LinkedList<>();
    }

    public boolean getIsMoving() {
        return isMoving;
    }

    public LatLng getPreviousLocation() {
        return previousLocation;
    }

    public LatLng getCurrentLocation() {
        return currentLocation;
    }

    public void startFieldRouteBuilding() {
        fieldBuildRoute = new LinkedList<>();
        isCreatingFieldRoute = true;
    }

    public void finishFieldRouteBuilding() {
        isCreatingFieldRoute = false;
        mapPresenter.finishCreatingRoute(fieldBuildRoute);
    }

    //TODO Check for dups location
    public void updateCurrentLocation(LatLng currentLocation) {
        if (this.currentLocation != null)
            previousLocation = this.currentLocation;
        this.currentLocation = currentLocation;

        isMoving = currentLocation != previousLocation;

        sessionRoute.add(currentLocation);

        if (isCreatingFieldRoute)
            fieldBuildRoute.add(currentLocation);

        updatePositionOnMap();
    }

    private void updatePositionOnMap() {
        if(currentLocation == null)
            return;

        float heading = previousLocation != null ?
                Float.parseFloat(SphericalUtil.computeHeading(previousLocation, currentLocation) + "") :
                0f;

        if (positionMarker == null) {
            positionMarker = mapPresenter.showMarker(new MarkerOptions()
                    .anchor(0.5f, 0.5f)
                    .flat(true)
                    .position(currentLocation)
                    .rotation(heading)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.current_position_moving_icon)));
            mapPresenter.moveCamera(CameraUpdateFactory.newCameraPosition(
                    CameraPosition
                            .builder()
                            .target(currentLocation)
                            .zoom(16)
                            .build()));
        } else {
            positionMarker.setPosition(currentLocation);
            positionMarker.setRotation(heading);
            mapPresenter.moveCamera(CameraUpdateFactory.newCameraPosition(
                    CameraPosition
                            .builder()
                            .target(currentLocation)
                            .zoom(16)
                            .build()));
        }
    }
}
