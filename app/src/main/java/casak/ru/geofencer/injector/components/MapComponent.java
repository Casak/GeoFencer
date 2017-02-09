package casak.ru.geofencer.injector.components;

import casak.ru.geofencer.injector.modules.ActivityModule;
import casak.ru.geofencer.injector.modules.MapModule;
import casak.ru.geofencer.injector.scopes.ActivityScope;
import casak.ru.geofencer.presentation.presenters.GoogleMapPresenter;
import casak.ru.geofencer.presentation.ui.fragment.GoogleMapFragment;
import dagger.Component;

/**
 * Created on 09.02.2017.
 */

@ActivityScope
@Component(modules = {ActivityModule.class, MapModule.class}, dependencies = {AppComponent.class})
public interface MapComponent extends AbstractActivityComponent {
    void inject(GoogleMapFragment fragment);

    GoogleMapPresenter.View getView();

    GoogleMapPresenter getPresenter();
}
