package casak.ru.geofencer.domain.repository;

import java.util.List;

import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.model.RouteModel;

public interface RouteRepository {

    RouteModel getRouteModel(int fieldId, RouteModel.Type type);
    RouteModel getRouteModel(Integer id);
    List<RouteModel> getAllRoutes(int fieldId);
    List<RouteModel> getAllRoutes(int fieldId, RouteModel.Type type);

    /**
     * Should create route at storage.
     * @param type int id of the field, RouteModel.Type of the creating route;
     * @return RouteModel of the route;
     */
    RouteModel createRouteModel(int fieldId, RouteModel.Type type);
    RouteModel addPointToRoute(Integer routeId, Point insertingPoint);
    boolean addRouteModel(RouteModel model);
    boolean deleteRouteModel(RouteModel model);
}
