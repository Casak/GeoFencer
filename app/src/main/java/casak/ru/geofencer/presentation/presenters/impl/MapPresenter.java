package casak.ru.geofencer.presentation.presenters.impl;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
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
import java.util.LinkedList;
import java.util.List;

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
import casak.ru.geofencer.storage.RouteRepositoryImpl;
import casak.ru.geofencer.presentation.presenters.IMapPresenter;
import casak.ru.geofencer.presentation.presenters.base.AbstractPresenter;
import casak.ru.geofencer.presentation.ui.activities.MapActivity;
import casak.ru.geofencer.presentation.ui.fragment.DeltaFragment;
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

    private DeltaFragment deltaFragment;

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

        deltaFragment = (DeltaFragment) ((MapActivity) context)
                .getSupportFragmentManager()
                .findFragmentById(R.id.delta_fragment);

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

    private class OnClickListener implements View.OnClickListener {
        boolean firstClick = true;

        @Override
        public void onClick(View view) {
            if (firstClick) {
                firstClick = false;
                harvester.startFieldRouteBuilding();
                //MapsUtils.mockLocations(getLocationListener());
            } else {
                firstClick = true;
                harvester.finishFieldRouteBuilding();
            }
        }
    }

    private List<Location> locations = new LinkedList<>();

    private class MapLocationListener implements LocationListener {
        private Location previous = new Location(LocationManager.GPS_PROVIDER);

        @Override
        public void onLocationChanged(Location location) {
            if (location.equals(previous))
                return;
            else {
                locations.add(location);
                previous = location;
            }

            LatLng currentLocation = new LatLng(location.getLatitude(),
                    location.getLongitude());

            //TODO Move to pointer interactor
            MapActivity.updateCTE(computePointerNew(location)+"");

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

       /* private void insertDataToProvider(Location location, Uri contentUri) {
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
*/
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

            return MapsUtils.createArrow(routeCenter, distanceBetween + 10, heading, toLeft);
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
            notHarvestedRoutes = createComputedPolylines(harvester.getFieldBuildingPolyline(),
                    Constants.HEADING_TO_LEFT);
        }
        if (polyline.equals(rightArrow)) {
            field1 = createField(start, end, false);
            notHarvestedRoutes = createComputedPolylines(harvester.getFieldBuildingPolyline(),
                    Constants.HEADING_TO_RIGHT);
        }
        if (polyline.equals(leftArrow) || polyline.equals(rightArrow)) {
            //TODO Delete this mock
            List<LatLng> list = new ArrayList<>();

            list.add(new LatLng(50.421403047796304, 30.425471959874532));
            list.add(new LatLng(50.421492947796295, 30.425563359499005));
            list.add(new LatLng(50.4215796477963, 30.425650459136847));
            list.add(new LatLng(50.4216630477963, 30.42568115878848));
            list.add(new LatLng(50.4217475477963, 30.42573765843551));
            list.add(new LatLng(50.42183264779631, 30.42579085808003));
            list.add(new LatLng(50.421915947796315, 30.425823957732078));
            list.add(new LatLng(50.42199704779629, 30.4258847573933));
            list.add(new LatLng(50.422077147796294, 30.425945157058703));
            list.add(new LatLng(50.42215504779629, 30.42599855673329));
            list.add(new LatLng(50.42242494779629, 30.42616205560584));
            list.add(new LatLng(50.422517047796305, 30.426176555221105));
            list.add(new LatLng(50.4226024477963, 30.42622365486436));
            list.add(new LatLng(50.422682847796295, 30.42625355452849));
            list.add(new LatLng(50.4227546477963, 30.426359654228563));
            list.add(new LatLng(50.4228218477963, 30.426372253947832));
            list.add(new LatLng(50.4228876477963, 30.426372153672965));
            list.add(new LatLng(50.422950947796274, 30.426433853408525));
            list.add(new LatLng(50.423012447796296, 30.42652715315161));
            list.add(new LatLng(50.42306504779629, 30.426528752931883));
            list.add(new LatLng(50.42311024779629, 30.426541052743065));
            list.add(new LatLng(50.42315084779628, 30.426602752573455));
            list.add(new LatLng(50.423222247796296, 30.42662315227518));
            list.add(new LatLng(50.42324924779629, 30.42659715216239));
            list.add(new LatLng(50.42326574779629, 30.42663665209346));
            list.add(new LatLng(50.42327484779629, 30.42663695205544));
            list.add(new LatLng(50.423281180329916, 30.426680696850383));


            //MapsUtils.mockLocations(getLocationListener(),
            //        list.toArray(new LatLng[list.size()]));

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




    //TODO Move to interactor
    //Pointer

    private RouteModel currentRouteModel;

    public double computePointerNew(Location position) {
        Point pointPosition = convertLocationToPoint(position);

        RouteModel nearestRoute = getCurrentRoute(position);

        if (nearestRoute == null)
            return 0;

        int index = nearestRoute.getRoutePoints().indexOf(getNearestPoint(nearestRoute.getRoutePoints(), pointPosition));

        nearestRoute.setRoutePoints(nearestRoute.getRoutePoints().subList(index, nearestRoute.getRoutePoints().size()));

        List<Point> nearestPoints = getNearAndNextPoints(nearestRoute.getRoutePoints(), pointPosition);

        int nearestPointsSize = nearestPoints.size();

        Log.d(TAG, "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        Log.d(TAG, "Position point: " + pointPosition.getLatitude() + ", " + pointPosition.getLongitude());

        double result = 0;
        switch (nearestPointsSize) {
            case 0:
                break;
            case 1:

                Log.d(TAG, "routeCurrent point: " + nearestPoints.get(0).getLatitude()
                        + ", " + nearestPoints.get(0).getLongitude());
                result = crossTrackError(nearestPoints.get(0), nearestPoints.get(0), pointPosition);
                break;
            case 2:
                Log.d(TAG, "routeCurrent point: " + nearestPoints.get(0).getLatitude()
                        + ", " + nearestPoints.get(0).getLongitude());
                Log.d(TAG, "routeNext point: " + nearestPoints.get(1).getLatitude()
                        + ", " + nearestPoints.get(1).getLongitude());
                result = crossTrackError(nearestPoints.get(0), nearestPoints.get(1), pointPosition);
        }
        Log.d(TAG, "CTE: " + result);
        return result;
    }

    public RouteModel getCurrentRoute(Location position) {
        Point pointPosition = convertLocationToPoint(position);

        if (!isStillCurrentRoute(pointPosition)) {
            RouteModel result = getNearestRoute(position);
            if (result == null)
                return null;
            double distanceToStart = MapUtils.computeDistanceBetween(
                    pointPosition,
                    result.getRoutePoints().get(0)
            );
            double distanceToEnd = MapUtils.computeDistanceBetween(
                    pointPosition,
                    result.getRoutePoints().get(result.getRoutePoints().size() - 1)
            );
            if (distanceToEnd < distanceToStart)
                result.setRoutePoints(reverseList(result.getRoutePoints()));
            setCurrentRoute(result);
            return result;
        }
        return currentRouteModel;
    }

    public boolean isStillCurrentRoute(Point location) {
        return currentRouteModel != null &&
                MapUtils.isLocationOnPath(
                        location,
                        currentRouteModel.getRoutePoints(),
                        true,
                        Constants.WIDTH_METERS / 2
                );
    }

    //TODO Check it
    public RouteModel getNearestRoute(Location location) {
        if (location == null || location.getLatitude() == 0 || location.getLongitude() == 0)
            return null;

        List<RouteModel> computedRoutes = getComputedRoutes(0); //fieldID

        if (computedRoutes == null || computedRoutes.size() == 0)
            return null;

        Point position = convertLocationToPoint(location);
        for (RouteModel model : computedRoutes)
            if (MapUtils.isLocationOnPath(
                    position,
                    model.getRoutePoints(),
                    true,
                    Constants.WIDTH_METERS / 2
            ))
                return model;

        /*double[] distances = new double[computedRoutes.size()];
        Point from = new Point(location.getLatitude(), location.getLongitude());
        for (int i = 0; i < computedRoutes.size(); i++) {
            List<Point> routePoints = computedRoutes.get(i).getRoutePoints();
            if (routePoints == null || routePoints.size() < 2)
                return null;

            Point start = routePoints.get(0);
            Point end = routePoints.get(routePoints.size() - 1);

            double distance1 = MapUtils.computeDistanceBetween(from, start);
            double distance2 = MapUtils.computeDistanceBetween(from, end);
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

        return result != null ? result : null;*/
        return null;
    }

    //TODO When moved - normal implementation
    public List<RouteModel> getComputedRoutes(int fieldId) {
        List<RouteModel> result = new ArrayList<>();
        if (notHarvestedRoutes != null && notHarvestedRoutes.size() > 0) {
            for (int i = 0; i < notHarvestedRoutes.size(); i++) {
                result.add(new RouteModel(i,
                        fieldId, RouteModel.Type.COMPUTED,
                        latLngToPoint(notHarvestedRoutes.get(i).getPoints())));
            }
            return result;
        }

        return new RouteRepositoryImpl().getAllRoutes(fieldId);
    }

    public void setCurrentRoute(RouteModel route) {
        currentRouteModel = route;
    }

    public Point getNearestPoint(List<Point> routePoints, Point current) {
        int routeSize = routePoints.size();

        if (routeSize == 1) {
            return routePoints.get(0);
        }

        if (routeSize == 2) {
            Point start = routePoints.get(0);
            Point end = routePoints.get(1);
            return getNearestPoint(start, end, current);
        }

        if (routeSize > 2)
            for (int i = 0; i < routePoints.size() - 1; i++) {
                Point start = routePoints.get(i);
                Point next = routePoints.get(i + 1);
                if (getNearestPoint(start, next, current) == start) {
                    double distanceCurrentAndNext = MapUtils.computeDistanceBetween(start, next);
                    double distancePositionAndNext = MapUtils.computeDistanceBetween(current, next);
                    double distancePositionAndCurrent = MapUtils.computeDistanceBetween(current, start);
                    double delta = distancePositionAndNext - (distanceCurrentAndNext + distancePositionAndCurrent);
                    if (delta < 0)
                        return next;
                    return start;
                }
            }
        return routePoints.get(0);
    }

    public Point getNearestPoint(Point start, Point end, Point current) {
        double distanceToStart = MapUtils.computeDistanceBetween(current, start);
        double distanceToEnd = MapUtils.computeDistanceBetween(current, end);

        return distanceToStart < distanceToEnd ? start : end;
    }

    //TODO Test list`s top bound; possibly move logic into computePointer
    public List<Point> getNearAndNextPoints(List<Point> routePoints, Point current) {
        int routeSize = routePoints.size();
        if (routeSize == 0 || routeSize == 1)
            return routePoints;

        Point nearestPoint = getNearestPoint(routePoints, current);
        int indexNearest = routePoints.indexOf(nearestPoint);
        int indexNext = indexNearest + 1;

        List<Point> result = new ArrayList<>();
        result.add(nearestPoint);

        if (indexNext != routeSize)
            result.add(routePoints.get(indexNext));
        return result;
    }

    public double crossTrackError(Point routeCurrent, Point routeNext, Point position) {
        double brng13 = MapUtils.computeHeading(routeCurrent, position); // in degrees
        double brng12 = MapUtils.computeHeading(routeCurrent, routeNext); //  in degrees
        double dist13 = MapUtils.computeDistanceBetween(routeCurrent, position); // in kilometers
        return Math.asin(Math.sin(dist13 / 6371009.0D) * Math.sin(Math.toRadians(brng13 - brng12))) * 6371009.0D;
    }

    private Point convertLocationToPoint(Location location) {
        return new Point(location.getLatitude(), location.getLongitude());
    }

    private List<Point> reverseList(List<Point> list) {
        List<Point> result = new LinkedList<>();
        for (int i = list.size() - 1; i >= 0; i--)
            result.add(list.get(i));

        return result;
    }

    private List<Point> latLngToPoint(List<LatLng> latLngs) {
        List<Point> result = new ArrayList<>();
        for (LatLng point : latLngs) {
            result.add(new Point(point.latitude, point.longitude));
        }
        return result;
    }
}
