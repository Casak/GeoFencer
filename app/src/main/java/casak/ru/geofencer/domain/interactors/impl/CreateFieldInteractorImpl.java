package casak.ru.geofencer.domain.interactors.impl;

import javax.inject.Inject;

import casak.ru.geofencer.domain.executor.Executor;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.domain.interactors.BuildArrowModelsInteractor;
import casak.ru.geofencer.domain.interactors.BuildFieldInteractor;
import casak.ru.geofencer.domain.interactors.CreateFieldInteractor;
import casak.ru.geofencer.domain.interactors.RouteBuilderInteractor;
import casak.ru.geofencer.domain.interactors.base.AbstractInteractor;
import casak.ru.geofencer.domain.model.Arrow;
import casak.ru.geofencer.domain.model.Field;
import casak.ru.geofencer.domain.model.Route;
import casak.ru.geofencer.domain.repository.ArrowRepository;
import casak.ru.geofencer.domain.repository.FieldRepository;
import casak.ru.geofencer.domain.repository.LocationRepository;
import casak.ru.geofencer.domain.repository.RouteRepository;

/**
 * Created on 05.01.2017.
 */

public class CreateFieldInteractorImpl extends AbstractInteractor implements CreateFieldInteractor, BuildArrowModelsInteractor.Callback, BuildFieldInteractor.Callback, RouteBuilderInteractor.Callback {
    private static final String TAG = CreateFieldInteractorImpl.class.getSimpleName();

    private CreateFieldInteractor.Callback mCallback;
    private RouteRepository mRouteRepository;
    private ArrowRepository mArrowRepository;
    private FieldRepository mFieldRepository;
    private LocationRepository mLocationRepository;
    private RouteBuilderInteractor mRouteBuilderInteractor;
    private BuildArrowModelsInteractor mArrowsInteractor;
    private BuildFieldInteractor mBuildFieldInteractor;
    private Route mBuildingRoute;
    private int fieldId;
    private Field field;

    @Inject
    public CreateFieldInteractorImpl(Executor threadExecutor, MainThread mainThread,
                                     CreateFieldInteractor.Callback callback,
                                     RouteRepository routeRepository, ArrowRepository arrowRepository,
                                     FieldRepository fieldRepository, LocationRepository locationRepository) {
        super(threadExecutor, mainThread);
        //TODO Fix it, bastard
        fieldId = computeFieldId();

        mCallback = callback;
        mRouteRepository = routeRepository;
        mArrowRepository = arrowRepository;
        mFieldRepository = fieldRepository;
        mLocationRepository = locationRepository;

        mArrowsInteractor = new BuildArrowModelsInteractorImpl(
                mThreadExecutor,
                mMainThread,
                this,
                mRouteRepository,
                mArrowRepository,
                fieldId);
    }

    @Override
    public void setMachineryWidth(int width) {
        mBuildFieldInteractor = new BuildFieldInteractorImpl(
                mThreadExecutor,
                mMainThread,
                this,
                mRouteRepository,
                mArrowRepository,
                mFieldRepository,
                fieldId,
                width);

        //TODO Move locationRepository usage logic to a class
        mRouteBuilderInteractor = new RouteBuilderInteractorImpl(
                mThreadExecutor,
                mMainThread,
                this,
                mLocationRepository,
                mRouteRepository,
                mArrowRepository,
                fieldId,
                width);
    }

    @Override
    public void run() {
        mBuildingRoute = mRouteRepository.getBaseRoute(fieldId);

        if (mBuildingRoute != null) {
            //TODO Alert
        }
    }

    public void onStartCreatingRouteClick() {
        mRouteBuilderInteractor.execute();
    }

    public void onFinishCreatingRouteClick() {
        mRouteBuilderInteractor.finish();
    }

    @Override
    public void routeBuildingFinished(Route route) {
        Route.Type routeType = route.getType();
        switch (routeType) {
            case BASE:
                mBuildingRoute = route;
                createArrows();
                break;
            case COMPUTED:
                if (field == null)
                    field = mFieldRepository.getField(fieldId);
                field.addRoute(route);
                mFieldRepository.updateField(field);
                mCallback.showRoute(route);
                break;
        }
    }

    @Override
    public void onArrowsBuildFinished(long fieldId) {
        mCallback.showArrow(mArrowRepository.getLeft(fieldId));
        mCallback.showArrow(mArrowRepository.getRight(fieldId));
    }

    @Override
    public void onArrowsBuildFailed(long fieldId) {
        //TODO Error handling
    }

    @Override
    public void onArrowClick(Arrow model) {
        model.setChosen(true);
        mBuildFieldInteractor.execute();
    }

    @Override
    public void onFieldBuildFinish() {
        field = mFieldRepository.getField(fieldId);
        mCallback.hideArrow(mArrowRepository.getLeft(fieldId));
        mCallback.hideArrow(mArrowRepository.getLeft(fieldId));
        mArrowRepository.delete(fieldId);
        mCallback.showField(field);

        createComputedRoutes();
    }

    @Override
    public void onFieldBuildFail() {
        //TODO Error handling
    }


    private void createArrows() {
        mArrowsInteractor.execute();
    }

    private void createComputedRoutes() {
        mRouteBuilderInteractor.createComputedRoutes(fieldId);
    }

    //TODO implement method
    private int computeFieldId() {
        return 1000;
    }
}