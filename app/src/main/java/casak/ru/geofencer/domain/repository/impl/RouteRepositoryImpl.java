package casak.ru.geofencer.domain.repository.impl;

import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.model.RouteModel;
import casak.ru.geofencer.domain.repository.RouteRepository;

public class RouteRepositoryImpl implements RouteRepository {

    //TODO Read/write routes to/from DB
    @Override
    public RouteModel getRouteModel(int fieldId, RouteModel.Type type) {
        return null;
    }

    @Override
    public RouteModel getRouteModel(Integer id) {
        return null;
    }

    @Override
    public RouteModel createRouteModel(int fieldId, RouteModel.Type type) {
        return null;
    }

    @Override
    public RouteModel addPointToRoute(Integer routeId, Point insertingPoint) {
        return null;
    }

    @Override
    public boolean addRouteModel(RouteModel model) {
        return false;
    }

    @Override
    public boolean deleteRouteModel(RouteModel model) {
        return false;
    }
}
