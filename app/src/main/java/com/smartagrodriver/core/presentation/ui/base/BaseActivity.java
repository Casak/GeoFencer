package com.smartagrodriver.core.presentation.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.smartagrodriver.core.AndroidApplication;
import com.smartagrodriver.core.di.components.AbstractActivityComponent;
import com.smartagrodriver.core.di.components.DaggerAbstractActivityComponent;
import com.smartagrodriver.core.di.modules.ActivityModule;

/**
 * Created on 08.02.2017.
 */

public class BaseActivity extends FragmentActivity {
    private static ActivityModule module;
    private static AbstractActivityComponent component;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (module == null)
            module = new ActivityModule(this);

        if (component == null)
            component = DaggerAbstractActivityComponent.builder()
                    .appComponent(AndroidApplication.getComponent())
                    .activityModule(module)
                    .build();

    }

    public static AbstractActivityComponent getAbstractActivityComponent() {
        return component;
    }

    public static ActivityModule getActivityModule() {
        return module;
    }
}
