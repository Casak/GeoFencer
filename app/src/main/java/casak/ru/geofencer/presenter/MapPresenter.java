package casak.ru.geofencer.presenter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.LinkedList;
import java.util.List;

import casak.ru.geofencer.Constants;
import casak.ru.geofencer.R;
import casak.ru.geofencer.service.LocationService;
import casak.ru.geofencer.util.MapsUtils;
import casak.ru.geofencer.presenter.interfaces.IMapPresenter;

/**
 * Created by Casak on 08.12.2016.
 */

public class MapPresenter implements IMapPresenter, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    private static final String TAG = MapPresenter.class.getSimpleName();

    private Context context;
    private GoogleMap mGoogleMap;
    private LocationService locationService;
    private List<LatLng> route;
    private Polyline routePolyline;
    private Polygon harvestedPolygon;
    private TileOverlay heatMap;
    private GoogleApiClient mGoogleApiClient;

    public MapPresenter(Context context) {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }

        this.context = context;
        route = new LinkedList<>();
    }

    @Override
    public void onStart() {
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Alert or dialog
            return;
        }
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(context, context.getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            locationService = new LocationService(context, mGoogleApiClient);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Location client connected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Location client connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Location client connection failed");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
/*
        //Combine width
        double width = 20;

        LatLng latLng1 = new LatLng(50.097119d, 30.124142d);
        LatLng latLng2 = new LatLng(50.099563d, 30.127152d);
        LatLng latLng3 = new LatLng(50.098466d, 30.125510d);

        Polygon polygon = mGoogleMap.addPolygon(MapsUtils.createFieldPolygonOptions(latLng1, latLng2, width, true)
                .strokeColor(0x7FFF0000)
                .fillColor(0x7F00FF00)
                .geodesic(true));
        //TODO Normalize the direction
        Polyline polyline = mGoogleMap.addPolyline(MapsUtils.createPolylineOptions(new LatLng[]{latLng1, latLng3, latLng2})
                .color(0x7F000000)
                .geodesic(true));

        List<Polyline> polylines = new LinkedList<>();
        polylines.add(polyline);


        for (int i = 0; i < 4; i++) {

            Polyline polyline1 = polylines.get(i);
            List<LatLng> oldPoints = polyline1.getPoints();

            LatLng start = oldPoints.get(Constants.SOUTH_WEST);
            LatLng end = oldPoints.get(Constants.SOUTH_EAST);

            double heading = SphericalUtil.computeHeading(start, end) + 90;
            LatLng[] points =
                    MapsUtils.computeNewPath(polyline1, width, heading).toArray(new LatLng[oldPoints.size()]);

            polylines.add(
                    mGoogleMap.addPolyline(MapsUtils.createPolylineOptions(points)
                            .color(0x7FFF00FF)
                            .width(5)
                            .geodesic(true)
                    ));
        }

        Field field = new Field(polygon, polylines);

        mGoogleMap.moveCamera(MapsUtils.polygonToCameraUpdate(polygon));*/
    }

    public void startCreatingRoute() {
        route = new LinkedList<>();
        locationService.startRecordRoute();
    }

    public void finishCreatingRoute() {
        locationService.stopRecordRoute();
//        route = locationService.getRoute();

        LatLng latLng1 = new LatLng(50.097119d, 30.124142d);
        LatLng latLng2 = new LatLng(50.098466d, 30.125510d);
        LatLng latLng3 = new LatLng(50.099563d, 30.127152d);
        route.add(latLng1);
        route.add(latLng2);
        route.add(latLng3);
        if (route != null && route.size() > 1) {
            Polygon leftField = createField(route.get(0), route.get(route.size() - 1), Constants.WIDTH_METERS, true);
            Polygon rightField = createField(route.get(0), route.get(route.size() - 1), Constants.WIDTH_METERS, false);
            leftField.setClickable(true);
            rightField.setClickable(true);

            mGoogleMap.moveCamera(MapsUtils.polygonToCameraUpdate(leftField));

            routePolyline = mGoogleMap.addPolyline(MapsUtils.createPolylineOptions(route));

            harvestedPolygon = mGoogleMap
                    .addPolygon(MapsUtils.harvestedPolygonOptions(routePolyline)
                            .fillColor(Color.BLUE)
                            .strokeColor(Color.BLUE)
                            .geodesic(true));

            addHeatMap(routePolyline);

            createPolylines(routePolyline, Constants.HEADING_TO_LEFT);
            createPolylines(routePolyline, Constants.HEADING_TO_RIGHT);
        }
    }

    private Polygon createField(LatLng start, LatLng end, double width, boolean toLeft) {
        return mGoogleMap.addPolygon(MapsUtils.createFieldPolygonOptions(start, end, width, toLeft)
                .strokeColor(0x7FFF0000)
                .fillColor(0x7F00FF00)
                .geodesic(true));
    }

    private List<Polyline> createPolylines(Polyline oldPolyline, double heading) {
        List<Polyline> polylines = new LinkedList<>();
        polylines.add(oldPolyline);

        List<LatLng> oldPolylineList = oldPolyline.getPoints();
        //TODO Normal check
        LatLng start = oldPolylineList.get(0);
        LatLng end = oldPolylineList.get(oldPolylineList.size() - 1);

        double currentHeading = SphericalUtil.computeHeading(start, end) + heading;

        int transparentColor = 0x9FFF00FF;
        for (int i = 0; i < 4; i++) {
            transparentColor = transparentColor - 0x20000000;
            Polyline polyline1 = polylines.get(i);
            List<LatLng> oldPoints = polyline1.getPoints();
            LatLng[] points =
                    MapsUtils.computeNewPath(polyline1, Constants.WIDTH_METERS, currentHeading)
                            .toArray(new LatLng[oldPoints.size()]);


            polylines.add(
                    mGoogleMap.addPolyline(MapsUtils.createPolylineOptions(points)
                            .color(transparentColor)
                            .width(5)
                            .geodesic(true)
                    ));
        }
        return polylines;
    }


    private void addHeatMap(Polyline path) {
        List<LatLng> list = path.getPoints();
        HeatmapTileProvider mProvider = new HeatmapTileProvider.Builder()
                .data(list)
                .radius(10)
                .build();
        heatMap = mGoogleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        locationService.onActivityResult(requestCode, resultCode, data, mGoogleApiClient);
    }

}
