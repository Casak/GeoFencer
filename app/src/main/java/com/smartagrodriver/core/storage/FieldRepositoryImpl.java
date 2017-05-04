package com.smartagrodriver.core.storage;

import android.database.Cursor;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import com.smartagrodriver.core.domain.model.Field;
import com.smartagrodriver.core.domain.repository.FieldRepository;
import com.smartagrodriver.core.storage.converters.FieldConverter;
import com.smartagrodriver.core.storage.converters.RouteConverter;
import com.smartagrodriver.core.storage.converters.Util;
import com.smartagrodriver.core.storage.model.Field_Table;

/**
 * Created on 05.01.2017.
 */

//TODO Store storage models here, instead of fetching them every time
public class FieldRepositoryImpl implements FieldRepository {

    @Override
    public Field createField() {
        com.smartagrodriver.core.storage.model.Field result = new com.smartagrodriver.core.storage.model.Field();
        result.insert();

        return FieldConverter.convertToDomainModel(result);
    }

    @Override
    public boolean addField(Field field) {
        com.smartagrodriver.core.storage.model.Field result = FieldConverter.convertToStorageModel(field);

        result.insert();

        return true;
    }

    @Override
    public Field getField(Integer id) {
        com.smartagrodriver.core.storage.model.Field result = SQLite.select()
                .from(com.smartagrodriver.core.storage.model.Field.class)
                .where(Field_Table.id.eq(id))
                .querySingle();

        return FieldConverter.convertToDomainModel(result);
    }

    @Override
    public boolean updateField(Field field) {
        com.smartagrodriver.core.storage.model.Field result = SQLite.select()
                .from(com.smartagrodriver.core.storage.model.Field.class)
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
                .from(com.smartagrodriver.core.storage.model.Field.class)
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
                .from(com.smartagrodriver.core.storage.model.Field.class)
                .where(Field_Table.id.eq(id))
                .execute();

        return getField(id) == null;
    }
}