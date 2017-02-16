package casak.ru.geofencer.presentation.converters;

import com.google.android.gms.maps.model.PolylineOptions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.model.Route;

import static org.junit.Assert.*;

/**
 * Created on 16.02.2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class RouteConverterTest {

    private Route route;
    private List<Point> points;

    @Before
    public void createRoute() {
        points = new ArrayList<Point>();
        points.add(new Point());
        route = new Route(99, 1, Route.Type.COMPUTED, points);
    }

    @Test
    public void convertToPresentation() {
        PolylineOptions result = RouteConverter.convertToPresentation(route);

        assertTrue(result.isGeodesic());
        assertFalse(result.isClickable());
        assertEquals(points.size(), result.getPoints().size());
    }

}