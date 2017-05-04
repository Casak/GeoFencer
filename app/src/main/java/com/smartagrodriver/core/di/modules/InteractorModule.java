package com.smartagrodriver.core.di.modules;

import com.smartagrodriver.core.di.scopes.ActivityScope;
import com.smartagrodriver.core.domain.interactors.CreateFieldInteractor;
import com.smartagrodriver.core.domain.interactors.LoadFieldInteractor;
import com.smartagrodriver.core.domain.interactors.LocationInteractor;
import com.smartagrodriver.core.domain.interactors.impl.CreateFieldInteractorImpl;
import com.smartagrodriver.core.domain.interactors.impl.LoadFieldInteractorImpl;
import com.smartagrodriver.core.domain.interactors.impl.LocationInteractorImpl;
import dagger.Module;
import dagger.Provides;

/**
 * Created on 24.02.2017.
 */

@Module
public class InteractorModule {
    @Provides
    @ActivityScope
    CreateFieldInteractor providesCreateFieldInteractor(CreateFieldInteractorImpl interactor) {
        return interactor;
    }

    @Provides
    @ActivityScope
    LoadFieldInteractor providesLoadFieldInteractor(LoadFieldInteractorImpl interactor) {
        return interactor;
    }

    @Provides
    @ActivityScope
    LocationInteractor providesLocationInteractor(LocationInteractorImpl interactor) {
        return interactor;
    }
}