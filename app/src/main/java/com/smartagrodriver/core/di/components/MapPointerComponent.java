package com.smartagrodriver.core.di.components;

import com.smartagrodriver.core.di.modules.ActivityModule;
import com.smartagrodriver.core.di.modules.MapPointerModule;
import com.smartagrodriver.core.di.modules.RepositoryModule;
import com.smartagrodriver.core.di.scopes.ActivityScope;
import com.smartagrodriver.core.domain.interactors.PointerInteractor;
import com.smartagrodriver.core.presentation.presenters.MapPointerPresenter;
import com.smartagrodriver.core.presentation.ui.fragment.MapPointerFragment;

import dagger.Component;

/**
 * Created on 21.02.2017.
 */

@ActivityScope
@Component(modules = {MapPointerModule.class, ActivityModule.class, RepositoryModule.class},
        dependencies = AppComponent.class)
public interface MapPointerComponent extends AbstractActivityComponent {
    void inject(MapPointerFragment fragment);

    MapPointerPresenter.View getPointerView();

    MapPointerPresenter getMapPointerPresenter();

    PointerInteractor getPointerInteractor();
}
