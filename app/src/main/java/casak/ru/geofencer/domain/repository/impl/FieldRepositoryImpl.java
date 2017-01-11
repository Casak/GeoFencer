package casak.ru.geofencer.domain.repository.impl;

import casak.ru.geofencer.domain.model.FieldModel;
import casak.ru.geofencer.domain.repository.FieldRepository;

/**
 * Created on 05.01.2017.
 */
public class FieldRepositoryImpl implements FieldRepository {
    @Override
    public boolean addField(FieldModel field) {
        return false;
    }

    @Override
    public FieldModel getField(Integer id) {
        return null;
    }

    @Override
    public boolean updateField(FieldModel field) {
        return false;
    }
}
