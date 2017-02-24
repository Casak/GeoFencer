package casak.ru.geofencer.domain.interactors;

import casak.ru.geofencer.domain.interactors.base.Interactor;
import casak.ru.geofencer.domain.model.Arrow;
import casak.ru.geofencer.domain.model.Field;
import casak.ru.geofencer.domain.model.Route;

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
