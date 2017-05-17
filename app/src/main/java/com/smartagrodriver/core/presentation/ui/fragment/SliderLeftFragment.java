package com.smartagrodriver.core.presentation.ui.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.gms.maps.model.Polyline;

import java.util.Map;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.smartagrodriver.core.AndroidApplication;
import com.smartagrodriver.core.R;
import com.smartagrodriver.core.di.components.DaggerSliderLeftComponent;
import com.smartagrodriver.core.di.components.SliderLeftComponent;
import com.smartagrodriver.core.di.modules.SliderLeftModule;
import com.smartagrodriver.core.domain.model.Arrow;
import com.smartagrodriver.core.presentation.presenters.MapPresenter;
import com.smartagrodriver.core.presentation.presenters.MapSliderPresenter;
import com.smartagrodriver.core.storage.FieldRepositoryImpl;
import com.smartagrodriver.core.threading.MainThread;

/**
 * Created on 15.02.2017.
 */

public class SliderLeftFragment extends Fragment implements MapSliderPresenter.View {

    private static SliderLeftComponent mComponent;
    private static SliderLeftModule mModule;

    @BindView(R.id.button_navigation)
    ImageButton mButtonNavigation;
    @BindView(R.id.button_messages)
    ImageButton mButtonMessages;

    View mRootView;
    boolean mIsSliderOpen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mModule == null) {
            mModule = new SliderLeftModule(this);
        }

        if (mComponent == null) {
            mComponent = DaggerSliderLeftComponent.builder()
                    .appComponent(AndroidApplication.getComponent())
                    .sliderLeftModule(mModule)
                    .build();
        }

        getSliderRightComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_map_slider_left, container, false);

        mIsSliderOpen = true;

        ButterKnife.bind(this, mRootView);

        return mRootView;
    }

    public void openCloseSlider() {
        if (mIsSliderOpen) {
            ObjectAnimator sliderAnimator = ObjectAnimator.ofFloat(mRootView, "x", -100);
            //ObjectAnimator openCloseAnimator = ObjectAnimator.ofFloat(mButtonOpenClose, "rotation", 0, -180);
            ObjectAnimator navigationAnimator = ObjectAnimator.ofFloat(mButtonNavigation, "rotation", 0, 360);
            ObjectAnimator navigationScaleAnimator = ObjectAnimator.ofFloat(mButtonNavigation, "scaleY", 0);
            ObjectAnimator messagesAnimator = ObjectAnimator.ofFloat(mButtonMessages, "rotation", 0, 360);
            ObjectAnimator messagesScaleAnimator = ObjectAnimator.ofFloat(mButtonMessages, "scaleY", 0);

            AnimatorSet animSet = new AnimatorSet();
            animSet.play(sliderAnimator)
                    //       .with(openCloseAnimator)
                    .with(navigationAnimator)
                    .with(navigationScaleAnimator)
                    .with(messagesAnimator)
                    .with(messagesScaleAnimator);
            animSet.start();


            //mButtonOpenClose.setScaleType(ImageView.ScaleType.FIT_START);
            mIsSliderOpen = false;
        } else {
            ObjectAnimator sliderAnimator = ObjectAnimator.ofFloat(mRootView, "x", 0);
            // ObjectAnimator openCloseAnimator = ObjectAnimator.ofFloat(mButtonOpenClose, "rotation", -180, 0);
            ObjectAnimator navigationAnimator = ObjectAnimator.ofFloat(mButtonNavigation, "rotation", 0, 360);
            ObjectAnimator navigationScaleAnimator = ObjectAnimator.ofFloat(mButtonNavigation, "scaleY", 1);
            ObjectAnimator messagesAnimator = ObjectAnimator.ofFloat(mButtonMessages, "rotation", 0, 360);
            ObjectAnimator messagesScaleAnimator = ObjectAnimator.ofFloat(mButtonMessages, "scaleY", 1);

            AnimatorSet animSet = new AnimatorSet();
            animSet.play(sliderAnimator)
                    //.with(openCloseAnimator)
                    .with(navigationAnimator)
                    .with(navigationScaleAnimator)
                    .with(messagesAnimator)
                    .with(messagesScaleAnimator);
            animSet.start();

            //mButtonOpenClose.setScaleType(ImageView.ScaleType.FIT_END);
            mIsSliderOpen = true;
        }
    }

    //TODO Delete
    boolean isNotFirstClick;

    @OnClick(R.id.button_navigation)
    public void onNavClick() {
        if (!isNotFirstClick) {
            isNotFirstClick = true;
            new Thread(new Runnable() {
                MapPresenter presenter = MapFragment.getMapComponent().getGoogleMapPresenter();
                MainThread mainThread = AndroidApplication.getComponent().getMainThread();

                @Override
                public void run() {
                    try {
                        presenter.startBuildField();
                        Thread.sleep(5000);
                        presenter.finishBuildField();
                        Thread.sleep(1000);
                        int i = 0;
                        for (final Map.Entry<Arrow, Polyline> e : presenter.getArrows().entrySet()) {
                            if (e.getKey().getType() == Arrow.Type.LEFT) {
                                mainThread.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        presenter.onPolylineClick(e.getValue());
                                    }
                                });
                            }
                        }
                        Thread.sleep(10000);
                        final int[] ids = new FieldRepositoryImpl().getAllFieldIds();
                        mainThread.post(new Runnable() {
                            @Override
                            public void run() {
                                presenter.onFieldLoad(ids[ids.length - 1]);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }


    public static SliderLeftModule getSliderRightModule() {
        return mModule;
    }

    public static SliderLeftComponent getSliderRightComponent() {
        return mComponent;
    }

    @Override
    public void startCloseAnimation() {

    }

    @Override
    public void startOpenAnimation() {

    }
}