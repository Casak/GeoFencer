package com.smartagrodriver.core.di.components;

import com.smartagrodriver.core.di.modules.MapSliderLeftModule;
import com.smartagrodriver.core.di.scopes.ActivityScope;
import com.smartagrodriver.core.presentation.presenters.MapSliderPresenter;
import com.smartagrodriver.core.presentation.ui.fragment.SliderLeftFragment;

import dagger.Component;

/**
 * Created on 21.02.2017.
 */

@ActivityScope
@Component(modules = {MapSliderLeftModule.class},
        dependencies = {AppComponent.class})
public interface MapSliderLeftComponent {
    void inject(SliderLeftFragment fragment);

    MapSliderPresenter.View getView();
}
