package casak.ru.geofencer.domain.interactors.impl;

import javax.inject.Inject;

import casak.ru.geofencer.threading.Executor;
import casak.ru.geofencer.threading.MainThread;
import casak.ru.geofencer.domain.interactors.BuildArrowsInteractor;
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

public class CreateFieldInteractorImpl extends AbstractInteractor implements CreateFieldInteractor,
        BuildFieldInteractor.Callback, RouteBuilderInteractor.Callback {
    private static final String TAG = CreateFieldInteractorImpl.class.getSimpleName();

    private CreateFieldInteractor.Callback mCallback;
    private RouteRepository mRouteRepository;
    private ArrowRepository mArrowRepository;
    private FieldRepository mFieldRepository;
    private RouteBuilderInteractor mRouteBuilderInteractor;
    private BuildArrowsInteractor mArrowsInteractor;
    private BuildFieldInteractor mBuildFieldInteractor;
    private int fieldId;
    private Field mField;
    private Arrow mArrowChosen;

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

        mArrowsInteractor = new BuildArrowsInteractorImpl(
                mThreadExecutor,
                mMainThread,
                mCallback,
                mRouteRepository,
                mArrowRepository);

        mBuildFieldInteractor = new BuildFieldInteractorImpl(
                mThreadExecutor,
                mMainThread,
                this,
                mRouteRepository,
                mFieldRepository,
                width);

        mRouteBuilderInteractor = new RouteBuilderInteractorImpl(
                mThreadExecutor,
                mMainThread,
                this,
                mRouteRepository,
                fieldId,
                width);
    }

    @Override
    public void run() {
        if (mCallback == null) {
            throw new NullPointerException(TAG + " was not initialized;");
        }
    }

    @Override
    public void onStartCreatingRoute() {
        mRouteBuilderInteractor.execute();
    }

    @Override
    public void onFinishCreatingRoute() {
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
                mArrowsInteractor.init(route, fieldId);
                mArrowsInteractor.execute();
                break;
            case COMPUTED:
                if (mField == null)
                    mField = mFieldRepository.getField(fieldId);
                mField.addRoute(route);
                mFieldRepository.updateField(mField);
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
    public void onArrowClick(Arrow model) {
        mArrowChosen = model;

        mArrowsInteractor.onArrowClick(model);

        mBuildFieldInteractor.init(mField, model);
        mBuildFieldInteractor.execute();
    }

    @Override
    public void onFieldBuildFinish(final Field field) {
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.showField(field);
            }
        });

        boolean toLeft = mArrowChosen.isChosen();

        if (mArrowChosen.getType() == Arrow.Type.RIGHT) {
            toLeft = !toLeft;
        }

        mRouteBuilderInteractor.createComputedRoutes(field.getId(), toLeft);
    }

    @Override
    public void onFieldBuildFail(Field field) {
        //TODO Error handling
    }

    private int computeFieldId() {
        mField = mFieldRepository.createField();
        return mField.getId();
    }
}