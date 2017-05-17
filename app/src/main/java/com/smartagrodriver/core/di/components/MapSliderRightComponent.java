package com.smartagrodriver.core.di.components;

import com.smartagrodriver.core.di.modules.InteractorModule;
import com.smartagrodriver.core.di.modules.MapModule;
import com.smartagrodriver.core.di.modules.MapSliderRightModule;
import com.smartagrodriver.core.di.modules.RepositoryModule;
import com.smartagrodriver.core.di.scopes.ActivityScope;
import com.smartagrodriver.core.presentation.presenters.MapSliderPresenter;
import com.smartagrodriver.core.presentation.ui.fragment.MapSliderRightFragment;

import dagger.Component;

/**
 * Created on 21.02.2017.
 */

@ActivityScope
@Component(modules = {MapSliderRightModule.class, MapModule.class, InteractorModule.class,
        RepositoryModule.class},
        dependencies = {AppComponent.class})
public interface MapSliderRightComponent {
    void inject(MapSliderRightFragment fragment);

    MapSliderPresenter.View getView();
}
