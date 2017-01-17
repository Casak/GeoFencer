package casak.ru.geofencer.presentation.presenters.impl;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import casak.ru.geofencer.BluetoothAntennaLocationSource;
import casak.ru.geofencer.R;
import casak.ru.geofencer.domain.Constants;
import casak.ru.geofencer.domain.executor.Executor;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.domain.interactors.impl.MapUtils;
import casak.ru.geofencer.domain.model.FieldModel;
import casak.ru.geofencer.domain.model.HarvesterModel;
import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.model.RouteModel;
import casak.ru.geofencer.domain.repository.db.Contract;
import casak.ru.geofencer.domain.repository.impl.RouteRepositoryImpl;
import casak.ru.geofencer.presentation.presenters.IMapPresenter;
import casak.ru.geofencer.presentation.presenters.base.AbstractPresenter;
import casak.ru.geofencer.presentation.ui.activities.MapActivity;
import casak.ru.geofencer.service.LocationService;
import casak.ru.geofencer.util.MapsUtils;

/**
 * Created on 08.12.2016.
 */

public class MapPresenter extends AbstractPresenter implements IMapPresenter, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, GoogleMap.OnPolylineClickListener {

    private static final String TAG = MapPresenter.class.getSimpleName();

    private Context context;
    private GoogleMap mGoogleMap;
    private static GoogleApiClient mGoogleApiClient;
    private LocationService locationService;
    private LocationSource locationSource;
    private View.OnClickListener onClickListener;
    private LocationListener mapLocationListener;

    //TODO Inject
    private FieldModel field;
    private HarvesterModel harvester;

    public MapPresenter(Context context, Executor executor, MainThread mainThread) {
        super(executor, mainThread);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }

        this.context = context;

        locationSource = new BluetoothAntennaLocationSource();

        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        LatLng currentLatLng = null;
        if (currentLocation != null)
            currentLatLng = new LatLng(currentLocation.getLatitude(),
                    currentLocation.getLongitude());

        harvester = new HarvesterModel(this, currentLatLng);
        field = new FieldModel(1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //TODO Possible NullPointer
        locationService.onActivityResult(requestCode, resultCode, data, mGoogleApiClient);
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
            locationService = new LocationService(context, mGoogleApiClient, getLocationListener());
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
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mGoogleMap.setOnPolylineClickListener(this);
        //TODO Create a not hardcoded version
        LatLng geoCentrUkraine = new LatLng(48.9592699d, 32.8723257d);
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(geoCentrUkraine, 6f));
        mGoogleMap.setLocationSource(locationSource);
        mGoogleMap.setMyLocationEnabled(true);
    }

    public void finishCreatingRoute(List<LatLng> route) {
        initBuildingField(route);
    }

    @Nullable
    public Marker showMarker(MarkerOptions options) {
        return options == null ? null : mGoogleMap.addMarker(options);
    }

    @Nullable
    public Polyline showPolyline(PolylineOptions options) {
        return options == null ? null : mGoogleMap.addPolyline(options);
    }

    @Nullable
    public Polygon showPolygon(PolygonOptions options) {
        return options == null ? null : mGoogleMap.addPolygon(options);
    }

    @Nullable
    public TileOverlay showTileOverlay(TileOverlayOptions options) {
        return options == null ? null : mGoogleMap.addTileOverlay(options);
    }

    public void removeMarker(Marker marker) {
        marker.setVisible(false);
        marker.remove();
    }

    public void removePolyline(Polyline polyline) {
        polyline.setVisible(false);
        polyline.remove();
    }

    public void removePolygon(Polygon polygon) {
        polygon.setVisible(false);
        polygon.remove();
    }

    public void removeTileOverlay(TileOverlay tileOverlay) {
        tileOverlay.setVisible(false);
        tileOverlay.remove();
    }

    public void moveCamera(CameraUpdate cameraUpdate) {
        mGoogleMap.moveCamera(cameraUpdate);
    }

    public void animateCamera(CameraUpdate cameraUpdate) {
        mGoogleMap.animateCamera(cameraUpdate);
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener == null ? onClickListener = new OnClickListener() : onClickListener;
    }

    public LocationListener getLocationListener() {
        return mapLocationListener == null ? mapLocationListener = new MapLocationListener() : mapLocationListener;
    }

    public double[] computePointer(Location location) {
        if (location == null || location.getLatitude() == 0 || location.getLongitude() == 0)
            return null;

        RouteModel nearestRoute = getNearestRoute(location);
        if (nearestRoute == null)
            return null;
        List<Point> routePoints = nearestRoute.getRoutePoints();

        Point current = new Point(location.getLatitude(), location.getLongitude());

        double pointerFromFirstApproach = computingFirstApproach(routePoints.get(0),
                routePoints.get(routePoints.size() - 1),
                current);
        double pointerFromSecondApproach = computingSecondApproach(routePoints, current);
        double pointerFromThirdApproach = computingThirdApproach(routePoints, current);
        double pointerFromForthApproach = computingForthApproach(routePoints, current);

        return new double[]{
                pointerFromFirstApproach,
                pointerFromSecondApproach,
                pointerFromThirdApproach,
                pointerFromForthApproach
        };
    }

    public double computingFirstApproach(Point start, Point end, Point current) {
        double distanceToStart = MapUtils.computeDistanceBetween(current, start);
        double distanceToEnd = MapUtils.computeDistanceBetween(current, end);

        if (distanceToStart < distanceToEnd)
            return computeDelta(start, end, current);
        else
            return computeDelta(end, start, current);
    }

    public double computeDelta(Point start, Point end, Point current) {
        double routeHeading = MapUtils.computeHeading(start, end);
        double currentHeading = MapUtils.computeHeading(current, end);
        return currentHeading - routeHeading;
    }

    public double computingSecondApproach(List<Point> routePoints, Point current) {
        List<Point> nearest = getNearestPoints(routePoints, current);
        Point pointPrev = nearest.get(0);
        Point pointNear = nearest.get(1);

        double currentHeading = MapUtils.computeHeading(current, pointNear);
        double routeHeading;
        if (nearest.size() == 3 || pointPrev != pointNear) {
            Point pointNext = nearest.get(2);
            routeHeading = MapUtils.computeHeading(pointNear, pointNext);
        } else
            routeHeading = MapUtils.computeHeading(pointPrev, pointNear);
        return currentHeading - routeHeading;
    }

    public double computingThirdApproach(List<Point> routePoints, Point current) {
        if (routePoints.size() > 2) {
            Point prevPoint = routePoints.get(0);
            for (Point point : routePoints.subList(1, routePoints.size())) {
                double angle = computeAngleBetweenPointAndLine(prevPoint, point, current);
                if (angle < 90)
                    return computeDelta(prevPoint, point, current);
            }
        }
        return 0;
    }

    public double computingForthApproach(List<Point> routePoints, Point current) {
        List<Point> nearest = getNearestPoints(routePoints, current);
        Point pointPrev = nearest.get(0);
        Point pointNear = nearest.get(1);

        if (nearest.size() == 3 || pointPrev != pointNear) {
            double angle = computeAngleBetweenPointAndLine(pointPrev, pointNear, current);
            if (angle < 90)
                return computeDelta(pointPrev, pointNear, current);
        }
        return 0;
    }

    public double computeAngleBetweenPointAndLine(Point lineStart, Point lineEnd, Point point) {
        double lineAngle = Math.abs(MapUtils.computeHeading(lineStart, lineEnd));
        double pointToLineAngle = Math.abs(MapUtils.computeHeading(point, lineEnd));
        return lineAngle - pointToLineAngle;
    }

    //TODO Test list`s top bound
    public List<Point> getNearestPoints(List<Point> routePoints, Point current) {
        Point nearestPoint = getNearestPoint(routePoints, current);
        int indexNearest = routePoints.indexOf(nearestPoint);
        int indexPrevious = indexNearest - 1;
        int indexNext = indexNearest + 1;
        int routeSize = routePoints.size();

        List<Point> result = new ArrayList<>();

        if (indexPrevious >= 0) {
            if (routeSize < 3) {
                result.add(nearestPoint);
                result.add(nearestPoint);

                if (routeSize > 1)
                    result.add(routePoints.get(routeSize - 1));
                return result;
            }
            if (indexNext == routeSize) {
                result.addAll(routePoints.subList(indexPrevious, routeSize));
                result.add(routePoints.get(routeSize - 1));
                return result;
            }
            if (indexNext < routeSize)
                return routePoints.subList(indexPrevious, ++indexNext);
            return routePoints.subList(indexPrevious, routeSize);
        }
        result.add(nearestPoint);
        result.add(nearestPoint);
        result.add(routePoints.get(indexNext));
        return result;

    }

    public Point getNearestPoint(List<Point> routePoints, Point current) {
        int routeSize = routePoints.size();

        if (routeSize == 1) {
            return routePoints.get(0);
        }

        if (routeSize == 2) {
            Point start = routePoints.get(0);
            Point end = routePoints.get(1);

            double distanceToStart = MapUtils.computeDistanceBetween(current, start);
            double distanceToEnd = MapUtils.computeDistanceBetween(current, end);

            return distanceToStart < distanceToEnd ? start : end;
        }

        if (routeSize > 2) {
            Point start = routePoints.get(0);
            Point end = routePoints.get(routeSize - 1);


            double distanceToStart = MapUtils.computeDistanceBetween(current, start);
            double distanceToEnd = MapUtils.computeDistanceBetween(current, end);

            routePoints = distanceToStart < distanceToEnd ?
                    routePoints.subList(0, routeSize / 2) : routePoints.subList(routeSize / 2, routeSize);
        }
        return getNearestPoint(routePoints, current);
    }

    public RouteModel getNearestRoute(Location location) {
        if (location == null || location.getLatitude() == 0 || location.getLongitude() == 0)
            return null;

        List<RouteModel> computedRoutes = getComputedRoutes(0); //fieldID

        if (computedRoutes == null || computedRoutes.size() == 0)
            return null;

        double[] distances = new double[computedRoutes.size()];
        for (int i = 0; i < computedRoutes.size(); i++) {
            List<Point> routePoints = computedRoutes.get(i).getRoutePoints();
            if (routePoints == null || routePoints.size() < 2)
                return null;

            Point from = new Point(location.getLatitude(), location.getLongitude());
            Point to1 = routePoints.get(0);
            Point to2 = routePoints.get(routePoints.size() - 1);

            double distance1 = MapUtils.computeDistanceBetween(from, to1);
            double distance2 = MapUtils.computeDistanceBetween(from, to2);
            double distance = distance1 < distance2 ? distance1 : distance2;
            distances[i] = distance;
        }

        RouteModel result = null;
        double previous = Double.MAX_VALUE;
        for (int i = 0; i < distances.length; i++) {
            if (distances[i] < previous)
                result = computedRoutes.get(i);
            previous = distances[i];
        }

        return result != null ? result : null;
    }

    public List<RouteModel> getComputedRoutes(int fieldId) {
        return new RouteRepositoryImpl().getAllRoutes(fieldId);
    }


    private class OnClickListener implements View.OnClickListener {
        boolean firstClick = true;

        @Override
        public void onClick(View view) {
            if (firstClick) {
                firstClick = false;
                harvester.startFieldRouteBuilding();
                MapsUtils.mockLocations(getLocationListener());
            } else {
                firstClick = true;
                harvester.finishFieldRouteBuilding();
            }
        }
    }

    private class MapLocationListener implements LocationListener {
        private List<Location> lastLocations = new LinkedList<>();
        private Location previous = new Location(LocationManager.GPS_PROVIDER);

        @Override
        public void onLocationChanged(Location location) {
            if (location.equals(previous))
                return;
            else
                previous = location;

            LatLng currentLocation = new LatLng(location.getLatitude(),
                    location.getLongitude());

            double[] deltas = computePointer(location);


            harvester.updateCurrentLocation(currentLocation);

            BluetoothAntennaLocationSource.getListener().onLocationChanged(location);
            Log.d("TAG", "Current location = " + location.getLatitude() +
                    ", " + location.getLongitude());
            /*insertDataToProvider(location, Contract.CoordEntry.CONTENT_URI);
            if (haveToInsert(location))
                insertDataToProvider(location, Contract.FilteredCoordEntry.CONTENT_URI);

            Log.d("TAG", "Current location = " + location.getLatitude() +
                    ", " + location.getLongitude());
            Toast.makeText(context, "Current location = " + location.getLatitude() +
                    ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();*/
        }

        private void insertDataToProvider(Location location, Uri contentUri) {
            ContentValues value = new ContentValues();
            value.put(Contract.CoordEntry.COLUMN_LAT, location.getLatitude());
            value.put(Contract.CoordEntry.COLUMN_LNG, location.getLongitude());
            value.put(Contract.CoordEntry.COLUMN_ALT, location.getAltitude());
            context.getContentResolver().insert(contentUri, value);
            ((MapActivity) context).showToast("Location with latitude: " + location.getLatitude() +
                    "; longitude: " + location.getLongitude() +
                    "; INSERTED");
        }

        private boolean isBearingDifferent(float first, float last) {
            return Math.abs(first - last) > Constants.FILTER_HEADING_DIFFERENCE;
        }

        private LatLng convertLocationToLatLng(Location location) {
            return new LatLng(location.getLatitude(), location.getLongitude());
        }

    }


    //FieldBuilding
    private List<LatLng> currentRoute;
    private Polyline leftArrow;
    private Polyline rightArrow;
    private List<Polyline> notHarvestedRoutes;
    private Polygon field1;

    //TODO delete this shit
    public void initBuildingField(List<LatLng> route) {
        currentRoute = route;
        leftArrow = showPolyline(createArrow(currentRoute, true));
        rightArrow = showPolyline(createArrow(currentRoute, false));

        CameraUpdate cameraUpdate = MapsUtils.harvestedPolygonToCameraUpdate(route);
        if (cameraUpdate != null)
            animateCamera(cameraUpdate);
    }

    private PolylineOptions createArrow(List<LatLng> route, boolean toLeft) {
        if (route.size() > 1) {
            LatLng start = route.get(0);
            LatLng end = route.get(route.size() - 1);

            double distanceBetween = SphericalUtil
                    .computeDistanceBetween(start, end);
            double heading = SphericalUtil.computeHeading(start, end);
            LatLng routeCenter = SphericalUtil.computeOffset(start, distanceBetween / 2, heading);

            return MapsUtils.createArrow(routeCenter, distanceBetween, heading, toLeft);
        }
        //TODO Normal error handling
        else
            return null;
    }

    @Override
    public void onPolylineClick(Polyline polyline) {
        LatLng start;
        LatLng end;
        if (leftArrow == null && rightArrow == null)
            return;
        else {
            start = currentRoute.get(0);
            end = currentRoute.get(currentRoute.size() - 1);
        }

        if (polyline.equals(leftArrow)) {
            field1 = createField(start, end, true);
            notHarvestedRoutes = createComputedPolylines(harvester.getHarvestedPolyline(),
                    Constants.HEADING_TO_LEFT);
        }
        if (polyline.equals(rightArrow)) {
            field1 = createField(start, end, false);
            notHarvestedRoutes = createComputedPolylines(harvester.getHarvestedPolyline(),
                    Constants.HEADING_TO_RIGHT);
        }
        if (polyline.equals(leftArrow) || polyline.equals(rightArrow)) {
            //TODO Delete this mock
//            List<LatLng> points = notHarvestedRoutes.get(1).getPoints();
//            points.remove(0);
//            points.remove(1);
//            points.remove(points.size() - 1);
//            points.remove(points.size() - 2);
//            MapsUtils.mockLocations(getLocationListener(),
//                    points.toArray(new LatLng[points.size()]));

            CameraUpdate cameraUpdate = MapsUtils.harvestedPolygonToCameraUpdate(notHarvestedRoutes.get(0).getPoints());
            if (cameraUpdate != null)
                animateCamera(cameraUpdate);
        }

        removeArrow(leftArrow);
        removeArrow(rightArrow);
        leftArrow = null;
        rightArrow = null;
    }

    private void removeArrow(Polyline arrow) {
        removePolyline(arrow);
    }

    private List<Polyline> createComputedPolylines(Polyline oldPolyline, double heading) {
        List<Polyline> routes = new LinkedList<>();
        routes.add(oldPolyline);

        List<LatLng> oldPolylineList = oldPolyline.getPoints();
        //TODO Normal check
        LatLng start = oldPolylineList.get(0);
        LatLng end = oldPolylineList.get(oldPolylineList.size() - 1);

        double computedHeading = SphericalUtil.computeHeading(start, end);
        double normalHeading = computedHeading + heading;
        double backwardHeading = computedHeading + 180;

        int transparentColor = Constants.COMPUTED_ROUTE_COLOR;
        boolean first = true;
        for (int i = 0; i < 4; i++) {
            transparentColor = transparentColor - 0x20000000;

            Polyline path = routes.get(i);

            List<LatLng> resultPoints = MapsUtils.computeNewPath(path,
                    Constants.WIDTH_METERS,
                    normalHeading);

            List<LatLng> points = new LinkedList<>();
            if (first)
                points.add(SphericalUtil.computeOffset(resultPoints.get(0),
                        Constants.WIDTH_METERS * 2,
                        backwardHeading));

            points.addAll(resultPoints);
            if (first) {
                first = false;
                points.add(SphericalUtil.computeOffset(resultPoints.get(resultPoints.size() - 1),
                        Constants.WIDTH_METERS * 2,
                        computedHeading));
            }

            routes.add(
                    showPolyline(MapsUtils.createPolylineOptions(points)
                            .color(transparentColor)
                            .width(5)
                            .geodesic(true)
                            .zIndex(Constants.ROUTE_INDEX)
                    ));
        }
        return routes;
    }

    private Polygon createField(LatLng start, LatLng end, boolean toLeft) {
        return showPolygon(MapsUtils.createFieldPolygonOptions(start,
                end,
                Constants.WIDTH_METERS,
                toLeft)
                .zIndex(Constants.FIELD_INDEX));
    }
}
