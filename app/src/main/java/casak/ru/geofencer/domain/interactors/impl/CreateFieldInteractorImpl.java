package casak.ru.geofencer.domain.interactors.impl;

import javax.inject.Inject;

import casak.ru.geofencer.domain.executor.Executor;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.domain.interactors.BuildArrowModelsInteractor;
import casak.ru.geofencer.domain.interactors.BuildFieldInteractor;
import casak.ru.geofencer.domain.interactors.CreateFieldInteractor;
import casak.ru.geofencer.domain.interactors.LocationInteractor;
import casak.ru.geofencer.domain.interactors.RouteBuilderInteractor;
import casak.ru.geofencer.domain.interactors.base.AbstractInteractor;
import casak.ru.geofencer.domain.model.Arrow;
import casak.ru.geofencer.domain.model.Field;
import casak.ru.geofencer.domain.model.Route;
import casak.ru.geofencer.domain.repository.ArrowRepository;
import casak.ru.geofencer.domain.repository.FieldRepository;
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
    private RouteBuilderInteractor mRouteBuilderInteractor;
    private BuildArrowModelsInteractor mArrowsInteractor;
    private BuildFieldInteractor mBuildFieldInteractor;
    private Route mBuildingRoute;
    private int fieldId;
    private Field field;

    @Inject
    public CreateFieldInteractorImpl(Executor threadExecutor, MainThread mainThread,
                                     RouteRepository routeRepository, ArrowRepository arrowRepository,
                                     FieldRepository fieldRepository) {
        super(threadExecutor, mainThread);

        mRouteRepository = routeRepository;
        mArrowRepository = arrowRepository;
        mFieldRepository = fieldRepository;
    }

    @Override
    public void init(CreateFieldInteractor.Callback callback, int width) {
        mCallback = callback;

        fieldId = computeFieldId();

        mArrowsInteractor = new BuildArrowModelsInteractorImpl(
                mThreadExecutor,
                mMainThread,
                this,
                mRouteRepository,
                mArrowRepository,
                fieldId);

        mBuildFieldInteractor = new BuildFieldInteractorImpl(
                mThreadExecutor,
                mMainThread,
                this,
                mRouteRepository,
                mArrowRepository,
                mFieldRepository,
                fieldId,
                width);

        mRouteBuilderInteractor = new RouteBuilderInteractorImpl(
                mThreadExecutor,
                mMainThread,
                this,
                mRouteRepository,
                mArrowRepository,
                fieldId,
                width);
    }

    @Override
    public void run() {
        if (mCallback == null) {
            throw new NullPointerException("CreateFieldInteractor was not initialized;");
        }

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
    public LocationInteractor.OnLocationChangedListener getOnLocationChangedListener() {
        return mRouteBuilderInteractor;
    }

    @Override
    public void routeBuildingFinished(final Route route) {
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
                mMainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallback.showRoute(route);
                    }
                });
                break;
        }
    }

    @Override
    public void onArrowsBuildFinished(final long fieldId) {
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.showArrow(mArrowRepository.getLeft(fieldId));
                mCallback.showArrow(mArrowRepository.getRight(fieldId));
            }
        });
    }

    @Override
    public void onArrowsBuildFailed(long fieldId) {
        //TODO Error handling
    }

    @Override
    public void onArrowClick(Arrow model) {
        final Arrow left = mArrowRepository.getLeft(fieldId);
        final Arrow right = mArrowRepository.getRight(fieldId);

        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.hideArrow(left);
                mCallback.hideArrow(right);
            }
        });

        model.setChosen(true);
        mBuildFieldInteractor.execute();
    }

    @Override
    public void onFieldBuildFinish() {
        field = mFieldRepository.getField(fieldId);

        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.showField(field);
            }
        });

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
        mArrowRepository.delete(fieldId);
    }

    private int computeFieldId() {
        Field result = mFieldRepository.createField();
        return result.getId();
    }
}