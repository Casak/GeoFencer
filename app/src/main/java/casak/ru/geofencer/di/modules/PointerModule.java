package casak.ru.geofencer.di.modules;

import casak.ru.geofencer.bluetooth.AntennaDataObservable;
import casak.ru.geofencer.di.scopes.ActivityScope;
import casak.ru.geofencer.threading.Executor;
import casak.ru.geofencer.threading.MainThread;
import casak.ru.geofencer.domain.interactors.PointerInteractor;
import casak.ru.geofencer.domain.interactors.impl.PointerInteractorImpl;
import casak.ru.geofencer.presentation.presenters.MapPointerPresenter;
import casak.ru.geofencer.presentation.presenters.impl.MapPointerPresenterImpl;
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