package com.smartagrodriver.core.domain.interactors;

import com.smartagrodriver.core.domain.interactors.base.Interactor;
import com.smartagrodriver.core.domain.model.Point;

/**
 * Created on 20.02.2017.
 */

public interface LocationInteractor extends Interactor {
    interface Callback {
        void addToSessionRoute(Point point);
    }

    interface OnLocationChangedListener {
        void onChange(Point point);
    }

    void init(Callback callback);

    OnLocationChangedListener getListener();
}
