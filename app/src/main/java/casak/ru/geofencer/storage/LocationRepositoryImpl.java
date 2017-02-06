package casak.ru.geofencer.storage;


import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import casak.ru.geofencer.domain.repository.LocationRepository;
import casak.ru.geofencer.storage.converters.PointConverter;
import casak.ru.geofencer.storage.model.Point;
import casak.ru.geofencer.storage.model.Point_Table;

public class LocationRepositoryImpl implements LocationRepository{

    @Override
    public boolean insert(casak.ru.geofencer.domain.model.Point point) {
        Point result = PointConverter.convertToStorage(point);

        result.insert();

        return true;
    }

    @Override
    public casak.ru.geofencer.domain.model.Point get(long id) {
        Point result = SQLite.select()
                .from(Point.class)
                .where(Point_Table.id.eq(id))
                .querySingle();

        return PointConverter.convertToDomain(result);
    }

    //TODO Check query statement
    @Override
    public casak.ru.geofencer.domain.model.Point getLastLocation() {
        Point result = SQLite.select(Method.max(Point_Table.date))
                .from(Point.class)
                .querySingle();
        return PointConverter.convertToDomain(result);
    }
}
