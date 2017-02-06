package casak.ru.geofencer.domain.repository;

import java.util.List;

import casak.ru.geofencer.domain.model.RouteModel;

public interface RouteRepository {

    boolean addRouteModel(RouteModel model);
    RouteModel createRouteModel(long fieldId, RouteModel.Type type);

    RouteModel getBaseRoute(long fieldId);
    RouteModel getRouteModel(long id);

    List<RouteModel> getAllRoutes(long fieldId);
}
