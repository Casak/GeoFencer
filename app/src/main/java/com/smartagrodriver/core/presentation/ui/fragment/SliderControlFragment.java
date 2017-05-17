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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created on 15.02.2017.
 */

public class SliderControlFragment extends Fragment {

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
    boolean mIsSliderOpen;


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

        mIsSliderOpen = true;

        ButterKnife.bind(this, mRootView);

        return mRootView;
    }


    @OnClick(R.id.button_open_left)
    public void openLeftSlider() {

    }

    /*
    public void openCloseSlider() {
        if (mIsSliderOpen) {
            ObjectAnimator sliderAnimator = ObjectAnimator.ofFloat(mRootView, "x", -100);
            ObjectAnimator openCloseAnimator = ObjectAnimator.ofFloat(mButtonOpenClose, "rotation", 0, -180);
            ObjectAnimator navigationAnimator = ObjectAnimator.ofFloat(mButtonNavigation, "rotation", 0, 360);
            ObjectAnimator navigationScaleAnimator = ObjectAnimator.ofFloat(mButtonNavigation, "scaleY", 0);
            ObjectAnimator messagesAnimator = ObjectAnimator.ofFloat(mButtonMessages, "rotation", 0, 360);
            ObjectAnimator messagesScaleAnimator = ObjectAnimator.ofFloat(mButtonMessages, "scaleY", 0);

            AnimatorSet animSet = new AnimatorSet();
            animSet.play(sliderAnimator)
                    .with(openCloseAnimator)
                    .with(navigationAnimator)
                    .with(navigationScaleAnimator)
                    .with(messagesAnimator)
                    .with(messagesScaleAnimator);
            animSet.start();


            mButtonOpenClose.setScaleType(ImageView.ScaleType.FIT_START);
            mIsSliderOpen = false;
        } else {
            ObjectAnimator sliderAnimator = ObjectAnimator.ofFloat(mRootView, "x", 0);
            ObjectAnimator openCloseAnimator = ObjectAnimator.ofFloat(mButtonOpenClose, "rotation", -180, 0);
            ObjectAnimator navigationAnimator = ObjectAnimator.ofFloat(mButtonNavigation, "rotation", 0, 360);
            ObjectAnimator navigationScaleAnimator = ObjectAnimator.ofFloat(mButtonNavigation, "scaleY", 1);
            ObjectAnimator messagesAnimator = ObjectAnimator.ofFloat(mButtonMessages, "rotation", 0, 360);
            ObjectAnimator messagesScaleAnimator = ObjectAnimator.ofFloat(mButtonMessages, "scaleY", 1);

            AnimatorSet animSet = new AnimatorSet();
            animSet.play(sliderAnimator)
                    .with(openCloseAnimator)
                    .with(navigationAnimator)
                    .with(navigationScaleAnimator)
                    .with(messagesAnimator)
                    .with(messagesScaleAnimator);
            animSet.start();

            mButtonOpenClose.setScaleType(ImageView.ScaleType.FIT_END);
            mIsSliderOpen = true;
        }
    }
*/


    public static MapSliderControlModule getSliderModule() {
        return mModule;
    }

    public static MapSliderControlComponent getSliderComponent() {
        return mComponent;
    }
}