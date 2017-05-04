package com.smartagrodriver.core.domain.repository;

import com.smartagrodriver.core.domain.model.Point;

public interface LocationRepository {

    boolean insert(Point point);

    Point get(long id);

    Point getLastLocation();
}
