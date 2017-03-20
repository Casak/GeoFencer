package casak.ru.geofencer.domain.interactors.impl;

import javax.inject.Inject;

import casak.ru.geofencer.domain.executor.Executor;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.domain.interactors.DeleteFieldInteractor;
import casak.ru.geofencer.domain.interactors.base.AbstractInteractor;
import casak.ru.geofencer.domain.repository.FieldRepository;

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