package com.smartagrodriver.core.storage.converters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import com.smartagrodriver.core.domain.model.Point;
import com.smartagrodriver.core.domain.model.Route;

import static org.junit.Assert.*;

/**
 * Created on 02.02.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class RouteConverterTest {

    @Test
    public void convertToStorageModel_fromNonEmptyModel_returnsConvertedModel() {
        List<Point> points = Util.stringToPoints("50.0,30.0;51.0,31.0;");

        Route model = new Route(1, 1, Route.Type.COMPUTED, points);

        com.smartagrodriver.core.storage.model.Route result = RouteConverter.convertToStorageModel(model);

        assertNotNull(result);
        assertEquals(model.getId(), result.id);
        assertEquals(model.getFieldId(), result.fieldId);
        assertEquals(model.getRoutePoints(), Util.stringToPoints(result.points));
        assertEquals(2, result.type);
    }

    @Test
    public void convertToDomainModel_fromNonEmptyModel_returnsConvertedModel() {
        com.smartagrodriver.core.storage.model.Route route = new com.smartagrodriver.core.storage.model.Route();
        route.id = 1;
        route.fieldId = 1;
        route.type = 2;
        route.points = "50.0,30.0;51.0,31.0;";
        Route result = RouteConverter.convertToDomainModel(route);

        assertNotNull(result);
        assertEquals(route.id, result.getId());
        assertEquals(route.fieldId, result.getFieldId());
        assertEquals(Util.stringToPoints(route.points), result.getRoutePoints());
        assertEquals(Route.Type.COMPUTED, result.getType());
    }


    @Test
    public void convertToStorageModel_fromEmptyList_returnsEmptyList() {
        List<Route> emptyList = new ArrayList<>();

        List<com.smartagrodriver.core.storage.model.Route> result = RouteConverter.convertToStorageModel(emptyList);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void convertToStorageModel_fromNonEmptyList_returnsConvertedList() {
        List<Route> routeList = new ArrayList<>();
        String stringPoints = "50.0,30.0;51.0,31.0;";
        List<Point> points = Util.stringToPoints(stringPoints);

        Route model1 = new Route(1, 1, Route.Type.COMPUTED, points);
        Route model2 = new Route(1, 1, Route.Type.COMPUTED, points);
        Route model3 = new Route(1, 1, Route.Type.COMPUTED, points);
        routeList.add(model1);
        routeList.add(model2);
        routeList.add(model3);

        List<com.smartagrodriver.core.storage.model.Route> result = RouteConverter.convertToStorageModel(routeList);

        assertNotNull(result);
        assertEquals(3, result.size());
        for (com.smartagrodriver.core.storage.model.Route route : result) {
            assertEquals(1, route.id);
            assertEquals(1, route.fieldId);
            assertEquals(2, route.type);
            assertEquals(stringPoints, route.points);

        }
    }

    @Test
    public void convertToDomainModel_fromEmptyList_returnsEmptyList() {
        List<com.smartagrodriver.core.storage.model.Route> emptyList = new ArrayList<>();

        List<Route> result = RouteConverter.convertToDomainModel(emptyList);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void convertToDomainModel_fromNonEmptyList_returnsConvertedList() {
        List<com.smartagrodriver.core.storage.model.Route> routeList = new ArrayList<>();

        String stringPoints = "50.0,30.0;51.0,31.0;";

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
            r.points = stringPoints;
        }

        List<Route> result = RouteConverter.convertToDomainModel(routeList);

        assertNotNull(result);
        assertEquals(3, result.size());
        for(Route model : result){
            assertEquals(1, model.getId());
            assertEquals(Route.Type.BASE, model.getType());
            assertEquals(1, model.getFieldId());
            assertEquals(Util.stringToPoints(stringPoints), model.getRoutePoints());
        }
    }


}