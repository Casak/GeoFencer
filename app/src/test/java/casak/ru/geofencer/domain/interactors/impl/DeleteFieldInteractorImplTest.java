package casak.ru.geofencer.domain.interactors.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import casak.ru.geofencer.domain.executor.impl.ThreadExecutor;
import casak.ru.geofencer.domain.repository.FieldRepository;
import casak.ru.geofencer.threading.MainThreadImpl;

import static org.mockito.Mockito.*;

/**
 * Created on 20.03.2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class DeleteFieldInteractorImplTest {
    @Mock
    public static FieldRepository mMockFieldRepository;
    @Mock
    public static MainThreadImpl mMockMainThread;
    @Mock
    public static ThreadExecutor mMockThreadExecutor;

    public static DeleteFieldInteractorImpl mInteractor;
    public static int mFieldId = 1;

    @Before
    public void init() {
        mInteractor = new DeleteFieldInteractorImpl(mMockThreadExecutor, mMockMainThread, mMockFieldRepository);
        mInteractor.init(mFieldId);

    }

    @Test(expected = NullPointerException.class)
    public void shouldBeInitialized() {
        mInteractor = new DeleteFieldInteractorImpl(mMockThreadExecutor, mMockMainThread, mMockFieldRepository);
        mInteractor.run();
    }

    @Test
    public void run_shouldDeleteFieldFromRepo() {
        mInteractor.run();

        verify(mMockFieldRepository).deleteField(mFieldId);
    }
}