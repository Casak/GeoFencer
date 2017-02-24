package casak.ru.geofencer.domain.interactors.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import casak.ru.geofencer.domain.Constants;
import casak.ru.geofencer.domain.executor.Executor;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.domain.interactors.RouteBuilderInteractor;
import casak.ru.geofencer.domain.model.Arrow;
import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.model.Route;
import casak.ru.geofencer.domain.repository.ArrowRepository;
import casak.ru.geofencer.domain.repository.LocationRepository;
import casak.ru.geofencer.domain.repository.RouteRepository;
import casak.ru.geofencer.storage.ArrowRepositoryImpl;

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
    @Mock
    static Arrow mLeftArrow;

    static RouteBuilderInteractor.Callback mMockedCallback;
    @Mock
    static LocationRepository mMockLocationRepository;
    @Mock
    static RouteRepository mMockRouteRepository;
    @Spy
    private ArrowRepository mMockArrowRepository = Mockito.spy(ArrowRepositoryImpl.class);

    static RouteBuilderInteractorImpl mInteractor;

    static Point point = new Point(50.4236835, 30.4266010);

    static Route mFieldBuildingRoute;
    static Route mComputedBuildingRoute;

    @Before
    public void setUp() {
        List<Point> points = new ArrayList<>();
        Point point2 = new Point(50.4236812, 30.4266095);
        Point point3 = new Point(50.4236714, 30.4266477);
        points.add(point);
        points.add(point2);
        points.add(point3);

        mFieldBuildingRoute = new Route(1, FIELD_ID, Route.Type.BASE, points);
        mComputedBuildingRoute = new Route(2, FIELD_ID, Route.Type.COMPUTED);

        mMockedCallback = Mockito.spy(new RouteBuilderInteractor.Callback() {
            @Override
            public void routeBuildingFinished(Route route) {
            }
        });

        mInteractor = new RouteBuilderInteractorImpl(
                mMockExecutor,
                mMockMainThread,
                mMockedCallback,
                mMockRouteRepository,
                FIELD_ID,
                Constants.WIDTH_METERS
        );
    }

    @Test
    public void run_withPointLocation_addPointToRoute() throws InterruptedException {
        when(mMockLocationRepository.getLastLocation()).thenReturn(point).thenReturn(null);
        when(mMockRouteRepository.create(FIELD_ID, Route.Type.BASE))
                .thenReturn(mFieldBuildingRoute);

        mInteractor.run();
        Thread.sleep(1000);
        mInteractor.finish();

        assertTrue(mFieldBuildingRoute.getRoutePoints().size() != 0);
        assertTrue(mFieldBuildingRoute.getRoutePoints().get(0) == point);
    }


    @Test
    public void whenRun_askForCreatingRouteModel() {
        mInteractor.run();

        Mockito.verify(mMockRouteRepository).create(anyInt(), any(Route.Type.class));
    }

    @Test
    public void whenFinish_addRouteToRepository() {
        when(mMockRouteRepository.create(FIELD_ID, Route.Type.BASE))
                .thenReturn(mFieldBuildingRoute);
        mInteractor.run();
        mInteractor.finish();

        Mockito.verify(mMockRouteRepository).update(any(Route.class));
    }

    @Test
    public void createComputedRoutes_askForFieldBuildingRoute() {
        mInteractor.createComputedRoutes(FIELD_ID, true);

        verify(mMockRouteRepository).getBaseRoute(FIELD_ID);
    }

    @Test
    public void createComputedRoutes_shouldAddRouteModelsToRepo() {
        when(mMockRouteRepository.getBaseRoute(anyInt())).thenReturn(mFieldBuildingRoute);
        when(mMockRouteRepository.create(anyInt(), any(Route.Type.class)))
                .thenReturn(mComputedBuildingRoute);

        mInteractor.createComputedRoutes(FIELD_ID, true);

        verify(mMockRouteRepository, times(5)).update(any(Route.class));
    }

    @Test
    public void createComputedRoutes_callCallback() {
        when(mMockRouteRepository.getBaseRoute(anyInt())).thenReturn(mFieldBuildingRoute);
        when(mMockRouteRepository.create(anyInt(), any(Route.Type.class)))
                .thenReturn(mComputedBuildingRoute);

        mInteractor.createComputedRoutes(FIELD_ID, true);

        verify(mMockedCallback, times(4)).routeBuildingFinished(any(Route.class));
    }
}