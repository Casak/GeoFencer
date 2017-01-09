package casak.ru.geofencer.domain.interactors.impl;

import java.util.LinkedList;
import java.util.List;

import casak.ru.geofencer.domain.Constants;
import casak.ru.geofencer.domain.executor.Executor;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.domain.interactors.BuildArrowsInteractor;
import casak.ru.geofencer.domain.interactors.base.AbstractInteractor;
import casak.ru.geofencer.domain.model.ArrowModel;
import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.model.RouteModel;
import casak.ru.geofencer.domain.repository.RouteRepository;


public class BuildArrowsInteractorImpl extends AbstractInteractor implements BuildArrowsInteractor {

    private BuildArrowsInteractor.Callback mCallback;
    private RouteRepository mRepository;

    private ArrowModel leftArrow;
    private ArrowModel rightArrow;

    public BuildArrowsInteractorImpl(Executor threadExecutor,
                                     MainThread mainThread,
                                     BuildArrowsInteractor.Callback callback,
                                     RouteRepository repository) {
        super(threadExecutor, mainThread);
        mCallback = callback;
        mRepository = repository;
    }

    @Override
    public void run() {
        RouteModel route = mRepository.getRoute(RouteModel.Type.FIELD_BUILDING);
        leftArrow = createArrow(route, true);
        rightArrow = createArrow(route, false);
        mCallback.onArrowsBuildFinished();
    }

    ArrowModel createArrow(RouteModel route, boolean toLeft) {
        List<Point> routePoints = route.getRoutePoints();
        if (routePoints != null && routePoints.size() > 1) {
            Point start = routePoints.get(0);
            Point end = routePoints.get(routePoints.size() - 1);

            double distanceBetween = MapUtils.computeDistanceBetween(start, end);
            double heading = MapUtils.computeHeading(start, end);
            Point routeCenter = MapUtils.computeOffset(start, distanceBetween / 2, heading);

            return toLeft ?
                    new ArrowModel(createArrowPoints(routeCenter, distanceBetween, heading, toLeft),
                            ArrowModel.Type.LEFT) :
                    new ArrowModel(createArrowPoints(routeCenter, distanceBetween, heading, toLeft),
                            ArrowModel.Type.RIGHT);
        }
        //TODO Normal error handling
        else
            mCallback.onArrowsBuildFailed();
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

    public ArrowModel getLeftArrow() {
        return leftArrow;
    }

    public ArrowModel getRightArrow() {
        return rightArrow;
    }
}
