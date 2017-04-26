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
    @BindView(R.id.pointer_left_red)
    TextView mTextViewLeftRed;
    @BindView(R.id.pointer_left_orange_dark)
    TextView mTextViewLeftOrangeDark;
    @BindView(R.id.pointer_left_orange)
    TextView mTextViewLeftOrange;
    @BindView(R.id.pointer_left_yellow_dark)
    TextView mTextViewLeftYellowDark;
    @BindView(R.id.pointer_left_yellow)
    TextView mTextViewLeftYellow;
    @BindView(R.id.pointer_left_green_dark)
    TextView mTextViewLeftGreenDark;
    @BindView(R.id.pointer_left_green)
    TextView mTextViewLeftGreen;

    @BindView(R.id.pointer_right_red)
    TextView mTextViewRightRed;
    @BindView(R.id.pointer_right_orange_dark)
    TextView mTextViewRightOrangeDark;
    @BindView(R.id.pointer_right_orange)
    TextView mTextViewRightOrange;
    @BindView(R.id.pointer_right_yellow_dark)
    TextView mTextViewRightYellowDark;
    @BindView(R.id.pointer_right_yellow)
    TextView mTextViewRightYellow;
    @BindView(R.id.pointer_right_green_dark)
    TextView mTextViewRightGreenDark;
    @BindView(R.id.pointer_right_green)
    TextView mTextViewRightGreen;

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

        return rootView;
    }

    @Override
    public void turnOff(int count, Type side) {
        /*for (int i = 0; i <= count; i++) {
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
        }*/
    }

    @Override
    public void turnOn(int count, Type side) {
        /*switch (side) {
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
        }*/
    }

    private void fill(int count, List<TextView> list) {
        /*switch (count) {
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
        }*/
    }

    public static PointerModule getPointerModule() {
        return mModule;
    }

    public static PointerComponent getPointerComponent() {
        return mComponent;
    }
}