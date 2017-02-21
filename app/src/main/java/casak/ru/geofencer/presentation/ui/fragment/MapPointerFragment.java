package casak.ru.geofencer.presentation.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import casak.ru.geofencer.R;

/**
 * Created on 21.02.2017.
 */

public class MapPointerFragment extends Fragment {
    @BindView(R.id.pointer_left_red_far)
    ImageView leftRedFar;
    @BindView(R.id.pointer_left_red_close)
    ImageView leftRedClose;
    @BindView(R.id.pointer_left_yellow_far)
    ImageView leftYellowFar;
    @BindView(R.id.pointer_left_yellow_close)
    ImageView leftYellowClose;
    @BindView(R.id.pointer_left_green_far)
    ImageView leftGrrenFar;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pointer, container, false);

        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
