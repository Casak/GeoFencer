package casak.ru.geofencer.bluetooth;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import casak.ru.geofencer.domain.interactors.LocationInteractor;
import casak.ru.geofencer.domain.model.Point;

/**
 * Created on 17.02.2017.
 */

public class AntennaDataProvider implements AntennaDataObservable {

    private List<LocationInteractor.OnLocationChanged> observers;

    @Inject
    public AntennaDataProvider() {
        observers = new LinkedList<>();
    }

    @Override
    public void registerObserver(LocationInteractor.OnLocationChanged observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(LocationInteractor.OnLocationChanged observer) {
        observers.remove(observer);
    }

    @Override
    public void passLocation(Point point) {
        notifyObservers(point);
    }

    private void notifyObservers(Point point) {
        for (LocationInteractor.OnLocationChanged observer : observers)
            observer.onChange(point);
    }
}
