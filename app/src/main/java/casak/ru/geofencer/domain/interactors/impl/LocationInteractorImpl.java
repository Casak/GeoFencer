package casak.ru.geofencer.domain.interactors.impl;

import casak.ru.geofencer.domain.executor.Executor;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.domain.interactors.LocationInteractor;
import casak.ru.geofencer.domain.interactors.base.AbstractInteractor;
import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.repository.LocationRepository;

/**
 * Created on 20.02.2017.
 */

public class LocationInteractorImpl extends AbstractInteractor implements LocationInteractor, LocationInteractor.OnLocationChanged {
    private LocationRepository mLocationRepository;

    public LocationInteractorImpl(Executor threadExecutor, MainThread mainThread,
                                  LocationRepository repository) {
        super(threadExecutor, mainThread);

        mLocationRepository = repository;
    }

    @Override
    public void run() {

    }

    @Override
    public void onChange(Point point) {
        mLocationRepository.insert(point);
    }
}
