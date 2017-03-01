package casak.ru.geofencer.di.components;

import casak.ru.geofencer.di.modules.ActivityModule;
import casak.ru.geofencer.di.modules.PointerModule;
import casak.ru.geofencer.di.modules.RepositoryModule;
import casak.ru.geofencer.di.scopes.ActivityScope;
import casak.ru.geofencer.domain.interactors.PointerInteractor;
import casak.ru.geofencer.presentation.presenters.MapPointerPresenter;
import casak.ru.geofencer.presentation.ui.fragment.MapPointerFragment;
import dagger.Component;

/**
 * Created on 21.02.2017.
 */

@ActivityScope
@Component(modules = {PointerModule.class, ActivityModule.class, RepositoryModule.class},
        dependencies = AppComponent.class)
public interface PointerComponent extends AbstractActivityComponent {
    void inject(MapPointerFragment fragment);

    MapPointerPresenter.View getPointerView();

    MapPointerPresenter getMapPointerPresenter();

    PointerInteractor getPointerInteractor();
}
