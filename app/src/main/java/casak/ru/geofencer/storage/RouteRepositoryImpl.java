package casak.ru.geofencer.storage;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import casak.ru.geofencer.domain.model.Route;
import casak.ru.geofencer.domain.repository.RouteRepository;
import casak.ru.geofencer.storage.converters.RouteConverter;
import casak.ru.geofencer.storage.converters.RouteTypeConverter;
import casak.ru.geofencer.storage.model.Route_Table;

public class RouteRepositoryImpl implements RouteRepository {

    @Override
    public Route getBaseRoute(long fieldId) {
        casak.ru.geofencer.storage.model.Route result = SQLite
                .select()
                .from(casak.ru.geofencer.storage.model.Route.class)
                .where(Route_Table.fieldId.eq(fieldId))
                .and(Route_Table.type_id
                        .eq(RouteTypeConverter.convertToStorageModel(Route.Type.BASE).id))
                .querySingle();
        return RouteConverter.convertToDomainModel(result);
    }

    @Override
    public Route get(long id) {
        casak.ru.geofencer.storage.model.Route result = SQLite
                .select()
                .from(casak.ru.geofencer.storage.model.Route.class)
                .where(Route_Table.id.eq(id))
                .querySingle();
        return RouteConverter.convertToDomainModel(result);
    }

    @Override
    public List<Route> getAll(long fieldId) {
        List<casak.ru.geofencer.storage.model.Route> result = SQLite
                .select()
                .from(casak.ru.geofencer.storage.model.Route.class)
                .where(Route_Table.fieldId.eq(fieldId))
                .queryList();
        return RouteConverter.convertToDomainModel(result);
    }

    //TODO Check returned route ID
    @Override
    public Route create(long fieldId, Route.Type type) {
        casak.ru.geofencer.storage.model.Route route = new casak.ru.geofencer.storage.model.Route();
        route.fieldId = fieldId;
        route.type = RouteTypeConverter.convertToStorageModel(type).id;
        route.points = "";

        route.insert();

        return RouteConverter.convertToDomainModel(route);
    }

    //TODO Check if inserted
    @Override
    public boolean update(Route model) {
        casak.ru.geofencer.storage.model.Route result = RouteConverter.convertToStorageModel(model);

        result.update();
        return true;
    }
}
