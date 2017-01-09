package casak.ru.geofencer.domain.interactors.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import casak.ru.geofencer.domain.executor.Executor;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.domain.interactors.CreateFieldInteractor;
import casak.ru.geofencer.domain.interactors.RouteBuilderInteractor;
import casak.ru.geofencer.domain.model.ArrowModel;
import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.model.RouteModel;
import casak.ru.geofencer.domain.repository.ArrowRepository;
import casak.ru.geofencer.domain.repository.FieldRepository;
import casak.ru.geofencer.domain.repository.LocationRepository;
import casak.ru.geofencer.domain.repository.RouteRepository;
import casak.ru.geofencer.domain.repository.impl.ArrowRepositoryImpl;
import casak.ru.geofencer.domain.repository.impl.FieldRepositoryImpl;
import casak.ru.geofencer.domain.repository.impl.LocationRepositoryImpl;
import casak.ru.geofencer.domain.repository.impl.RouteRepositoryImpl;

import static org.junit.Assert.*;

/**
 * Created on 10.01.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class CreateFieldInteractorImplTest {

    @Mock
    static Executor mMockExecutor;
    @Mock
    static MainThread mMockMainThread;
    @Mock
    static CreateFieldInteractor.Callback mMockedCreateFieldCallback;
    @Mock
    static RouteBuilderInteractor.Callback mMockedRouteBuilderCallback;
    @Mock
    static RouteRepository mMockRouteRepository = Mockito.spy(RouteRepositoryImpl.class);
    @Mock
    static ArrowRepository mMockArrowRepository = Mockito.spy(ArrowRepositoryImpl.class);
    @Mock
    static FieldRepository mMockFieldRepository = Mockito.spy(FieldRepositoryImpl.class);
    @Mock
    static LocationRepository mMockLocationRepository = Mockito.spy(LocationRepositoryImpl.class);
    @Mock
    static RouteBuilderInteractor mRouteBuilderInteractor;

    static CreateFieldInteractorImpl mInteractor;



    @Before
    public void setUp() throws Exception {
        mMockedCreateFieldCallback = Mockito.spy(new CreateFieldInteractor.Callback() {
            @Override
            public void removeArrow(ArrowModel model) {

            }
        });

        mMockedRouteBuilderCallback = Mockito.spy(new RouteBuilderInteractor.Callback() {
            @Override
            public void positionUpdate(Point point) {

            }
        });

       mRouteBuilderInteractor = Mockito.spy(new RouteBuilderInteractorImpl(
                mMockExecutor,
                mMockMainThread,
                mMockedRouteBuilderCallback,
                mMockLocationRepository,
                mMockRouteRepository
        ));

        mInteractor = new CreateFieldInteractorImpl(
                mMockExecutor,
                mMockMainThread,
                mMockedCreateFieldCallback,
                mMockRouteRepository,
                mMockArrowRepository,
                mMockFieldRepository,
                mRouteBuilderInteractor
        );
    }

    @Test
    public void whenStarts_shouldRunRouteBuilderInteractor_callsExecuteMethod(){
        mInteractor.run();

        Mockito.verify(mRouteBuilderInteractor).execute();
    }

    @Test
    public void whenStartBuildingRoute_shouldStartBuildRoute(){
        mInteractor.run();
        mInteractor.onStartCreatingRouteClick();

        Mockito.verify(mRouteBuilderInteractor).startBuildRoute(RouteModel.Type.FIELD_BUILDING);
    }

}