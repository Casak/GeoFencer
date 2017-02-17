package casak.ru.geofencer.domain.interactors.impl;

import java.util.LinkedList;
import java.util.List;

import casak.ru.geofencer.domain.Constants;
import casak.ru.geofencer.domain.executor.Executor;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.domain.interactors.BuildArrowModelsInteractor;
import casak.ru.geofencer.domain.interactors.base.AbstractInteractor;
import casak.ru.geofencer.domain.model.Arrow;
import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.model.Route;
import casak.ru.geofencer.domain.repository.ArrowRepository;
import casak.ru.geofencer.domain.repository.RouteRepository;


public class BuildArrowModelsInteractorImpl extends AbstractInteractor implements BuildArrowModelsInteractor {

    private BuildArrowModelsInteractor.Callback mCallback;
    private RouteRepository mRouteRepository;
    private ArrowRepository mArrowRepository;

    private Arrow leftArrow;
    private Arrow rightArrow;
    private int fieldId;

    public BuildArrowModelsInteractorImpl(Executor threadExecutor,
                                          MainThread mainThread,
                                          BuildArrowModelsInteractor.Callback callback,
                                          RouteRepository routeRepository,
                                          ArrowRepository arrowRepository,
                                          int fieldId) {
        super(threadExecutor, mainThread);
        this.fieldId = fieldId;
        mCallback = callback;
        mRouteRepository = routeRepository;
        mArrowRepository = arrowRepository;
    }

    @Override
    public void run() {
        Route route = mRouteRepository.getBaseRoute(fieldId);
        leftArrow = createArrow(route, true);
        rightArrow = createArrow(route, false);
        if (leftArrow != null && rightArrow != null) {
            mArrowRepository.add(leftArrow, fieldId);
            mArrowRepository.add(rightArrow, fieldId);
            mCallback.onArrowsBuildFinished(fieldId);
        } else mCallback.onArrowsBuildFailed(fieldId);
    }

    Arrow createArrow(Route route, boolean toLeft) {
        if (route == null)
            return null;

        List<Point> routePoints = route.getRoutePoints();
        if (routePoints != null && routePoints.size() > 1) {
            Point start = routePoints.get(0);
            Point end = routePoints.get(routePoints.size() - 1);

            double distanceBetween = MapUtils.computeDistanceBetween(start, end);
            double heading = MapUtils.computeHeading(start, end);
            Point routeCenter = MapUtils.computeOffset(start, distanceBetween / 2, heading);

            return toLeft ?
                    new Arrow(createArrowPoints(routeCenter, distanceBetween, heading, toLeft),
                            Arrow.Type.LEFT) :
                    new Arrow(createArrowPoints(routeCenter, distanceBetween, heading, toLeft),
                            Arrow.Type.RIGHT);
        }
        return null;
    }

    List<Point> createArrowPoints(Point routeCenter, double routeDistance, double routeHeading, boolean toLeft) {
        List<Point> result = new LinkedList<>();

        result.add(routeCenter);

        double headingArrowToTop;

        if (toLeft) {
            headingArrowToTop = routeHeading + Constants.HEADING_TO_LEFT;
        } else {
            headingArrowToTop = routeHeading + Constants.HEADING_TO_RIGHT;
        }

        double headingArrowToLeft = headingArrowToTop + 180 + 30;
        double headingArrowToRight = headingArrowToTop + 180 - 30;

        Point leftArrowTop = MapUtils.computeOffset(routeCenter, routeDistance / 2, headingArrowToTop);
        Point leftArrowFromTopToLeft = MapUtils.computeOffset(leftArrowTop, routeDistance / 4, headingArrowToLeft);
        Point leftArrowFromTopToRight = MapUtils.computeOffset(leftArrowTop, routeDistance / 4, headingArrowToRight);

        result.add(leftArrowTop);
        result.add(leftArrowFromTopToLeft);
        result.add(leftArrowTop);
        result.add(leftArrowFromTopToRight);
        result.add(leftArrowTop);

        return result;
    }

    public Arrow getLeftArrow() {
        return leftArrow;
    }

    public Arrow getRightArrow() {
        return rightArrow;
    }
}
