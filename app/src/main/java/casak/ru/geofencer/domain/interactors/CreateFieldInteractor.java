package casak.ru.geofencer.domain.interactors;

import casak.ru.geofencer.domain.interactors.base.Interactor;
import casak.ru.geofencer.domain.model.ArrowModel;
import casak.ru.geofencer.domain.model.FieldModel;

/**
 * Created on 05.01.2017.
 */

public interface CreateFieldInteractor extends Interactor{
    interface Callback {
        void showArrow(ArrowModel model);
        void hideArrow(ArrowModel model);
        void showField(FieldModel model);
    }

    void onArrowClick(ArrowModel model);
    void onStartCreatingRouteClick();
    void onFinishCreatingRouteClick();
}
