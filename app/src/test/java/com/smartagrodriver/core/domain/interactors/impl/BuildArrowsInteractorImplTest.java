package com.smartagrodriver.core.domain.interactors.impl;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.smartagrodriver.core.threading.Executor;
import com.smartagrodriver.core.threading.MainThread;
import com.smartagrodriver.core.domain.interactors.CreateFieldInteractor;
import com.smartagrodriver.core.domain.model.Arrow;
import com.smartagrodriver.core.domain.model.Field;
import com.smartagrodriver.core.domain.model.Point;
import com.smartagrodriver.core.domain.model.Route;
import com.smartagrodriver.core.domain.repository.ArrowRepository;
import com.smartagrodriver.core.domain.repository.RouteRepository;

/**
 * Created on 06.01.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class BuildArrowsInteractorImplTest {
    static final int FIELD_ID = 1;

    @Mock
    Route mMockRoute;
    @Mock
    static Executor mMockExecutor;
    @Mock
    static MainThread mMockMainThread;
    @Mock
    static CreateFieldInteractor.Callback mMockedCallback;
    @Mock
    static RouteRepository mMockRouteRepository;
    @Mock
    static ArrowRepository mMockArrowRepository;

    static BuildArrowsInteractorImpl mInteractor;

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

        mMockedCallback = Mockito.spy(new CreateFieldInteractor.Callback() {
            @Override
            public void showArrow(Arrow model) {
            }

            @Override
            public void hideArrow(Arrow model) {
            }

            @Override
            public void showField(Field model) {
            }

            @Override
            public void hideField(Field model) {
            }

            @Override
            public void showRoute(Route model) {
            }

            @Override
            public void hideRoute(Route model) {
            }
        });

        mInteractor = new BuildArrowsInteractorImpl(
                mMockExecutor,
                mMockMainThread,
                mMockedCallback,
                mMockRouteRepository,
                mMockArrowRepository
        );

        mInteractor.init(mFieldBuildingRoute, FIELD_ID);
    }

    @Ignore
    @Test
    public void run_from3PointsRoute_finishedCallbackIsCalled() {
        when(mMockRouteRepository.getBaseRoute(FIELD_ID))
                .thenReturn(mFieldBuildingRoute);

        mInteractor.run();

        //verify(mMockedCallback).onArrowsBuildFinished(mFieldBuildingRoute.getFieldId());
    }

    @Test
    public void run_from3PointsRoute_2ArrowsCreated() {
        when(mMockRouteRepository.getBaseRoute(FIELD_ID))
                .thenReturn(mFieldBuildingRoute);

        mInteractor.run();

        verify(mMockArrowRepository, times(2)).add(any(Arrow.class), anyInt());
    }

    @Test
    public void createArrow_fromNullRoute_nullReturned() {
        when(mMockRoute.getRoutePoints()).thenReturn(null);
        mInteractor.init(mMockRoute, FIELD_ID);

        Arrow result = mInteractor.createArrow(true);

        assertNull(result);
    }

    @Test
    public void createArrow_from3PointsRoute_modelReturned() {
        mInteractor.init(mFieldBuildingRoute, FIELD_ID);

        Arrow result = mInteractor.createArrow(true);

        assertNotNull(result);
    }

    @Test
    public void createArrow_from3PointsRoute_leftModelReturned() {
        mInteractor.init(mFieldBuildingRoute, FIELD_ID);

        Arrow result = mInteractor.createArrow(true);

        assertTrue(result.getType() == Arrow.Type.LEFT);
    }

    @Ignore
    @Test
    public void createArrow_fromNullRoute_failCallbackIsCalled() {
        when(mMockRouteRepository.getBaseRoute(mMockRoute.getFieldId()))
                .thenReturn(mMockRoute);
        when(mMockRoute.getRoutePoints()).thenReturn(null);

        mInteractor.run();

        //verify(mMockedCallback).onArrowsBuildFailed(mFieldBuildingRoute.getFieldId());
    }

    @Test(expected = NullPointerException.class)
    public void onArrowClick_fromNull_throwsException() {
        mInteractor.onArrowClick(null);
    }

    @Test
    public void onArrowClick_fromNormalModel_shouldSetModelBeChosen() {
        Arrow model = new Arrow(new ArrayList<Point>(), Arrow.Type.LEFT);

        mInteractor.onArrowClick(model);

        assertTrue(model.isChosen());
    }

    @Test
    public void onArrowClick_fromNormalModel_shouldDeleteFromRepo() {
        Arrow model = new Arrow(new ArrayList<Point>(), Arrow.Type.LEFT);

        mInteractor.onArrowClick(model);

        verify(mMockArrowRepository).delete(anyInt());
    }
}