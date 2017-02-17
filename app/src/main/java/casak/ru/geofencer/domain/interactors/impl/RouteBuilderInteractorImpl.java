package casak.ru.geofencer.domain.interactors.impl;

import android.util.Log;

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
import casak.ru.geofencer.domain.repository.LocationRepository;
import casak.ru.geofencer.domain.repository.RouteRepository;

/**
 * Created on 05.01.2017.
 */
public class RouteBuilderInteractorImpl extends AbstractInteractor implements RouteBuilderInteractor {

    private static final String TAG = RouteBuilderInteractorImpl.class.getSimpleName();

    private static Thread routeDaemon;

    private static Route mFieldBuildingRoute;

    private boolean isBuildingRoute;

    private RouteBuilderInteractor.Callback mCallback;
    private LocationRepository mLocationRepository;
    private RouteRepository mRouteRepository;
    private ArrowRepository mArrowRepository;
    private int fieldId;
    private int machineryWidth;

    public RouteBuilderInteractorImpl(Executor threadExecutor, MainThread mainThread,
                                      RouteBuilderInteractor.Callback callback, LocationRepository locationRepository,
                                      RouteRepository routeRepository, ArrowRepository arrowRepository,
                                      int fieldId, int machineryWidth) {
        super(threadExecutor, mainThread);
        this.fieldId = fieldId;
        this.machineryWidth = machineryWidth;

        mCallback = callback;
        mLocationRepository = locationRepository;
        mRouteRepository = routeRepository;
        mArrowRepository = arrowRepository;
    }

    @Override
    public void run() {
        mFieldBuildingRoute = mRouteRepository.createRouteModel(fieldId, Route.Type.BASE);

        isBuildingRoute = true;

        routeDaemon = new LocationChangeListenerDaemon();
        routeDaemon.setDaemon(true);
        //TODO Move to resources; recreate thread if down
        routeDaemon.setName("LocationChangeListenerDaemon");
        routeDaemon.start();
    }

    @Override
    public void finish() {
        if (routeDaemon != null && routeDaemon.isAlive())
            ((LocationChangeListenerDaemon) routeDaemon).cancel();
        isBuildingRoute = false;
        mRouteRepository.addRouteModel(mFieldBuildingRoute);
    }

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

    //TODO Pass to computeNewPath() normal values, now returns similar routes
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

        for (int i = 0; i < 4; i++) {
            List<Point> routePoints = computeNewPath(fieldBuildingPoints,
                    machineryWidth,
                    normalHeading);

            //TODO Create via repo
            Route route = new Route(createRouteModelId(),
                    fieldId, Route.Type.COMPUTED,
                    routePoints);

            result.add(route);
        }
        return result;
    }

    //TODO Move to repository
    int routeId = 0;

    private int createRouteModelId() {
        return routeId++;
    }

    List<Point> computeNewPath(List<Point> route, double width, double heading) {
        List<Point> result = new ArrayList<>(route.size());

        for (Point point : route) {
            Point newPoint = MapUtils.computeOffset(point, width, heading);
            result.add(newPoint);
        }

        return result;
    }

    private void newPoint(Point point) {
        if (mFieldBuildingRoute != null && isBuildingRoute)
            mFieldBuildingRoute.addRoutePoint(point);
    }

    private class LocationChangeListenerDaemon extends Thread {
        private volatile boolean running = true;
        Point currentPosition;

        @Override
        public void run() {
            while (running) {
                Point retrievedPosition = mLocationRepository.getLastLocation();
                currentPosition = acceptPoint(retrievedPosition);
                if (currentPosition != null && !retrievedPosition.equals(currentPosition))
                    newPoint(currentPosition);
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    Log.e(TAG, "from LocationChangeListenerDaemon:\n " + "Interrupted while sleeping", e);
                }
            }
        }

        public void cancel() {
            running = false;
        }

        //TODO Implement this shit, dude
        private Point acceptPoint(Point newPoint) {
            return newPoint == currentPosition ? currentPosition : newPoint;
        }
    }
}
