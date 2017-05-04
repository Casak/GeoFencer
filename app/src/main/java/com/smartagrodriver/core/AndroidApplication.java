package com.smartagrodriver.core;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import com.smartagrodriver.core.di.components.AppComponent;
import com.smartagrodriver.core.di.components.DaggerAppComponent;
import com.smartagrodriver.core.di.modules.AppModule;

/**
 * Created on 01.02.2017.
 */

public class AndroidApplication extends Application {
    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        FlowManager.init(new FlowConfig.Builder(this).build());

        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static AppComponent getComponent() {
        return component;
    }
}
