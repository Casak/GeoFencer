package casak.ru.geofencer.storage.converters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import casak.ru.geofencer.domain.model.FieldModel;
import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.model.RouteModel;
import casak.ru.geofencer.storage.model.Field;
import casak.ru.geofencer.storage.model.Route;

import static org.junit.Assert.*;

/**
 * Created on 02.02.2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class FieldConverterTest {

    @Test
    public void convertToStorageModel_fromEmptyModel_returnNull() {
        Field result = FieldConverter.convertToStorageModel(null);

        assertNull(result);
    }

    @Test
    public void convertToStorageModel_fromDomainModel_returnConvertedModel() {
        String coords = "50.0,30.0;51.0,31.0;";
        List<Point> points = Util.stringToPoints(coords);
        RouteModel route1 = new RouteModel(1, RouteModel.Type.COMPUTED, 1, points);
        RouteModel route2 = new RouteModel(1, RouteModel.Type.COMPUTED, 1, points);
        RouteModel route3 = new RouteModel(1, RouteModel.Type.COMPUTED, 1, points);

        FieldModel model = new FieldModel(1, points);
        model.addRoute(route1);
        model.addRoute(route2);
        model.addRoute(route3);

        Field result = FieldConverter.convertToStorageModel(model);

        assertNotNull(result);
        assertEquals(model.getId(), result.id);
        assertEquals(model.getRoutes().size(), result.routes.size());
        for (Route route : result.getRoutes()){
            assertEquals(1, route.id);
            assertEquals(1, route.fieldId);
            assertEquals(2, route.type);
            assertEquals(coords, route.points);
        }
    }

    @Test
    public void convertToDomainModel_fromEmptyModel_returnNull() {
        FieldModel result = FieldConverter.convertToDomainModel(null);

        assertNull(result);
    }

    @Test
    public void convertToDomainModel_fromStorageModel_returnConvertedModel() {
        String coords = "50.0,30.0;51.0,31.0;";

        List<Route> routeList = new ArrayList<>();
        Route route1 = new Route();
        Route route2 = new Route();
        Route route3 = new Route();

        routeList.add(route1);
        routeList.add(route2);
        routeList.add(route3);

        for(Route r : routeList){
            r.id = 1;
            r.type = 1;
            r.fieldId = 1;
            r.points = coords;
        }

        Field model = new Field();
        model.id = 1;
        model.points = coords;
        model.routes = routeList;

        FieldModel result = FieldConverter.convertToDomainModel(model);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(Util.stringToPoints(coords), result.getPoints());

        List<RouteModel> routes = new ArrayList<>();
        routes.addAll(result.getRoutes());
        for(RouteModel route : routes){
            assertEquals(1, route.getId());
            assertEquals(1, route.getFieldId());
            assertEquals(1, RouteTypeConverter.convertToStorageModel(route.getType()).id);
            assertEquals(Util.stringToPoints(coords), route.getRoutePoints());
        }


    }
}