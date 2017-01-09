package casak.ru.geofencer.domain.interactors.impl;

import casak.ru.geofencer.domain.executor.Executor;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.domain.interactors.CreateFieldInteractor;
import casak.ru.geofencer.domain.interactors.RouteBuilderInteractor;
import casak.ru.geofencer.domain.interactors.base.AbstractInteractor;
import casak.ru.geofencer.domain.model.RouteModel;
import casak.ru.geofencer.domain.repository.ArrowRepository;
import casak.ru.geofencer.domain.repository.FieldRepository;
import casak.ru.geofencer.domain.repository.RouteRepository;

/**
 * Created on 05.01.2017.
 */

public class CreateFieldInteractorImpl extends AbstractInteractor implements CreateFieldInteractor {
    private static final String TAG = CreateFieldInteractorImpl.class.getSimpleName();

    private CreateFieldInteractor.Callback mCallback;
    private RouteRepository mRouteRepository;
    private ArrowRepository mArrowRepository;
    private FieldRepository mFieldRepository;
    private RouteBuilderInteractor mRouteBuilderInteractor;

    public CreateFieldInteractorImpl(Executor threadExecutor, MainThread mainThread,
                                     CreateFieldInteractor.Callback callback, RouteRepository routeRepository,
                                     ArrowRepository arrowRepository, FieldRepository fieldRepository,
                                     RouteBuilderInteractor routeBuilderInteractor) {
        super(threadExecutor, mainThread);
        mCallback = callback;
        mRouteRepository = routeRepository;
        mArrowRepository = arrowRepository;
        mFieldRepository = fieldRepository;
        mRouteBuilderInteractor = routeBuilderInteractor;
    }

    @Override
    public void run() {
        mRouteBuilderInteractor.execute();
    }

    public void onStartCreatingRouteClick(){
        mRouteBuilderInteractor.startBuildRoute(RouteModel.Type.FIELD_BUILDING);
    }
}