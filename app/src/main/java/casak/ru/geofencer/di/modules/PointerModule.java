package casak.ru.geofencer.di.modules;

import casak.ru.geofencer.bluetooth.AntennaDataProvider;
import casak.ru.geofencer.di.scopes.ActivityScope;
import casak.ru.geofencer.domain.executor.Executor;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.domain.interactors.PointerInteractor;
import casak.ru.geofencer.domain.interactors.impl.PointerInteractorImpl;
import casak.ru.geofencer.domain.repository.FieldRepository;
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
                                                    AntennaDataProvider provider,
                                                    FieldRepository fieldRepository) {
        return new MapPointerPresenterImpl(
                threadExecutor,
                mainThread,
                interactor,
                provider,
                fieldRepository
        );
    }

    @Provides
    @ActivityScope
    PointerInteractor providesPointerInteractor(PointerInteractorImpl interactor) {
        return interactor;
    }
}
