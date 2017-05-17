package com.smartagrodriver.core.di.components;

import com.smartagrodriver.core.di.modules.SliderLeftModule;
import com.smartagrodriver.core.di.scopes.ActivityScope;
import com.smartagrodriver.core.presentation.presenters.MapSliderPresenter;
import com.smartagrodriver.core.presentation.ui.fragment.SliderLeftFragment;

import dagger.Component;

/**
 * Created on 21.02.2017.
 */

@ActivityScope
@Component(modules = {SliderLeftModule.class},
        dependencies = {AppComponent.class})
public interface SliderLeftComponent {
    void inject(SliderLeftFragment fragment);

    MapSliderPresenter.View getView();
}
