package com.smartagrodriver.core.storage;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import com.smartagrodriver.core.domain.model.Route;
import com.smartagrodriver.core.domain.repository.RouteRepository;
import com.smartagrodriver.core.storage.converters.RouteConverter;
import com.smartagrodriver.core.storage.converters.RouteTypeConverter;
import com.smartagrodriver.core.storage.model.Route_Table;

public class RouteRepositoryImpl implements RouteRepository {

    @Override
    public Route getBaseRoute(long fieldId) {
        com.smartagrodriver.core.storage.model.Route result = SQLite
                .select()
                .from(com.smartagrodriver.core.storage.model.Route.class)
                .where(Route_Table.fieldId.eq(fieldId))
                .and(Route_Table.type_id
                        .eq(RouteTypeConverter.convertToStorageModel(Route.Type.BASE).id))
                .querySingle();
        return RouteConverter.convertToDomainModel(result);
    }

    @Override
    public Route get(long id) {
        com.smartagrodriver.core.storage.model.Route result = SQLite
                .select()
                .from(com.smartagrodriver.core.storage.model.Route.class)
                .where(Route_Table.id.eq(id))
                .querySingle();
        return RouteConverter.convertToDomainModel(result);
    }

    @Override
    public List<Route> getAll(long fieldId) {
        List<com.smartagrodriver.core.storage.model.Route> result = SQLite
                .select()
                .from(com.smartagrodriver.core.storage.model.Route.class)
                .where(Route_Table.fieldId.eq(fieldId))
                .queryList();
        return RouteConverter.convertToDomainModel(result);
    }

    //TODO Check returned route ID
    @Override
    public Route create(long fieldId, Route.Type type) {
        com.smartagrodriver.core.storage.model.Route route = new com.smartagrodriver.core.storage.model.Route();
        route.fieldId = fieldId;
        route.type = RouteTypeConverter.convertToStorageModel(type).id;
        route.points = "";

        route.insert();

        return RouteConverter.convertToDomainModel(route);
    }

    //TODO Check if inserted
    @Override
    public boolean update(Route model) {
        com.smartagrodriver.core.storage.model.Route result = RouteConverter.convertToStorageModel(model);

        result.update();
        return true;
    }
}
