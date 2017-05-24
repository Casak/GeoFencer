package com.smartagrodriver.core.presentation.ui.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.smartagrodriver.core.AndroidApplication;
import com.smartagrodriver.core.R;
import com.smartagrodriver.core.di.components.DaggerMapSliderRightComponent;
import com.smartagrodriver.core.di.components.MapSliderRightComponent;
import com.smartagrodriver.core.di.modules.MapSliderRightModule;
import com.smartagrodriver.core.presentation.presenters.MapPresenter;
import com.smartagrodriver.core.presentation.presenters.MapSliderPresenter;

/**
 * Created on 15.02.2017.
 */

public class MapSliderRightFragment extends Fragment implements MapSliderPresenter.View {

    private static final int MAP_3D = 90;
    private static final int MAP_2D = 0;

    private static MapSliderRightComponent mComponent;
    private static MapSliderRightModule mModule;

    @Inject
    MapPresenter mMapPresenter;
    @Inject
    Context mContext;
    @BindView(R.id.textview_harvested)
    TextView mTextViewHarvested;
    @BindView(R.id.textview_harvested_sign)
    TextView mTextViewHarvestedSign;
    @BindView(R.id.textview_speed)
    TextView mTextViewSpeed;
    @BindView(R.id.textview_speed_sign)
    TextView mTextViewSpeedSign;
    @BindView(R.id.seekbar_zoom)
    SeekBar mSeekBarZoom;
    @BindView(R.id.button_2d3d)
    ImageButton mButton2d3d;

    View mRootView;
    Point mScreenSize;
    int mBaseSliderSize;
    float mBaseSliderXPosition;
    int mCurrentTilt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCurrentTilt = MAP_2D;

        if (mModule == null) {
            mModule = new MapSliderRightModule(this);
        }

        if (mComponent == null) {
            mComponent = DaggerMapSliderRightComponent.builder()
                    .appComponent(AndroidApplication.getComponent())
                    .mapModule(MapFragment.getMapModule())
                    .mapSliderRightModule(mModule)
                    .build();
        }

        getSliderRightComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_map_slider_right, container, false);

        ButterKnife.bind(this, mRootView);

        Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        mScreenSize = new Point();
        display.getSize(mScreenSize);

        mSeekBarZoom.setOnSeekBarChangeListener(new SeekBarListener());

        return mRootView;
    }

    @Override
    public void openPartially() {
        float x = mRootView.getWidth();
        if (x < mScreenSize.x / 2) {
            ObjectAnimator sliderAnimator = ObjectAnimator.ofFloat(mRootView, "x", mRootView.getX() - 100);
            ObjectAnimator harvestedAnimator = ObjectAnimator.ofFloat(mTextViewHarvested, "scaleY", 1);
            ObjectAnimator harvestedSignAnimator = ObjectAnimator.ofFloat(mTextViewHarvestedSign, "scaleY", 1);
            ObjectAnimator speedAnimator = ObjectAnimator.ofFloat(mTextViewSpeed, "scaleY", 1);
            ObjectAnimator speedSignAnimator = ObjectAnimator.ofFloat(mTextViewSpeedSign, "scaleY", 1);
            ObjectAnimator seekbarZoomAnimator = ObjectAnimator.ofFloat(mSeekBarZoom, "scaleY", 1);
            ObjectAnimator button2d3dAnimator = ObjectAnimator.ofFloat(mButton2d3d, "scaleY", 1);

            AnimatorSet animSet = new AnimatorSet();
            animSet.play(sliderAnimator)
                    .with(harvestedAnimator)
                    .with(harvestedSignAnimator)
                    .with(speedAnimator)
                    .with(seekbarZoomAnimator)
                    .with(speedSignAnimator)
                    .with(button2d3dAnimator);
            animSet.start();
        } else {
            mRootView.setLayoutParams(new FrameLayout.LayoutParams(mBaseSliderSize, mScreenSize.y));
            mRootView.setX(mBaseSliderXPosition);
        }
    }

    @Override
    public void openFully() {
        if (mBaseSliderSize == 0) {
            mBaseSliderSize = mRootView.getWidth();
            mBaseSliderXPosition = mRootView.getX();
        }
        //TODO Fix first time usage misposition bug
        mRootView.setLayoutParams(new FrameLayout.LayoutParams(mScreenSize.x / 2, mScreenSize.y));
        mRootView.setX(mScreenSize.x / 2);
        Log.d("TAG", mRootView.getX()+"");
    }

    @Override
    public void close() {
        ObjectAnimator sliderAnimator = ObjectAnimator.ofFloat(mRootView, "x", mRootView.getX() + 100);
        ObjectAnimator harvestedAnimator = ObjectAnimator.ofFloat(mTextViewHarvested, "scaleY", 0);
        ObjectAnimator harvestedSignAnimator = ObjectAnimator.ofFloat(mTextViewHarvestedSign, "scaleY", 0);
        ObjectAnimator speedAnimator = ObjectAnimator.ofFloat(mTextViewSpeed, "scaleY", 0);
        ObjectAnimator speedSignAnimator = ObjectAnimator.ofFloat(mTextViewSpeedSign, "scaleY", 0);
        ObjectAnimator seekbarZoomAnimator = ObjectAnimator.ofFloat(mSeekBarZoom, "scaleY", 0);
        ObjectAnimator button2d3dAnimator = ObjectAnimator.ofFloat(mButton2d3d, "scaleY", 0);

        AnimatorSet animSet = new AnimatorSet();
        animSet.play(sliderAnimator)
                .with(harvestedAnimator)
                .with(harvestedSignAnimator)
                .with(speedAnimator)
                .with(seekbarZoomAnimator)
                .with(speedSignAnimator)
                .with(button2d3dAnimator);
        animSet.start();
    }

    @OnClick(R.id.button_zoom_in)
    public void onZoomMoreClicked() {
        mSeekBarZoom.incrementProgressBy(1);
    }

    @OnClick(R.id.button_zoom_out)
    public void onZoomLessClicked() {
        mSeekBarZoom.incrementProgressBy(-1);
    }

    @OnClick(R.id.button_2d3d)
    public void on2d3dClicked() {
        Resources resources = getResources();
        switch (mCurrentTilt) {
            case MAP_2D:
                mCurrentTilt = MAP_3D;
                mMapPresenter.changeTilt(MAP_3D);
                mButton2d3d.setImageDrawable(resources.getDrawable(R.drawable.button_3d2d));
                break;
            case MAP_3D:
                mCurrentTilt = MAP_2D;
                mMapPresenter.changeTilt(MAP_2D);
                mButton2d3d.setImageDrawable(resources.getDrawable(R.drawable.button_2d3d));
                break;
        }
    }


    public static MapSliderRightModule getSliderRightModule() {
        return mModule;
    }

    public static MapSliderRightComponent getSliderRightComponent() {
        return mComponent;
    }

    private class SeekBarListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (progress == 0) {
                progress++;
            }
            mMapPresenter.changeZoom(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }
}

