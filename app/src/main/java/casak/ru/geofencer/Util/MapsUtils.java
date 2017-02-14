package casak.ru.geofencer.util;

import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.maps.android.SphericalUtil;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import casak.ru.geofencer.domain.Constants;

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
                .color(Constants.ARROW_COLOR)
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
                .fillColor(Constants.FIELD_FILL_COLOR)
                .strokeColor(Constants.FIELD_STROKE_COLOR)
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
    public static CameraUpdate harvestedPolygonToCameraUpdate(List<LatLng> points) {
        if (points == null || points.size() < 1)
            return null;

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        boundsBuilder.include(points.get(0));
        boundsBuilder.include(points.get(points.size() - 1));
        LatLngBounds bounds = boundsBuilder.build();

        return CameraUpdateFactory.newLatLngBounds(bounds, 1);
    }

    public static CameraUpdate fieldPolygonToCameraUpdate(Polygon polygon) {
        List<LatLng> points = polygon.getPoints();
        if (points == null || points.size() < 4)
            return null;

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        boundsBuilder.include(points.get(Constants.NORTH_EAST));
        boundsBuilder.include(points.get(Constants.SOUTH_WEST));
        LatLngBounds bounds = boundsBuilder.build();

        return CameraUpdateFactory.newLatLngBounds(bounds, 100);
    }

    public static CameraUpdate latLngToCameraUpdate(LatLng latLng) {
        return CameraUpdateFactory.newLatLng(latLng);
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
            return new PolygonOptions()
                    .add(fullArea.toArray(new LatLng[fullArea.size()]))
                    .fillColor(Constants.HARVESTED_FILL_COLOR)
                    .strokeColor(Constants.HARVESTED_STROKE_COLOR)
                    .zIndex(Constants.HARVESTED_INDEX)
                    .geodesic(true);
        }
        return null;
    }

    public LatLng computeCentralPoint(LatLng start, LatLng end) {
        double distanceToCenter = SphericalUtil.computeDistanceBetween(start, end) / 2;
        double heading = SphericalUtil.computeHeading(start, end);
        return SphericalUtil.computeOffset(start, distanceToCenter, heading);
    }


    public static void mockLocations(LocationListener listener, int timeout, @Nullable LatLng... locations) {
        new MockLocationAsyncTask(listener, timeout).execute(locations);
    }

    private static class MockLocationAsyncTask extends AsyncTask<LatLng, Location, String> {
        List<Location> locations = new LinkedList<>();
        LocationListener listener;
        int timeout;

        public MockLocationAsyncTask(LocationListener locationListener, int timeout) {
            listener = locationListener;
            this.timeout = timeout;
        }

        private void initLocations() {
            String locationsString = "50.421355,30.4256428;" +
                    "50.4214449,30.4256972;" +
                    "50.4215316,30.4257533;" +
                    "50.421615,30.425807;" +
                    "50.4216995,30.4258595;" +
                    "50.4217846,30.4259127;" +
                    "50.4218679,30.4259648;" +
                    "50.421949,30.4260166;" +
                    "50.4220291,30.426066;" +
                    "50.422107,30.4261144;" +
                    "50.4223769,30.4262649;" +
                    "50.422469,30.4263184;" +
                    "50.4225544,30.4263715;" +
                    "50.4226348,30.4264244;" +
                    "50.4227066,30.4264715;" +
                    "50.4227738,30.4265161;" +
                    "50.4228396,30.426558;" +
                    "50.4229029,30.4265987;" +
                    "50.4229644,30.426637;" +
                    "50.423017,30.4266696;" +
                    "50.4230622,30.4266969;" +
                    "50.4231028,30.4267206;" +
                    "50.4231742,30.42676;" +
                    "50.4232012,30.426774;" +
                    "50.4232177,30.4267845;" +
                    "50.4232268,30.4267898;" +
                    "50.42323,30.4267916";
            String[] locationArray = locationsString.split(";");
            for (String aLocationArray : locationArray) {
                String[] locationSting = aLocationArray.split(",");
                Location location = new Location(LocationManager.GPS_PROVIDER);
                location.setLatitude(Double.parseDouble(locationSting[0]));
                location.setLongitude(Double.parseDouble(locationSting[1]));
                locations.add(location);
            }
        }

        private void initLocations(LatLng... locationArray) {
            List<Location> reverse = new LinkedList<>();
            for (LatLng point : locationArray) {
                Location location = new Location(LocationManager.GPS_PROVIDER);
                location.setLatitude(point.latitude);
                location.setLongitude(point.longitude);
                reverse.add(location);
            }

            for (int i = reverse.size() - 1; i > 0; i--) {
                locations.add(reverse.get(i));
            }
        }

        @Override
        protected String doInBackground(LatLng... locationArray) {
            if (locationArray.length == 0)
                initLocations();
            else
                initLocations(locationArray);

            for (Location point : locations) {
                point.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
                point.setTime(System.currentTimeMillis());
                point.setAccuracy(1);
                publishProgress(point);
                try {
                    Thread.sleep(timeout);
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

        @Override
        protected void onPostExecute(String s) {
            Log.d("All mocked locations ", "are passed to the app");
        }
    }
}
