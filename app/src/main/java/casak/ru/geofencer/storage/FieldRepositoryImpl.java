package casak.ru.geofencer.storage;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import casak.ru.geofencer.domain.model.Field;
import casak.ru.geofencer.domain.repository.FieldRepository;
import casak.ru.geofencer.storage.converters.FieldConverter;
import casak.ru.geofencer.storage.converters.RouteConverter;
import casak.ru.geofencer.storage.converters.Util;
import casak.ru.geofencer.storage.model.Field_Table;

/**
 * Created on 05.01.2017.
 */
public class FieldRepositoryImpl implements FieldRepository {
    private int mFieldId;

    @Override
    public Field createField() {
        casak.ru.geofencer.storage.model.Field result = new casak.ru.geofencer.storage.model.Field();
        result.insert();

        mFieldId = result.id;

        return FieldConverter.convertToDomainModel(result);
    }

    @Override
    public boolean addField(Field field) {
        casak.ru.geofencer.storage.model.Field result = FieldConverter.convertToStorageModel(field);

        result.insert();

        mFieldId = result.id;

        return true;
    }

    @Override
    public Field getField(Integer id) {
        mFieldId = id;

        casak.ru.geofencer.storage.model.Field result = SQLite.select()
                .from(casak.ru.geofencer.storage.model.Field.class)
                .where(Field_Table.id.eq(id))
                .querySingle();

        return FieldConverter.convertToDomainModel(result);
    }

    @Override
    public boolean updateField(Field field) {
        casak.ru.geofencer.storage.model.Field result = SQLite.select()
                .from(casak.ru.geofencer.storage.model.Field.class)
                .where(Field_Table.id.eq(field.getId()))
                .querySingle();

        if (result == null)
            return false;

        result.points = Util.pointsToString(field.getPoints());
        result.routes = RouteConverter.convertToStorageModel(field.getRoutes());

        result.update();

        mFieldId = result.id;
        return true;
    }

    @Override
    public Integer getCurrentFieldId() {
        return mFieldId;
    }
}
