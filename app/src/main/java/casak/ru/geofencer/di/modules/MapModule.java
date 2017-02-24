package casak.ru.geofencer.di.modules;

import com.google.android.gms.maps.LocationSource;


import casak.ru.geofencer.bluetooth.AntennaDataProvider;
import casak.ru.geofencer.bluetooth.old.BluetoothAntennaLocationSource;
import casak.ru.geofencer.domain.executor.Executor;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.domain.interactors.CreateFieldInteractor;
import casak.ru.geofencer.domain.interactors.LoadFieldInteractor;
import casak.ru.geofencer.domain.interactors.LocationInteractor;
import casak.ru.geofencer.domain.interactors.impl.CreateFieldInteractorImpl;
import casak.ru.geofencer.domain.interactors.impl.LoadFieldInteractorImpl;
import casak.ru.geofencer.domain.interactors.impl.LocationInteractorImplListener;
import casak.ru.geofencer.domain.repository.ArrowRepository;
import casak.ru.geofencer.domain.repository.FieldRepository;
import casak.ru.geofencer.domain.repository.RouteRepository;
import casak.ru.geofencer.di.scopes.ActivityScope;
import casak.ru.geofencer.presentation.presenters.CameraPresenter;
import casak.ru.geofencer.presentation.presenters.GoogleMapPresenter;
import casak.ru.geofencer.presentation.presenters.impl.CameraPresenterImpl;
import casak.ru.geofencer.presentation.presenters.impl.GoogleMapPresenterImpl;
import casak.ru.geofencer.storage.ArrowRepositoryImpl;
import casak.ru.geofencer.storage.FieldRepositoryImpl;
import casak.ru.geofencer.storage.RouteRepositoryImpl;
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
                                                   FieldRepository fieldRepository,
                                                   AntennaDataProvider provider,
                                                   LocationInteractor locationInteractor,
                                                   CreateFieldInteractor createFieldInteractor,
                                                   CameraPresenter cameraPresenter) {
        return new GoogleMapPresenterImpl(
                threadExecutor,
                mainThread,
                createFieldInteractor,
                locationInteractor,
                mapView,
                fieldRepository,
                provider,
                cameraPresenter);
    }

    @Provides
    @ActivityScope
    CameraPresenter providesCameraPresenter(CameraPresenterImpl presenter) {
        return presenter;
    }

    @Provides
    @ActivityScope
    LoadFieldInteractor providesLoadFieldInteractor(LoadFieldInteractorImpl interactor) {
        return interactor;
    }

    @Provides
    @ActivityScope
    LocationInteractor providesLocationInteractor(LocationInteractorImplListener interactor) {
        return interactor;
    }

    @Provides
    @ActivityScope
    CreateFieldInteractor providesCreateFieldInteractor(CreateFieldInteractorImpl interactor) {
        return interactor;
    }

    @Provides
    @ActivityScope
    CreateFieldInteractor.Callback providesCreateFieldInteractorCallback(GoogleMapPresenter callback) {
        return callback;
    }

    @Provides
    @ActivityScope
    LocationSource providesLocationSource() {
        return new BluetoothAntennaLocationSource();
    }

    @Provides
    @ActivityScope
    RouteRepository providesRouteRepository() {
        return new RouteRepositoryImpl();
    }

    @Provides
    @ActivityScope
    ArrowRepository providesArrowRepository() {
        return new ArrowRepositoryImpl();
    }

    @Provides
    @ActivityScope
    FieldRepository providesFieldRepository() {
        return new FieldRepositoryImpl();
    }
}
