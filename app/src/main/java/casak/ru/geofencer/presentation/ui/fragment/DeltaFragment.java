package casak.ru.geofencer.presentation.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import casak.ru.geofencer.R;

/**
 * Created on 17.01.2017.
 */

public class DeltaFragment extends Fragment {
    public TextView mTextViewCrossTrackError;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delta, container, false);

        mTextViewCrossTrackError = (TextView) view.findViewById(R.id.cross_track_error);
        return view;
    }
}
