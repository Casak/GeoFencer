package com.smartagrodriver.core.domain.interactors.impl;

import javax.inject.Inject;

import com.smartagrodriver.core.threading.Executor;
import com.smartagrodriver.core.threading.MainThread;
import com.smartagrodriver.core.domain.interactors.DeleteFieldInteractor;
import com.smartagrodriver.core.domain.interactors.base.AbstractInteractor;
import com.smartagrodriver.core.domain.repository.FieldRepository;

/**
 * Created on 20.03.2017.
 */

public class DeleteFieldInteractorImpl extends AbstractInteractor implements DeleteFieldInteractor {

    private FieldRepository mFieldRepository;
    private int mFieldId;

    @Inject
    public DeleteFieldInteractorImpl(Executor threadExecutor, MainThread mainThread,
                                     FieldRepository fieldRepository) {
        super(threadExecutor, mainThread);

        mFieldRepository = fieldRepository;
    }

    @Override
    public void init(int fieldId) {
        mFieldId = fieldId;
    }

    @Override
    public void run() {
        if (mFieldId == 0) {
            throw new NullPointerException("DeleteFieldInteractor was not initialized!");
        }
        mFieldRepository.deleteField(mFieldId);
    }
}