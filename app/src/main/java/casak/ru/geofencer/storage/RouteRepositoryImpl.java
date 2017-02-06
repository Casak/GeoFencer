package casak.ru.geofencer.storage;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import casak.ru.geofencer.domain.model.RouteModel;
import casak.ru.geofencer.domain.repository.RouteRepository;
import casak.ru.geofencer.storage.converters.RouteConverter;
import casak.ru.geofencer.storage.converters.RouteTypeConverter;
import casak.ru.geofencer.storage.model.Route;
import casak.ru.geofencer.storage.model.Route_Table;

public class RouteRepositoryImpl implements RouteRepository {

    @Override
    public RouteModel getBaseRoute(int fieldId) {
        Route result = SQLite
                .select()
                .from(Route.class)
                .where(Route_Table.fieldId.eq(fieldId))
                .and(Route_Table.type_id
                        .eq(RouteTypeConverter.convertToStorageModel(RouteModel.Type.BASE).id))
                .querySingle();
        return RouteConverter.convertToDomainModel(result);
    }

    @Override
    public RouteModel getRouteModel(Integer id) {
        Route result = SQLite
                .select()
                .from(Route.class)
                .where(Route_Table.id.eq(id))
                .querySingle();
        return RouteConverter.convertToDomainModel(result);
    }

    @Override
    public List<RouteModel> getAllRoutes(int fieldId) {
        List<Route> result = SQLite
                .select()
                .from(Route.class)
                .where(Route_Table.fieldId.eq(fieldId))
                .queryList();
        return RouteConverter.convertToDomainModel(result);
    }

    //TODO Check returned route ID
    @Override
    public RouteModel createRouteModel(int fieldId, RouteModel.Type type) {
        Route route = new Route();
        route.fieldId = fieldId;
        route.type = RouteTypeConverter.convertToStorageModel(type).id;
        route.points = "";

        route.insert();

        return RouteConverter.convertToDomainModel(route);
    }

    //TODO Check if inserted
    @Override
    public boolean addRouteModel(RouteModel model) {
        Route result = RouteConverter.convertToStorageModel(model);

        result.insert();
        return true;
    }
}
