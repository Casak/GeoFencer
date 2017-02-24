package casak.ru.geofencer.di.components;

import casak.ru.geofencer.di.modules.RepositoryModule;
import casak.ru.geofencer.domain.interactors.CreateFieldInteractor;
import casak.ru.geofencer.domain.interactors.LoadFieldInteractor;
import casak.ru.geofencer.domain.interactors.LocationInteractor;
import casak.ru.geofencer.di.modules.ActivityModule;
import casak.ru.geofencer.di.modules.MapModule;
import casak.ru.geofencer.di.scopes.ActivityScope;
import casak.ru.geofencer.presentation.presenters.CameraPresenter;
import casak.ru.geofencer.presentation.presenters.GoogleMapPresenter;
import casak.ru.geofencer.presentation.presenters.impl.GoogleMapPresenterImpl;
import casak.ru.geofencer.presentation.ui.fragment.GoogleMapFragment;
import casak.ru.geofencer.presentation.ui.fragment.MapButtonFragment;
import dagger.Component;

/**
 * Created on 09.02.2017.
 */

@ActivityScope
@Component(modules = {ActivityModule.class, MapModule.class, RepositoryModule.class},
        dependencies = AppComponent.class)
public interface MapComponent extends AbstractActivityComponent {
    void inject(GoogleMapFragment fragment);

    void inject(MapButtonFragment fragment);

    void inject(GoogleMapPresenterImpl presenter);

    GoogleMapPresenter.View getView();

    GoogleMapPresenter getPresenter();

    CameraPresenter getCameraPresenter();

    CreateFieldInteractor getCreateFieldInteractor();

    LoadFieldInteractor getLoadFieldInteractor();

    LocationInteractor getLocationInteractor();

    CreateFieldInteractor.Callback getCreateFieldInteractorCallback();

}
