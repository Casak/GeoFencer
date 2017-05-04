package com.smartagrodriver.core.presentation.ui.fragment;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.smartagrodriver.core.AndroidApplication;
import com.smartagrodriver.core.R;
import com.smartagrodriver.core.di.components.DaggerPointerComponent;
import com.smartagrodriver.core.di.components.PointerComponent;
import com.smartagrodriver.core.di.modules.PointerModule;
import com.smartagrodriver.core.presentation.presenters.MapPointerPresenter;
import com.smartagrodriver.core.presentation.ui.base.BaseActivity;

/**
 * Created on 21.02.2017.
 */

public class MapPointerFragment extends Fragment implements MapPointerPresenter.View,
        ValueAnimator.AnimatorUpdateListener {

    @BindView(R.id.pointer_left_red)
    TextView mLeftRed;
    @BindView(R.id.pointer_left_orange_dark)
    TextView mLeftOrangeDark;
    @BindView(R.id.pointer_left_orange)
    TextView mLeftOrange;
    @BindView(R.id.pointer_left_yellow_dark)
    TextView mLeftYellowDark;
    @BindView(R.id.pointer_left_yellow)
    TextView mLeftYellow;
    @BindView(R.id.pointer_left_green_dark)
    TextView mLeftGreenDark;
    @BindView(R.id.pointer_left_green)
    TextView mLeftGreen;

    @BindView(R.id.pointer_center)
    TextView mCenter;

    @BindView(R.id.pointer_right_red)
    TextView mRightRed;
    @BindView(R.id.pointer_right_orange_dark)
    TextView mRightOrangeDark;
    @BindView(R.id.pointer_right_orange)
    TextView mRightOrange;
    @BindView(R.id.pointer_right_yellow_dark)
    TextView mRightYellowDark;
    @BindView(R.id.pointer_right_yellow)
    TextView mRightYellow;
    @BindView(R.id.pointer_right_green_dark)
    TextView mRightGreenDark;
    @BindView(R.id.pointer_right_green)
    TextView mRightGreen;

    @BindView(R.id.pointer_anchor)
    ImageView mAnchor;

    private static PointerComponent mComponent;
    private static PointerModule mModule;

    private TextView mPreviousLight;
    private int mPreviousColor;
    private float mCenterPosition;
    private float mMultiplier;
    private float mTextSizeZero;
    private float mTextSizeNormal;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources resources = getResources();


        mPreviousColor = resources.getColor(R.color.color_primary);
        mTextSizeZero = resources.getDimension(R.dimen.zero_size);
        mTextSizeNormal = resources.getDimension(R.dimen.pointer_text_size);
        mMultiplier = resources.getDimension(R.dimen.pointer_margin_right) +
                resources.getDimension(R.dimen.pointer_drawable_width);


        if (mModule == null) {
            mModule = new PointerModule(this);
        }

        if (mComponent == null) {
            mComponent = DaggerPointerComponent.builder()
                    .appComponent(AndroidApplication.getComponent())
                    .activityModule(BaseActivity.getActivityModule())
                    .pointerModule(mModule)
                    .build();
        }

        getPointerComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pointer, container, false);

        ButterKnife.bind(this, rootView);

        mPreviousLight = mCenter;

        mCenterPosition = mAnchor.getX();

        return rootView;
    }

    //TODO Improve logic
    @Override
    public void moveAnchor(Type side, int toPosition) {
        if (mPreviousLight != mCenter) {
            mPreviousLight.setTextSize(mTextSizeZero);
        }

        int newColor = getResources().getColor(R.color.slider_primary);
        ValueAnimator animator = new ValueAnimator();
        ArgbEvaluator evaluator = new ArgbEvaluator();
        animator.setEvaluator(evaluator);
        animator.addUpdateListener(this);

        if (side == Type.LEFT) {
            float shift = mCenterPosition - mMultiplier * toPosition;
            mAnchor.animate().translationX(shift);

            switch (toPosition) {
                case NONE:
                    mPreviousLight = mCenter;
                    break;
                case GREEN:
                    newColor = getResources().getColor(R.color.pointer_green);
                    mLeftGreen.setTextSize(mTextSizeNormal);
                    mPreviousLight = mLeftGreen;
                    break;
                case GREEN_DARK:
                    newColor = getResources().getColor(R.color.pointer_green_dark);
                    mLeftGreenDark.setTextSize(mTextSizeNormal);
                    mPreviousLight = mLeftGreenDark;
                    break;
                case YELLOW:
                    newColor = getResources().getColor(R.color.pointer_yellow);
                    mLeftYellow.setTextSize(mTextSizeNormal);
                    mPreviousLight = mLeftYellow;
                    break;
                case YELLOW_DARK:
                    newColor = getResources().getColor(R.color.pointer_yellow_dark);
                    mLeftYellowDark.setTextSize(mTextSizeNormal);
                    mPreviousLight = mLeftYellowDark;
                    break;
                case ORANGE:
                    newColor = getResources().getColor(R.color.pointer_orange);
                    mLeftOrange.setTextSize(mTextSizeNormal);
                    mPreviousLight = mLeftOrange;
                    break;
                case ORANGE_DARK:
                    newColor = getResources().getColor(R.color.pointer_orange_dark);
                    mLeftOrangeDark.setTextSize(mTextSizeNormal);
                    mPreviousLight = mLeftOrangeDark;
                    break;
                case RED:
                    newColor = getResources().getColor(R.color.pointer_red);
                    mLeftRed.setTextSize(mTextSizeNormal);
                    mPreviousLight = mLeftRed;
                    break;
            }

        } else if (side == Type.RIGHT) {
            float shift = mCenterPosition + mMultiplier * toPosition;
            mAnchor.animate().translationX(shift);

            switch (toPosition) {
                case NONE:
                    mPreviousLight = mCenter;
                    break;
                case GREEN:
                    newColor = getResources().getColor(R.color.pointer_green);
                    mRightGreen.setTextSize(mTextSizeNormal);
                    mPreviousLight = mRightGreen;
                    break;
                case GREEN_DARK:
                    newColor = getResources().getColor(R.color.pointer_green_dark);
                    mRightGreenDark.setTextSize(mTextSizeNormal);
                    mPreviousLight = mRightGreenDark;
                    break;
                case YELLOW:
                    newColor = getResources().getColor(R.color.pointer_yellow);
                    mRightYellow.setTextSize(mTextSizeNormal);
                    mPreviousLight = mRightYellow;
                    break;
                case YELLOW_DARK:
                    newColor = getResources().getColor(R.color.pointer_yellow_dark);
                    mRightYellowDark.setTextSize(mTextSizeNormal);
                    mPreviousLight = mRightYellowDark;
                    break;
                case ORANGE:
                    newColor = getResources().getColor(R.color.pointer_orange);
                    mRightOrange.setTextSize(mTextSizeNormal);
                    mPreviousLight = mRightOrange;
                    break;
                case ORANGE_DARK:
                    newColor = getResources().getColor(R.color.pointer_orange_dark);
                    mRightOrangeDark.setTextSize(mTextSizeNormal);
                    mPreviousLight = mRightOrangeDark;
                    break;
                case RED:
                    newColor = getResources().getColor(R.color.pointer_red);
                    mRightRed.setTextSize(mTextSizeNormal);
                    mPreviousLight = mRightRed;
                    break;
            }
        }

        animator.setIntValues(
                mPreviousColor,
                newColor);
        mPreviousColor = newColor;
        animator.start();
    }

    public static PointerModule getPointerModule() {
        return mModule;
    }

    public static PointerComponent getPointerComponent() {
        return mComponent;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        mAnchor.getDrawable()
                .setColorFilter((Integer) animation.getAnimatedValue(), PorterDuff.Mode.SRC);
    }
}