package casak.ru.geofencer.domain.repository.impl;


import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.repository.LocationRepository;

public class LocationRepositoryImpl implements LocationRepository{

    @Override
    public boolean insert(Point point, Destination destination) {
        return false;
    }

    @Override
    public boolean update(Point point) {
        return false;
    }

    @Override
    public Point get(Integer id) {
        return null;
    }

    @Override
    public Point getLastLocation() {
        return null;
    }

    @Override
    public boolean delete(Point point) {
        return false;
    }
}
