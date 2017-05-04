package com.smartagrodriver.core.presentation.ui.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.smartagrodriver.core.R;
import com.smartagrodriver.core.presentation.presenters.GoogleMapPresenter;

/**
 * Created on 15.02.2017.
 */

public class SliderRightFragment extends Fragment {

    @Inject
    GoogleMapPresenter mGoogleMapPresenter;
    @BindView(R.id.button_open_close_right)
    ImageButton mButtonOpenClose;
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
    boolean mIsSliderOpen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleMapFragment.getMapComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_map_slider_right, container, false);

        mIsSliderOpen = true;
        ButterKnife.bind(this, mRootView);

        mSeekBarZoom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress == 0){
                    progress++;
                }
                mGoogleMapPresenter.changeZoom(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return mRootView;
    }


    @OnClick(R.id.button_open_close_right)
    public void openCloseSlider() {
        if (mIsSliderOpen) {
            ObjectAnimator sliderAnimator = ObjectAnimator.ofFloat(mRootView, "x", mRootView.getX() + 140);
            ObjectAnimator openCloseAnimator = ObjectAnimator.ofFloat(mButtonOpenClose, "rotation", -180, 0);
            ObjectAnimator harvestedAnimator = ObjectAnimator.ofFloat(mTextViewHarvested, "scaleY", 0);
            ObjectAnimator harvestedSignAnimator = ObjectAnimator.ofFloat(mTextViewHarvestedSign, "scaleY", 0);
            ObjectAnimator speedAnimator = ObjectAnimator.ofFloat(mTextViewSpeed, "scaleY", 0);
            ObjectAnimator speedSignAnimator = ObjectAnimator.ofFloat(mTextViewSpeedSign, "scaleY", 0);
            ObjectAnimator seekbarZoomAnimator = ObjectAnimator.ofFloat(mSeekBarZoom, "scaleY", 0);
            ObjectAnimator button2d3dAnimator = ObjectAnimator.ofFloat(mButton2d3d, "scaleY", 0);

            AnimatorSet animSet = new AnimatorSet();
            animSet.play(sliderAnimator)
                    .with(openCloseAnimator)
                    .with(harvestedAnimator)
                    .with(harvestedSignAnimator)
                    .with(speedAnimator)
                    .with(seekbarZoomAnimator)
                    .with(speedSignAnimator)
                    .with(button2d3dAnimator);
            animSet.start();


            mButtonOpenClose.setScaleType(ImageView.ScaleType.FIT_START);
            mIsSliderOpen = false;
        } else {
            ObjectAnimator sliderAnimator = ObjectAnimator.ofFloat(mRootView, "x", mRootView.getX() - 140);
            ObjectAnimator openCloseAnimator = ObjectAnimator.ofFloat(mButtonOpenClose, "rotation", 0, -180);
            ObjectAnimator harvestedAnimator = ObjectAnimator.ofFloat(mTextViewHarvested, "scaleY", 1);
            ObjectAnimator harvestedSignAnimator = ObjectAnimator.ofFloat(mTextViewHarvestedSign, "scaleY", 1);
            ObjectAnimator speedAnimator = ObjectAnimator.ofFloat(mTextViewSpeed, "scaleY", 1);
            ObjectAnimator speedSignAnimator = ObjectAnimator.ofFloat(mTextViewSpeedSign, "scaleY", 1);
            ObjectAnimator seekbarZoomAnimator = ObjectAnimator.ofFloat(mSeekBarZoom, "scaleY", 1);
            ObjectAnimator button2d3dAnimator = ObjectAnimator.ofFloat(mButton2d3d, "scaleY", 1);

            AnimatorSet animSet = new AnimatorSet();
            animSet.play(sliderAnimator)
                    .with(openCloseAnimator)
                    .with(harvestedAnimator)
                    .with(harvestedSignAnimator)
                    .with(speedAnimator)
                    .with(seekbarZoomAnimator)
                    .with(speedSignAnimator)
                    .with(button2d3dAnimator);
            animSet.start();

            mButtonOpenClose.setScaleType(ImageView.ScaleType.FIT_END);
            mIsSliderOpen = true;
        }
    }

    @OnClick(R.id.button_zoom_in)
    public void onZoomMoreClicked() {
        mSeekBarZoom.incrementProgressBy(1);
    }

    @OnClick(R.id.button_zoom_out)
    public void onZoomLessClicked() {
        mSeekBarZoom.incrementProgressBy(-1);
    }


}
