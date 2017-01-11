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
import casak.ru.geofencer.domain.model.FieldModel;
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

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

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
    static ArrowModel mMockArrowModel;

    static CreateFieldInteractorImpl mInteractor;

    static RouteModel mRouteModel = new RouteModel(1, RouteModel.Type.FIELD_BUILDING, FIELD_ID);


    @Before
    public void setUp() throws Exception {
        mMockedCreateFieldCallback = Mockito.spy(new CreateFieldInteractor.Callback() {
            @Override
            public void showArrow(ArrowModel model) {

            }

            @Override
            public void hideArrow(ArrowModel model) {

            }

            @Override
            public void showField(FieldModel model) {

            }
        });

        mMockedRouteBuilderCallback = Mockito.spy(RouteBuilderInteractor.Callback.class);

        mInteractor = Mockito.spy(new CreateFieldInteractorImpl(
                mMockExecutor,
                mMockMainThread,
                mMockedCreateFieldCallback,
                mMockRouteRepository,
                mMockArrowRepository,
                mMockFieldRepository,
                mMockLocationRepository
        ));
    }


    @Test
    public void run_shouldGetRouteModel(){
        mInteractor.run();
        verify(mMockRouteRepository).getRouteModel(anyInt(), any(RouteModel.Type.class));
    }

    @Test
    public void onArrowsBuildFinished_shouldAskToShowArrows(){
        mInteractor.onArrowsBuildFinished(FIELD_ID);

        verify(mMockedCreateFieldCallback, times(2)).showArrow(any(ArrowModel.class));
    }

    @Test
    public void onArrowClick_shouldSetChosen(){
        mInteractor.onArrowClick(mMockArrowModel);

        verify(mMockArrowModel).setChosen(true);
    }
}