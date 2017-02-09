package casak.ru.geofencer.injector.modules;

import android.app.Activity;

import casak.ru.geofencer.injector.scopes.ActivityScope;
import dagger.Module;
import dagger.Provides;

/**
 * Created on 08.02.2017.
 */

@Module
public class ActivityModule {
    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    Activity getActivityContext() {
        return activity;
    }
}