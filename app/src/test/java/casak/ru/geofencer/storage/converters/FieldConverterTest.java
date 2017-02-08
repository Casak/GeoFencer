package casak.ru.geofencer.storage.converters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import casak.ru.geofencer.domain.model.Field;
import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.model.Route;

import static org.junit.Assert.*;

/**
 * Created on 02.02.2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class FieldConverterTest {

    @Test
    public void convertToStorageModel_fromEmptyModel_returnNull() {
        casak.ru.geofencer.storage.model.Field result = FieldConverter.convertToStorageModel(null);

        assertNull(result);
    }

    @Test
    public void convertToStorageModel_fromDomainModel_returnConvertedModel() {
        String coords = "50.0,30.0;51.0,31.0;";
        List<Point> points = Util.stringToPoints(coords);
        Route route1 = new Route(1, 1, Route.Type.COMPUTED, points);
        Route route2 = new Route(1, 1, Route.Type.COMPUTED, points);
        Route route3 = new Route(1, 1, Route.Type.COMPUTED, points);

        Field model = new Field(1, points);
        model.addRoute(route1);
        model.addRoute(route2);
        model.addRoute(route3);

        casak.ru.geofencer.storage.model.Field result = FieldConverter.convertToStorageModel(model);

        assertNotNull(result);
        assertEquals(model.getId(), result.id);
        assertEquals(model.getRoutes().size(), result.routes.size());
        for (casak.ru.geofencer.storage.model.Route route : result.getRoutes()){
            assertEquals(1, route.id);
            assertEquals(1, route.fieldId);
            assertEquals(2, route.type);
            assertEquals(coords, route.points);
        }
    }

    @Test
    public void convertToDomainModel_fromEmptyModel_returnNull() {
        Field result = FieldConverter.convertToDomainModel(null);

        assertNull(result);
    }

    @Test
    public void convertToDomainModel_fromStorageModel_returnConvertedModel() {
        String coords = "50.0,30.0;51.0,31.0;";

        List<casak.ru.geofencer.storage.model.Route> routeList = new ArrayList<>();
        casak.ru.geofencer.storage.model.Route route1 = new casak.ru.geofencer.storage.model.Route();
        casak.ru.geofencer.storage.model.Route route2 = new casak.ru.geofencer.storage.model.Route();
        casak.ru.geofencer.storage.model.Route route3 = new casak.ru.geofencer.storage.model.Route();

        routeList.add(route1);
        routeList.add(route2);
        routeList.add(route3);

        for(casak.ru.geofencer.storage.model.Route r : routeList){
            r.id = 1;
            r.type = 1;
            r.fieldId = 1;
            r.points = coords;
        }

        casak.ru.geofencer.storage.model.Field model = new casak.ru.geofencer.storage.model.Field();
        model.id = 1;
        model.points = coords;
        model.routes = routeList;

        Field result = FieldConverter.convertToDomainModel(model);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(Util.stringToPoints(coords), result.getPoints());

        List<Route> routes = new ArrayList<>();
        routes.addAll(result.getRoutes());
        for(Route route : routes){
            assertEquals(1, route.getId());
            assertEquals(1, route.getFieldId());
            assertEquals(1, RouteTypeConverter.convertToStorageModel(route.getType()).id);
            assertEquals(Util.stringToPoints(coords), route.getRoutePoints());
        }


    }
}