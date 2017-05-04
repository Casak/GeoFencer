package com.smartagrodriver.core.storage.converters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import com.smartagrodriver.core.storage.model.Point;

import static org.junit.Assert.*;

/**
 * Created on 06.02.2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class PointConverterTest {

    @Test
    public void convertToStorage_fromEmptyPoint_returnEmptyStoragePoint() {
        com.smartagrodriver.core.domain.model.Point point = new com.smartagrodriver.core.domain.model.Point();

        Point result = PointConverter.convertToStorage(point);

        assertNotNull(result);
        assertEquals(0, result.lat, 0);
        assertEquals(0, result.lng, 0);
        assertEquals(0, result.alt, 0);
        assertEquals(0, result.speed, 0);
        assertEquals(0, result.bearing, 0);
        assertEquals(0, result.accuracy, 0);
    }

    @Test
    public void convertToStorage_fromPoint_returnConvertedStoragePoint() {
        com.smartagrodriver.core.domain.model.Point point = new com.smartagrodriver.core.domain.model.Point();
        point.setLatitude(50d);
        point.setLongitude(50d);
        point.setAltitude(1);
        point.setSpeed(1);
        point.setBearing(90);
        point.setAccuracy(1);
        point.setDate(99999);

        Point result = PointConverter.convertToStorage(point);

        assertNotNull(result);
        assertEquals(50, result.lat, 0);
        assertEquals(50, result.lng, 0);
        assertEquals(1, result.alt, 0);
        assertEquals(1, result.speed, 0);
        assertEquals(90, result.bearing, 0);
        assertEquals(1, result.accuracy, 0);
        assertEquals(new Date(point.getDate()), result.date);
    }

    @Test
    public void convertToDomain_fromEmptyPoint_returnEmptyDomainPoint(){
        Point point = new Point();

        com.smartagrodriver.core.domain.model.Point result = PointConverter.convertToDomain(point);

        assertNotNull(result);
        assertEquals(0, result.getLatitude(), 0);
        assertEquals(0, result.getLongitude(), 0);
        assertEquals(0, result.getAltitude(), 0);
        assertEquals(0, result.getSpeed(), 0);
        assertEquals(0, result.getBearing(), 0);
        assertEquals(0, result.getAccuracy(), 0);
        assertEquals(0, result.getDate(), 0);
    }

    @Test
    public void convertToDomain_fromStoragePoint_returnConvertedDomainPoint(){
        Point point = new Point();
        point.lat = 50;
        point.lng = 50;
        point.alt = 1;
        point.speed = 1;
        point.bearing = 90;
        point.accuracy = 1;
        point.date = new Date(9999);


        com.smartagrodriver.core.domain.model.Point result = PointConverter.convertToDomain(point);

        assertNotNull(result);
        assertEquals(point.lat, result.getLatitude(), 0);
        assertEquals(point.lng, result.getLongitude(), 0);
        assertEquals(point.alt, result.getAltitude(), 0);
        assertEquals(point.speed, result.getSpeed(), 0);
        assertEquals(point.bearing, result.getBearing(), 0);
        assertEquals(point.accuracy, result.getAccuracy(), 0);
        assertEquals(9999, result.getDate(), 0);
    }
}