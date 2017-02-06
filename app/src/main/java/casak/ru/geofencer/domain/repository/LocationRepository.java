package casak.ru.geofencer.domain.repository;

import casak.ru.geofencer.domain.model.Point;

public interface LocationRepository {

    boolean insert(Point point);

    boolean update(Point point);

    Point get(Integer id);

    Point getLastLocation();

    boolean delete(Point point);
}
