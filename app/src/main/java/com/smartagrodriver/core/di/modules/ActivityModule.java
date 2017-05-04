package com.smartagrodriver.core.di.modules;

import android.app.Activity;

import com.smartagrodriver.core.di.scopes.ActivityScope;
import dagger.Module;
import dagger.Provides;

/**
 * Created on 08.02.2017.
 */

@Module
public class ActivityModule {
    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    Activity getActivityContext() {
        return activity;
    }
}