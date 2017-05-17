package com.smartagrodriver.core.di.components;

import com.smartagrodriver.core.di.modules.ActivityModule;
import com.smartagrodriver.core.di.modules.MapModule;
import com.smartagrodriver.core.di.modules.SliderModule;
import com.smartagrodriver.core.di.modules.SliderRightModule;
import com.smartagrodriver.core.di.scopes.ActivityScope;
import com.smartagrodriver.core.presentation.presenters.MapSliderPresenter;
import com.smartagrodriver.core.presentation.ui.fragment.SliderControlFragment;
import com.smartagrodriver.core.presentation.ui.fragment.SliderRightFragment;

import dagger.Component;

/**
 * Created on 21.02.2017.
 */

@ActivityScope
@Component(modules = {SliderRightModule.class, MapModule.class},
        dependencies = AppComponent.class)
public interface SliderRightComponent extends AbstractActivityComponent {
    void inject(SliderRightFragment fragment);

    MapSliderPresenter.View getView();

    MapSliderPresenter getMapSliderPresenter();
}
