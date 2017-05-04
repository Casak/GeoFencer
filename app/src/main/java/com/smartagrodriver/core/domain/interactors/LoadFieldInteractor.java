package com.smartagrodriver.core.domain.interactors;

import com.smartagrodriver.core.domain.interactors.base.Interactor;
import com.smartagrodriver.core.domain.model.Field;
import com.smartagrodriver.core.domain.model.Route;

/**
 * Created on 21.02.2017.
 */

public interface LoadFieldInteractor extends Interactor {
    interface Callback {
        void showField(Field model);

        void showRoute(Route model);
    }

    void init(LoadFieldInteractor.Callback callback, int fieldId);
}
