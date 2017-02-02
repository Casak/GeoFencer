package casak.ru.geofencer.storage.converters;

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
}
