package casak.ru.geofencer.domain.interactors.impl;

import java.util.LinkedList;
import java.util.List;

import casak.ru.geofencer.domain.Constants;
import casak.ru.geofencer.domain.executor.Executor;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.domain.interactors.BuildArrowsInteractor;
import casak.ru.geofencer.domain.interactors.CreateFieldInteractor;
import casak.ru.geofencer.domain.interactors.base.AbstractInteractor;
import casak.ru.geofencer.domain.model.Arrow;
import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.model.Route;
import casak.ru.geofencer.domain.repository.ArrowRepository;
import casak.ru.geofencer.domain.repository.RouteRepository;


public class BuildArrowsInteractorImpl extends AbstractInteractor implements BuildArrowsInteractor {
    private static final String TAG = BuildArrowsInteractorImpl.class.getSimpleName();

    private static final boolean LEFT = true;
    private static final boolean RIGHT = false;

    private CreateFieldInteractor.Callback mCallback;
    private RouteRepository mRouteRepository;
    private ArrowRepository mArrowRepository;
    private Route mBaseRoute;
    private Arrow mLeftArrow;
    private Arrow mRightArrow;
    private int mFieldId;

    public BuildArrowsInteractorImpl(Executor threadExecutor,
                                     MainThread mainThread,
                                     CreateFieldInteractor.Callback callback,
                                     RouteRepository routeRepository,
                                     ArrowRepository arrowRepository) {
        super(threadExecutor, mainThread);

        mCallback = callback;
        mRouteRepository = routeRepository;
        mArrowRepository = arrowRepository;
    }

    @Override
    public void init(Route route, int fieldId) {
        mBaseRoute = route;
        this.mFieldId = fieldId;
    }

    @Override
    public void run() {
        if (mBaseRoute == null) {
            mBaseRoute = mRouteRepository.getBaseRoute(mFieldId);
        }

        mLeftArrow = createArrow(LEFT);
        mRightArrow = createArrow(RIGHT);

        if (mLeftArrow != null && mRightArrow != null) {
            mArrowRepository.add(mLeftArrow, mFieldId);
            mArrowRepository.add(mRightArrow, mFieldId);

            mMainThread.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.showArrow(mArrowRepository.getLeft(mFieldId));
                    mCallback.showArrow(mArrowRepository.getRight(mFieldId));
                }
            });
        } else {
            throw new NullPointerException(TAG + ": Can`t create arrows!");
        }
    }

    @Override
    public void onArrowClick(Arrow model) {
        if (model == null) {
            throw new NullPointerException(TAG + ".onArrowClick(): Arrow is null");
        }

        model.setChosen(true);

        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.hideArrow(mLeftArrow);
                mCallback.hideArrow(mRightArrow);
            }
        });

        mArrowRepository.delete(mFieldId);
    }

    Arrow createArrow(boolean toLeft) {
        if (mBaseRoute == null)
            return null;

        List<Point> routePoints = mBaseRoute.getRoutePoints();
        if (routePoints != null && routePoints.size() > 1) {
            Point start = routePoints.get(0);
            Point end = routePoints.get(routePoints.size() - 1);

            double distanceBetween = MapUtils.computeDistanceBetween(start, end);
            double heading = MapUtils.computeHeading(start, end);
            Point routeCenter = MapUtils.computeOffset(start, distanceBetween / 2, heading);

            return toLeft ?
                    new Arrow(createArrowPoints(routeCenter, distanceBetween, heading, true),
                            Arrow.Type.LEFT) :
                    new Arrow(createArrowPoints(routeCenter, distanceBetween, heading, false),
                            Arrow.Type.RIGHT);
        }
        return null;
    }

    private List<Point> createArrowPoints(Point routeCenter, double routeDistance, double routeHeading, boolean toLeft) {
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
}
