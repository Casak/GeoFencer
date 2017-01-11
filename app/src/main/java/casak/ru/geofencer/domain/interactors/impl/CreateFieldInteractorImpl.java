package casak.ru.geofencer.domain.interactors.impl;

import casak.ru.geofencer.domain.executor.Executor;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.domain.interactors.BuildArrowModelsInteractor;
import casak.ru.geofencer.domain.interactors.BuildFieldInteractor;
import casak.ru.geofencer.domain.interactors.CreateFieldInteractor;
import casak.ru.geofencer.domain.interactors.RouteBuilderInteractor;
import casak.ru.geofencer.domain.interactors.base.AbstractInteractor;
import casak.ru.geofencer.domain.model.ArrowModel;
import casak.ru.geofencer.domain.model.FieldModel;
import casak.ru.geofencer.domain.model.RouteModel;
import casak.ru.geofencer.domain.repository.ArrowRepository;
import casak.ru.geofencer.domain.repository.FieldRepository;
import casak.ru.geofencer.domain.repository.LocationRepository;
import casak.ru.geofencer.domain.repository.RouteRepository;

import static casak.ru.geofencer.domain.model.RouteModel.Type.FIELD_BUILDING;

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
    private RouteModel mBuildingRouteModel;
    private int fieldId;
    private FieldModel field;

    public CreateFieldInteractorImpl(Executor threadExecutor, MainThread mainThread,
                                     CreateFieldInteractor.Callback callback,
                                     RouteRepository routeRepository, ArrowRepository arrowRepository,
                                     FieldRepository fieldRepository, LocationRepository locationRepository) {
        super(threadExecutor, mainThread);
        fieldId = computeFieldId();

        mCallback = callback;
        mRouteRepository = routeRepository;
        mArrowRepository = arrowRepository;
        mFieldRepository = fieldRepository;
        mLocationRepository = locationRepository;

        mRouteBuilderInteractor = new RouteBuilderInteractorImpl(
                mThreadExecutor,
                mMainThread,
                this,
                mLocationRepository,
                mRouteRepository,
                mArrowRepository,
                fieldId);

        mArrowsInteractor = new BuildArrowModelsInteractorImpl(
                mThreadExecutor,
                mMainThread,
                this,
                mRouteRepository,
                fieldId);

        mBuildFieldInteractor = new BuildFieldInteractorImpl(
                mThreadExecutor,
                mMainThread,
                this,
                mRouteRepository,
                mArrowRepository,
                mFieldRepository,
                fieldId);
    }

    @Override
    public void run() {
        mBuildingRouteModel = mRouteRepository.getRouteModel(fieldId, FIELD_BUILDING);

        if (mBuildingRouteModel != null) {
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
    public void routeBuildingFinished(RouteModel route) {
        RouteModel.Type routeType = route.getType();
        switch (routeType) {
            case FIELD_BUILDING:
                mBuildingRouteModel = route;
                createArrows();
                break;
            case COMPUTED:
                field.addComputedRoute(route);
                mFieldRepository.updateField(field);
                mCallback.showRoute(route);
                break;
        }
    }

    @Override
    public void onArrowsBuildFinished(int fieldId) {
        mCallback.showArrow(mArrowRepository.getLeftArrow(fieldId));
        mCallback.showArrow(mArrowRepository.getRightArrow(fieldId));
    }

    @Override
    public void onArrowsBuildFailed(int fieldId) {
        //TODO Error handling
    }

    @Override
    public void onArrowClick(ArrowModel model) {
        model.setChosen(true);
        mBuildFieldInteractor.execute();
    }

    @Override
    public void onFieldBuildFinish() {
        field = mFieldRepository.getField(fieldId);
        mCallback.hideArrow(mArrowRepository.getLeftArrow(fieldId));
        mCallback.hideArrow(mArrowRepository.getLeftArrow(fieldId));
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

    private void createComputedRoutes(){
        mRouteBuilderInteractor.createComputedRoutes(fieldId);
    }

    //TODO implement method
    private int computeFieldId() {
        return 1;
    }
}