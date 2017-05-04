package com.smartagrodriver.core.domain.interactors.impl;

import javax.inject.Inject;

import com.smartagrodriver.core.threading.Executor;
import com.smartagrodriver.core.threading.MainThread;
import com.smartagrodriver.core.domain.interactors.BuildArrowsInteractor;
import com.smartagrodriver.core.domain.interactors.BuildFieldInteractor;
import com.smartagrodriver.core.domain.interactors.CreateFieldInteractor;
import com.smartagrodriver.core.domain.interactors.LocationInteractor;
import com.smartagrodriver.core.domain.interactors.RouteBuilderInteractor;
import com.smartagrodriver.core.domain.interactors.base.AbstractInteractor;
import com.smartagrodriver.core.domain.model.Arrow;
import com.smartagrodriver.core.domain.model.Field;
import com.smartagrodriver.core.domain.model.Route;
import com.smartagrodriver.core.domain.repository.ArrowRepository;
import com.smartagrodriver.core.domain.repository.FieldRepository;
import com.smartagrodriver.core.domain.repository.RouteRepository;

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