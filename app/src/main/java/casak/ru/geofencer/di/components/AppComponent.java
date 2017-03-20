package casak.ru.geofencer.di.components;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import casak.ru.geofencer.AndroidApplication;
import casak.ru.geofencer.bluetooth.AntennaDataObservable;
import casak.ru.geofencer.bluetooth.AntennaDataProvider;
import casak.ru.geofencer.threading.Executor;
import casak.ru.geofencer.threading.MainThread;
import casak.ru.geofencer.di.modules.AppModule;
import dagger.Component;

/**
 * Created on 09.02.2017.
 */

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(AndroidApplication app);

    Context getContext();

    Executor getExecutor();

    MainThread getMainThread();

    SharedPreferences getSharedPreferences();

    AntennaDataProvider getAntennaDataProvider();

    AntennaDataObservable getAntennaDataObservable();
}