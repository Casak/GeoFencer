package com.smartagrodriver.core.di.components;

import com.smartagrodriver.core.di.modules.InteractorModule;
import com.smartagrodriver.core.di.modules.RepositoryModule;
import com.smartagrodriver.core.domain.interactors.CreateFieldInteractor;
import com.smartagrodriver.core.di.modules.ActivityModule;
import com.smartagrodriver.core.di.modules.MapModule;
import com.smartagrodriver.core.di.scopes.ActivityScope;
import com.smartagrodriver.core.presentation.presenters.CameraPresenter;
import com.smartagrodriver.core.presentation.presenters.GoogleMapPresenter;
import com.smartagrodriver.core.presentation.presenters.impl.GoogleMapPresenterImpl;
import com.smartagrodriver.core.presentation.ui.fragment.GoogleMapFragment;
import com.smartagrodriver.core.presentation.ui.fragment.MapButtonFragment;
import com.smartagrodriver.core.presentation.ui.fragment.SliderRightFragment;
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
    void inject(GoogleMapFragment fragment);

    void inject(MapButtonFragment fragment);

    void inject(SliderRightFragment fragment);

    void inject(GoogleMapPresenterImpl presenter);

    GoogleMapPresenter.View getGoogleMapPresenterView();

    GoogleMapPresenter getGoogleMapPresenter();

    CameraPresenter getCameraPresenter();

    CreateFieldInteractor.Callback getCreateFieldInteractorCallback();

}
