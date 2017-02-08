package casak.ru.geofencer.domain.repository;

import java.util.List;

import casak.ru.geofencer.domain.model.Route;

public interface RouteRepository {

    boolean addRouteModel(Route model);
    Route createRouteModel(long fieldId, Route.Type type);

    Route getBaseRoute(long fieldId);
    Route getRouteModel(long id);

    List<Route> getAllRoutes(long fieldId);
}
