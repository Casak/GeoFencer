package com.smartagrodriver.core.di.modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import com.smartagrodriver.core.AndroidApplication;
import com.smartagrodriver.core.bluetooth.AntennaDataObservable;
import com.smartagrodriver.core.bluetooth.AntennaDataObservableImpl;
import com.smartagrodriver.core.bluetooth.AntennaDataProvider;
import com.smartagrodriver.core.threading.Executor;
import com.smartagrodriver.core.threading.MainThread;
import com.smartagrodriver.core.threading.impl.ThreadExecutor;
import com.smartagrodriver.core.threading.impl.MainThreadImpl;
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