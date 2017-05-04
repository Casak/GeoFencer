package casak.ru.geofencer.presentation.presenters;

import casak.ru.geofencer.domain.interactors.PointerInteractor;
import casak.ru.geofencer.presentation.presenters.base.BasePresenter;

/**
 * Created on 21.02.2017.
 */

public interface MapPointerPresenter extends BasePresenter, PointerInteractor.Callback {
    interface View {
        int NONE = 0;
        int GREEN = 1;
        int GREEN_DARK = 2;
        int YELLOW = 3;
        int YELLOW_DARK = 4;
        int ORANGE = 5;
        int ORANGE_DARK = 6;
        int RED = 7;

        enum Type {
            LEFT,
            RIGHT
        }

        void moveAnchor(Type side, int toPosition);
    }
}
