package casak.ru.geofencer.storage.converters;

import java.util.ArrayList;
import java.util.List;

import casak.ru.geofencer.domain.model.Field;
import casak.ru.geofencer.storage.model.Route;

/**
 * Created on 02.02.2017.
 */

public class FieldConverter {

    public static casak.ru.geofencer.storage.model.Field convertToStorageModel(Field model) {
        if (model == null)
            return null;

        List<Route> routes = new ArrayList<>();
        routes.addAll(RouteConverter.convertToStorageModel(model.getRoutes()));

        casak.ru.geofencer.storage.model.Field result = new casak.ru.geofencer.storage.model.Field();
        result.id = model.getId();
        result.points = Util.pointsToString(model.getPoints());
        result.routes = routes;

        return result;
    }

    public static Field convertToDomainModel(casak.ru.geofencer.storage.model.Field model) {
        if (model == null)
            return null;

        Field result = new Field(model.id);
        result.setPoints(Util.stringToPoints(model.points));
        result.setRoutes(RouteConverter.convertToDomainModel(model.getRoutes()));

        return result;
    }
}