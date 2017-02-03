package casak.ru.geofencer.storage.converters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.model.RouteModel;
import casak.ru.geofencer.storage.model.Route;

import static org.junit.Assert.*;

/**
 * Created on 02.02.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class RouteConverterTest {

    @Test
    public void convertToStorageModel_fromNonEmptyModel_returnsConvertedModel() {
        List<Point> points = Util.stringToPoints("50.0,30.0;51.0,31.0;");

        RouteModel model = new RouteModel(1, RouteModel.Type.COMPUTED, 1, points);

        Route result = RouteConverter.convertToStorageModel(model);

        assertNotNull(result);
        assertEquals(model.getId(), result.id);
        assertEquals(model.getFieldId(), result.fieldId);
        assertEquals(model.getRoutePoints(), Util.stringToPoints(result.points));
        assertEquals(2, result.type);
    }

    @Test
    public void convertToDomainModel_fromEmptyModel_returnsNull() {
        RouteModel result = RouteConverter.convertToDomainModel(null);

        assertNull(result);
    }

    @Test
    public void convertToDomainModel_fromNonEmptyModel_returnsConvertedModel() {
        Route route = new Route();
        route.id = 1;
        route.fieldId = 1;
        route.type = 2;
        route.points = "50.0,30.0;51.0,31.0;";
        RouteModel result = RouteConverter.convertToDomainModel(route);

        assertNotNull(result);
        assertEquals(route.id, result.getId());
        assertEquals(route.fieldId, result.getFieldId());
        assertEquals(Util.stringToPoints(route.points), result.getRoutePoints());
        assertEquals(RouteModel.Type.COMPUTED, result.getType());
    }


    @Test
    public void convertToStorageModel_fromEmptyList_returnsEmptyList() {
        List<RouteModel> emptyList = new ArrayList<>();

        List<Route> result = RouteConverter.convertToStorageModel(emptyList);

        assertNotNull(result);
        assertEquals(emptyList.size(), result.size());
    }

    @Test
    public void convertToStorageModel_fromNonEmptyList_returnsConvertedList() {
        List<RouteModel> routeList = new ArrayList<>();
        String stringPoints = "50.0,30.0;51.0,31.0;";
        List<Point> points = Util.stringToPoints(stringPoints);

        RouteModel model1 = new RouteModel(1, RouteModel.Type.COMPUTED, 1, points);
        RouteModel model2 = new RouteModel(1, RouteModel.Type.COMPUTED, 1, points);
        RouteModel model3 = new RouteModel(1, RouteModel.Type.COMPUTED, 1, points);
        routeList.add(model1);
        routeList.add(model2);
        routeList.add(model3);

        List<Route> result = RouteConverter.convertToStorageModel(routeList);

        assertNotNull(result);
        assertEquals(routeList.size(), result.size());
        for (Route route : result)
            assertEquals(stringPoints, route.points);
    }
}