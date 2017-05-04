package com.smartagrodriver.core.domain.interactors;

import com.smartagrodriver.core.domain.interactors.base.Interactor;
import com.smartagrodriver.core.domain.model.Arrow;
import com.smartagrodriver.core.domain.model.Field;
import com.smartagrodriver.core.domain.model.Route;

/**
 * Created on 05.01.2017.
 */

public interface CreateFieldInteractor extends Interactor {
    interface Callback {
        void showArrow(Arrow model);

        void hideArrow(Arrow model);

        void showField(Field model);

        void hideField(Field model);

        void showRoute(Route model);

        void hideRoute(Route model);
    }

    void init(Callback callback, int width);

    LocationInteractor.OnLocationChangedListener getOnLocationChangedListener();

    void onArrowClick(Arrow model);

    void onStartCreatingRoute();

    void onFinishCreatingRoute();
}
