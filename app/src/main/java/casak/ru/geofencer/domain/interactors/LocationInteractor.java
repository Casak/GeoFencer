package casak.ru.geofencer.domain.interactors;

import casak.ru.geofencer.domain.interactors.base.Interactor;
import casak.ru.geofencer.domain.model.Point;

/**
 * Created on 20.02.2017.
 */

public interface LocationInteractor extends Interactor {
    interface Callback {
    }

    interface OnLocationChanged {
        void onChange(Point point);
    }
}
