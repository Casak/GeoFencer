package casak.ru.geofencer.di.components;

import android.app.Activity;

import casak.ru.geofencer.di.modules.ActivityModule;
import casak.ru.geofencer.di.scopes.ActivityScope;
import dagger.Component;

/**
 * Created on 09.02.2017.
 */

@ActivityScope
@Component(modules = {ActivityModule.class}, dependencies = {AppComponent.class})
public interface AbstractActivityComponent {

    Activity getActivityContext();
}
