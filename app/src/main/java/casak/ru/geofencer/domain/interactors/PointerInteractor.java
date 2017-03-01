package casak.ru.geofencer.domain.interactors;

import casak.ru.geofencer.domain.interactors.base.Interactor;
import casak.ru.geofencer.domain.model.Point;

/**
 * Created on 16.02.2017.
 */

public interface PointerInteractor extends Interactor, LocationInteractor.OnLocationChangedListener {
    interface Callback {
        void showPointer(double value);
    }

    void init(PointerInteractor.Callback callback, int fieldId);

    double getCurrentRouteBearing();

    Point getNearestPoint();

    long getCurrentRouteId();
}