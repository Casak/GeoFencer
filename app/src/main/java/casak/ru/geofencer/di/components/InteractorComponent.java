package casak.ru.geofencer.di.components;

import casak.ru.geofencer.di.modules.InteractorModule;
import casak.ru.geofencer.di.modules.RepositoryModule;
import casak.ru.geofencer.di.scopes.ActivityScope;
import casak.ru.geofencer.domain.interactors.CreateFieldInteractor;
import casak.ru.geofencer.domain.interactors.LoadFieldInteractor;
import casak.ru.geofencer.domain.interactors.LocationInteractor;
import dagger.Component;

/**
 * Created on 24.02.2017.
 */

@ActivityScope
@Component(modules = {InteractorModule.class, RepositoryModule.class},
        dependencies = AppComponent.class)
public interface InteractorComponent {
    CreateFieldInteractor getCreateFieldInteractor();

    LoadFieldInteractor getLoadFieldInteractor();

    LocationInteractor getLocationInteractor();
}