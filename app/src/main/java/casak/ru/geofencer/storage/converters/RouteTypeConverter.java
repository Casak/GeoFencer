package casak.ru.geofencer.storage.converters;

import casak.ru.geofencer.domain.model.Route;
import casak.ru.geofencer.storage.model.RouteType;

import static casak.ru.geofencer.domain.model.Route.Type.COMPUTED;
import static casak.ru.geofencer.domain.model.Route.Type.BASE;

/**
 * Created on 02.02.2017.
 */

public class RouteTypeConverter {

    public static RouteType convertToStorageModel(Route.Type type) {
        if(type == null)
            return null;
        RouteType result = new RouteType();
        switch (type){
            case BASE:
                result.id = 1;
                result.type = BASE.toString();
                return result;
            case COMPUTED:
                result.id = 2;
                result.type = COMPUTED.toString();
                return result;
            default:
                return null;
        }
    }

    public static Route.Type convertToDomainModel(RouteType type){
        if(type == null)
            return null;

        switch (type.id){
            case 1:
                return Route.Type.BASE;
            case 2:
                return Route.Type.COMPUTED;
            default:
                return null;
        }
    }
}
