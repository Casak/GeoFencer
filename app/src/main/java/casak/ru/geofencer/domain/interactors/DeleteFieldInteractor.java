package casak.ru.geofencer.domain.interactors;

import casak.ru.geofencer.domain.interactors.base.Interactor;

/**
 * Created on 20.03.2017.
 */

public interface DeleteFieldInteractor extends Interactor {
    void init(int fieldId);
}