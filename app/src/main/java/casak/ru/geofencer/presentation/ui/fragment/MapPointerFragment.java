package casak.ru.geofencer.presentation.ui.fragment;

import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.pointer_left_red_far)
    TextView leftRedFar;
    @BindView(R.id.pointer_left_red_close)
    TextView leftRedClose;
    @BindView(R.id.pointer_left_yellow_far)
    TextView leftYellowFar;
    @BindView(R.id.pointer_left_yellow_close)
    TextView leftYellowClose;
    @BindView(R.id.pointer_left_green_far)
    TextView leftGreenFar;
    @BindView(R.id.pointer_left_green_close)
    TextView leftGreenClose;

    @BindView(R.id.pointer_right_green_close)
    TextView rightGreenClose;
    @BindView(R.id.pointer_right_green_far)
    TextView rightGreenFar;
    @BindView(R.id.pointer_right_yellow_close)
    TextView rightYellowClose;
    @BindView(R.id.pointer_right_yellow_far)
    TextView rightYellowFar;
    @BindView(R.id.pointer_right_red_close)
    TextView rightRedClose;
    @BindView(R.id.pointer_right_red_far)
    TextView rightRedFar;

    private List<TextView> right;
    private List<TextView> left;
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

        return rootView;
    }

    @Override
    public void turnOff(int count, Type side) {
        for (int i = 0; i <= count; i++) {
            switch (side) {
                case LEFT:
                    left.get(i).setBackground(off);
                    break;
                case RIGHT:
                    right.get(i).setBackground(off);
                    break;
                case ALL:
                default:
                    left.get(i).setBackground(off);
                    right.get(i).setBackground(off);
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

    private void fill(int count, List<TextView> list) {
        switch (count) {
            case MapPointerPresenter.View.ALL_SEMAPHORES:
                list.get(MapPointerPresenter.View.ALL_SEMAPHORES).setBackground(red);
            case MapPointerPresenter.View.TO_RED_CLOSE:
                list.get(MapPointerPresenter.View.TO_RED_CLOSE).setBackground(red);
            case MapPointerPresenter.View.TO_YELLOW_FAR:
                list.get(MapPointerPresenter.View.TO_YELLOW_FAR).setBackground(yellow);
            case MapPointerPresenter.View.TO_YELLOW_CLOSE:
                list.get(MapPointerPresenter.View.TO_YELLOW_CLOSE).setBackground(yellow);
            case MapPointerPresenter.View.TO_GREEN_FAR:
                list.get(MapPointerPresenter.View.TO_GREEN_FAR).setBackground(green);
            case MapPointerPresenter.View.TO_GREEN_CLOSE:
                list.get(MapPointerPresenter.View.TO_GREEN_CLOSE).setBackground(green);
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