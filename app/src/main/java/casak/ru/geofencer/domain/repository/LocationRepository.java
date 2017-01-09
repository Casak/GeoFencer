package casak.ru.geofencer.domain.repository;

import casak.ru.geofencer.domain.model.Point;

public interface LocationRepository {
    public enum Destination{
        COORDS_TABLE,
        ROUTE_TABLE
    }

    boolean insert(Point point, Destination destination);

    boolean update(Point point);

    Point get(Integer id);

    Point getLastLocation();

    boolean delete(Point point);
}
