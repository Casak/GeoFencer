package com.smartagrodriver.core.presentation.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.smartagrodriver.core.R;
import com.smartagrodriver.core.presentation.ui.fragment.SettingsFragment;

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