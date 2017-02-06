package casak.ru.geofencer.storage.converters;

import java.util.ArrayList;
import java.util.List;

import casak.ru.geofencer.domain.model.FieldModel;
import casak.ru.geofencer.storage.model.Field;
import casak.ru.geofencer.storage.model.Route;

/**
 * Created on 02.02.2017.
 */

public class FieldConverter {

    public static Field convertToStorageModel(FieldModel model) {
        if (model == null)
            return null;

        List<Route> routes = new ArrayList<>();
        routes.addAll(RouteConverter.convertToStorageModel(model.getRoutes()));

        Field result = new Field();
        result.id = model.getId();
        result.points = Util.pointsToString(model.getPoints());
        result.routes = routes;

        return result;
    }

    public static FieldModel convertToDomainModel(Field model) {
        if (model == null)
            return null;

        FieldModel result = new FieldModel(model.id);
        result.setPoints(Util.stringToPoints(model.points));
        result.setRoutes(RouteConverter.convertToDomainModel(model.getRoutes()));

        return result;
    }
}