package casak.ru.geofencer.injector.modules;

import com.google.android.gms.maps.LocationSource;


import casak.ru.geofencer.bluetooth.BluetoothAntennaLocationSource;
import casak.ru.geofencer.domain.interactors.CreateFieldInteractor;
import casak.ru.geofencer.domain.interactors.impl.CreateFieldInteractorImpl;
import casak.ru.geofencer.domain.repository.ArrowRepository;
import casak.ru.geofencer.domain.repository.FieldRepository;
import casak.ru.geofencer.domain.repository.LocationRepository;
import casak.ru.geofencer.domain.repository.RouteRepository;
import casak.ru.geofencer.injector.scopes.ActivityScope;
import casak.ru.geofencer.presentation.presenters.GoogleMapPresenter;
import casak.ru.geofencer.presentation.presenters.impl.GoogleMapPresenterImpl;
import casak.ru.geofencer.storage.ArrowRepositoryImpl;
import casak.ru.geofencer.storage.FieldRepositoryImpl;
import casak.ru.geofencer.storage.LocationRepositoryImpl;
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
    GoogleMapPresenter providesGoogleMapsPresenter() {
        return new GoogleMapPresenterImpl(mapView);
    }

    @Provides
    @ActivityScope
    CreateFieldInteractor.Callback providesCreateFieldInteractorCallback(GoogleMapPresenter presenter){
        return presenter;
    }

    @Provides
    @ActivityScope
    CreateFieldInteractor providesCreateFieldInteractor(CreateFieldInteractorImpl interactor) {
        return interactor;
    }

    @Provides
    @ActivityScope
    LocationSource providesLocationSource(){
        return new BluetoothAntennaLocationSource();
    }

    @Provides
    @ActivityScope
    RouteRepository providesRouteRepository(){
        return new RouteRepositoryImpl();
    }

    @Provides
    @ActivityScope
    ArrowRepository providesArrowRepository(){
        return new ArrowRepositoryImpl();
    }

    @Provides
    @ActivityScope
    FieldRepository providesFieldRepository(){
        return new FieldRepositoryImpl();
    }

    @Provides
    @ActivityScope
    LocationRepository providesLocationRepository(){
        return new LocationRepositoryImpl();
    }
}