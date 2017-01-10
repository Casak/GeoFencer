package casak.ru.geofencer.domain.interactors.impl;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
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

    @Mock
    static Executor mMockExecutor;
    @Mock
    static MainThread mMockMainThread;
    @Mock
    static RouteBuilderInteractor.Callback mMockedCallback;
    @Mock
    static LocationRepository mMockLocationRepository = Mockito.spy(LocationRepositoryImpl.class);
    @Mock
    static RouteRepository mMockRouteRepository = Mockito.spy(RouteRepositoryImpl.class);

    static RouteBuilderInteractor mInteractor;

    static Point point = new Point(50.4236835, 30.4266010);

    static RouteModel mFieldBuildingRouteModel = new RouteModel(1, RouteModel.Type.FIELD_BUILDING, 1);


    @BeforeClass
    public static void setUp() {
        when(mMockRouteRepository.getRouteModel(anyInt(), RouteModel.Type.FIELD_BUILDING))
                .thenReturn(null);
        when(mMockRouteRepository.createRouteModel(anyInt(), RouteModel.Type.FIELD_BUILDING))
                .thenReturn(mFieldBuildingRouteModel);

        mInteractor = new RouteBuilderInteractorImpl(
                mMockExecutor,
                mMockMainThread,
                mMockedCallback,
                mMockLocationRepository,
                mMockRouteRepository,
                anyInt()
        );
    }

/*
    @Test
    public void startAndFinishBuildRouteWithNewPoint_TypeAndRouteModel_AddNewPointToRouteModel() {
        RouteModel result = mInteractor.startBuildRoute(RouteModel.Type.FIELD_BUILDING);

        assertNotNull(result);
        assertTrue(result.getRoutePoints().size() == 0);

        mInteractor.newPoint(point);

        assertTrue(result.getRoutePoints().size() == 1);

        mInteractor.finishBuildRoute(result);
        mInteractor.newPoint(point);

        assertTrue(result.getRoutePoints().size() == 1);
    }
*/


}