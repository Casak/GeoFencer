package com.smartagrodriver.core.domain.interactors.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import javax.inject.Inject;

import com.smartagrodriver.core.threading.Executor;
import com.smartagrodriver.core.threading.MainThread;
import com.smartagrodriver.core.domain.interactors.PointerInteractor;
import com.smartagrodriver.core.domain.interactors.base.AbstractInteractor;
import com.smartagrodriver.core.domain.model.Point;
import com.smartagrodriver.core.domain.model.Route;
import com.smartagrodriver.core.domain.repository.RouteRepository;

/**
 * Created on 16.02.2017.
 */

public class PointerInteractorImpl extends AbstractInteractor implements PointerInteractor {
    private static final String TAG = PointerInteractorImpl.class.getSimpleName();
    private static final int ROUTE_START = 0;
    private static final int ROUTE_NEXT_FROM_START = 1;

    private Route mCurrentRoute;
    private RouteRepository mRepository;
    private PointerInteractor.Callback mCallback;
    private WeakHashMap<Integer, List<Route>> mComputedRoutesCache;
    private int mFieldId;
    private double mMachineryWidth;
    private double mCurrentRouteBearing;
    private Point mNearestPoint;

    @Inject
    public PointerInteractorImpl(Executor threadExecutor,
                                 MainThread mainThread,
                                 RouteRepository routeRepository) {
        super(threadExecutor, mainThread);
        mRepository = routeRepository;
    }

    //TODO Pass machinery width
    @Override
    public void init(PointerInteractor.Callback callback, int fieldId, int machineryWidth) {
        mCallback = callback;
        mFieldId = fieldId;
        mMachineryWidth = machineryWidth;
        mComputedRoutesCache = new WeakHashMap<>();
        mComputedRoutesCache.put(fieldId, getComputedRoutes(mFieldId));
    }

    @Override
    public void run() {
        if (mCallback == null || mFieldId == 0 || mMachineryWidth == 0)
            throw new NullPointerException("PointerInteractor was not initialized!");
    }

    @Override
    public void onChange(Point point) {
        final double result = computePointerNew(point);
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.showPointer(result);
            }
        });
    }

    @Override
    public double getCurrentRouteBearing() {
        return mCurrentRouteBearing;
    }

    @Override
    public Point getNearestPoint() {
        return mNearestPoint;
    }

    @Override
    public long getCurrentRouteId() {
        return mCurrentRoute == null ? -1 : mCurrentRoute.getId();
    }

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
                result = crossTrackError(
                        nearestPoints.get(ROUTE_START),
                        nearestPoints.get(ROUTE_START),
                        pointPosition
                );
                break;
            case 2:
                result = crossTrackError(
                        nearestPoints.get(ROUTE_START),
                        nearestPoints.get(ROUTE_NEXT_FROM_START),
                        pointPosition
                );

        }
        return result;
    }

    //TODO Compute bearing of the route via heading, not distances
    Route getCurrentRoute(Point pointPosition) {
        if (!isStillCurrentRoute(pointPosition)) {
            Route result = getNearestRoute(pointPosition);
            if (result == null)
                return null;
            double distanceToStart = MapUtils.computeDistanceBetween(
                    pointPosition,
                    result.getRoutePoints().get(ROUTE_START)
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
        return mCurrentRoute;
    }

    boolean isStillCurrentRoute(Point location) {
        return mCurrentRoute != null &&
                MapUtils.isLocationOnPath(
                        location,
                        mCurrentRoute.getRoutePoints(),
                        true,
                        mMachineryWidth / 2
                );
    }

    //TODO Check it
    Route getNearestRoute(Point position) {
        if (position == null || position.getLatitude() == 0 || position.getLongitude() == 0)
            return null;

        List<Route> computedRoutes = getComputedRoutes(mFieldId);

        if (computedRoutes == null || computedRoutes.size() == 0) {
            return null;
        }

        for (Route model : computedRoutes) {
            if (MapUtils.isLocationOnPath(position, model.getRoutePoints(), true, mMachineryWidth / 2)) {
                return model;
            }
        }
        return null;
    }

    //TODO Check implementation
    List<Route> getComputedRoutes(Integer fieldId) {
        List<Route> result = mComputedRoutesCache.get(fieldId);
        if (result != null && result.size() != 0) {
            return result;
        }

        result = mRepository.getAll(fieldId);
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getType() != Route.Type.COMPUTED) {
                result.remove(i);
            }
        }

        mComputedRoutesCache.put(fieldId, result);
        return result;
    }

    void setCurrentRoute(Route route) {
        List<Point> points = route.getRoutePoints();
        mCurrentRouteBearing = MapUtils.computeHeading(points.get(0), points.get(points.size() - 1));

        mCurrentRoute = route;
    }

    //TODO Check usage
    Point getNearestPoint(List<Point> routePoints, Point current) {
        int routeSize = routePoints.size();

        if (routeSize == 1) {
            return routePoints.get(ROUTE_START);
        }

        if (routeSize == 2) {
            Point start = routePoints.get(ROUTE_START);
            Point end = routePoints.get(ROUTE_NEXT_FROM_START);
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
        return routePoints.get(ROUTE_START);
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

        mNearestPoint = getNearestPoint(routePoints, current);
        int indexNearest = routePoints.indexOf(mNearestPoint);
        int indexNext = indexNearest + 1;

        List<Point> result = new ArrayList<>();
        result.add(mNearestPoint);

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