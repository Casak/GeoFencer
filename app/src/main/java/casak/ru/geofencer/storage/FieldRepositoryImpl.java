package casak.ru.geofencer.storage;

import android.database.Cursor;

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

//TODO Store storage models here, instead of fetching them every time
public class FieldRepositoryImpl implements FieldRepository {

    @Override
    public Field createField() {
        casak.ru.geofencer.storage.model.Field result = new casak.ru.geofencer.storage.model.Field();
        result.insert();

        return FieldConverter.convertToDomainModel(result);
    }

    @Override
    public boolean addField(Field field) {
        casak.ru.geofencer.storage.model.Field result = FieldConverter.convertToStorageModel(field);

        result.insert();

        return true;
    }

    @Override
    public Field getField(Integer id) {
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

        return true;
    }

    @Override
    public int[] getAllFieldIds() {
        Cursor cursor = SQLite.select(Field_Table.id)
                .from(casak.ru.geofencer.storage.model.Field.class)
                .query();
        if (cursor == null)
            return null;
        int[] result = new int[cursor.getCount()];

        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            result[i] = cursor.getInt(0);

            cursor.moveToNext();
        }
        cursor.close();
        return result;
    }

    @Override
    public boolean deleteField(Integer id) {
        SQLite.delete()
                .from(casak.ru.geofencer.storage.model.Field.class)
                .where(Field_Table.id.eq(id))
                .execute();

        return getField(id) == null;
    }
}