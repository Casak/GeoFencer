package com.smartagrodriver.core.di.components;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import com.smartagrodriver.core.AndroidApplication;
import com.smartagrodriver.core.bluetooth.AntennaDataObservable;
import com.smartagrodriver.core.bluetooth.AntennaDataProvider;
import com.smartagrodriver.core.threading.Executor;
import com.smartagrodriver.core.threading.MainThread;
import com.smartagrodriver.core.di.modules.AppModule;
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