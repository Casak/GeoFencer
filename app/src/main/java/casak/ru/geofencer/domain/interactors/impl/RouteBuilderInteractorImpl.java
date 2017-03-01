package casak.ru.geofencer.domain.interactors.impl;

import java.util.ArrayList;
import java.util.List;

import casak.ru.geofencer.domain.executor.Executor;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.domain.interactors.RouteBuilderInteractor;
import casak.ru.geofencer.domain.interactors.base.AbstractInteractor;
import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.model.Route;
import casak.ru.geofencer.domain.repository.RouteRepository;

/**
 * Created on 05.01.2017.
 */
//TODO Callbacks
public class RouteBuilderInteractorImpl extends AbstractInteractor implements RouteBuilderInteractor {
    private static final String TAG = RouteBuilderInteractorImpl.class.getSimpleName();

    public static final double HEADING_TO_LEFT = -90;
    public static final double HEADING_TO_RIGHT = 90;

    private RouteBuilderInteractor.Callback mCallback;
    private RouteRepository mRouteRepository;
    private Route mFieldBuildingRoute;
    private int fieldId;
    private int machineryWidth;
    private boolean isBuildingRoute;

    public RouteBuilderInteractorImpl(Executor threadExecutor,
                                      MainThread mainThread,
                                      RouteBuilderInteractor.Callback callback,
                                      RouteRepository routeRepository,
                                      int fieldId,
                                      int machineryWidth) {
        super(threadExecutor, mainThread);

        mCallback = callback;
        mRouteRepository = routeRepository;
        this.fieldId = fieldId;
        this.machineryWidth = machineryWidth;
    }

    @Override
    public void run() {
        mFieldBuildingRoute = mRouteRepository.create(fieldId, Route.Type.BASE);

        isBuildingRoute = true;
    }

    @Override
    public void onChange(Point point) {
        if (mFieldBuildingRoute != null && isBuildingRoute)
            mFieldBuildingRoute.addRoutePoint(point);
    }

    @Override
    public void finish() {
        isBuildingRoute = false;

        if (mFieldBuildingRoute == null || mFieldBuildingRoute.getRoutePoints().isEmpty()) {
            throw new NullPointerException(TAG + ": Can`t create route for building field!");
        }

        mRouteRepository.update(mFieldBuildingRoute);
        mCallback.routeBuildingFinished(mFieldBuildingRoute);
    }

    @Override
    public void createComputedRoutes(int fieldId, boolean toLeft) {
        if (mFieldBuildingRoute == null || mFieldBuildingRoute.getRoutePoints().isEmpty()) {
            mFieldBuildingRoute = mRouteRepository.getBaseRoute(fieldId);
        }

        List<Route> result = computeRouteModels(mFieldBuildingRoute, toLeft);
        if (result != null) {
            Route base = mRouteRepository.create(fieldId, Route.Type.COMPUTED);
            base.setRoutePoints(mFieldBuildingRoute.getRoutePoints());
            mRouteRepository.update(base);

            for (Route model : result) {
                mRouteRepository.update(model);
                mCallback.routeBuildingFinished(model);
            }
        }
    }

    private List<Route> computeRouteModels(Route fieldBuildingRoute, boolean toLeft) {
        if (fieldBuildingRoute == null || fieldBuildingRoute.getRoutePoints().size() < 2)
            return null;

        List<Route> result = new ArrayList<>();

        List<Point> fieldBuildingPoints = fieldBuildingRoute.getRoutePoints();
        Point start = fieldBuildingPoints.get(0);
        Point end = fieldBuildingPoints.get(fieldBuildingPoints.size() - 1);

        double arrowHeading = toLeft ? HEADING_TO_LEFT : HEADING_TO_RIGHT;

        double computedHeading = MapUtils.computeHeading(start, end);
        double normalHeading = computedHeading + arrowHeading;

        //TODO Check values
        int offset = 0;
        for (int i = 0; i < 4; i++) {
            List<Point> routePoints = computeNewPath(fieldBuildingPoints,
                    offset += machineryWidth,
                    normalHeading);

            Route route = mRouteRepository.create(fieldId, Route.Type.COMPUTED);
            route.setRoutePoints(routePoints);
            result.add(route);
        }
        return result;
    }

    private List<Point> computeNewPath(List<Point> route, double offset, double heading) {
        List<Point> result = new ArrayList<>(route.size());

        for (Point point : route) {
            Point newPoint = MapUtils.computeOffset(point, offset, heading);
            result.add(newPoint);
        }

        return result;
    }
}
