package casak.ru.geofencer.domain.interactors.impl;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import casak.ru.geofencer.threading.Executor;
import casak.ru.geofencer.threading.MainThread;
import casak.ru.geofencer.domain.interactors.CreateFieldInteractor;
import casak.ru.geofencer.domain.interactors.RouteBuilderInteractor;
import casak.ru.geofencer.domain.model.Arrow;
import casak.ru.geofencer.domain.model.Field;
import casak.ru.geofencer.domain.model.Route;
import casak.ru.geofencer.domain.repository.ArrowRepository;
import casak.ru.geofencer.domain.repository.FieldRepository;
import casak.ru.geofencer.domain.repository.LocationRepository;
import casak.ru.geofencer.domain.repository.RouteRepository;
import casak.ru.geofencer.storage.ArrowRepositoryImpl;
import casak.ru.geofencer.storage.FieldRepositoryImpl;
import casak.ru.geofencer.storage.LocationRepositoryImpl;
import casak.ru.geofencer.storage.RouteRepositoryImpl;

import static org.mockito.Mockito.*;

/**
 * Created on 10.01.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class CreateFieldInteractorImplTest {
    static final int FIELD_ID = 1;

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
    static Arrow mMockArrow;
    @Mock
    static Field mMockField;

    static Field mField;

    static CreateFieldInteractorImpl mInteractor;

    static Route mComputedRoute = new Route(0, FIELD_ID, Route.Type.COMPUTED);


    @Before
    public void setUp() throws Exception {
        mField = new Field(FIELD_ID);

        mMockedCreateFieldCallback = Mockito.spy(new CreateFieldInteractor.Callback() {
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

        mMockedRouteBuilderCallback = Mockito.spy(RouteBuilderInteractor.Callback.class);

        mInteractor = Mockito.spy(new CreateFieldInteractorImpl(
                mMockExecutor,
                mMockMainThread,
                mMockRouteRepository,
                mMockArrowRepository,
                mMockFieldRepository
        ));

        when(mMockFieldRepository.createField()).thenReturn(mField);
        mInteractor.init(mMockedCreateFieldCallback, 20);
    }


    @Test(expected = NullPointerException.class)
    public void run_withoutInit_throwsException() {
        mInteractor.init(null, 0);

        mInteractor.run();
    }

    @Ignore
    @Test
    public void onArrowsBuildFinished_shouldAskToShowArrows() {
        //mInteractor.onArrowsBuildFinished(FIELD_ID);

        verify(mMockedCreateFieldCallback, times(2)).showArrow(any(Arrow.class));
    }

    @Test
    public void onArrowClick_shouldSetChosenAtArrowInteractor() {
        mInteractor.onArrowClick(mMockArrow);

        verify(mMockArrow).setChosen(true);
    }

    @Test
    public void routeBuildingFinished_withComputedRouteModel_shouldAddRoutesToField() {
        when(mMockFieldRepository.createField()).thenReturn(mMockField);
        mInteractor.init(mMockedCreateFieldCallback, 20);

        mInteractor.routeBuildingFinished(mComputedRoute);

        verify(mMockField).addRoute(any(Route.class));
    }

    @Test
    public void routeBuildingFinished_withComputedRouteModel_shouldUpdateFieldInRepo() {
        when(mMockFieldRepository.getField(anyInt())).thenReturn(mMockField);

        mInteractor.routeBuildingFinished(mComputedRoute);

        verify(mMockFieldRepository).updateField(any(Field.class));
    }
}