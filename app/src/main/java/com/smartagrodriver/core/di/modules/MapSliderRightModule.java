package com.smartagrodriver.core.di.modules;

import com.smartagrodriver.core.di.scopes.ActivityScope;
import com.smartagrodriver.core.presentation.presenters.MapSliderPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 5/17/2017.
 */

@Module
public class MapSliderRightModule {
    private final MapSliderPresenter.View sliderRightView;

    public MapSliderRightModule(MapSliderPresenter.View sliderRightView) {
        this.sliderRightView = sliderRightView;
    }

    @Provides
    @ActivityScope
    MapSliderPresenter.View provideView() {
        return sliderRightView;
    }

}