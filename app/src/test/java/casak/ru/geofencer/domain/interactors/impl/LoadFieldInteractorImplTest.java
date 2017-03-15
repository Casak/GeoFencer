package casak.ru.geofencer.domain.interactors.impl;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import casak.ru.geofencer.threading.impl.ThreadExecutor;
import casak.ru.geofencer.domain.interactors.LoadFieldInteractor;
import casak.ru.geofencer.domain.model.Field;
import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.model.Route;
import casak.ru.geofencer.domain.repository.FieldRepository;
import casak.ru.geofencer.threading.impl.MainThreadImpl;

import static org.mockito.Mockito.*;

/**
 * Created by Casak on 21.02.2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class LoadFieldInteractorImplTest {
    static class Called extends RuntimeException {
    }

    @Mock
    public static FieldRepository mMockFieldRepository;
    @Mock
    public static MainThreadImpl mMockMainThread;
    @Mock
    public static ThreadExecutor mMockThreadExecutor;

    public static LoadFieldInteractorImpl mInteractor;
    public static LoadFieldInteractor.Callback mCallback;
    public static List<Point> mFieldPoints;
    public static Field mField;
    public static int mFieldId;


    @BeforeClass
    public static void fieldInit() {
        mFieldPoints = new ArrayList<>();
        mFieldPoints.add(new Point(50, 50));
        mFieldPoints.add(new Point(50, 51));
        mFieldPoints.add(new Point(51, 51));
        mFieldPoints.add(new Point(51, 50));

        mFieldId = 9000;

        mField = new Field(mFieldId, mFieldPoints);

        for (int i = 0; i < 5; i++) {
            mField.addRoute(new Route(i, mFieldId, Route.Type.COMPUTED));
        }
    }

    @Before
    public void init() {
        mCallback = spy(new LoadFieldInteractor.Callback() {
            @Override
            public void showField(Field model) {
                throw new Called();
            }

            @Override
            public void hideField(Field model) {
                throw new Called();
            }

            @Override
            public void showRoute(Route model) {
                throw new Called();
            }

            @Override
            public void hideRoute(Route model) {
                throw new Called();
            }
        });

        mInteractor = spy(new LoadFieldInteractorImpl(mMockThreadExecutor, mMockMainThread, mMockFieldRepository));
        mInteractor.init(mCallback, mFieldId);
    }

    @Test(expected = NullPointerException.class)
    public void beforeExecute_shouldBeInited_orThrowException() {
        mInteractor.run();

        verify(mInteractor).init(mCallback, mFieldId);

        mInteractor = new LoadFieldInteractorImpl(mMockThreadExecutor, mMockMainThread, mMockFieldRepository);

        mInteractor.run();

        verify(mInteractor, never()).init(mCallback, mFieldId);
    }

    @Ignore
    @Test
    public void run_inited_callsCallback() {
        when(mMockFieldRepository.getField(anyInt())).thenReturn(mField);

        mInteractor.run();

        verify(mCallback).showField(any(Field.class));
        verify(mCallback, times(mField.getRoutes().size())).showRoute(any(Route.class));
    }
}