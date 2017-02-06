package casak.ru.geofencer.domain.repository;

import casak.ru.geofencer.domain.model.Point;

public interface LocationRepository {

    boolean insert(Point point);

    Point get(long id);

    Point getLastLocation();
}
