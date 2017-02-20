package casak.ru.geofencer.bluetooth;

import casak.ru.geofencer.domain.interactors.LocationInteractor;
import casak.ru.geofencer.domain.model.Point;

/**
 * Created on 20.02.2017.
 */

public interface AntennaDataObservable {
    void registerObserver(LocationInteractor.OnLocationChanged observer);

    void removeObserver(LocationInteractor.OnLocationChanged observer);

    void passLocation(Point point);
}
