package com.smartagrodriver.core.di.components;

import com.smartagrodriver.core.di.modules.InteractorModule;
import com.smartagrodriver.core.di.modules.RepositoryModule;
import com.smartagrodriver.core.domain.interactors.CreateFieldInteractor;
import com.smartagrodriver.core.di.modules.ActivityModule;
import com.smartagrodriver.core.di.modules.MapModule;
import com.smartagrodriver.core.di.scopes.ActivityScope;
import com.smartagrodriver.core.presentation.presenters.MapCameraPresenter;
import com.smartagrodriver.core.presentation.presenters.MapPresenter;
import com.smartagrodriver.core.presentation.presenters.impl.MapPresenterImpl;
import com.smartagrodriver.core.presentation.ui.fragment.MapFragment;
import com.smartagrodriver.core.presentation.ui.fragment.MapButtonFragment;

import dagger.Component;

/**
 * Created on 09.02.2017.
 */

@ActivityScope
@Component(modules = {
        ActivityModule.class,
        MapModule.class,
        RepositoryModule.class,
        InteractorModule.class},
        dependencies = AppComponent.class)
public interface MapComponent extends AbstractActivityComponent, InteractorComponent {
    void inject(MapFragment fragment);

    void inject(MapButtonFragment fragment);

    void inject(MapPresenterImpl presenter);

    MapPresenter.View getGoogleMapPresenterView();

    MapPresenter getGoogleMapPresenter();

    MapCameraPresenter getCameraPresenter();

    CreateFieldInteractor.Callback getCreateFieldInteractorCallback();

}
