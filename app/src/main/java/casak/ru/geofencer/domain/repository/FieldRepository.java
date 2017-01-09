package casak.ru.geofencer.domain.repository;

import casak.ru.geofencer.domain.model.FieldModel;

public interface FieldRepository {
    boolean addField(FieldModel field);
    FieldModel getField(Integer id);
}
