package casak.ru.geofencer.domain.repository;

import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.model.RouteModel;

public interface RouteRepository {

    RouteModel getRoute(RouteModel.Type type);
    RouteModel getRoute(Integer id);

    /**
     * Should create route at storage.
     * @param type RouteModel.Type of the creating route;
     * @return RouteModel of the route;
     */
    RouteModel createRoute(RouteModel.Type type);
    RouteModel addPointToRoute(Integer routeId, Point insertingPoint);
}
