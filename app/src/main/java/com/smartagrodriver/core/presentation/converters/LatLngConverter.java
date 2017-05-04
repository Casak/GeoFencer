package com.smartagrodriver.core.presentation.converters;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import com.smartagrodriver.core.domain.model.Point;

/**
 * Created on 15.02.2017.
 */

public class LatLngConverter {
    public static LatLng convertToLatLng(Point point) {
        LatLng result = new LatLng(point.getLatitude(), point.getLongitude());

        return result;
    }

    public static List<LatLng> convertToLatLng(List<Point> points) {
        int size = points.size();

        List<LatLng> result = new ArrayList<>(size);

        if (size == 0) {
            return result;
        }

        for (Point point : points) {
            result.add(convertToLatLng(point));
        }

        return result;
    }


}
