package casak.ru.geofencer.presentation.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import casak.ru.geofencer.R;

/**
 * Created on 15.02.2017.
 */

public class SliderRightFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //GoogleMapFragment.getMapComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map_slider_right, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;
    }

}
