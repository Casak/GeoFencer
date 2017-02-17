package casak.ru.geofencer.presentation.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import casak.ru.geofencer.R;
import casak.ru.geofencer.presentation.ui.fragment.SettingsFragment;

/**
 * Created on 17.02.2017.
 */

public class SettingsActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        getFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, new SettingsFragment())
                .commit();
    }
}