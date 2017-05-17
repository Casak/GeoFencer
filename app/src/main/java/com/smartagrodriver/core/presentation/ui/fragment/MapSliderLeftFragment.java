package com.smartagrodriver.core.presentation.ui.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.gms.maps.model.Polyline;

import java.util.Map;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.smartagrodriver.core.AndroidApplication;
import com.smartagrodriver.core.R;
import com.smartagrodriver.core.di.components.DaggerMapSliderLeftComponent;
import com.smartagrodriver.core.di.components.MapSliderLeftComponent;
import com.smartagrodriver.core.di.modules.MapSliderLeftModule;
import com.smartagrodriver.core.domain.model.Arrow;
import com.smartagrodriver.core.presentation.presenters.MapPresenter;
import com.smartagrodriver.core.presentation.presenters.MapSliderPresenter;
import com.smartagrodriver.core.storage.FieldRepositoryImpl;
import com.smartagrodriver.core.threading.MainThread;

import javax.inject.Inject;

/**
 * Created on 15.02.2017.
 */

public class MapSliderLeftFragment extends Fragment implements MapSliderPresenter.View {

    private static MapSliderLeftComponent mComponent;
    private static MapSliderLeftModule mModule;

    @Inject
    Context mContext;
    @BindView(R.id.button_navigation)
    ImageButton mButtonNavigation;
    @BindView(R.id.button_messages)
    ImageButton mButtonMessages;

    View mRootView;
    Point mScreenSize;
    int mBaseSliderSize;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mModule == null) {
            mModule = new MapSliderLeftModule(this);
        }

        if (mComponent == null) {
            mComponent = DaggerMapSliderLeftComponent.builder()
                    .appComponent(AndroidApplication.getComponent())
                    .mapSliderLeftModule(mModule)
                    .build();
        }

        getSliderLeftComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_map_slider_left, container, false);

        ButterKnife.bind(this, mRootView);

        Display display = ((WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        mScreenSize = new Point();
        display.getSize(mScreenSize);

        return mRootView;
    }

    @Override
    public void openPartially() {
        float x = mRootView.getWidth();
        if (x < mScreenSize.x/2) {
            ObjectAnimator sliderAnimator = ObjectAnimator.ofFloat(mRootView, "x", 0);
            ObjectAnimator navigationAnimator = ObjectAnimator.ofFloat(mButtonNavigation, "rotation", 0, 360);
            ObjectAnimator navigationScaleAnimator = ObjectAnimator.ofFloat(mButtonNavigation, "scaleY", 1);
            ObjectAnimator messagesAnimator = ObjectAnimator.ofFloat(mButtonMessages, "rotation", 0, 360);
            ObjectAnimator messagesScaleAnimator = ObjectAnimator.ofFloat(mButtonMessages, "scaleY", 1);

            AnimatorSet animSet = new AnimatorSet();
            animSet.play(sliderAnimator)
                    .with(navigationAnimator)
                    .with(navigationScaleAnimator)
                    .with(messagesAnimator)
                    .with(messagesScaleAnimator);
            animSet.start();
        } else {
            mRootView.setLayoutParams(new FrameLayout.LayoutParams(mBaseSliderSize, mScreenSize.y));
        }
    }

    @Override
    public void openFully() {
        if(mBaseSliderSize == 0) {
            mBaseSliderSize = mRootView.getWidth();
        }
        mRootView.setLayoutParams(new FrameLayout.LayoutParams(mScreenSize.x/2, mScreenSize.y));
    }

    @Override
    public void close() {
        ObjectAnimator sliderAnimator = ObjectAnimator.ofFloat(mRootView, "x", -100);
        ObjectAnimator navigationAnimator = ObjectAnimator.ofFloat(mButtonNavigation, "rotation", 0, 360);
        ObjectAnimator navigationScaleAnimator = ObjectAnimator.ofFloat(mButtonNavigation, "scaleY", 0);
        ObjectAnimator messagesAnimator = ObjectAnimator.ofFloat(mButtonMessages, "rotation", 0, 360);
        ObjectAnimator messagesScaleAnimator = ObjectAnimator.ofFloat(mButtonMessages, "scaleY", 0);

        AnimatorSet animSet = new AnimatorSet();
        animSet.play(sliderAnimator)
                .with(navigationAnimator)
                .with(navigationScaleAnimator)
                .with(messagesAnimator)
                .with(messagesScaleAnimator);
        animSet.start();
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


    public static MapSliderLeftModule getSliderLeftModule() {
        return mModule;
    }

    public static MapSliderLeftComponent getSliderLeftComponent() {
        return mComponent;
    }
}