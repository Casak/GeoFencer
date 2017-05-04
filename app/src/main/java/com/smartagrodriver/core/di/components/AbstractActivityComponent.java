package com.smartagrodriver.core.di.components;

import android.app.Activity;

import com.smartagrodriver.core.di.modules.ActivityModule;
import com.smartagrodriver.core.di.scopes.ActivityScope;
import dagger.Component;

/**
 * Created on 09.02.2017.
 */

@ActivityScope
@Component(modules = {ActivityModule.class}, dependencies = {AppComponent.class})
public interface AbstractActivityComponent {

    Activity getActivityContext();
}
