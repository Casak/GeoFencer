package casak.ru.geofencer.Util;

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
import java.util.List;

import casak.ru.geofencer.Constants;


/**
 * Created by Casak on 06.12.2016.
 */

public class MapsUtils {

    static final double FIELD_HEIGHT_METERS = 1000;
    static final double EARTH_RADIUS_METERS = 6371.008 * 1000;


    public static PolylineOptions createPolylineOptions(LatLng... latLngs) {
        return new PolylineOptions().add(latLngs);
    }

    //TODO rename signature
    public static PolygonOptions createFieldPolygonOptions(LatLng start, LatLng end, double width) {
        double offset = width == 0 ? 0d : width / 2;
        return new PolygonOptions().add(computeCorners(start, end, offset).toArray(new LatLng[4]));
    }

    public static List<LatLng> computeCorners(LatLng start, LatLng end, double offset) {
        double heading = SphericalUtil.computeHeading(start, end);

        List<LatLng> result = new ArrayList<>(4);

        LatLng cornerSouthWest = SphericalUtil.computeOffset(start, offset, heading + 90);
        LatLng cornerNorthWest = SphericalUtil.computeOffset(cornerSouthWest, FIELD_HEIGHT_METERS, heading - 90);
        LatLng cornerSouthEast = SphericalUtil.computeOffset(end, offset, heading + 90);
        LatLng cornerNorthEast = SphericalUtil.computeOffset(cornerSouthEast, FIELD_HEIGHT_METERS, heading - 90);

        result.add(cornerNorthWest);
        result.add(cornerSouthWest);
        result.add(cornerSouthEast);
        result.add(cornerNorthEast);
        return result;
    }


    //TODO rename
    public static List<LatLng> computeNewPath(Polyline polyline, double width) {
        List<LatLng> points = polyline.getPoints();
        List<LatLng> result = new ArrayList<>(points.size());
        for (LatLng point : points){
            LatLng newPoint = SphericalUtil.computeOffset(point, width, Constants.HEADING);
            result.add(newPoint);
        }
        return result;
    }
    //TODO implement
    public static CameraUpdate polygonToCameraUpdate(Polygon polygon){
        return CameraUpdateFactory.newCameraPosition(
                CameraPosition.builder()
                        .target(polygon.getPoints().get(0))
                        .zoom(16)
                        .bearing(Float.parseFloat(Constants.HEADING + ""))
                        .build()
        );
    }
}
