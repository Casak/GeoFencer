package com.smartagrodriver.core.domain.repository;

import java.util.List;

import com.smartagrodriver.core.domain.model.Route;

public interface RouteRepository {
    Route create(long fieldId, Route.Type type);

    Route get(long routeId);

    Route getBaseRoute(long fieldId);

    List<Route> getAll(long fieldId);

    boolean update(Route model);
}
