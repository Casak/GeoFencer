package com.smartagrodriver.core.presentation.converters;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import com.smartagrodriver.core.domain.model.Point;

import static org.junit.Assert.*;

/**
 * Created on 16.02.2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class LatLngConverterTest {

    private List<Point> points;

    private Point point;

    @Before
    public void fillList() {
        point = new Point(0, 0);

        points = new ArrayList<>();
        points.add(point);
        points.add(point);
        points.add(point);
    }

    @Test
    public void convertToLatLng_fromPoint_returnLatLng() {
        LatLng result = LatLngConverter.convertToLatLng(point);

        assertEquals(0, result.latitude, 0);
        assertEquals(0, result.longitude, 0);
    }

    @Test
    public void convertToLatLng_fromListOfPoints_returnListOfLatLngs() {
        List<LatLng> result = LatLngConverter.convertToLatLng(points);

        for(LatLng latLng : result){
            assertEquals(0, latLng.latitude, 0);
            assertEquals(0, latLng.longitude, 0);
        }
    }
}