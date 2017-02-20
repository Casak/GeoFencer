package casak.ru.geofencer.domain.interactors;


import casak.ru.geofencer.domain.interactors.base.Interactor;
import casak.ru.geofencer.domain.model.Route;

/**
 * Created on 05.01.2017.
 */

public interface RouteBuilderInteractor extends Interactor, LocationInteractor.OnLocationChanged {

    interface Callback {
        void routeBuildingFinished(Route route);
    }

    void finish();

    void createComputedRoutes(int fieldId);
}
