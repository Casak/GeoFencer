package com.smartagrodriver.core.domain.interactors;

import com.smartagrodriver.core.domain.interactors.base.Interactor;
import com.smartagrodriver.core.domain.model.Route;

/**
 * Created on 05.01.2017.
 */

public interface RouteBuilderInteractor extends Interactor, LocationInteractor.OnLocationChangedListener {
    interface Callback {
        void routeBuildingFinished(Route route);
    }

    void createComputedRoutes(int fieldId, boolean toLeft);

    void finish();
}