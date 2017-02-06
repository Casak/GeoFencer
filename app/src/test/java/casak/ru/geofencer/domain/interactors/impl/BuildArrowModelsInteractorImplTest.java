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
import casak.ru.geofencer.domain.model.ArrowModel;
import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.model.RouteModel;
import casak.ru.geofencer.domain.repository.ArrowRepository;
import casak.ru.geofencer.domain.repository.RouteRepository;

/**
 * Created on 06.01.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class BuildArrowModelsInteractorImplTest {
    static final int FIELD_ID = 1;

    @Mock
    RouteModel mMockRoute;
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

    static BuildArrowModelsInteractor mInteractor;

    RouteModel mFieldBuildingRouteModel = new RouteModel(1, FIELD_ID, RouteModel.Type.BASE);



    @Before
    public void setUp(){
        List<Point> points = new ArrayList<>();
        Point point1 = new Point(50.4236835,30.4266010);
        Point point2 = new Point(50.4236812,30.4266095);
        Point point3 = new Point(50.4236714,30.4266477);
        points.add(point1);
        points.add(point2);
        points.add(point3);

        mFieldBuildingRouteModel.setRoutePoints(points);

        mMockedCallback = Mockito.spy(new BuildArrowModelsInteractor.Callback() {
            @Override
            public void onArrowsBuildFinished(int fieldId) {

            }

            @Override
            public void onArrowsBuildFailed(int fieldId) {

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
    public void run_from3PointsRoute_finishedCallbackIsCalled(){
        when(mMockRouteRepository.getBaseRoute(FIELD_ID))
                .thenReturn(mFieldBuildingRouteModel);

        ((BuildArrowModelsInteractorImpl)mInteractor).run();

        verify(mMockedCallback).onArrowsBuildFinished(mFieldBuildingRouteModel.getFieldId());
    }

    @Test
    public void run_from3PointsRoute_2ArrowsCreated(){
        when(mMockRouteRepository.getBaseRoute(FIELD_ID))
                .thenReturn(mFieldBuildingRouteModel);

        ((BuildArrowModelsInteractorImpl)mInteractor).run();

        assertNotNull(mInteractor.getLeftArrow());
        assertNotNull(mInteractor.getRightArrow());
    }

    @Test
    public void createArrow_fromNullRoute_nullReturned(){
        when(mMockRoute.getRoutePoints()).thenReturn(null);

        ArrowModel result = ((BuildArrowModelsInteractorImpl)mInteractor).createArrow(mMockRoute, true);

        assertNull(result);
    }

    @Test
    public void createArrow_from3PointsRoute_modelReturned(){
        ArrowModel result = ((BuildArrowModelsInteractorImpl)mInteractor)
                .createArrow(mFieldBuildingRouteModel, true);

        assertNotNull(result);
    }

    @Test
    public void createArrow_from3PointsRoute_leftModelReturned(){
        ArrowModel result = ((BuildArrowModelsInteractorImpl)mInteractor)
                .createArrow(mFieldBuildingRouteModel, true);

        assertTrue(result.getType() == ArrowModel.Type.LEFT);
    }

    @Test
    public void createArrow_fromNullRoute_failCallbackIsCalled(){
        when(mMockRouteRepository.getBaseRoute(mMockRoute.getFieldId()))
                .thenReturn(mMockRoute);
        when(mMockRoute.getRoutePoints()).thenReturn(null);

        ((BuildArrowModelsInteractorImpl)mInteractor).run();

        verify(mMockedCallback).onArrowsBuildFailed(mFieldBuildingRouteModel.getFieldId());


    }

}