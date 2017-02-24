package casak.ru.geofencer.di.components;

import casak.ru.geofencer.di.modules.RepositoryModule;
import casak.ru.geofencer.di.scopes.ActivityScope;
import casak.ru.geofencer.domain.repository.ArrowRepository;
import casak.ru.geofencer.domain.repository.FieldRepository;
import casak.ru.geofencer.domain.repository.LocationRepository;
import casak.ru.geofencer.domain.repository.RouteRepository;
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