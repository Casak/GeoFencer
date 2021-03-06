package casak.ru.geofencer.domain.interactors;

import casak.ru.geofencer.domain.interactors.base.Interactor;
import casak.ru.geofencer.domain.model.Route;

/**
 * Created on 05.01.2017.
 */

public interface RouteBuilderInteractor extends Interactor, LocationInteractor.OnLocationChangedListener {
    interface Callback {
        void routeBuildingFinished(Route route);
    }

    void createComputedRoutes(int fieldId, boolean toLeft);

    void finish();
}