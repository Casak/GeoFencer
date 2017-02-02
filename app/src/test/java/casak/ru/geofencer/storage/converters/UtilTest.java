package casak.ru.geofencer.storage.converters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.LinkedList;
import java.util.List;

import casak.ru.geofencer.domain.model.Point;

import static org.junit.Assert.*;

/**
 * Created on 02.02.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class UtilTest {

    @Test
    public void pointsToString_fromEmptyList_returnsEmptyString() {
        List<Point> points = new LinkedList<>();

        String result = Util.pointsToString(points);

        assertEquals("", result);
    }

    @Test
    public void pointsToString_fromNonEmptyList_returnsBuildedString() {
        Point point = new Point(50d, 30d);
        Point point2 = new Point(51d, 31d);
        List<Point> points = new LinkedList<>();
        points.add(point);
        points.add(point2);

        String result = Util.pointsToString(points);

        assertEquals("50.0,30.0;51.0,31.0;", result);
    }
}