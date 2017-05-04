package com.smartagrodriver.core.storage.converters;

import java.util.ArrayList;
import java.util.List;

import com.smartagrodriver.core.domain.model.Route;
import com.smartagrodriver.core.storage.model.RouteType;

/**
 * Created on 02.02.2017.
 */

public class RouteConverter {

    public static com.smartagrodriver.core.storage.model.Route convertToStorageModel(Route model) {
        if (model == null) {
            return null;
        }
        com.smartagrodriver.core.storage.model.Route result = new com.smartagrodriver.core.storage.model.Route();
        result.id = model.getId();
        result.fieldId = model.getFieldId();
        result.points = Util.pointsToString(model.getRoutePoints());
        result.type = RouteTypeConverter.convertToStorageModel(model.getType()).id;

        return result;
    }


    public static Route convertToDomainModel(com.smartagrodriver.core.storage.model.Route model) {
        if (model == null) {
            return null;
        }
        RouteType type = new RouteType();
        type.id = model.type;
        Route result = new Route(
                model.id,
                model.fieldId, RouteTypeConverter.convertToDomainModel(type),
                Util.stringToPoints(model.points));

        return result;
    }

    public static List<com.smartagrodriver.core.storage.model.Route> convertToStorageModel(List<Route> routes) {
        List<com.smartagrodriver.core.storage.model.Route> result = new ArrayList<>();
        if (routes == null || routes.size() == 0) {
            return result;
        }

        for (Route model : routes) {
            com.smartagrodriver.core.storage.model.Route route = new com.smartagrodriver.core.storage.model.Route();
            route.id = model.getId();
            route.fieldId = model.getFieldId();
            route.points = Util.pointsToString(model.getRoutePoints());
            route.type = RouteTypeConverter.convertToStorageModel(model.getType()).id;

            result.add(route);
        }

        return result;
    }

    public static List<Route> convertToDomainModel(List<com.smartagrodriver.core.storage.model.Route> routes) {
        List<Route> result = new ArrayList<>();
        if (routes == null || routes.size() == 0) {
            return result;
        }

        for (com.smartagrodriver.core.storage.model.Route model : routes) {
            RouteType type = new RouteType();
            type.id = model.type;

            Route route = new Route(
                    model.id,
                    model.fieldId, RouteTypeConverter.convertToDomainModel(type),
                    Util.stringToPoints(model.points)
            );

            result.add(route);
        }

        return result;
    }
}
