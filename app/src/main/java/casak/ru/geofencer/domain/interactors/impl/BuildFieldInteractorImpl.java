package casak.ru.geofencer.domain.interactors.impl;

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

    private BuildFieldInteractor.Callback mCallback;
    private RouteRepository mRouteRepository;
    private ArrowRepository mArrowRepository;
    private FieldModel mFieldModel;
    private int fieldId;

    public BuildFieldInteractorImpl(Executor threadExecutor, MainThread mainThread,
                                    BuildFieldInteractor.Callback callback, RouteRepository routeRepository,
                                    ArrowRepository arrowRepository, int fieldId) {
        super(threadExecutor, mainThread);
        this.fieldId = fieldId;
        mCallback = callback;
        mRouteRepository = routeRepository;
        mArrowRepository = arrowRepository;
    }

    @Override
    public void run() {
        RouteModel route = mRouteRepository.getRouteModel(fieldId, RouteModel.Type.FIELD_BUILDING);

        ArrowModel leftArrow = mArrowRepository.getArrow(ArrowModel.Type.LEFT);
        ArrowModel rightArrow = mArrowRepository.getArrow(ArrowModel.Type.RIGHT);

        if (!leftArrow.isChosen() && !rightArrow.isChosen()) {
            mCallback.onFieldBuildFail();
            return;
        }

        Point start = route.getRoutePoints().get(0);
        Point end = route.getRoutePoints().get(route.getRoutePoints().size() - 1);
        boolean toLeft = leftArrow.isChosen();

        mFieldModel = buildField(start, end, toLeft);
    }

    FieldModel buildField(Point start, Point end, boolean toLeft) {
        FieldModel field = new FieldModel(computeCorners(start,
                end,
                //TODO Implement different sizes
                Constants.WIDTH_METERS,
                toLeft));

        if (field.getPoints() != null)
            mCallback.onFieldBuildFinish();
        else
            mCallback.onFieldBuildFail();

        return field;
    }

    List<Point> computeCorners(Point start, Point end, double width, boolean toLeft) {
        if(start == null || end == null || width == 0.0)
            return null;
        double heading = MapUtils.computeHeading(start, end);
        double offset = width == 0 ? 0d : width / 2;

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

    public FieldModel getFieldModel() {
        return mFieldModel;
    }
}
