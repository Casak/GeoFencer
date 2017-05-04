package com.smartagrodriver.core.storage.converters;

import java.util.ArrayList;
import java.util.List;

import com.smartagrodriver.core.domain.model.Field;
import com.smartagrodriver.core.storage.model.Route;

/**
 * Created on 02.02.2017.
 */

public class FieldConverter {

    public static com.smartagrodriver.core.storage.model.Field convertToStorageModel(Field model) {
        if (model == null)
            return null;

        List<Route> routes = new ArrayList<>();
        routes.addAll(RouteConverter.convertToStorageModel(model.getRoutes()));

        com.smartagrodriver.core.storage.model.Field result = new com.smartagrodriver.core.storage.model.Field();
        result.id = model.getId();
        result.points = Util.pointsToString(model.getPoints());
        result.routes = routes;

        return result;
    }

    public static Field convertToDomainModel(com.smartagrodriver.core.storage.model.Field model) {
        if (model == null)
            return null;

        Field result = new Field(model.id);
        result.setPoints(Util.stringToPoints(model.points));
        result.setRoutes(RouteConverter.convertToDomainModel(model.getRoutes()));

        return result;
    }
}