package casak.ru.geofencer.injector.modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import javax.inject.Singleton;

import casak.ru.geofencer.AndroidApplication;
import casak.ru.geofencer.domain.executor.Executor;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.domain.executor.impl.ThreadExecutor;
import casak.ru.geofencer.domain.repository.LocationRepository;
import casak.ru.geofencer.injector.scopes.ActivityScope;
import casak.ru.geofencer.storage.LocationRepositoryImpl;
import casak.ru.geofencer.threading.MainThreadImpl;
import dagger.Module;
import dagger.Provides;

/**
 * Created on 08.02.2017.
 */

@Module
public class AppModule {
    private final AndroidApplication application;

    public AppModule(AndroidApplication androidApplication) {
        application = androidApplication;
    }

    @Provides
    @Singleton
    Context providesApplicationContext() {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    Executor providesExecutor() {
        return new ThreadExecutor();
    }

    @Provides
    @Singleton
    MainThread providesMainThread() {
        return new MainThreadImpl();
    }

    @Provides
    @Singleton
    LocationRepository providesLocationRepository(){
        return new LocationRepositoryImpl();
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
