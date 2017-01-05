package casak.ru.geofencer.domain.repository;

import casak.ru.geofencer.domain.model.ArrowModel;

public interface ArrowRepository {

    ArrowModel getArrow(ArrowModel.Type type);
    ArrowModel getArrow(Integer id);
}
