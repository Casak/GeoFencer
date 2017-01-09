package casak.ru.geofencer.domain.repository.impl;

import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.model.RouteModel;
import casak.ru.geofencer.domain.repository.RouteRepository;

public class RouteRepositoryImpl implements RouteRepository {

    //TODO Read/write routes to/from DB
    @Override
    public RouteModel getRoute(RouteModel.Type type) {
        return null;
    }

    @Override
    public RouteModel getRoute(Integer id) {
        return null;
    }

    @Override
    public RouteModel createRoute(RouteModel.Type type) {
        return null;
    }

    @Override
    public RouteModel addPointToRoute(Integer routeId, Point insertingPoint) {
        return null;
    }
}
