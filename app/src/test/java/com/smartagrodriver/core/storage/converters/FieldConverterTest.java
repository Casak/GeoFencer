package com.smartagrodriver.core.storage.converters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import com.smartagrodriver.core.domain.model.Field;
import com.smartagrodriver.core.domain.model.Point;
import com.smartagrodriver.core.domain.model.Route;

import static org.junit.Assert.*;

/**
 * Created on 02.02.2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class FieldConverterTest {

    @Test
    public void convertToStorageModel_fromEmptyModel_returnNull() {
        com.smartagrodriver.core.storage.model.Field result = FieldConverter.convertToStorageModel(null);

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

        com.smartagrodriver.core.storage.model.Field result = FieldConverter.convertToStorageModel(model);

        assertNotNull(result);
        assertEquals(model.getId(), result.id);
        assertEquals(model.getRoutes().size(), result.routes.size());
        for (com.smartagrodriver.core.storage.model.Route route : result.getRoutes()){
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

        List<com.smartagrodriver.core.storage.model.Route> routeList = new ArrayList<>();
        com.smartagrodriver.core.storage.model.Route route1 = new com.smartagrodriver.core.storage.model.Route();
        com.smartagrodriver.core.storage.model.Route route2 = new com.smartagrodriver.core.storage.model.Route();
        com.smartagrodriver.core.storage.model.Route route3 = new com.smartagrodriver.core.storage.model.Route();

        routeList.add(route1);
        routeList.add(route2);
        routeList.add(route3);

        for(com.smartagrodriver.core.storage.model.Route r : routeList){
            r.id = 1;
            r.type = 1;
            r.fieldId = 1;
            r.points = coords;
        }

        com.smartagrodriver.core.storage.model.Field model = new com.smartagrodriver.core.storage.model.Field();
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