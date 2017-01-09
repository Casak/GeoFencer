package casak.ru.geofencer.domain.interactors;

import casak.ru.geofencer.domain.interactors.base.Interactor;
import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.model.RouteModel;

/**
 * Created on 05.01.2017.
 */

public interface RouteBuilderInteractor extends Interactor{

    interface Callback{
        void positionUpdate(Point point);
    }

    void newPoint(Point point);
    RouteModel startBuildRoute(RouteModel.Type type);
    void finishBuildRoute(RouteModel routeModel);
}
