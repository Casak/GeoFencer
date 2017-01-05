package casak.ru.geofencer.domain.repository;

import casak.ru.geofencer.domain.model.RouteModel;

public interface RouteRepository {

    RouteModel getRoute(RouteModel.Type type);
    RouteModel getRoute(Integer id);
}
