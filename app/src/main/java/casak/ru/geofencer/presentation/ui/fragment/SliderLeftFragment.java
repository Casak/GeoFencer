package casak.ru.geofencer.presentation.ui.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //GoogleMapFragment.getMapComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_slider_left, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

}
