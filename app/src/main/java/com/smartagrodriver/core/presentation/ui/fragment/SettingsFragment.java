package com.smartagrodriver.core.presentation.ui.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.smartagrodriver.core.R;

/**
 * Created on 17.02.2017.
 */

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}