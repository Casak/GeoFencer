package casak.ru.geofencer.di.modules;

import casak.ru.geofencer.bluetooth.AntennaDataObservable;
import casak.ru.geofencer.threading.Executor;
import casak.ru.geofencer.threading.MainThread;
import casak.ru.geofencer.domain.interactors.CreateFieldInteractor;
import casak.ru.geofencer.domain.interactors.LoadFieldInteractor;
import casak.ru.geofencer.domain.interactors.LocationInteractor;
import casak.ru.geofencer.di.scopes.ActivityScope;
import casak.ru.geofencer.presentation.presenters.CameraPresenter;
import casak.ru.geofencer.presentation.presenters.GoogleMapPresenter;
import casak.ru.geofencer.presentation.presenters.impl.CameraPresenterImpl;
import casak.ru.geofencer.presentation.presenters.impl.GoogleMapPresenterImpl;
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