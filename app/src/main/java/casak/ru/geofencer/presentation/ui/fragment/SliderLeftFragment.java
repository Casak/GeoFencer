package casak.ru.geofencer.presentation.ui.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.maps.model.Polyline;

import java.util.Map;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import casak.ru.geofencer.AndroidApplication;
import casak.ru.geofencer.R;
import casak.ru.geofencer.domain.model.Arrow;
import casak.ru.geofencer.presentation.presenters.GoogleMapPresenter;
import casak.ru.geofencer.storage.FieldRepositoryImpl;
import casak.ru.geofencer.threading.MainThread;

/**
 * Created on 15.02.2017.
 */

public class SliderLeftFragment extends Fragment {

    @BindView(R.id.button_open_close_left)
    ImageButton mButtonOpenClose;
    @BindView(R.id.button_navigation)
    ImageButton mButtonNavigation;
    @BindView(R.id.button_messages)
    ImageButton mButtonMessages;

    View mRootView;
    boolean mIsSliderOpen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_map_slider_left, container, false);

        mIsSliderOpen = true;

        ButterKnife.bind(this, mRootView);

        return mRootView;
    }

    @OnClick(R.id.button_open_close_left)
    public void openCloseSlider() {
        if (mIsSliderOpen) {
            ObjectAnimator sliderAnimator = ObjectAnimator.ofFloat(mRootView, "x", -140);
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

    //TODO Delete
    boolean isNotFirstClick;
    @OnClick(R.id.button_navigation)
    public void onNavClick() {
        if (!isNotFirstClick) {
            isNotFirstClick = true;
            new Thread(new Runnable() {
                GoogleMapPresenter presenter = GoogleMapFragment.getMapComponent().getGoogleMapPresenter();
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
}