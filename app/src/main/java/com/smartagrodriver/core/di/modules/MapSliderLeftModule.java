package com.smartagrodriver.core.di.modules;


import com.smartagrodriver.core.di.scopes.ActivityScope;
import com.smartagrodriver.core.presentation.presenters.MapSliderPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 5/17/2017.
 */

@Module
public class MapSliderLeftModule {
    private final MapSliderPresenter.View sliderLeftView;

    public MapSliderLeftModule(MapSliderPresenter.View sliderLeftView) {
        this.sliderLeftView = sliderLeftView;
    }

    @Provides
    @ActivityScope
    public MapSliderPresenter.View getSliderLeftView() {
        return sliderLeftView;
    }
}
