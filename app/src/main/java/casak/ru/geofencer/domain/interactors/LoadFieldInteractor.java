package casak.ru.geofencer.domain.interactors;

import casak.ru.geofencer.domain.interactors.base.Interactor;
import casak.ru.geofencer.domain.model.Field;
import casak.ru.geofencer.domain.model.Route;
import casak.ru.geofencer.domain.repository.FieldRepository;

/**
 * Created on 21.02.2017.
 */

public interface LoadFieldInteractor extends Interactor {
    interface Callback {
        void showField(Field model);

        void hideField(Field model);

        void showRoute(Route model);

        void hideRoute(Route model);
    }

    void init(LoadFieldInteractor.Callback callback, int fieldId);

}
