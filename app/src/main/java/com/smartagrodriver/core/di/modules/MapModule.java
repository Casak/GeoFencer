package com.smartagrodriver.core.di.modules;

import com.smartagrodriver.core.bluetooth.AntennaDataObservable;
import com.smartagrodriver.core.threading.Executor;
import com.smartagrodriver.core.threading.MainThread;
import com.smartagrodriver.core.domain.interactors.CreateFieldInteractor;
import com.smartagrodriver.core.domain.interactors.LoadFieldInteractor;
import com.smartagrodriver.core.domain.interactors.LocationInteractor;
import com.smartagrodriver.core.di.scopes.ActivityScope;
import com.smartagrodriver.core.presentation.presenters.CameraPresenter;
import com.smartagrodriver.core.presentation.presenters.GoogleMapPresenter;
import com.smartagrodriver.core.presentation.presenters.impl.CameraPresenterImpl;
import com.smartagrodriver.core.presentation.presenters.impl.GoogleMapPresenterImpl;
import dagger.Module;
import dagger.Provides;

/**
 * Created on 09.02.2017.
 */

@Module
public class MapModule {
    private final GoogleMapPresenter.View mapView;

    public MapModule(GoogleMapPresenter.View view) {
        mapView = view;
    }

    @Provides
    @ActivityScope
    GoogleMapPresenter.View providesMapView() {
        return mapView;
    }

    @Provides
    @ActivityScope
    GoogleMapPresenter providesGoogleMapsPresenter(Executor threadExecutor,
                                                   MainThread mainThread,
                                                   AntennaDataObservable observable,
                                                   LocationInteractor locationInteractor,
                                                   LoadFieldInteractor loadFieldInteractor,
                                                   CreateFieldInteractor createFieldInteractor,
                                                   CameraPresenter cameraPresenter) {
        return new GoogleMapPresenterImpl(
                threadExecutor,
                mainThread,
                createFieldInteractor,
                loadFieldInteractor,
                locationInteractor,
                mapView,
                observable,
                cameraPresenter);
    }

    @Provides
    @ActivityScope
    CameraPresenter providesCameraPresenter(CameraPresenterImpl presenter) {
        return presenter;
    }

    @Provides
    @ActivityScope
    CreateFieldInteractor.Callback providesCreateFieldInteractorCallback(GoogleMapPresenter callback) {
        return callback;
    }
}