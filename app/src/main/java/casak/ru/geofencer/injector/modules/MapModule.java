package casak.ru.geofencer.injector.modules;

import com.google.android.gms.maps.LocationSource;

import casak.ru.geofencer.bluetooth.BluetoothAntennaLocationSource;
import casak.ru.geofencer.injector.scopes.ActivityScope;
import casak.ru.geofencer.presentation.presenters.GoogleMapPresenter;
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
    GoogleMapPresenter providesGoogleMapsPresenter(GoogleMapPresenterImpl presenter) {
        return presenter;
    }

    @Provides
    @ActivityScope
    LocationSource providesLocationSource(){
        return new BluetoothAntennaLocationSource();
    }

}
