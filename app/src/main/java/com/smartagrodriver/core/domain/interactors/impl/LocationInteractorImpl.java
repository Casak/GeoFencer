package com.smartagrodriver.core.domain.interactors.impl;

import javax.inject.Inject;

import com.smartagrodriver.core.threading.Executor;
import com.smartagrodriver.core.threading.MainThread;
import com.smartagrodriver.core.domain.interactors.LocationInteractor;
import com.smartagrodriver.core.domain.interactors.base.AbstractInteractor;
import com.smartagrodriver.core.domain.model.Point;
import com.smartagrodriver.core.domain.repository.LocationRepository;

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
