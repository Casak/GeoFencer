package com.smartagrodriver.core.storage;


import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import com.smartagrodriver.core.domain.repository.LocationRepository;
import com.smartagrodriver.core.storage.converters.PointConverter;
import com.smartagrodriver.core.storage.model.Point;
import com.smartagrodriver.core.storage.model.Point_Table;

public class LocationRepositoryImpl implements LocationRepository {

    @Override
    public boolean insert(com.smartagrodriver.core.domain.model.Point point) {
        Point result = PointConverter.convertToStorage(point);

        result.async().insert();

        return true;
    }

    @Override
    public com.smartagrodriver.core.domain.model.Point get(long id) {
        Point result = SQLite.select()
                .from(Point.class)
                .where(Point_Table.id.eq(id))
                .querySingle();

        return PointConverter.convertToDomain(result);
    }

    //TODO Check query statement
    @Override
    public com.smartagrodriver.core.domain.model.Point getLastLocation() {
        Point result = SQLite.select(Method.max(Point_Table.date))
                .from(Point.class)
                .querySingle();
        return PointConverter.convertToDomain(result);
    }
}
