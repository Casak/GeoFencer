package casak.ru.geofencer.bluetooth;

import casak.ru.geofencer.domain.interactors.LocationInteractor;
import casak.ru.geofencer.domain.model.Point;

/**
 * Created on 20.02.2017.
 */

public interface AntennaDataObservable {
    void registerObserver(LocationInteractor.OnLocationChangedListener observer);

    void removeObserver(LocationInteractor.OnLocationChangedListener observer);

    void passLocation(Point point);
}
