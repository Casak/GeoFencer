package casak.ru.geofencer.presentation.ui.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.health.PackageHealthStats;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import casak.ru.geofencer.R;
import casak.ru.geofencer.domain.repository.FieldRepository;
import casak.ru.geofencer.presentation.presenters.CameraPresenter;
import casak.ru.geofencer.presentation.presenters.GoogleMapPresenter;
import casak.ru.geofencer.presentation.ui.activities.SettingsActivity;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //GoogleMapFragment.getMapComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_map_slider_left, container, false);

        mIsSliderOpen = true;

        ButterKnife.bind(this, mRootView);

        return mRootView;
    }

    @OnClick(R.id.button_open_close_left)
    public void openCloseSlider() {
        if(mIsSliderOpen) {
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
        }
        else {
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

}
