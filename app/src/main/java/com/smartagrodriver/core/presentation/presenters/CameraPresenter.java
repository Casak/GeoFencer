package com.smartagrodriver.core.presentation.presenters;

import com.smartagrodriver.core.domain.interactors.LocationInteractor;
import com.smartagrodriver.core.presentation.presenters.base.BasePresenter;

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