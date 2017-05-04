package com.smartagrodriver.core.di.modules;

import com.smartagrodriver.core.bluetooth.AntennaDataObservable;
import com.smartagrodriver.core.di.scopes.ActivityScope;
import com.smartagrodriver.core.threading.Executor;
import com.smartagrodriver.core.threading.MainThread;
import com.smartagrodriver.core.domain.interactors.PointerInteractor;
import com.smartagrodriver.core.domain.interactors.impl.PointerInteractorImpl;
import com.smartagrodriver.core.presentation.presenters.MapPointerPresenter;
import com.smartagrodriver.core.presentation.presenters.impl.MapPointerPresenterImpl;
import dagger.Module;
import dagger.Provides;

/**
 * Created on 21.02.2017.
 */

@Module
public class PointerModule {
    private final MapPointerPresenter.View pointerView;

    public PointerModule(MapPointerPresenter.View view) {
        pointerView = view;
    }

    @Provides
    @ActivityScope
    MapPointerPresenter.View providesPointerView() {
        return pointerView;
    }

    @Provides
    @ActivityScope
    MapPointerPresenter providesMapPointerPresenter(Executor threadExecutor,
                                                    MainThread mainThread,
                                                    PointerInteractor interactor,
                                                    AntennaDataObservable observable) {
        return new MapPointerPresenterImpl(
                threadExecutor,
                mainThread,
                interactor,
                observable
        );
    }

    @Provides
    @ActivityScope
    PointerInteractor providesPointerInteractor(PointerInteractorImpl interactor) {
        return interactor;
    }
}