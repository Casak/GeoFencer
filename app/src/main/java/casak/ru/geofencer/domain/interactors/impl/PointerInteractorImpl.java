package casak.ru.geofencer.domain.interactors.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import casak.ru.geofencer.domain.executor.Executor;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.domain.interactors.PointerInteractor;
import casak.ru.geofencer.domain.interactors.base.AbstractInteractor;
import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.model.Route;
import casak.ru.geofencer.domain.repository.RouteRepository;

/**
 * Created on 16.02.2017.
 */

public class PointerInteractorImpl extends AbstractInteractor implements PointerInteractor {

    private Route currentRoute;
    private RouteRepository repository;
    private PointerInteractor.Callback callback;
    private int fieldId;
    private int width;

    @Inject
    public PointerInteractorImpl(Executor threadExecutor,
                                 MainThread mainThread,
                                 RouteRepository routeRepository,
                                 PointerInteractor.Callback pointerCallback) {
        super(threadExecutor, mainThread);
        repository = routeRepository;
        callback = pointerCallback;
    }

    @Override
    public void setFieldId(int id) {
        fieldId = id;
    }

    @Override
    public void setWidth(int meters) {
        width = meters;
    }

    @Override
    public void run() {

    }

    @Override
    public void onPositionChanged(Point point) {
        double result = computePointerNew(point);
        callback.showPointer(result);
    }

    //TODO Probably move to presenter or somewhere else at presentation layer
   /* private Circle currentDot;
    private Circle nextDot;

    public void updatePointerVisualization(long routeId, Point current, Point next) {
        if (currentRouteId != routeId) {
            currentRouteId = routeId;
            for (Polyline polyline : notHarvestedRoutes) {
                int id = -1;
                try {
                    id = Integer.parseInt(polyline.getId());
                } catch (NumberFormatException e) {
                    Log.d(TAG, "Invalid polyline. It is not computed one");
                }
                if (polyline.getColor() == Color.RED &&
                        id != currentRouteId)
                    polyline.setColor(Color.BLUE);
            }
            notHarvestedRoutes.get((int)currentRouteId).setColor(Color.RED);
        }

        CircleOptions currentOptions = new CircleOptions()
                .center(new LatLng(current.getLatitude(), current.getLongitude()))
                .radius(0.5)
                .fillColor(Color.GRAY);
        CircleOptions nextOptions = new CircleOptions()
                .center(new LatLng(next.getLatitude(), next.getLongitude()))
                .radius(0.5)
                .fillColor(Color.DKGRAY);

        if (currentDot != null && nextDot != null) {
            removeCircle(currentDot);
            removeCircle(nextDot);
            currentDot = null;
            nextDot = null;
        }

        if (currentDot == null && nextDot == null) {
            currentDot = showCircle(currentOptions);
            nextDot = showCircle(nextOptions);
        }
    }*/

    double computePointerNew(Point pointPosition) {
        Route nearestRoute = getCurrentRoute(pointPosition);

        if (nearestRoute == null)
            return 0;

        int index = nearestRoute.getRoutePoints().indexOf(getNearestPoint(nearestRoute.getRoutePoints(), pointPosition));

        nearestRoute.setRoutePoints(nearestRoute.getRoutePoints().subList(index, nearestRoute.getRoutePoints().size()));

        List<Point> nearestPoints = getNearAndNextPoints(nearestRoute.getRoutePoints(), pointPosition);

        int nearestPointsSize = nearestPoints.size();

        double result = 0;
        switch (nearestPointsSize) {
            case 0:
                break;
            case 1:
                result = crossTrackError(nearestPoints.get(0), nearestPoints.get(0), pointPosition);

                break;
            case 2:
                result = crossTrackError(nearestPoints.get(0), nearestPoints.get(1), pointPosition);

        }
        return result;
    }

    Route getCurrentRoute(Point pointPosition) {
        if (!isStillCurrentRoute(pointPosition)) {
            Route result = getNearestRoute(pointPosition);
            if (result == null)
                return null;
            double distanceToStart = MapUtils.computeDistanceBetween(
                    pointPosition,
                    result.getRoutePoints().get(0)
            );
            double distanceToEnd = MapUtils.computeDistanceBetween(
                    pointPosition,
                    result.getRoutePoints().get(result.getRoutePoints().size() - 1)
            );
            if (distanceToEnd < distanceToStart)
                result.setRoutePoints(reverseList(result.getRoutePoints()));
            setCurrentRoute(result);
            return result;
        }
        return currentRoute;
    }

    boolean isStillCurrentRoute(Point location) {
        return currentRoute != null &&
                MapUtils.isLocationOnPath(
                        location,
                        currentRoute.getRoutePoints(),
                        true,
                        width / 2
                );
    }

    //TODO Check it
    Route getNearestRoute(Point position) {
        if (position == null || position.getLatitude() == 0 || position.getLongitude() == 0)
            return null;

        List<Route> computedRoutes = getComputedRoutes(fieldId);

        if (computedRoutes == null || computedRoutes.size() == 0)
            return null;

        for (Route model : computedRoutes) {
            if (MapUtils.isLocationOnPath(position, model.getRoutePoints(), true, width / 2)) {
                return model;
            }
        }
        return null;
    }

    //TODO Check implementation
    List<Route> getComputedRoutes(int fieldId) {
        List<Route> result = repository.getAllRoutes(fieldId);
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getType() != Route.Type.COMPUTED) {
                result.remove(i);
            }
        }
        return result;
    }

    void setCurrentRoute(Route route) {
        currentRoute = route;
    }

    Point getNearestPoint(List<Point> routePoints, Point current) {
        int routeSize = routePoints.size();

        if (routeSize == 1) {
            return routePoints.get(0);
        }

        if (routeSize == 2) {
            Point start = routePoints.get(0);
            Point end = routePoints.get(1);
            return getNearestPoint(start, end, current);
        }

        if (routeSize > 2)
            for (int i = 0; i < routePoints.size() - 1; i++) {
                Point start = routePoints.get(i);
                Point next = routePoints.get(i + 1);
                if (getNearestPoint(start, next, current) == start) {
                    double distanceCurrentAndNext = MapUtils.computeDistanceBetween(start, next);
                    double distancePositionAndNext = MapUtils.computeDistanceBetween(current, next);
                    double distancePositionAndCurrent = MapUtils.computeDistanceBetween(current, start);
                    double delta = distancePositionAndNext - (distanceCurrentAndNext + distancePositionAndCurrent);
                    if (delta < 0)
                        return next;
                    return start;
                }
            }
        return routePoints.get(0);
    }

    Point getNearestPoint(Point start, Point end, Point current) {
        double distanceToStart = MapUtils.computeDistanceBetween(current, start);
        double distanceToEnd = MapUtils.computeDistanceBetween(current, end);

        return distanceToStart < distanceToEnd ? start : end;
    }

    //TODO Test list`s top bound; possibly move logic into computePointer
    List<Point> getNearAndNextPoints(List<Point> routePoints, Point current) {
        int routeSize = routePoints.size();
        if (routeSize == 0 || routeSize == 1)
            return routePoints;

        Point nearestPoint = getNearestPoint(routePoints, current);
        int indexNearest = routePoints.indexOf(nearestPoint);
        int indexNext = indexNearest + 1;

        List<Point> result = new ArrayList<>();
        result.add(nearestPoint);

        if (indexNext != routeSize)
            result.add(routePoints.get(indexNext));
        return result;
    }

    double crossTrackError(Point routeCurrent, Point routeNext, Point position) {
        double brng13 = MapUtils.computeHeading(routeCurrent, position); // in degrees
        double brng12 = MapUtils.computeHeading(routeCurrent, routeNext); //  in degrees
        double dist13 = MapUtils.computeDistanceBetween(routeCurrent, position); // in kilometers
        return Math.asin(Math.sin(dist13 / 6371009.0D) * Math.sin(Math.toRadians(brng13 - brng12))) * 6371009.0D;
    }

    private List<Point> reverseList(List<Point> list) {
        List<Point> result = new ArrayList<>();
        for (int i = list.size() - 1; i >= 0; i--)
            result.add(list.get(i));

        return result;
    }
}