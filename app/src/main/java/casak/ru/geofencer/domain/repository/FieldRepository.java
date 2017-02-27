package casak.ru.geofencer.domain.repository;

import casak.ru.geofencer.domain.model.Field;

public interface FieldRepository {
    Field createField();

    Field getField(Integer id);

    boolean addField(Field field);

    boolean updateField(Field field);

    Integer getCurrentFieldId();
}
