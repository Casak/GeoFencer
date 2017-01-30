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

import casak.ru.geofencer.domain.executor.Executor;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.domain.interactors.RouteBuilderInteractor;
import casak.ru.geofencer.domain.model.ArrowModel;
import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.model.RouteModel;
import casak.ru.geofencer.domain.repository.ArrowRepository;
import casak.ru.geofencer.domain.repository.LocationRepository;
import casak.ru.geofencer.domain.repository.RouteRepository;
import casak.ru.geofencer.storage.ArrowRepositoryImpl;
import casak.ru.geofencer.storage.LocationRepositoryImpl;
import casak.ru.geofencer.storage.RouteRepositoryImpl;

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
    static ArrowModel mLeftArrowModel;

    static RouteBuilderInteractor.Callback mMockedCallback;
    @Spy
    static LocationRepository mMockLocationRepository = Mockito.spy(LocationRepositoryImpl.class);
    @Spy
    static RouteRepository mMockRouteRepository = Mockito.spy(RouteRepositoryImpl.class);
    @Spy
    private ArrowRepository mMockArrowRepository = Mockito.spy(ArrowRepositoryImpl.class);


    static RouteBuilderInteractor mInteractor;

    static Point point = new Point(50.4236835, 30.4266010);

    static RouteModel mFieldBuildingRouteModel;


    @Before
    public void setUp() {
        List<Point> points = new ArrayList<>();
        Point point2 = new Point(50.4236812,30.4266095);
        Point point3 = new Point(50.4236714,30.4266477);
        points.add(point);
        points.add(point2);
        points.add(point3);

        mFieldBuildingRouteModel = new RouteModel(1, RouteModel.Type.FIELD_BUILDING, FIELD_ID, points);

        mMockedCallback = Mockito.spy(new RouteBuilderInteractor.Callback() {
            @Override
            public void routeBuildingFinished(RouteModel route) {
            }
        });

        mInteractor = new RouteBuilderInteractorImpl(
                mMockExecutor,
                mMockMainThread,
                mMockedCallback,
                mMockLocationRepository,
                mMockRouteRepository,
                mMockArrowRepository,
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

    @Test
    public void createComputedRoutes_askForFieldBuildingRoute() {
        when(mMockArrowRepository.getLeftArrow(FIELD_ID)).thenReturn(mLeftArrowModel);
        when(mLeftArrowModel.isChosen()).thenReturn(true);

        mInteractor.createComputedRoutes(FIELD_ID);

        verify(mMockRouteRepository).getRouteModel(FIELD_ID, RouteModel.Type.FIELD_BUILDING);
    }

    @Test
    public void createComputedRoutes_shouldAddRouteModelsToRepo() {
        when(mMockRouteRepository.getRouteModel(FIELD_ID, RouteModel.Type.FIELD_BUILDING))
                .thenReturn(mFieldBuildingRouteModel);
        when(mMockArrowRepository.getLeftArrow(FIELD_ID)).thenReturn(mLeftArrowModel);
        when(mLeftArrowModel.isChosen()).thenReturn(true);

        mInteractor.createComputedRoutes(FIELD_ID);

        verify(mMockRouteRepository, times(4)).addRouteModel(any(RouteModel.class));
    }

    @Test
    public void createComputedRoutes_askForLeftArrow() {
        when(mMockArrowRepository.getLeftArrow(FIELD_ID)).thenReturn(mLeftArrowModel);
        when(mLeftArrowModel.isChosen()).thenReturn(true);

        mInteractor.createComputedRoutes(FIELD_ID);

        verify(mMockArrowRepository).getLeftArrow(FIELD_ID);
    }

    @Test
    public void createComputedRoutes_callCallback(){

        when(mMockRouteRepository.getRouteModel(FIELD_ID, RouteModel.Type.FIELD_BUILDING))
                .thenReturn(mFieldBuildingRouteModel);
        when(mMockArrowRepository.getLeftArrow(FIELD_ID)).thenReturn(mLeftArrowModel);
        when(mLeftArrowModel.isChosen()).thenReturn(true);

        mInteractor.createComputedRoutes(FIELD_ID);

        verify(mMockedCallback, times(4)).routeBuildingFinished(any(RouteModel.class));
    }
}