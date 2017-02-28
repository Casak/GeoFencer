package casak.ru.geofencer.presentation.presenters.impl;

import javax.inject.Inject;

import casak.ru.geofencer.bluetooth.AntennaDataProvider;
import casak.ru.geofencer.domain.executor.Executor;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.domain.interactors.PointerInteractor;
import casak.ru.geofencer.domain.repository.FieldRepository;
import casak.ru.geofencer.presentation.presenters.MapPointerPresenter;
import casak.ru.geofencer.presentation.presenters.base.AbstractPresenter;
import casak.ru.geofencer.presentation.ui.fragment.MapPointerFragment;

/**
 * Created on 21.02.2017.
 */

public class MapPointerPresenterImpl extends AbstractPresenter implements MapPointerPresenter {
    private static final String TAG = MapPointerPresenterImpl.class.getSimpleName();

    private PointerInteractor mInteractor;
    private AntennaDataProvider mAntennaDataProvider;
    private FieldRepository mFieldRepository;
    private MapPointerPresenter.View mView;

    @Inject
    public MapPointerPresenterImpl(Executor executor, MainThread mainThread,
                                   PointerInteractor interactor, AntennaDataProvider provider,
                                   FieldRepository fieldRepository) {
        super(executor, mainThread);
        mInteractor = interactor;
        mAntennaDataProvider = provider;
        mFieldRepository = fieldRepository;
    }

    //TODO Improve logic
    @Override
    public void showPointer(double value) {
        mView.turnOff(View.ALL_SEMAPHORES, View.Type.ALL);
        if (Math.abs(value) > 1.8D) {
            if (value > 0) {
                mView.turnOn(View.ALL_SEMAPHORES, View.Type.RIGHT);
            } else {
                mView.turnOn(View.ALL_SEMAPHORES, View.Type.LEFT);
            }
        } else if (Math.abs(value) > 1.2D) {
            if (value > 0) {
                mView.turnOn(View.TO_RED_CLOSE, View.Type.RIGHT);
            } else {
                mView.turnOn(View.TO_RED_CLOSE, View.Type.LEFT);
            }
        } else if (Math.abs(value) > 0.8D) {
            if (value > 0) {
                mView.turnOn(View.TO_YELLOW_FAR, View.Type.RIGHT);
            } else {
                mView.turnOn(View.TO_YELLOW_FAR, View.Type.LEFT);
            }
        } else if (Math.abs(value) > 0.4D) {
            if (value > 0) {
                mView.turnOn(View.TO_YELLOW_CLOSE, View.Type.RIGHT);
            } else {
                mView.turnOn(View.TO_YELLOW_CLOSE, View.Type.LEFT);
            }
        } else if (Math.abs(value) > 0.2D) {
            if (value > 0) {
                mView.turnOn(View.TO_GREEN_FAR, View.Type.RIGHT);
            } else {
                mView.turnOn(View.TO_GREEN_FAR, View.Type.LEFT);
            }
        } else if (Math.abs(value) > 0.1D) {
            if (value > 0) {
                mView.turnOn(View.TO_GREEN_CLOSE, View.Type.RIGHT);
            } else {
                mView.turnOn(View.TO_GREEN_CLOSE, View.Type.LEFT);
            }
        }
    }

    @Override
    public void resume() {
        if (mView == null) {
            mView = MapPointerFragment.getPointerComponent().getPointerView();
        }

        //TODO Fetch fieldId
        mInteractor.init(this, 6);
        mAntennaDataProvider.registerObserver(mInteractor);
        mInteractor.execute();
    }


    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void onError(String message) {

    }
}
