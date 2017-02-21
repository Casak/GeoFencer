package casak.ru.geofencer.domain.interactors.impl;

import java.util.ArrayList;
import java.util.List;

import casak.ru.geofencer.domain.Constants;
import casak.ru.geofencer.domain.executor.Executor;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.domain.interactors.RouteBuilderInteractor;
import casak.ru.geofencer.domain.interactors.base.AbstractInteractor;
import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.model.Route;
import casak.ru.geofencer.domain.repository.ArrowRepository;
import casak.ru.geofencer.domain.repository.RouteRepository;

/**
 * Created on 05.01.2017.
 */
//TODO Callbacks
public class RouteBuilderInteractorImpl extends AbstractInteractor implements RouteBuilderInteractor {
    private static final String TAG = RouteBuilderInteractorImpl.class.getSimpleName();

    private static Route mFieldBuildingRoute;

    private boolean isBuildingRoute;

    private RouteBuilderInteractor.Callback mCallback;
    private RouteRepository mRouteRepository;
    private ArrowRepository mArrowRepository;
    private int fieldId;
    private int machineryWidth;

    public RouteBuilderInteractorImpl(Executor threadExecutor,
                                      MainThread mainThread,
                                      RouteBuilderInteractor.Callback callback,
                                      RouteRepository routeRepository,
                                      ArrowRepository arrowRepository,
                                      int fieldId,
                                      int machineryWidth) {
        super(threadExecutor, mainThread);
        this.fieldId = fieldId;
        this.machineryWidth = machineryWidth;

        mCallback = callback;
        mRouteRepository = routeRepository;
        mArrowRepository = arrowRepository;
    }

    @Override
    public void run() {
        mFieldBuildingRoute = mRouteRepository.createRouteModel(fieldId, Route.Type.BASE);

        isBuildingRoute = true;
    }

    @Override
    public void finish() {
        isBuildingRoute = false;

        mRouteRepository.addRouteModel(mFieldBuildingRoute);
        mCallback.routeBuildingFinished(mFieldBuildingRoute);
    }

    //TODO also add starting route as Type.Computed
    @Override
    public void createComputedRoutes(int fieldId) {
        Route fieldBuildingRoute = mRouteRepository.getBaseRoute(fieldId);
        boolean toLeft = mArrowRepository.getLeft(fieldId).isChosen();

        List<Route> result = computeRouteModels(fieldBuildingRoute, toLeft);
        if (result != null)
            for (Route model : result) {
                mRouteRepository.addRouteModel(model);
                mCallback.routeBuildingFinished(model);
            }
    }

    @Override
    public void onChange(Point point) {
        if (mFieldBuildingRoute != null && isBuildingRoute)
            mFieldBuildingRoute.addRoutePoint(point);
    }

    private List<Route> computeRouteModels(Route fieldBuildingRoute, boolean toLeft) {
        //TODO Normal check
        if (fieldBuildingRoute == null)
            return null;

        List<Route> result = new ArrayList<>();

        List<Point> fieldBuildingPoints = fieldBuildingRoute.getRoutePoints();
        Point start = fieldBuildingPoints.get(0);
        Point end = fieldBuildingPoints.get(fieldBuildingPoints.size() - 1);

        double arrowHeading = toLeft ? Constants.HEADING_TO_LEFT : Constants.HEADING_TO_RIGHT;

        double computedHeading = MapUtils.computeHeading(start, end);
        double normalHeading = computedHeading + arrowHeading;

        //TODO Check values
        int offset = 0;
        for (int i = 0; i < 4; i++) {
            List<Point> routePoints = computeNewPath(fieldBuildingPoints,
                    offset += machineryWidth,
                    normalHeading);

            Route route = mRouteRepository.createRouteModel(fieldId, Route.Type.COMPUTED);
            route.setRoutePoints(routePoints);
            result.add(route);
        }
        return result;
    }

    List<Point> computeNewPath(List<Point> route, double offset, double heading) {
        List<Point> result = new ArrayList<>(route.size());

        for (Point point : route) {
            Point newPoint = MapUtils.computeOffset(point, offset, heading);
            result.add(newPoint);
        }

        return result;
    }
}
