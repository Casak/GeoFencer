package com.smartagrodriver.core.di.modules;

import com.smartagrodriver.core.di.scopes.ActivityScope;
import com.smartagrodriver.core.domain.repository.ArrowRepository;
import com.smartagrodriver.core.domain.repository.FieldRepository;
import com.smartagrodriver.core.domain.repository.LocationRepository;
import com.smartagrodriver.core.domain.repository.RouteRepository;
import com.smartagrodriver.core.storage.ArrowRepositoryImpl;
import com.smartagrodriver.core.storage.FieldRepositoryImpl;
import com.smartagrodriver.core.storage.LocationRepositoryImpl;
import com.smartagrodriver.core.storage.RouteRepositoryImpl;
import dagger.Module;
import dagger.Provides;

/**
 * Created on 24.02.2017.
 */

@Module
public class RepositoryModule {
    @Provides
    @ActivityScope
    RouteRepository providesRouteRepository() {
        return new RouteRepositoryImpl();
    }

    @Provides
    @ActivityScope
    ArrowRepository providesArrowRepository() {
        return new ArrowRepositoryImpl();
    }

    @Provides
    @ActivityScope
    FieldRepository providesFieldRepository() {
        return new FieldRepositoryImpl();
    }

    @Provides
    @ActivityScope
    LocationRepository providesLocationRepository() {
        return new LocationRepositoryImpl();
    }
}