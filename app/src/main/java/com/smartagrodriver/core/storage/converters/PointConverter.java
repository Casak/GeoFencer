package com.smartagrodriver.core.storage.converters;

import java.util.Date;

import com.smartagrodriver.core.storage.model.Point;

/**
 * Created on 06.02.2017.
 */

public class PointConverter {

    public static Point convertToStorage(com.smartagrodriver.core.domain.model.Point point) {
        Point result = new Point();

        result.lat = point.getLatitude();
        result.lng = point.getLongitude();
        result.alt = point.getAltitude();
        result.speed = point.getSpeed();
        result.bearing = point.getBearing();
        result.accuracy = point.getAccuracy();
        result.date = new Date(point.getDate());

        return result;
    }

    public static com.smartagrodriver.core.domain.model.Point convertToDomain(Point point) {
        com.smartagrodriver.core.domain.model.Point result = new com.smartagrodriver.core.domain.model.Point();

        result.setLatitude(point.lat);
        result.setLongitude(point.lng);
        result.setAltitude(point.alt);
        result.setSpeed(point.speed);
        result.setBearing(point.bearing);
        result.setAccuracy(point.accuracy);
        if (point.date != null)
            result.setDate(point.date.getTime());
        else
            result.setDate(0);

        return result;
    }

}
