package casak.ru.geofencer.domain.interactors;

import casak.ru.geofencer.domain.interactors.base.Interactor;
import casak.ru.geofencer.domain.model.Field;
import casak.ru.geofencer.domain.model.Route;

/**
 * Created on 21.02.2017.
 */

public interface LoadFieldInteractor extends Interactor {
    interface Callback {
        void showField(Field model);

        void showRoute(Route model);
    }

    void init(LoadFieldInteractor.Callback callback, int fieldId);
}
