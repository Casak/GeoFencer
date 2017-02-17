package casak.ru.geofencer.domain.interactors.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import casak.ru.geofencer.domain.executor.Executor;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.domain.interactors.BuildArrowModelsInteractor;
import casak.ru.geofencer.domain.model.Arrow;
import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.model.Route;
import casak.ru.geofencer.domain.repository.ArrowRepository;
import casak.ru.geofencer.domain.repository.RouteRepository;

/**
 * Created on 06.01.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class BuildArrowModelsInteractorImplTest {
    static final int FIELD_ID = 1;

    @Mock
    Route mMockRoute;
    @Mock
    static Executor mMockExecutor;
    @Mock
    static MainThread mMockMainThread;
    @Mock
    static BuildArrowModelsInteractor.Callback mMockedCallback;
    @Mock
    static RouteRepository mMockRouteRepository;
    @Mock
    static ArrowRepository mMockArrowRepository;

    static BuildArrowModelsInteractorImpl mInteractor;

    Route mFieldBuildingRoute = new Route(1, FIELD_ID, Route.Type.BASE);


    @Before
    public void setUp() {
        List<Point> points = new ArrayList<>();
        Point point1 = new Point(50.4236835, 30.4266010);
        Point point2 = new Point(50.4236812, 30.4266095);
        Point point3 = new Point(50.4236714, 30.4266477);
        points.add(point1);
        points.add(point2);
        points.add(point3);

        mFieldBuildingRoute.setRoutePoints(points);

        mMockedCallback = Mockito.spy(new BuildArrowModelsInteractor.Callback() {
            @Override
            public void onArrowsBuildFinished(long fieldId) {

            }

            @Override
            public void onArrowsBuildFailed(long fieldId) {

            }
        });

        mInteractor = new BuildArrowModelsInteractorImpl(
                mMockExecutor,
                mMockMainThread,
                mMockedCallback,
                mMockRouteRepository,
                mMockArrowRepository,
                FIELD_ID
        );
    }

    @Test
    public void run_from3PointsRoute_finishedCallbackIsCalled() {
        when(mMockRouteRepository.getBaseRoute(FIELD_ID))
                .thenReturn(mFieldBuildingRoute);

        mInteractor.run();

        verify(mMockedCallback).onArrowsBuildFinished(mFieldBuildingRoute.getFieldId());
    }

    @Test
    public void run_from3PointsRoute_2ArrowsCreated() {
        when(mMockRouteRepository.getBaseRoute(FIELD_ID))
                .thenReturn(mFieldBuildingRoute);

        mInteractor.run();

        assertNotNull(mInteractor.getLeftArrow());
        assertNotNull(mInteractor.getRightArrow());
    }

    @Test
    public void createArrow_fromNullRoute_nullReturned() {
        when(mMockRoute.getRoutePoints()).thenReturn(null);

        Arrow result = mInteractor.createArrow(mMockRoute, true);

        assertNull(result);
    }

    @Test
    public void createArrow_from3PointsRoute_modelReturned() {
        Arrow result = mInteractor.createArrow(mFieldBuildingRoute, true);

        assertNotNull(result);
    }

    @Test
    public void createArrow_from3PointsRoute_leftModelReturned() {
        Arrow result = mInteractor.createArrow(mFieldBuildingRoute, true);

        assertTrue(result.getType() == Arrow.Type.LEFT);
    }

    @Test
    public void createArrow_fromNullRoute_failCallbackIsCalled() {
        when(mMockRouteRepository.getBaseRoute(mMockRoute.getFieldId()))
                .thenReturn(mMockRoute);
        when(mMockRoute.getRoutePoints()).thenReturn(null);

        mInteractor.run();

        verify(mMockedCallback).onArrowsBuildFailed(mFieldBuildingRoute.getFieldId());
    }
}