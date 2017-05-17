package com.smartagrodriver.core.presentation.presenters.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import com.smartagrodriver.core.presentation.presenters.MapPresenter;
import com.smartagrodriver.core.presentation.presenters.MapSliderPresenter;
import com.smartagrodriver.core.presentation.presenters.base.AbstractPresenter;
import com.smartagrodriver.core.threading.Executor;
import com.smartagrodriver.core.threading.MainThread;

import javax.inject.Inject;

/**
 * Created on 21.02.2017.
 */

public class MapSliderPresenterImpl extends AbstractPresenter implements MapSliderPresenter {
    private static final String TAG = MapSliderPresenterImpl.class.getSimpleName();

    private MapPresenter mMapPresenter;
    private View mLeftSlider;
    private View mRightSlider;
    private Resources mResources;
    private Context mContext;
    private SharedPreferences mPreferences;

    @Inject
    public MapSliderPresenterImpl(Executor executor, MainThread mainThread,
                                  SharedPreferences preferences, Context context) {
        super(executor, mainThread);
        mContext = context;
        mPreferences = preferences;
        mResources = mContext.getResources();
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
