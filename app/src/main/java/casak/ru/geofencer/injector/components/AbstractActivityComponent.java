package casak.ru.geofencer.injector.components;

import android.app.Activity;

import casak.ru.geofencer.injector.modules.ActivityModule;
import casak.ru.geofencer.injector.scopes.ActivityScope;
import dagger.Component;

/**
 * Created on 09.02.2017.
 */

@ActivityScope
@Component(modules = {ActivityModule.class}, dependencies = {AppComponent.class})
public interface AbstractActivityComponent {

    Activity getActivityContext();
}
