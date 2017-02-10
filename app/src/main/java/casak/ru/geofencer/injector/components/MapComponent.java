package casak.ru.geofencer.injector.components;

import com.google.android.gms.maps.LocationSource;

import casak.ru.geofencer.domain.interactors.CreateFieldInteractor;
import casak.ru.geofencer.domain.repository.ArrowRepository;
import casak.ru.geofencer.domain.repository.FieldRepository;
import casak.ru.geofencer.domain.repository.LocationRepository;
import casak.ru.geofencer.domain.repository.RouteRepository;
import casak.ru.geofencer.injector.modules.ActivityModule;
import casak.ru.geofencer.injector.modules.MapModule;
import casak.ru.geofencer.injector.scopes.ActivityScope;
import casak.ru.geofencer.presentation.presenters.GoogleMapPresenter;
import casak.ru.geofencer.presentation.presenters.impl.GoogleMapPresenterImpl;
import casak.ru.geofencer.presentation.ui.fragment.GoogleMapFragment;
import dagger.Component;

/**
 * Created on 09.02.2017.
 */

@ActivityScope
@Component(modules = {ActivityModule.class, MapModule.class}, dependencies = {AppComponent.class})
public interface MapComponent extends AbstractActivityComponent {
    void inject(GoogleMapFragment fragment);
    void inject(GoogleMapPresenterImpl presenter);

    GoogleMapPresenter.View getView();

    GoogleMapPresenter getPresenter();

    LocationSource getLocationSource();

    CreateFieldInteractor.Callback getCreateFieldInteractorCallback();

    RouteRepository getRouteRepository();

    ArrowRepository getArrowRepository();

    FieldRepository getFieldRepository();

    LocationRepository getLocationRepository();
}
