package casak.ru.geofencer.domain.interactors;

import casak.ru.geofencer.domain.interactors.base.Interactor;
import casak.ru.geofencer.domain.model.Point;

/**
 * Created on 16.02.2017.
 */

public interface PointerInteractor extends Interactor {

    interface Callback {
        void showPointer(double value);
    }

    void setWidth(int meters);

    void setFieldId(int id);

    void onPositionChanged(Point point);
}
