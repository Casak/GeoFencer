package com.smartagrodriver.core.domain.interactors;

import com.smartagrodriver.core.domain.interactors.base.Interactor;
import com.smartagrodriver.core.domain.model.Point;

/**
 * Created on 16.02.2017.
 */

public interface PointerInteractor extends Interactor, LocationInteractor.OnLocationChangedListener {
    interface Callback {
        void showPointer(double value);
    }

    void init(PointerInteractor.Callback callback, int fieldId, int machineryWidth);

    double getCurrentRouteBearing();

    Point getNearestPoint();

    long getCurrentRouteId();
}