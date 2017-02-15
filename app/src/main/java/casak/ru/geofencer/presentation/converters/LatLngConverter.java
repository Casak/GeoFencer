package casak.ru.geofencer.presentation.converters;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import casak.ru.geofencer.domain.model.Point;

/**
 * Created on 15.02.2017.
 */

public class LatLngConverter {
    public static LatLng convertToLatLng(Point point) {
        LatLng result = new LatLng(point.getLatitude(), point.getLongitude());

        return result;
    }

    public static List<LatLng> convertToLatLng(List<Point> points) {
        List<LatLng> result = new ArrayList<>(points.size());

        for (Point point : points) {
            result.add(convertToLatLng(point));
        }

        return result;
    }


}
