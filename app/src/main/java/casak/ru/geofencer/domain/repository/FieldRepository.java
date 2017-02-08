package casak.ru.geofencer.domain.repository;

import casak.ru.geofencer.domain.model.Field;

public interface FieldRepository {
    boolean addField(Field field);
    Field getField(Integer id);
    boolean updateField(Field field);
}
