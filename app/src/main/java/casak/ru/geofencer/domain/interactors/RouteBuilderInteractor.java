package casak.ru.geofencer.domain.interactors;


import casak.ru.geofencer.domain.interactors.base.Interactor;
import casak.ru.geofencer.domain.model.RouteModel;

/**
 * Created on 05.01.2017.
 */

public interface RouteBuilderInteractor extends Interactor{

    interface Callback{
        void routeBuildingFinished(RouteModel route);
    }

    void finish();
    void createComputedRoutes(int fieldId);
}
