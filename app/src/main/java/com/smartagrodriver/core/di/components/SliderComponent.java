package com.smartagrodriver.core.di.components;

import com.smartagrodriver.core.di.modules.ActivityModule;
import com.smartagrodriver.core.di.modules.SliderModule;
import com.smartagrodriver.core.di.scopes.ActivityScope;
import com.smartagrodriver.core.presentation.presenters.MapSliderPresenter;
import com.smartagrodriver.core.presentation.ui.fragment.SliderControlFragment;

import dagger.Component;

/**
 * Created on 21.02.2017.
 */

@ActivityScope
@Component(modules = {SliderModule.class, ActivityModule.class},
        dependencies = AppComponent.class)
public interface SliderComponent extends AbstractActivityComponent {
    void inject(SliderControlFragment fragment);
/*
    MapSliderPresenter.View getLeftView();

    MapSliderPresenter.View getRightView();*/

    MapSliderPresenter getMapSliderPresenter();
}
