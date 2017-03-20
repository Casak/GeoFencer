package casak.ru.geofencer.domain.interactors.impl;

import java.util.LinkedList;
import java.util.List;

import casak.ru.geofencer.threading.Executor;
import casak.ru.geofencer.threading.MainThread;
import casak.ru.geofencer.domain.interactors.BuildFieldInteractor;
import casak.ru.geofencer.domain.interactors.base.AbstractInteractor;
import casak.ru.geofencer.domain.model.Arrow;
import casak.ru.geofencer.domain.model.Field;
import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.model.Route;
import casak.ru.geofencer.domain.repository.FieldRepository;
import casak.ru.geofencer.domain.repository.RouteRepository;

public class BuildFieldInteractorImpl extends AbstractInteractor implements BuildFieldInteractor {
    private static final String TAG = BuildFieldInteractorImpl.class.getSimpleName();

    private static final double HEADING_TO_LEFT = -90;
    private static final double HEADING_TO_RIGHT = 90;
    public static final double FIELD_HEIGHT_METERS = 100;

    private BuildFieldInteractor.Callback mCallback;
    private RouteRepository mRouteRepository;
    private FieldRepository mFieldRepository;
    private Field mField;
    private Arrow mArrowChosen;
    private int machineryWidth;

    public BuildFieldInteractorImpl(Executor threadExecutor, MainThread mainThread,
                                    BuildFieldInteractor.Callback callback,
                                    RouteRepository routeRepository, FieldRepository fieldRepository,
                                    int machineryWidth) {
        super(threadExecutor, mainThread);

        mCallback = callback;
        mRouteRepository = routeRepository;
        mFieldRepository = fieldRepository;
        this.machineryWidth = machineryWidth;
    }

    @Override
    public void init(Field field, Arrow arrow) {
        mArrowChosen = arrow;
        mField = field;
    }

    @Override
    public void run() {
        if (mArrowChosen == null) {
            throw new NullPointerException(TAG + ": Can`t create field. Arrow isn`t set!");
        }

        populateFieldPoints();

        if (mField.getPoints() != null || mField.getPoints().isEmpty()) {
            mFieldRepository.updateField(mField);
            mCallback.onFieldBuildFinish(mField);
        } else {
            mCallback.onFieldBuildFail(mField);
        }
    }

    private void populateFieldPoints() {
        Route route = mRouteRepository.getBaseRoute(mField.getId());

        if (route == null || route.getRoutePoints().size() < 2) {
            throw new NullPointerException(TAG + ": Can`t create field, base route is empty!");
        }

        List<Point> points = route.getRoutePoints();
        Point start = points.get(0);
        Point end = points.get(points.size() - 1);
        boolean toLeft = mArrowChosen.getType() == Arrow.Type.LEFT;

        //TODO Possibly compute "bottom" and "top" edges as route
        mField.setPoints(computeCorners(start,
                end,
                machineryWidth,
                toLeft));
    }

    private List<Point> computeCorners(Point start, Point end, double width, boolean toLeft) {
        if (start == null || end == null) {
            return null;
        }

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

        Point cornerNorthWest = MapUtils.computeOffset(
                cornerSouthWest,
                FIELD_HEIGHT_METERS,
                heading + heading2
        );

        Point cornerSouthEast = MapUtils.computeOffset(end, offset, heading + heading1);

        Point cornerNorthEast = MapUtils.computeOffset(
                cornerSouthEast,
                FIELD_HEIGHT_METERS,
                heading + heading2
        );

        result.add(cornerNorthWest);
        result.add(cornerSouthWest);
        result.add(cornerSouthEast);
        result.add(cornerNorthEast);
        return result;
    }
}
