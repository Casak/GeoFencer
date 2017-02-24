package casak.ru.geofencer.domain.interactors;

import casak.ru.geofencer.domain.interactors.base.Interactor;
import casak.ru.geofencer.domain.model.Arrow;
import casak.ru.geofencer.domain.model.Field;

public interface BuildFieldInteractor extends Interactor {
    interface Callback {
        void onFieldBuildFinish(Field field);

        void onFieldBuildFail(Field field);
    }

    void init(Field field, Arrow arrow);
}