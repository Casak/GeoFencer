package casak.ru.geofencer.presentation.presenters;

import android.view.View;

import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.presentation.presenters.base.BasePresenter;

/**
 * Created on 21.02.2017.
 */

public interface CameraPresenter extends BasePresenter, View.OnTouchListener {
    void onLocationChanged(Point point);
}
