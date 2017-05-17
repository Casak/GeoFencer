package com.smartagrodriver.core.di.modules;

import com.smartagrodriver.core.di.scopes.ActivityScope;
import com.smartagrodriver.core.domain.interactors.LocationInteractor;
import com.smartagrodriver.core.domain.interactors.impl.LocationInteractorImpl;
import com.smartagrodriver.core.domain.repository.LocationRepository;
import com.smartagrodriver.core.presentation.presenters.MapPresenter;
import com.smartagrodriver.core.presentation.presenters.MapSliderPresenter;
import com.smartagrodriver.core.presentation.presenters.impl.MapPresenterImpl;
import com.smartagrodriver.core.presentation.presenters.impl.MapSliderPresenterImpl;
import com.smartagrodriver.core.storage.LocationRepositoryImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 5/17/2017.
 */

@Module
public class SliderRightModule {
    private final MapSliderPresenter.View sliderRightView;

    public SliderRightModule(MapSliderPresenter.View sliderRightView) {
        this.sliderRightView = sliderRightView;
    }

    @Provides
    @ActivityScope
    MapSliderPresenter.View provideView(){
        return sliderRightView;
    }

    @Provides
    @ActivityScope
    MapSliderPresenter provideMapPresenter(MapSliderPresenterImpl presenter){
        return presenter;
    }

    @Provides
    @ActivityScope
    LocationInteractor provideLocationInteractor(LocationInteractorImpl interactor){
        return interactor;
    }

    @Provides
    @ActivityScope
    LocationRepository provideLocationRepository(LocationRepositoryImpl repository){
        return repository;
    }
}
