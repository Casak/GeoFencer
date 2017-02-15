package casak.ru.geofencer.presentation.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import casak.ru.geofencer.R;
import casak.ru.geofencer.presentation.presenters.GoogleMapPresenter;

/**
 * Created on 15.02.2017.
 */

public class MapButtonFragment extends Fragment {
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
    public void tiltMore(){
        presenter.onTiltMore();
    }

    @OnClick(R.id.button_tilt_less)
    public void tiltLess(){
        presenter.onTiltLess();
    }
}
