package casak.ru.geofencer.presentation.ui.fragment;

import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import casak.ru.geofencer.AndroidApplication;
import casak.ru.geofencer.R;
import casak.ru.geofencer.di.components.DaggerPointerComponent;
import casak.ru.geofencer.di.components.PointerComponent;
import casak.ru.geofencer.di.modules.PointerModule;
import casak.ru.geofencer.presentation.presenters.MapPointerPresenter;
import casak.ru.geofencer.presentation.ui.base.BaseActivity;

/**
 * Created on 21.02.2017.
 */

public class MapPointerFragment extends Fragment implements MapPointerPresenter.View {
    @Inject
    MapPointerPresenter mPresenter;

    @BindView(R.id.pointer_left_red_far)
    ImageView leftRedFar;
    @BindView(R.id.pointer_left_red_close)
    ImageView leftRedClose;
    @BindView(R.id.pointer_left_yellow_far)
    ImageView leftYellowFar;
    @BindView(R.id.pointer_left_yellow_close)
    ImageView leftYellowClose;
    @BindView(R.id.pointer_left_green_far)
    ImageView leftGreenFar;
    @BindView(R.id.pointer_left_green_close)
    ImageView leftGreenClose;

    @BindView(R.id.pointer_right_green_close)
    ImageView rightGreenClose;
    @BindView(R.id.pointer_right_green_far)
    ImageView rightGreenFar;
    @BindView(R.id.pointer_right_yellow_close)
    ImageView rightYellowClose;
    @BindView(R.id.pointer_right_yellow_far)
    ImageView rightYellowFar;
    @BindView(R.id.pointer_right_red_close)
    ImageView rightRedClose;
    @BindView(R.id.pointer_right_red_far)
    ImageView rightRedFar;

    private List<ImageView> right;
    private List<ImageView> left;
    private Drawable red;
    private Drawable yellow;
    private Drawable green;
    private Drawable off;

    private static PointerComponent mComponent;
    private static PointerModule mModule;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        init();

        //TODO Create listener for starting pointer computing
        mPresenter.resume();

        return rootView;
    }

    @Override
    public void turnOff(int count, Type side) {
        for (int i = 0; i <= count; i++) {
            switch (side) {
                case LEFT:
                    left.get(i).setImageDrawable(off);
                    break;
                case RIGHT:
                    right.get(i).setImageDrawable(off);
                    break;
                case ALL:
                default:
                    left.get(i).setImageDrawable(off);
                    right.get(i).setImageDrawable(off);
            }
        }
    }

    @Override
    public void turnOn(int count, Type side) {
        switch (side) {
            case LEFT:
                fill(count, left);
                break;
            case RIGHT:
                fill(count, right);
                break;
            case ALL:
                fill(count, left);
                fill(count, right);
            default:
                turnOff(MapPointerPresenter.View.ALL_SEMAPHORES, Type.ALL);
        }
    }

    private void fill(int count, List<ImageView> list) {
        switch (count) {
            case MapPointerPresenter.View.ALL_SEMAPHORES:
                list.get(MapPointerPresenter.View.ALL_SEMAPHORES).setImageDrawable(red);
            case MapPointerPresenter.View.TO_RED_CLOSE:
                list.get(MapPointerPresenter.View.TO_RED_CLOSE).setImageDrawable(red);
            case MapPointerPresenter.View.TO_YELLOW_FAR:
                list.get(MapPointerPresenter.View.TO_YELLOW_FAR).setImageDrawable(yellow);
            case MapPointerPresenter.View.TO_YELLOW_CLOSE:
                list.get(MapPointerPresenter.View.TO_YELLOW_CLOSE).setImageDrawable(yellow);
            case MapPointerPresenter.View.TO_GREEN_FAR:
                list.get(MapPointerPresenter.View.TO_GREEN_FAR).setImageDrawable(green);
            case MapPointerPresenter.View.TO_GREEN_CLOSE:
                list.get(MapPointerPresenter.View.TO_GREEN_CLOSE).setImageDrawable(green);
        }
    }

    private void init() {
        right = new ArrayList<>();
        left = new ArrayList<>();

        right.add(rightGreenClose);
        right.add(rightGreenFar);
        right.add(rightYellowClose);
        right.add(rightYellowFar);
        right.add(rightRedClose);
        right.add(rightRedFar);

        left.add(leftGreenClose);
        left.add(leftGreenFar);
        left.add(leftYellowClose);
        left.add(leftYellowFar);
        left.add(leftRedClose);
        left.add(leftRedFar);

        Resources resources = getResources();
        red = resources.getDrawable(R.drawable.pointer_red);
        yellow = resources.getDrawable(R.drawable.pointer_yellow);
        green = resources.getDrawable(R.drawable.pointer_green);
        off = resources.getDrawable(R.drawable.pointer_off);
    }

    public static PointerModule getPointerModule() {
        return mModule;
    }

    public static PointerComponent getPointerComponent() {
        return mComponent;
    }
}