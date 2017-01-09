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
import casak.ru.geofencer.domain.interactors.BuildArrowsInteractor;
import casak.ru.geofencer.domain.model.ArrowModel;
import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.model.RouteModel;
import casak.ru.geofencer.domain.repository.RouteRepository;

/**
 * Created on 06.01.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class BuildArrowsInteractorImplTest {
    @Mock
    RouteModel mMockRoute;
    @Mock
    static Executor mMockExecutor;
    @Mock
    static MainThread mMockMainThread;
    @Mock
    static BuildArrowsInteractor.Callback mMockedCallback;
    @Mock
    static RouteRepository mMockRouteRepository;

    static BuildArrowsInteractor mInteractor;

    RouteModel mFieldBuildingRouteModel = new RouteModel(1, RouteModel.Type.FIELD_BUILDING);



    @Before
    public void setUpClass(){
        //MockitoAnnotations.initMocks(this);
        mMockedCallback = Mockito.spy(new BuildArrowsInteractor.Callback() {
            @Override
            public void onArrowsBuildFinished() {

            }

            @Override
            public void onArrowsBuildFailed() {
            }
        });
        mInteractor = new BuildArrowsInteractorImpl(
                mMockExecutor,
                mMockMainThread,
                mMockedCallback,
                mMockRouteRepository
        );

        List<Point> points = new ArrayList<>();
        Point point1 = new Point(50.4236835,30.4266010);
        Point point2 = new Point(50.4236812,30.4266095);
        Point point3 = new Point(50.4236714,30.4266477);
        points.add(point1);
        points.add(point2);
        points.add(point3);

        mFieldBuildingRouteModel.setRoutePoints(points);
    }

    @Test
    public void run_from3PointsRoute_FinishedCallbackIsCalled(){
        when(mMockRouteRepository.getRoute(RouteModel.Type.FIELD_BUILDING)).thenReturn(mFieldBuildingRouteModel);

        ((BuildArrowsInteractorImpl)mInteractor).run();

        Mockito.verify(mMockedCallback).onArrowsBuildFinished();
    }

    @Test
    public void run_from3PointsRoute_2ArrowsCreated(){
        when(mMockRouteRepository.getRoute(RouteModel.Type.FIELD_BUILDING)).thenReturn(mFieldBuildingRouteModel);

        ((BuildArrowsInteractorImpl)mInteractor).run();

        assertNotNull(mInteractor.getLeftArrow());
        assertNotNull(mInteractor.getRightArrow());
    }

    @Test
    public void createArrow_fromNullRoute_NullReturned(){
        when(mMockRouteRepository.getRoute(RouteModel.Type.FIELD_BUILDING)).thenReturn(mMockRoute);
        when(mMockRoute.getRoutePoints()).thenReturn(null);

        ArrowModel result = ((BuildArrowsInteractorImpl)mInteractor).createArrow(mMockRoute, true);

        assertNull(result);
    }

    @Test
    public void createArrow_from3PointsRoute_ModelReturned(){
        when(mMockRouteRepository.getRoute(RouteModel.Type.FIELD_BUILDING)).thenReturn(mFieldBuildingRouteModel);

        ArrowModel result = ((BuildArrowsInteractorImpl)mInteractor).createArrow(mFieldBuildingRouteModel, true);

        assertNotNull(result);
    }

    @Test
    public void createArrow_from3PointsRoute_LeftModelReturned(){
        when(mMockRouteRepository.getRoute(RouteModel.Type.FIELD_BUILDING)).thenReturn(mFieldBuildingRouteModel);

        ArrowModel result = ((BuildArrowsInteractorImpl)mInteractor).createArrow(mFieldBuildingRouteModel, true);

        assertTrue(result.getType() == ArrowModel.Type.LEFT);
    }

    @Test
    public void createArrow_fromNullRoute_FailCallbackIsCalled(){
        when(mMockRouteRepository.getRoute(RouteModel.Type.FIELD_BUILDING)).thenReturn(mMockRoute);
        when(mMockRoute.getRoutePoints()).thenReturn(null);

        ((BuildArrowsInteractorImpl)mInteractor).createArrow(mMockRoute, true);

        Mockito.verify(mMockedCallback).onArrowsBuildFailed();
    }

}