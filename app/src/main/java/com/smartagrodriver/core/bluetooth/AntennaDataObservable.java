package com.smartagrodriver.core.bluetooth;

import com.smartagrodriver.core.domain.interactors.LocationInteractor;
import com.smartagrodriver.core.domain.model.Point;

/**
 * Created on 20.02.2017.
 */

public interface AntennaDataObservable {
    void registerObserver(LocationInteractor.OnLocationChangedListener observer);

    void removeObserver(LocationInteractor.OnLocationChangedListener observer);

    void passLocation(Point point);
}
