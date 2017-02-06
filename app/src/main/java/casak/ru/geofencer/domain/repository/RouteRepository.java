package casak.ru.geofencer.domain.repository;

import java.util.List;

import casak.ru.geofencer.domain.model.RouteModel;

public interface RouteRepository {

    boolean addRouteModel(RouteModel model);
    RouteModel createRouteModel(int fieldId, RouteModel.Type type);

    RouteModel getBaseRoute(int fieldId);
    RouteModel getRouteModel(Integer id);

    List<RouteModel> getAllRoutes(int fieldId);
}
