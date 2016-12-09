package casak.ru.geofencer.util;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.maps.android.SphericalUtil;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
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

    public static PolylineOptions createPolylineOptions(List<LatLng> latLngs) {
        return new PolylineOptions().add(latLngs.toArray(new LatLng[latLngs.size()]));
    }

    //TODO rename signature
    public static PolygonOptions createFieldPolygonOptions(LatLng start, LatLng end, double width, boolean toLeft) {
        double offset = width == 0 ? 0d : width / 2;
        return new PolygonOptions().add(computeCorners(start, end, offset, toLeft).toArray(new LatLng[4]));
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
        List<LatLng> points = polygon.getPoints();
        LatLng start = points.get(Constants.SOUTH_WEST);
        LatLng end = points.get(Constants.SOUTH_EAST);
        return CameraUpdateFactory.newCameraPosition(
                CameraPosition.builder()
                        .target(polygon.getPoints().get(Constants.SOUTH_WEST))
                        .zoom(16)
                        .bearing(Float.parseFloat(SphericalUtil.computeHeading(start, end)+""))
                        .build()
        );
    }

    public static PolygonOptions harvestedPolygonOptions(Polyline route){
        List<LatLng> points = route.getPoints();
        List<LatLng> upperBound = new LinkedList<>();
        List<LatLng> bottomBound = new LinkedList<>();
        List<LatLng> fullArea = new LinkedList<>();

        if(points.size() > 1) {
            LatLng start = points.get(0);
            LatLng end = points.get(points.size()-1);
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
            for (int i = bottomBound.size()-1; i >= 0; i--)
                fullArea.add(bottomBound.get(i));
            //TODO Handle this shit as a man
            if(fullArea.size() % 2 != 0)
                return null;
            return new PolygonOptions().add(fullArea.toArray(new LatLng[fullArea.size()]));
        }
        return null;
    }
}
