package casak.ru.geofencer.presentation.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import casak.ru.geofencer.R;
import casak.ru.geofencer.presentation.presenters.GoogleMapPresenter;
import casak.ru.geofencer.presentation.ui.activities.SettingsActivity;

/**
 * Created on 15.02.2017.
 */

public class MapButtonFragment extends Fragment {
    private final int MAP_3D = 90;
    private final int MAP_2D = 0;

    @Inject
    GoogleMapPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleMapFragment.getMapComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_button, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @OnClick(R.id.button_tilt_more)
    public void onTiltMoreClicked() {
        presenter.onTiltMore();
    }

    @OnClick(R.id.button_tilt_less)
    public void onTiltLessClicked() {
        presenter.onTiltLess();
    }

    @OnClick(R.id.button_2d)
    public void on2DClicked() {
        presenter.changeTilt(MAP_2D);
    }

    @OnClick(R.id.button_3d)
    public void on3DClicked() {
        presenter.changeTilt(MAP_3D);
    }

    @OnClick(R.id.button_change_type)
    public void onChangeTypeClicked() {
        presenter.changeMapType();
    }

    @OnClick(R.id.button_zoom_more)
    public void onZoomMoreClicked() {
        presenter.onZoomMore();
    }

    @OnClick(R.id.button_zoom_less)
    public void onZoomLessClicked() {
        presenter.onZoomLess();
    }

    @OnClick(R.id.button_settings)
    public void onSettingsClicked() {
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent);
    }
}
