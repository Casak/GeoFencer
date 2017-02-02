package casak.ru.geofencer.storage.converters;

import java.util.ArrayList;
import java.util.List;

import casak.ru.geofencer.domain.model.Point;

/**
 * Created on 02.02.2017.
 */

public class Util {
    public static String pointsToString(List<Point> points) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Point point : points){
            stringBuilder.append(point.getLatitude());
            stringBuilder.append(",");
            stringBuilder.append(point.getLongitude());
            stringBuilder.append(";");
        }

        return stringBuilder.toString();
    }

    public static List<Point> stringToPoints(String in){
        List<Point> result = new ArrayList<>();
        if(in.length() == 0)
            return result;

        String[] points = in.split(";");
        for (String point : points){
            String[] latlng = point.split(",");
            double lat = Double.parseDouble(latlng[0]);
            double lng = Double.parseDouble(latlng[1]);
            Point p = new Point(lat, lng);
            result.add(p);
        }
        return result;
    }
}
