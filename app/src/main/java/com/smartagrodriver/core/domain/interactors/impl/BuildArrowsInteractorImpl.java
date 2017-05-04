package com.smartagrodriver.core.domain.interactors.impl;

import java.util.LinkedList;
import java.util.List;

import com.smartagrodriver.core.threading.Executor;
import com.smartagrodriver.core.threading.MainThread;
import com.smartagrodriver.core.domain.interactors.BuildArrowsInteractor;
import com.smartagrodriver.core.domain.interactors.CreateFieldInteractor;
import com.smartagrodriver.core.domain.interactors.base.AbstractInteractor;
import com.smartagrodriver.core.domain.model.Arrow;
import com.smartagrodriver.core.domain.model.Point;
import com.smartagrodriver.core.domain.model.Route;
import com.smartagrodriver.core.domain.repository.ArrowRepository;
import com.smartagrodriver.core.domain.repository.RouteRepository;


public class BuildArrowsInteractorImpl extends AbstractInteractor implements BuildArrowsInteractor {
    private static final String TAG = BuildArrowsInteractorImpl.class.getSimpleName();

    private static final boolean LEFT = true;
    private static final boolean RIGHT = false;
    public static final double HEADING_TO_LEFT = -90;
    public static final double HEADING_TO_RIGHT = 90;

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
            headingArrowToTop = routeHeading + HEADING_TO_LEFT;
        } else {
            headingArrowToTop = routeHeading + HEADING_TO_RIGHT;
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
