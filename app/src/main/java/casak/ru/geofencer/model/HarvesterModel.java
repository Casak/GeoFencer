package casak.ru.geofencer.model;

import android.support.annotation.Nullable;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.maps.android.SphericalUtil;

import java.util.LinkedList;
import java.util.List;

import casak.ru.geofencer.R;
import casak.ru.geofencer.presenter.MapPresenter;
import casak.ru.geofencer.util.MapsUtils;

public class HarvesterModel {
    private boolean isMoving;

    private LatLng previousLocation;
    private LatLng currentLocation;

    private List<LatLng> sessionRoute;
    private List<LatLng> fieldBuildRoute;

    private Marker positionMarker;

    private MapPresenter mapPresenter;
    //TODO Getter|setter
    private Polyline harvestedPolyline;
    private Polygon harvestedPolygon;

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

    public Polygon getHarvestedPolygon(){
        return harvestedPolygon;
    }

    public Polyline getHarvestedPolyline(){
        return harvestedPolyline;
    }

    public void startFieldRouteBuilding() {
        fieldBuildRoute = new LinkedList<>();
        isCreatingFieldRoute = true;
    }

    public void finishFieldRouteBuilding() {
        isCreatingFieldRoute = false;
        //TODO Create alert "Can`t create field from 1 point"
        if(fieldBuildRoute.size() > 1)
        mapPresenter.finishCreatingRoute(fieldBuildRoute);
    }

    //TODO Check for dups location
    public void updateCurrentLocation(LatLng currentLocation) {
        if (currentLocation == null)
            return;

        if (this.currentLocation != null)
            previousLocation = this.currentLocation;
        this.currentLocation = currentLocation;

        isMoving = currentLocation != previousLocation;

        sessionRoute.add(currentLocation);

        if (isCreatingFieldRoute)
            fieldBuildRoute.add(currentLocation);

        updateRouteUI();
        updatePositionOnMap();
    }

    private void updateRouteUI() {
        if (harvestedPolyline == null)
            harvestedPolyline = mapPresenter.showPolyline(MapsUtils.createPolylineOptions(sessionRoute));
        else
            harvestedPolyline.setPoints(sessionRoute);
//TODO location On path
        if (harvestedPolygon == null)
            harvestedPolygon = mapPresenter
                    .showPolygon(MapsUtils.harvestedPolygonOptions(harvestedPolyline));
        else{
            List<LatLng> points = MapsUtils.harvestedPolygonOptions(harvestedPolyline).getPoints();
            if (points != null)
                harvestedPolygon.setPoints(points);
        }
    }

    private void updatePositionOnMap() {
        if (currentLocation == null)
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
            mapPresenter.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 18));
        } else {
            positionMarker.setPosition(currentLocation);
            positionMarker.setRotation(heading);
            mapPresenter.animateCamera(MapsUtils.latLngToCameraUpdate(currentLocation));
        }
    }
}
