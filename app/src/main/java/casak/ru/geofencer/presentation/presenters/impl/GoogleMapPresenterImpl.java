package casak.ru.geofencer.presentation.presenters.impl;

import android.util.Log;

import javax.inject.Inject;

import casak.ru.geofencer.domain.executor.Executor;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.presentation.presenters.GoogleMapPresenter;

/**
 * Created on 09.02.2017.
 */

public class GoogleMapPresenterImpl implements GoogleMapPresenter{
    private static final String TAG = GoogleMapPresenterImpl.class.getSimpleName();

    GoogleMapPresenter.View mapView;

    @Inject
    Executor threadExecutor;
    @Inject
    MainThread mainThread;

    @Inject
    public GoogleMapPresenterImpl(GoogleMapPresenter.View view){
        mapView = view;
    }


    public void injected(){
        Log.d(TAG, "INJECTED!!");
    }


    @Override
    public void resume() {

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
