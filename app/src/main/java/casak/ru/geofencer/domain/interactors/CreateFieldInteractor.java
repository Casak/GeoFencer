package casak.ru.geofencer.domain.interactors;

import casak.ru.geofencer.domain.interactors.base.Interactor;
import casak.ru.geofencer.domain.model.ArrowModel;

/**
 * Created on 05.01.2017.
 */

public interface CreateFieldInteractor extends Interactor{
    interface Callback {
        void removeArrow(ArrowModel model);
    }
}
