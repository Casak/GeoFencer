package casak.ru.geofencer.storage.converters;

import java.util.ArrayList;
import java.util.List;

import casak.ru.geofencer.domain.model.RouteModel;
import casak.ru.geofencer.storage.model.Route;
import casak.ru.geofencer.storage.model.RouteType;

/**
 * Created on 02.02.2017.
 */

public class RouteConverter {

    public static Route convertToStorageModel(RouteModel model){
        if(model == null)
            return null;
        Route result = new Route();
        result.id = model.getId();
        result.fieldId = model.getFieldId();
        result.points = Util.pointsToString(model.getRoutePoints());
        result.type = RouteTypeConverter.convertToStorageModel(model.getType()).id;

        return result;
    }


    public static RouteModel convertToDomainModel(Route model){
        if(model == null)
            return null;
        RouteType type = new RouteType();
        type.id = model.type;
        RouteModel result = new RouteModel(
                model.id,
                model.fieldId, RouteTypeConverter.convertToDomainModel(type),
                Util.stringToPoints(model.points));

        return result;
    }

    public static List<Route> convertToStorageModel(List<RouteModel> routeModels){
        List<Route> result = new ArrayList<>();
        if(routeModels == null || routeModels.size() == 0)
            return result;

        for(RouteModel model : routeModels){
            Route route = new Route();
            route.id = model.getId();
            route.fieldId = model.getFieldId();
            route.points = Util.pointsToString(model.getRoutePoints());
            route.type = RouteTypeConverter.convertToStorageModel(model.getType()).id;

            result.add(route);
        }

        return result;
    }

    public static List<RouteModel> convertToDomainModel(List<Route> routeModels){
        List<RouteModel> result = new ArrayList<>();
        if(routeModels == null || routeModels.size() == 0)
            return result;

        for(Route model : routeModels){
            RouteType type = new RouteType();
            type.id = model.type;

            RouteModel routeModel = new RouteModel(
                    model.id,
                    model.fieldId, RouteTypeConverter.convertToDomainModel(type),
                    Util.stringToPoints(model.points)
            );

            result.add(routeModel);
        }

        return result;
    }
}
