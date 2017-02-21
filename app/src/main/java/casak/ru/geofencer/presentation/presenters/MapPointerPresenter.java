package casak.ru.geofencer.presentation.presenters;

import casak.ru.geofencer.domain.interactors.PointerInteractor;
import casak.ru.geofencer.presentation.presenters.base.BasePresenter;

/**
 * Created on 21.02.2017.
 */

public interface MapPointerPresenter extends BasePresenter, PointerInteractor.Callback {
    interface View {
        int ALL_SEMAPHORES = 5;
        int TO_RED_CLOSE = 4;
        int TO_YELLOW_FAR = 3;
        int TO_YELLOW_CLOSE = 2;
        int TO_GREEN_FAR = 1;
        int TO_GREEN_CLOSE = 0;

        enum Type {
            LEFT,
            RIGHT,
            ALL
        }

        void turnOff(int count, Type side);

        void turnOn(int count, Type side);
    }
}
