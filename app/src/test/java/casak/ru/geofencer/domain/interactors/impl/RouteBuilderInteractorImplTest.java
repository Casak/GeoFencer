package casak.ru.geofencer.domain.interactors.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import casak.ru.geofencer.domain.executor.Executor;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.domain.interactors.RouteBuilderInteractor;
import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.model.RouteModel;
import casak.ru.geofencer.domain.repository.LocationRepository;
import casak.ru.geofencer.domain.repository.RouteRepository;
import casak.ru.geofencer.domain.repository.impl.LocationRepositoryImpl;
import casak.ru.geofencer.domain.repository.impl.RouteRepositoryImpl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created on 08.01.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class RouteBuilderInteractorImplTest {
    static final int FIELD_ID = 1;

    @Mock
    static Executor mMockExecutor;
    @Mock
    static MainThread mMockMainThread;

    static RouteBuilderInteractor.Callback mMockedCallback;
    @Spy
    static LocationRepository mMockLocationRepository = Mockito.spy(LocationRepositoryImpl.class);
    @Spy
    static RouteRepository mMockRouteRepository = Mockito.spy(RouteRepositoryImpl.class);

    static RouteBuilderInteractor mInteractor;

    static Point point = new Point(50.4236835, 30.4266010);

    static RouteModel mFieldBuildingRouteModel = new RouteModel(1, RouteModel.Type.FIELD_BUILDING, FIELD_ID);


    @Before
    public void setUp() {
        when(mMockRouteRepository.getRouteModel(FIELD_ID, RouteModel.Type.FIELD_BUILDING))
                .thenReturn(null);

        mMockedCallback = Mockito.spy(new RouteBuilderInteractor.Callback() {
            @Override
            public void finished(RouteModel route) {
            }
        });

        mInteractor = new RouteBuilderInteractorImpl(
                mMockExecutor,
                mMockMainThread,
                mMockedCallback,
                mMockLocationRepository,
                mMockRouteRepository,
                FIELD_ID
        );
    }

    @Test
    public void run_withPointLocation_addPointToRoute() throws InterruptedException {
        when(mMockLocationRepository.getLastLocation()).thenReturn(point).thenReturn(null);
        when(mMockRouteRepository.createRouteModel(FIELD_ID, RouteModel.Type.FIELD_BUILDING))
                .thenReturn(mFieldBuildingRouteModel);

        ((RouteBuilderInteractorImpl) mInteractor).run();
        Thread.sleep(1000);
        mInteractor.finish();

        assertTrue(mFieldBuildingRouteModel.getRoutePoints().size() != 0);
        assertTrue(mFieldBuildingRouteModel.getRoutePoints().get(0) == point);
    }


    @Test
    public void whenRun_askForCreatingRouteModel() {
        ((RouteBuilderInteractorImpl) mInteractor).run();

        Mockito.verify(mMockRouteRepository).createRouteModel(anyInt(), any(RouteModel.Type.class));
    }

    @Test
    public void whenFinish_addRouteToRepository() {
        mInteractor.finish();

        Mockito.verify(mMockRouteRepository).addRouteModel(null);
    }

}