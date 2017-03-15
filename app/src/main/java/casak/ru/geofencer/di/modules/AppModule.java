package casak.ru.geofencer.di.modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import casak.ru.geofencer.AndroidApplication;
import casak.ru.geofencer.bluetooth.AntennaDataObservable;
import casak.ru.geofencer.bluetooth.AntennaDataObservableImpl;
import casak.ru.geofencer.bluetooth.AntennaDataProvider;
import casak.ru.geofencer.threading.Executor;
import casak.ru.geofencer.threading.MainThread;
import casak.ru.geofencer.threading.impl.ThreadExecutor;
import casak.ru.geofencer.threading.impl.MainThreadImpl;
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
    SharedPreferences providesSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @Singleton
    AntennaDataProvider providesAntennaDataProvider(AntennaDataObservable observable) {
        return new AntennaDataProvider(observable);
    }

    @Provides
    @Singleton
    AntennaDataObservable providesAntennaDataObservable() {
        return new AntennaDataObservableImpl();
    }
}