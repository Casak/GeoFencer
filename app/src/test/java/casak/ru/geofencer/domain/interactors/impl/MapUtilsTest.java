package casak.ru.geofencer.domain.interactors.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import casak.ru.geofencer.domain.model.Point;

import static org.junit.Assert.*;

/**
 * Created on 11.01.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class MapUtilsTest {

    double distanceBetween = 111195.08372419086d;

    Point from = new Point(50d, 30d);
    Point to = new Point(51d, 30d);
    Point offsetPoint = new Point(51d, 29.999999999999996d);

    @Test
    public void computeOffset_withPointDistanceAndHeading_returnPointWithOffset() {
        Point result = MapUtils.computeOffset(from, distanceBetween, 0);

        assertEquals(offsetPoint, result);
    }

    @Test
    public void computeHeading_withTwoPoints_returnHeading() {
        double heading = MapUtils.computeHeading(from, to);

        assertEquals(heading, 0, 0.02);
    }

    @Test
    public void computeDistanceBetween_withTwoPoint_returnDistanceBetween() {
        double distance = MapUtils.computeDistanceBetween(from, offsetPoint);

        assertEquals(distance, distanceBetween, 0.02d);
    }

    @Test
    public void computeAngleBetween_withTwoPoint_returnAngle() {
        double angle = MapUtils.computeAngleBetween(from, to);

        assertEquals(angle, 0d, 0.018);
    }
}