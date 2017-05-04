package com.smartagrodriver.core.domain.interactors;

import com.smartagrodriver.core.domain.interactors.base.Interactor;
import com.smartagrodriver.core.domain.model.Arrow;
import com.smartagrodriver.core.domain.model.Field;

public interface BuildFieldInteractor extends Interactor {
    interface Callback {
        void onFieldBuildFinish(Field field);

        void onFieldBuildFail(Field field);
    }

    void init(Field field, Arrow arrow);
}