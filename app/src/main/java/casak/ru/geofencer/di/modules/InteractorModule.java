package casak.ru.geofencer.di.modules;

import casak.ru.geofencer.di.scopes.ActivityScope;
import casak.ru.geofencer.domain.interactors.CreateFieldInteractor;
import casak.ru.geofencer.domain.interactors.LoadFieldInteractor;
import casak.ru.geofencer.domain.interactors.LocationInteractor;
import casak.ru.geofencer.domain.interactors.impl.CreateFieldInteractorImpl;
import casak.ru.geofencer.domain.interactors.impl.LoadFieldInteractorImpl;
import casak.ru.geofencer.domain.interactors.impl.LocationInteractorImpl;
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