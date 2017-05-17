package com.smartagrodriver.core.di.components;

import com.smartagrodriver.core.di.modules.SliderModule;
import com.smartagrodriver.core.di.scopes.ActivityScope;
import com.smartagrodriver.core.presentation.presenters.MapSliderPresenter;
import com.smartagrodriver.core.presentation.ui.fragment.SliderControlFragment;

import dagger.Component;

/**
 * Created on 21.02.2017.
 */

@ActivityScope
@Component(modules = {SliderModule.class},
        dependencies = AppComponent.class)
public interface SliderComponent {
    void inject(SliderControlFragment fragment);

    MapSliderPresenter getMapSliderPresenter();
}
