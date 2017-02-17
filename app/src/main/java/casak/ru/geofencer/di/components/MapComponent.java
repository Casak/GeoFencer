package casak.ru.geofencer.di.components;

import com.google.android.gms.maps.LocationSource;

import casak.ru.geofencer.domain.interactors.CreateFieldInteractor;
import casak.ru.geofencer.domain.repository.ArrowRepository;
import casak.ru.geofencer.domain.repository.FieldRepository;
import casak.ru.geofencer.domain.repository.RouteRepository;
import casak.ru.geofencer.di.modules.ActivityModule;
import casak.ru.geofencer.di.modules.MapModule;
import casak.ru.geofencer.di.scopes.ActivityScope;
import casak.ru.geofencer.presentation.presenters.GoogleMapPresenter;
import casak.ru.geofencer.presentation.presenters.impl.GoogleMapPresenterImpl;
import casak.ru.geofencer.presentation.ui.fragment.GoogleMapFragment;
import casak.ru.geofencer.presentation.ui.fragment.MapButtonFragment;
import dagger.Component;

/**
 * Created on 09.02.2017.
 */

@ActivityScope
@Component(modules = {ActivityModule.class, MapModule.class}, dependencies = {AppComponent.class})
public interface MapComponent extends AbstractActivityComponent {
    void inject(GoogleMapFragment fragment);

    void inject(MapButtonFragment fragment);

    void inject(GoogleMapPresenterImpl presenter);

    GoogleMapPresenter.View getView();

    GoogleMapPresenter getPresenter();

    LocationSource getLocationSource();

    CreateFieldInteractor getCreateFieldInteractor();

    CreateFieldInteractor.Callback getCreateFieldInteractorCallback();

    RouteRepository getRouteRepository();

    ArrowRepository getArrowRepository();

    FieldRepository getFieldRepository();
}
