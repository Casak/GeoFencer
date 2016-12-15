package casak.ru.geofencer.util;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.SphericalUtil;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import casak.ru.geofencer.Constants;


/**
 * Created by Casak on 06.12.2016.
 */

public class MapsUtils {

    static final double FIELD_HEIGHT_METERS = 100;
    static final double EARTH_RADIUS_METERS = 6371.008 * 1000;


    public static PolylineOptions createPolylineOptions(LatLng... latLngs) {
        return new PolylineOptions().add(latLngs);
    }

    public static PolylineOptions createArrow(LatLng routeCenter, double routeDistance, double routeHeading, boolean toLeft) {
        List<LatLng> result = new LinkedList<>();

        result.add(routeCenter);

        double headingArrowToTop;

        if (toLeft) {
            headingArrowToTop = routeHeading + Constants.HEADING_TO_LEFT;
        } else {
            headingArrowToTop = routeHeading + Constants.HEADING_TO_RIGHT;
        }

        double headingArrowToLeft = headingArrowToTop + 180 + 30;
        double headingArrowToRight = headingArrowToTop + 180 - 30;

        LatLng leftArrowTop = SphericalUtil.computeOffset(routeCenter, routeDistance / 2, headingArrowToTop);
        LatLng leftArrowFromTopToLeft = SphericalUtil.computeOffset(leftArrowTop, routeDistance / 4, headingArrowToLeft);
        LatLng leftArrowFromTopToRight = SphericalUtil.computeOffset(leftArrowTop, routeDistance / 4, headingArrowToRight);

        result.add(leftArrowTop);
        result.add(leftArrowFromTopToLeft);
        result.add(leftArrowTop);
        result.add(leftArrowFromTopToRight);
        result.add(leftArrowTop);

        return new PolylineOptions()
                .addAll(result)
                .width(20f)
                .color(0x7FF4F142)
                .geodesic(true)
                .clickable(true);
    }

    public static PolylineOptions createPolylineOptions(List<LatLng> latLngs) {
        return new PolylineOptions()
                .add(latLngs.toArray(new LatLng[latLngs.size()]))
                .geodesic(true);
    }

    //TODO rename signature
    public static PolygonOptions createFieldPolygonOptions(LatLng start, LatLng end, double width, boolean toLeft) {
        double offset = width == 0 ? 0d : width / 2;
        return new PolygonOptions()
                .add(computeCorners(start, end, offset, toLeft).toArray(new LatLng[4]))
                .strokeColor(0x7F00F000)
                .fillColor(0x7F00FF00)
                .geodesic(true);
    }

    public static List<LatLng> computeCorners(LatLng start, LatLng end, double offset, boolean toLeft) {
        double heading = SphericalUtil.computeHeading(start, end);

        List<LatLng> result = new ArrayList<>(4);

        double heading1, heading2;
        if (toLeft) {
            heading1 = 90;
            heading2 = -90;
        } else {
            heading1 = -90;
            heading2 = 90;
        }

        LatLng cornerSouthWest = SphericalUtil.computeOffset(start, offset, heading + heading1);
        LatLng cornerNorthWest = SphericalUtil.computeOffset(cornerSouthWest, FIELD_HEIGHT_METERS, heading + heading2);
        LatLng cornerSouthEast = SphericalUtil.computeOffset(end, offset, heading + heading1);
        LatLng cornerNorthEast = SphericalUtil.computeOffset(cornerSouthEast, FIELD_HEIGHT_METERS, heading + heading2);

        result.add(cornerNorthWest);
        result.add(cornerSouthWest);
        result.add(cornerSouthEast);
        result.add(cornerNorthEast);
        return result;
    }


    //TODO rename
    public static List<LatLng> computeNewPath(Polyline polyline, double width, double heading) {
        List<LatLng> points = polyline.getPoints();
        List<LatLng> result = new ArrayList<>(points.size());

        for (LatLng point : points) {
            LatLng newPoint = SphericalUtil.computeOffset(point, width, heading);
            result.add(newPoint);
        }
        return result;
    }

    //TODO implement
    public static CameraUpdate polygonToCameraUpdate(Polygon polygon) {
        return CameraUpdateFactory.newCameraPosition(
                CameraPosition.builder()
                        .target(polygon.getPoints().get(Constants.SOUTH_WEST))
                        .zoom(16)
                        .build()
        );
    }

    public static PolygonOptions harvestedPolygonOptions(Polyline route) {
        List<LatLng> points = route.getPoints();
        List<LatLng> upperBound = new LinkedList<>();
        List<LatLng> bottomBound = new LinkedList<>();
        List<LatLng> fullArea = new LinkedList<>();

        if (points.size() > 1) {
            LatLng start = points.get(0);
            LatLng end = points.get(points.size() - 1);
            double heading = SphericalUtil.computeHeading(start, end);

            for (LatLng point : points) {
                upperBound.add(SphericalUtil.computeOffset(point,
                        Constants.WIDTH_METERS / 2,
                        heading + 90));
                bottomBound.add(SphericalUtil.computeOffset(point,
                        Constants.WIDTH_METERS / 2,
                        heading - 90));
            }
            fullArea.addAll(upperBound);
            for (int i = bottomBound.size() - 1; i >= 0; i--)
                fullArea.add(bottomBound.get(i));
            //TODO Handle this shit as a man
            if (fullArea.size() % 2 != 0)
                return null;
            return new PolygonOptions()
                    .add(fullArea.toArray(new LatLng[fullArea.size()]))
                    .geodesic(true);
        }
        return null;
    }


    public static void startMockingLocations(final LocationListener listener) {

        (new AsyncTask<String, Location, String>(){
            List<Location> locations = new LinkedList<>();

            private void initLocations(){
                String locationsString = "50.077209,30.041981;" +
                        "50.077113,30.041895;" +
                        "50.077086,30.041724;" +
                        "50.077044,30.041552;" +
                        "50.076962,30.041466;" +
                        "50.076920,30.041295;" +
                        "50.076893,30.041209;" +
                        "50.076879,30.041123;" +
                        "50.076810,30.040951;" +
                        "50.076810,30.040865;" +
                        "50.076769,30.040780;" +
                        "50.076727,30.040694;" +
                        "50.076727,30.040608;" +
                        "50.076714,30.040522;" +
                        "50.076686,30.040436;" +
                        "50.076659,30.040265;" +
                        "50.076604,30.040179;" +
                        "50.076548,30.039943;" +
                        "50.076521,30.039685;" +
                        "50.076480,30.039599;" +
                        "50.076425,30.039363;" +
                        "50.076383,30.039235;" +
                        "50.076328,30.039106;" +
                        "50.076273,30.038977;" +
                        "50.076163,30.038720;" +
                        "50.076122,30.038484;" +
                        "50.076094,30.038291;" +
                        "50.076053,30.038119;" +
                        "50.075998,30.037947;" +
                        "50.075929,30.037776;" +
                        "50.075901,30.037539;" +
                        "50.075846,30.037282;" +
                        "50.075722,30.037089;" +
                        "50.075667,30.036746;" +
                        "50.075516,30.036338;" +
                        "50.075474,30.036016;" +
                        "50.075439,30.035932;" +
                        "50.075370,30.035675;" +
                        "50.075308,30.035525;" +
                        "50.075273,30.035353;" +
                        "50.075198,30.035181;" +
                        "50.075136,30.034935;" +
                        "50.075067,30.034795;" +
                        "50.075005,30.034538;" +
                        "50.074929,30.034291;" +
                        "50.074874,30.034087;" +
                        "50.074860,30.033947;" +
                        "50.074805,30.033765;" +
                        "50.074771,30.033615;" +
                        "50.074771,30.033465;" +
                        "50.074750,30.033336;" +
                        "50.074674,30.033100;" +
                        "50.074612,30.032950;" +
                        "50.074571,30.032778;" +
                        "50.074516,30.032563;" +
                        "50.074461,30.032306;" +
                        "50.074406,30.032156;" +
                        "50.074364,30.031963;" +
                        "50.074337,30.031802;" +
                        "50.074275,30.031684;" +
                        "50.074192,30.031405;" +
                        "50.074165,30.031233;" +
                        "50.074130,30.031051;" +
                        "50.074068,30.030868;" +
                        "50.074041,30.030697;" +
                        "50.074013,30.030546;" +
                        "50.073993,30.030504;" +
                        "50.073944,30.030418;" +
                        "50.073924,30.030310;" +
                        "50.073903,30.030203;" +
                        "50.073855,30.029967;" +
                        "50.073820,30.029881;" +
                        "50.073738,30.029645;" +
                        "50.073703,30.029559;" +
                        "50.073683,30.029420;" +
                        "50.073641,30.029366";
                String[] locationArray = locationsString.split(";");
                for (String aLocationArray : locationArray) {
                    String[] locationSting = aLocationArray.split(",");
                    Location location = new Location(LocationManager.GPS_PROVIDER);
                    location.setLatitude(Double.parseDouble(locationSting[0]));
                    location.setLongitude(Double.parseDouble(locationSting[1]));
                    locations.add(location);
                }
            }

            @Override
            protected String doInBackground(String... string) {
                initLocations();
                for (Location point : locations) {
                    point.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
                    point.setTime(System.currentTimeMillis());
                    point.setAccuracy(1);
                    publishProgress(point);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Location... point) {
                listener.onLocationChanged(point[0]);
            }
        }).execute("");
    }
}
