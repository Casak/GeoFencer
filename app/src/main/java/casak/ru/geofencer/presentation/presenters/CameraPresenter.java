package casak.ru.geofencer.presentation.presenters;

import casak.ru.geofencer.domain.interactors.LocationInteractor;
import casak.ru.geofencer.presentation.presenters.base.BasePresenter;

/**
 * Created on 21.02.2017.
 */

public interface CameraPresenter extends BasePresenter, LocationInteractor.OnLocationChangedListener {
    enum FollowType {
        NON_FOLLOW,
        FOLLOW_ROUTE,
        FOLLOW_POINT
    }

    void setFollowType(FollowType type);
}