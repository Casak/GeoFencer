package casak.ru.geofencer.domain.interactors.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import casak.ru.geofencer.domain.Constants;
import casak.ru.geofencer.domain.executor.Executor;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.domain.interactors.BuildFieldInteractor;
import casak.ru.geofencer.domain.model.Arrow;
import casak.ru.geofencer.domain.model.Field;
import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.model.Route;
import casak.ru.geofencer.domain.repository.ArrowRepository;
import casak.ru.geofencer.domain.repository.FieldRepository;
import casak.ru.geofencer.domain.repository.RouteRepository;
import casak.ru.geofencer.storage.ArrowRepositoryImpl;
import casak.ru.geofencer.storage.FieldRepositoryImpl;
import casak.ru.geofencer.storage.RouteRepositoryImpl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created on 09.01.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class BuildFieldInteractorImplTest {
    static final int FIELD_ID = 1;

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
            public void onFieldBuildFinish() {

            }

            @Override
            public void onFieldBuildFail() {

            }
        });

        mInteractor = new BuildFieldInteractorImpl(
                mMockExecutor,
                mMockMainThread,
                mMockedCallback,
                mMockRouteRepository,
                mMockArrowRepository,
                mMockFieldRepository,
                FIELD_ID,
                Constants.WIDTH_METERS
        );
    }

    @Test
    public void run_withChosenArrow_shouldAddFieldToRepo() {
        mLeftArrow.setChosen(true);
        mRightArrow.setChosen(false);

        mInteractor.run();

        verify(mMockFieldRepository).addField(any(Field.class));
    }

    @Test
    public void run_withChosenArrowsAndRouteModel_returnFieldModel() {
        mLeftArrow.setChosen(true);
        mRightArrow.setChosen(false);

        mInteractor.run();
        Field compareModel = mInteractor.getFieldModel();

        assertNotNull(compareModel);

        setUp();
        mLeftArrow.setChosen(false);
        mRightArrow.setChosen(true);

        mInteractor.run();

        assertNotNull(mInteractor.getFieldModel());
        assertFalse(mInteractor.getFieldModel().getPoints().equals(compareModel.getPoints()));
    }

    @Test
    public void run_withChosenArrow_callsFinishCallback() {
        mLeftArrow.setChosen(true);

        mInteractor.run();

        Mockito.verify(mMockedCallback).onFieldBuildFinish();
    }

    @Test
    public void run_withoutChosenArrow_callFailCallback() {
        mLeftArrow.setChosen(false);
        mRightArrow.setChosen(false);

        mInteractor.run();

        Mockito.verify(mMockedCallback).onFieldBuildFail();
    }

    @Test
    public void buildField_withoutPoints_callsFailCallback() {
        mInteractor.buildField(null, null, true);

        Mockito.verify(mMockedCallback).onFieldBuildFail();
    }
}