package com.smartagrodriver.core.presentation.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.smartagrodriver.core.AndroidApplication;
import com.smartagrodriver.core.R;
import com.smartagrodriver.core.di.components.DaggerMapSliderControlComponent;
import com.smartagrodriver.core.di.components.MapSliderControlComponent;
import com.smartagrodriver.core.di.modules.MapSliderControlModule;
import com.smartagrodriver.core.presentation.presenters.MapSliderPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 15.02.2017.
 */

public class MapSliderControlFragment extends Fragment {

    private static final int STATE_CLOSE = 0;
    private static final int STATE_OPEN_PART = 1;
    private static final int STATE_OPEN_FULL = 2;

    private static MapSliderControlComponent mComponent;
    private static MapSliderControlModule mModule;

    @BindView(R.id.button_close_left)
    ImageButton mButtonCloseLeft;
    @BindView(R.id.button_open_left)
    ImageButton mButtonOpenLeft;
    @BindView(R.id.button_close_right)
    ImageButton mButtonCloseRight;
    @BindView(R.id.button_open_right)
    ImageButton mButtonOpenRight;


    View mRootView;
    MapSliderPresenter.View mLeftSliderView;
    MapSliderPresenter.View mRightSliderView;

    int mCurrentLeftSliderState;
    int mCurrentRightSliderState;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mModule == null) {
            mModule = new MapSliderControlModule(this);
        }

        if (mComponent == null) {
            mComponent = DaggerMapSliderControlComponent.builder()
                    .appComponent(AndroidApplication.getComponent())
                    .mapSliderControlModule(mModule)
                    .build();
        }

        getSliderComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_map_slider_control, container, false);

        ButterKnife.bind(this, mRootView);

        mLeftSliderView = MapSliderLeftFragment.getSliderLeftComponent().getView();
        mRightSliderView = MapSliderRightFragment.getSliderRightComponent().getView();

        mCurrentLeftSliderState = STATE_OPEN_PART;
        mCurrentRightSliderState = STATE_OPEN_PART;

        return mRootView;
    }


    @OnClick(R.id.button_open_left)
    public void openLeftSlider() {
        switch (mCurrentLeftSliderState) {
            case STATE_CLOSE:
                mLeftSliderView.openPartially();
                break;
            case STATE_OPEN_PART:
                mLeftSliderView.openFully();
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.button_open_right)
    public void openRightSlider() {
        switch (mCurrentRightSliderState) {
            case STATE_CLOSE:
                mRightSliderView.openPartially();
                break;
            case STATE_OPEN_PART:
                mRightSliderView.openFully();
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.button_close_left)
    public void closeLeftSlider() {
        switch (mCurrentRightSliderState) {
            case STATE_OPEN_FULL:
                mLeftSliderView.openPartially();
                break;
            case STATE_OPEN_PART:
                mLeftSliderView.close();
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.button_close_right)
    public void closeRightSlider() {
        switch (mCurrentRightSliderState) {
            case STATE_OPEN_FULL:
                mRightSliderView.openPartially();
                break;
            case STATE_OPEN_PART:
                mRightSliderView.close();
                break;
            default:
                break;
        }
    }

    public static MapSliderControlModule getSliderModule() {
        return mModule;
    }

    public static MapSliderControlComponent getSliderComponent() {
        return mComponent;
    }
}