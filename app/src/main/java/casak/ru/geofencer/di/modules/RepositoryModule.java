package casak.ru.geofencer.di.modules;

import casak.ru.geofencer.di.scopes.ActivityScope;
import casak.ru.geofencer.domain.repository.ArrowRepository;
import casak.ru.geofencer.domain.repository.FieldRepository;
import casak.ru.geofencer.domain.repository.LocationRepository;
import casak.ru.geofencer.domain.repository.RouteRepository;
import casak.ru.geofencer.storage.ArrowRepositoryImpl;
import casak.ru.geofencer.storage.FieldRepositoryImpl;
import casak.ru.geofencer.storage.LocationRepositoryImpl;
import casak.ru.geofencer.storage.RouteRepositoryImpl;
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