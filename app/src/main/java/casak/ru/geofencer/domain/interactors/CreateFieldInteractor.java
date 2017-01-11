package casak.ru.geofencer.domain.interactors;

import casak.ru.geofencer.domain.interactors.base.Interactor;
import casak.ru.geofencer.domain.model.ArrowModel;
import casak.ru.geofencer.domain.model.FieldModel;
import casak.ru.geofencer.domain.model.RouteModel;

/**
 * Created on 05.01.2017.
 */

public interface CreateFieldInteractor extends Interactor{
    interface Callback {
        void showArrow(ArrowModel model);
        void hideArrow(ArrowModel model);
        void showField(FieldModel model);
        void hideField(FieldModel model);
        void showRoute(RouteModel model);
        void hideRoute(RouteModel model);
    }

    void onArrowClick(ArrowModel model);
    void onStartCreatingRouteClick();
    void onFinishCreatingRouteClick();
}
