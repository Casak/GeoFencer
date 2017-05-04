package com.smartagrodriver.core.domain.interactors.impl;

import java.util.List;

import javax.inject.Inject;

import com.smartagrodriver.core.threading.Executor;
import com.smartagrodriver.core.threading.MainThread;
import com.smartagrodriver.core.domain.interactors.LoadFieldInteractor;
import com.smartagrodriver.core.domain.interactors.base.AbstractInteractor;
import com.smartagrodriver.core.domain.model.Field;
import com.smartagrodriver.core.domain.model.Route;
import com.smartagrodriver.core.domain.repository.FieldRepository;

/**
 * Created on 21.02.2017.
 */

public class LoadFieldInteractorImpl extends AbstractInteractor implements LoadFieldInteractor {

    private FieldRepository mFieldRepository;
    private LoadFieldInteractor.Callback mCallback;
    private int mFieldId;

    @Inject
    public LoadFieldInteractorImpl(Executor threadExecutor, MainThread mainThread,
                                   FieldRepository fieldRepository) {
        super(threadExecutor, mainThread);

        mFieldRepository = fieldRepository;
    }

    @Override
    public void init(LoadFieldInteractor.Callback callback, int fieldId) {
        mFieldId = fieldId;
        mCallback = callback;
    }

    @Override
    public void run() {
        if (mCallback == null || mFieldId == 0) {
            throw new NullPointerException("LoadFieldInteractor was not initialized!");
        }

        final Field field = mFieldRepository.getField(mFieldId);
        final List<Route> routes = field.getRoutes();

        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.showField(field);
                for (Route route : routes) {
                    mCallback.showRoute(route);
                }
            }
        });
    }
}
