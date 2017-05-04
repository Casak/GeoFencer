package com.smartagrodriver.core.di.modules;

import com.smartagrodriver.core.bluetooth.AntennaDataObservable;
import com.smartagrodriver.core.presentation.presenters.MapCameraPresenter;
import com.smartagrodriver.core.presentation.presenters.MapPresenter;
import com.smartagrodriver.core.presentation.presenters.impl.MapCameraPresenterImpl;
import com.smartagrodriver.core.presentation.presenters.impl.MapPresenterImpl;
import com.smartagrodriver.core.threading.Executor;
import com.smartagrodriver.core.threading.MainThread;
import com.smartagrodriver.core.domain.interactors.CreateFieldInteractor;
import com.smartagrodriver.core.domain.interactors.LoadFieldInteractor;
import com.smartagrodriver.core.domain.interactors.LocationInteractor;
import com.smartagrodriver.core.di.scopes.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 09.02.2017.
 */

@Module
public class MapModule {
    private final MapPresenter.View mapView;

    public MapModule(MapPresenter.View view) {
        mapView = view;
    }

    @Provides
    @ActivityScope
    MapPresenter.View providesMapView() {
        return mapView;
    }

    @Provides
    @ActivityScope
    MapPresenter providesGoogleMapsPresenter(Executor threadExecutor,
                                             MainThread mainThread,
                                             AntennaDataObservable observable,
                                             LocationInteractor locationInteractor,
                                             LoadFieldInteractor loadFieldInteractor,
                                             CreateFieldInteractor createFieldInteractor,
                                             MapCameraPresenter mapCameraPresenter) {
        return new MapPresenterImpl(
                threadExecutor,
                mainThread,
                createFieldInteractor,
                loadFieldInteractor,
                locationInteractor,
                mapView,
                observable,
                mapCameraPresenter);
    }

    @Provides
    @ActivityScope
    MapCameraPresenter providesCameraPresenter(MapCameraPresenterImpl presenter) {
        return presenter;
    }

    @Provides
    @ActivityScope
    CreateFieldInteractor.Callback providesCreateFieldInteractorCallback(MapPresenter callback) {
        return callback;
    }
}