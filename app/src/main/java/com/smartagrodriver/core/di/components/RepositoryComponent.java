package com.smartagrodriver.core.di.components;

import com.smartagrodriver.core.di.modules.RepositoryModule;
import com.smartagrodriver.core.di.scopes.ActivityScope;
import com.smartagrodriver.core.domain.repository.ArrowRepository;
import com.smartagrodriver.core.domain.repository.FieldRepository;
import com.smartagrodriver.core.domain.repository.LocationRepository;
import com.smartagrodriver.core.domain.repository.RouteRepository;
import dagger.Component;

/**
 * Created on 24.02.2017.
 */

@ActivityScope
@Component(modules = RepositoryModule.class)
public interface RepositoryComponent {
    RouteRepository getRouteRepository();

    ArrowRepository getArrowRepository();

    FieldRepository getFieldRepository();

    LocationRepository getLocationRepository();
}