package casak.ru.geofencer.domain.interactors.impl;

import javax.inject.Inject;

import casak.ru.geofencer.domain.executor.Executor;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.domain.interactors.LocationInteractor;
import casak.ru.geofencer.domain.interactors.base.AbstractInteractor;
import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.repository.LocationRepository;

/**
 * Created on 20.02.2017.
 */

public class LocationInteractorImpl extends AbstractInteractor implements LocationInteractor, LocationInteractor.OnLocationChangedListener {
    private LocationRepository mLocationRepository;
    private LocationInteractor.Callback mCallback;

    @Inject
    public LocationInteractorImpl(Executor threadExecutor, MainThread mainThread,
                                  LocationRepository repository) {
        super(threadExecutor, mainThread);

        mLocationRepository = repository;
    }

    @Override
    public void init(LocationInteractor.Callback callback) {
        mCallback = callback;
    }

    @Override
    public void run() {
        if (mCallback == null) {
            throw new NullPointerException("LocationInteractor was not initialized!");
        }
    }

    @Override
    public void onChange(final Point point) {
        mLocationRepository.insert(point);
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.addToSessionRoute(point);
            }
        });
    }

    @Override
    public OnLocationChangedListener getListener() {
        return this;
    }
}
