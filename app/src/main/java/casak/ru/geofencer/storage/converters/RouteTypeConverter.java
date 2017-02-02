package casak.ru.geofencer.storage.converters;

import casak.ru.geofencer.domain.model.RouteModel;
import casak.ru.geofencer.storage.model.RouteType;

import static casak.ru.geofencer.domain.model.RouteModel.Type.COMPUTED;
import static casak.ru.geofencer.domain.model.RouteModel.Type.FIELD_BUILDING;

/**
 * Created on 02.02.2017.
 */

public class RouteTypeConverter {

    public static RouteType convertToStorageModel(RouteModel.Type type) {
        if(type == null)
            return null;
        RouteType result = new RouteType();
        switch (type){
            case FIELD_BUILDING:
                result.id = 1;
                result.type = FIELD_BUILDING.toString();
                return result;
            case COMPUTED:
                result.id = 2;
                result.type = COMPUTED.toString();
                return result;
            default:
                return null;
        }
    }

    public static RouteModel.Type convertToDomainModel(RouteType type){
        if(type == null)
            return null;

        switch (type.id){
            case 1:
                return RouteModel.Type.FIELD_BUILDING;
            case 2:
                return RouteModel.Type.COMPUTED;
            default:
                return null;
        }
    }
}