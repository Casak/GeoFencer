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
    public TextView delta1;
    public TextView delta2;
    public TextView delta3;
    public TextView delta4;
    public TextView delta5;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delta, container, false);

        delta1 = (TextView) view.findViewById(R.id.delta1);
        delta2 = (TextView) view.findViewById(R.id.delta2);
        delta3 = (TextView) view.findViewById(R.id.delta3);
        delta4 = (TextView) view.findViewById(R.id.delta4);
        delta5 = (TextView) view.findViewById(R.id.delta5);

        return view;
    }
}
