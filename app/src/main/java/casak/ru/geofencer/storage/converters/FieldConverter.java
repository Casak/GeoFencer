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

        Field result = new Field();
        result.id = model.getId();
        result.points = Util.pointsToString(model.getPoints());
        List<Route> routes = new ArrayList<>();


        result.routes = routes;

        return result;
    }
}