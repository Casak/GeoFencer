package com.smartagrodriver.core.di.modules;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;

import com.smartagrodriver.core.di.scopes.ActivityScope;
import com.smartagrodriver.core.presentation.presenters.MapSliderPresenter;
import com.smartagrodriver.core.presentation.presenters.impl.MapSliderPresenterImpl;
import com.smartagrodriver.core.threading.Executor;
import com.smartagrodriver.core.threading.MainThread;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 5/17/2017.
 */

@Module
public class MapSliderControlModule {
    private final Fragment sliderControlView;

    public MapSliderControlModule(Fragment sliderControlView) {
        this.sliderControlView = sliderControlView;
    }

    @Provides
    @ActivityScope
    MapSliderPresenter provideMapSliderPresenter(Executor threadExecutor, MainThread mainThread,
                                                 SharedPreferences preferences, Context context) {
        return new MapSliderPresenterImpl(threadExecutor, mainThread, preferences, context);
    }
}
