package com.smartagrodriver.core.di.components;

import com.smartagrodriver.core.di.modules.InteractorModule;
import com.smartagrodriver.core.di.modules.RepositoryModule;
import com.smartagrodriver.core.di.scopes.ActivityScope;
import com.smartagrodriver.core.domain.interactors.CreateFieldInteractor;
import com.smartagrodriver.core.domain.interactors.LoadFieldInteractor;
import com.smartagrodriver.core.domain.interactors.LocationInteractor;
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