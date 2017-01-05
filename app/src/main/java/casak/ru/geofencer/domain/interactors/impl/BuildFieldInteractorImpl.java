package casak.ru.geofencer.domain.interactors.impl;

import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedList;
import java.util.List;

import casak.ru.geofencer.domain.Constants;
import casak.ru.geofencer.domain.executor.Executor;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.domain.interactors.BuildFieldInteractor;
import casak.ru.geofencer.domain.interactors.base.AbstractInteractor;
import casak.ru.geofencer.domain.model.ArrowModel;
import casak.ru.geofencer.domain.model.FieldModel;
import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.model.RouteModel;
import casak.ru.geofencer.domain.repository.ArrowRepository;
import casak.ru.geofencer.domain.repository.RouteRepository;

import static casak.ru.geofencer.domain.Constants.FIELD_HEIGHT_METERS;
import static casak.ru.geofencer.domain.Constants.HEADING_TO_RIGHT;
import static casak.ru.geofencer.domain.Constants.HEADING_TO_LEFT;

public class BuildFieldInteractorImpl extends AbstractInteractor implements BuildFieldInteractor {

    private Callback mCallback;
    private ArrowRepository mArrowRepository;
    private RouteRepository mRouteRepository;

    public BuildFieldInteractorImpl(Executor threadExecutor, MainThread mainThread,
                                    Callback callback, ArrowRepository arrowRepository,
                                    RouteRepository routeRepository) {
        super(threadExecutor, mainThread);
        mCallback = callback;
        mRouteRepository = routeRepository;
        mArrowRepository = arrowRepository;
    }

    @Override
    public void run() {
        FieldModel field = new FieldModel();
        RouteModel route = mRouteRepository.getRoute(RouteModel.Type.FIELD_BUILDING);
        Point start;
        Point end;
        ArrowModel leftArrow = mArrowRepository.getArrow(ArrowModel.Type.LEFT);
        ArrowModel rightArrow = mArrowRepository.getArrow(ArrowModel.Type.RIGHT);
        if (leftArrow == null && rightArrow == null)
            return;
        else {
            start = route.getRoutePoints().get(0);
            end = route.getRoutePoints().get(route.getRoutePoints().size() - 1);
        }

        if (leftArrow.isChosen()) {
            field = buildField(start, end, true);
        }
        if (rightArrow.isChosen()) {
            field = buildField(start, end, false);
        }

        mCallback.removeArrow(leftArrow);
        mCallback.removeArrow(rightArrow);
        if (field.getPoints() != null)
            mCallback.onFieldBuildFinish();
        else
            mCallback.onFieldBuildFail();
    }

    private FieldModel buildField(Point start, Point end, boolean toLeft) {
        return new FieldModel(createFieldPoints(start,
                end,
                Constants.WIDTH_METERS,
                toLeft));
    }

    private List<Point> createFieldPoints(Point start, Point end, double width, boolean toLeft) {
        double offset = width == 0 ? 0d : width / 2;
        return computeCorners(start, end, offset, toLeft);
    }

    private List<Point> computeCorners(Point start, Point end, double offset, boolean toLeft) {
        double heading = MapUtils.computeHeading(start, end);

        List<Point> result = new LinkedList<>();

        double heading1, heading2;
        if (toLeft) {
            heading1 = HEADING_TO_RIGHT;
            heading2 = HEADING_TO_LEFT;
        } else {
            heading1 = HEADING_TO_LEFT;
            heading2 = HEADING_TO_RIGHT;
        }

        Point cornerSouthWest = MapUtils.computeOffset(start, offset, heading + heading1);
        Point cornerNorthWest = MapUtils.computeOffset(cornerSouthWest, FIELD_HEIGHT_METERS, heading + heading2);
        Point cornerSouthEast = MapUtils.computeOffset(end, offset, heading + heading1);
        Point cornerNorthEast = MapUtils.computeOffset(cornerSouthEast, FIELD_HEIGHT_METERS, heading + heading2);

        result.add(cornerNorthWest);
        result.add(cornerSouthWest);
        result.add(cornerSouthEast);
        result.add(cornerNorthEast);
        return result;
    }
}
