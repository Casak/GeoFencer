package casak.ru.geofencer.di.modules;

import com.google.android.gms.maps.LocationSource;


import casak.ru.geofencer.bluetooth.old.BluetoothAntennaLocationSource;
import casak.ru.geofencer.domain.executor.Executor;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.domain.interactors.CreateFieldInteractor;
import casak.ru.geofencer.domain.interactors.impl.CreateFieldInteractorImpl;
import casak.ru.geofencer.domain.repository.ArrowRepository;
import casak.ru.geofencer.domain.repository.FieldRepository;
import casak.ru.geofencer.domain.repository.RouteRepository;
import casak.ru.geofencer.di.scopes.ActivityScope;
import casak.ru.geofencer.presentation.presenters.GoogleMapPresenter;
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
                                                   LocationSource locationSource) {
        return new GoogleMapPresenterImpl(threadExecutor, mainThread, mapView, locationSource);
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
