package com.smartagrodriver.core.domain.interactors.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import com.smartagrodriver.core.threading.Executor;
import com.smartagrodriver.core.threading.MainThread;
import com.smartagrodriver.core.domain.interactors.BuildFieldInteractor;
import com.smartagrodriver.core.domain.model.Arrow;
import com.smartagrodriver.core.domain.model.Field;
import com.smartagrodriver.core.domain.model.Point;
import com.smartagrodriver.core.domain.model.Route;
import com.smartagrodriver.core.domain.repository.ArrowRepository;
import com.smartagrodriver.core.domain.repository.FieldRepository;
import com.smartagrodriver.core.domain.repository.RouteRepository;
import com.smartagrodriver.core.storage.ArrowRepositoryImpl;
import com.smartagrodriver.core.storage.FieldRepositoryImpl;
import com.smartagrodriver.core.storage.RouteRepositoryImpl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created on 09.01.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class BuildFieldInteractorImplTest {
    static final int FIELD_ID = 1;
    static final int WIDTH_METERS = 10;

    @Mock
    static Executor mMockExecutor;
    @Mock
    static MainThread mMockMainThread;
    @Mock
    static BuildFieldInteractor.Callback mMockedCallback;
    @Mock
    static ArrowRepository mMockArrowRepository = Mockito.spy(ArrowRepositoryImpl.class);
    @Mock
    static RouteRepository mMockRouteRepository = Mockito.spy(RouteRepositoryImpl.class);
    @Mock
    static FieldRepository mMockFieldRepository = Mockito.spy(FieldRepositoryImpl.class);

    static BuildFieldInteractorImpl mInteractor;

    static Route mFieldBuildingRoute = new Route(1, FIELD_ID, Route.Type.BASE);
    static Arrow mLeftArrow;
    static Arrow mRightArrow;
    static Field mField;

    @Before
    public void setUp() {
        List<Point> points = new ArrayList<>();
        Point point1 = new Point(50.4236835, 30.4266010);
        Point point2 = new Point(50.4236812, 30.4266095);
        Point point3 = new Point(50.4236714, 30.4266477);
        points.add(point1);
        points.add(point2);
        points.add(point3);

        mLeftArrow = new Arrow(points, Arrow.Type.LEFT);
        mRightArrow = new Arrow(points, Arrow.Type.RIGHT);

        when(mMockRouteRepository.getBaseRoute(FIELD_ID))
                .thenReturn(mFieldBuildingRoute);
        when(mMockArrowRepository.getLeft(anyInt()))
                .thenReturn(mLeftArrow);
        when(mMockArrowRepository.getRight(anyInt()))
                .thenReturn(mRightArrow);

        mFieldBuildingRoute.setRoutePoints(points);

        mMockedCallback = Mockito.spy(new BuildFieldInteractor.Callback() {
            @Override
            public void onFieldBuildFinish(Field field) {

            }

            @Override
            public void onFieldBuildFail(Field field) {

            }
        });

        mInteractor = new BuildFieldInteractorImpl(
                mMockExecutor,
                mMockMainThread,
                mMockedCallback,
                mMockRouteRepository,
                mMockFieldRepository,
                WIDTH_METERS
        );

        mField = new Field(FIELD_ID, points);

        mInteractor.init(mField, mLeftArrow);
    }

    @Test
    public void run_withChosenArrow_shouldAddArrowsToRepo() {
        mLeftArrow.setChosen(true);
        mRightArrow.setChosen(false);

        mInteractor.run();

        verify(mMockFieldRepository).updateField(any(Field.class));
    }

    @Test
    public void run_withChosenArrowsAndRouteModel_returnFieldModelWithComputedCorners() {
        mInteractor.run();

        assertEquals(4, mField.getPoints().size());
    }

    @Test
    public void run_withChosenArrow_callsFinishCallback() {
        mLeftArrow.setChosen(true);

        mInteractor.run();

        Mockito.verify(mMockedCallback).onFieldBuildFinish(any(Field.class));
    }

    @Test(expected = NullPointerException.class)
    public void buildField_withoutPoints_callsFailCallback() {
        mFieldBuildingRoute.setRoutePoints(new ArrayList<Point>());

        mInteractor.run();
    }
}