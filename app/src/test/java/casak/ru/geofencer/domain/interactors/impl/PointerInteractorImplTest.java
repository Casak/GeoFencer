package casak.ru.geofencer.domain.interactors.impl;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import casak.ru.geofencer.domain.executor.Executor;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.domain.interactors.PointerInteractor;
import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.model.Route;
import casak.ru.geofencer.domain.repository.RouteRepository;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Created on 16.02.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class PointerInteractorImplTest {

    static final int FIELD_ID = 1;
    static final int WIDTH_METERS = 10;

    @Mock
    public Point mMockLocation;
    @Mock
    public Executor mMockExecutor;
    @Mock
    public MainThread mMockMainThread;
    @Mock
    public RouteRepository mMockRouteRepository;
    @Mock
    public Point mMockRealLocation;
    @Mock
    public PointerInteractor.Callback mMockCallback;

    public PointerInteractorImpl mInteractor;

    public static Route mRouteBase;
    public static List<Route> mRoutesList;
    public static List<Route> mComputedRoutesList;
    public static List<Point> mPointsList;
    public static List<Point> mBaseRoutePointsList;
    public static List<Point> mHarvestingRoute;

    public static Point mCurrentPoint = new Point(50.42, 30.42);
    public static Point mNearPoint = new Point(51.0d, 31.0d);
    public static Point mFarPoint = new Point(51.8d, 31.0d);

    @BeforeClass
    public static void setUpClass() {
        mRoutesList = new ArrayList<>();
        mPointsList = new ArrayList<>();
        mHarvestingRoute = new ArrayList<>();
        mBaseRoutePointsList = new ArrayList<>();

        fillLists();

        mRouteBase = new Route(0, 0, Route.Type.BASE, mBaseRoutePointsList);
        mComputedRoutesList = computeRouteModels(mRouteBase);

        double lat = 50.0d;
        double lng = 30.0d;
        mRoutesList.add(new Route(0, FIELD_ID, Route.Type.COMPUTED, mPointsList));
        for (int i = 0; i < 5; i++) {
            List<Point> points = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                points.add(new Point(lat + j, lng + i));
            }
            mRoutesList.add(new Route(i, FIELD_ID, Route.Type.COMPUTED, points));
        }
    }

    @Before
    public void setUp() {
        when(mMockRealLocation.getLatitude()).thenReturn(50.0d);
        when(mMockRealLocation.getLongitude()).thenReturn(30.0d);

        mInteractor = new PointerInteractorImpl(
                mMockExecutor,
                mMockMainThread,
                mMockRouteRepository
        );


        when(mMockRouteRepository.getAll(anyInt()))
                .thenReturn(mRoutesList);
        mInteractor.init(mMockCallback, FIELD_ID);
    }

    @Test
    public void computeMachineryWidth() {
        double result = mInteractor.computeMachineryWidth(
                mComputedRoutesList.get(0),
                mComputedRoutesList.get(1)
        );

        assertEquals(10, result, 0.01);
    }

    @Test(expected = NullPointerException.class)
    public void beforeRun_shouldBeInited() {
        mInteractor = new PointerInteractorImpl(
                mMockExecutor,
                mMockMainThread,
                mMockRouteRepository
        );

        mInteractor.run();
    }

    @Ignore
    @Test
    public void getNearestRoute_mockedRealDataAndNearLocation_nearestRouteModel() {
        Point firstComputedStart = mHarvestingRoute.get(0);
        Point firstComputedEnd = mHarvestingRoute.get(mHarvestingRoute.size() - 1);
        Point firstComputedMiddle = mHarvestingRoute.get(mHarvestingRoute.size() / 2);

        Point location = mock(Point.class);
        when(location.getLatitude())
                .thenReturn(firstComputedStart.getLatitude())
                .thenReturn(firstComputedStart.getLatitude())
                .thenReturn(firstComputedEnd.getLatitude())
                .thenReturn(firstComputedEnd.getLatitude())
                .thenReturn(firstComputedMiddle.getLatitude())
                .thenReturn(firstComputedMiddle.getLatitude())
                .thenReturn(mCurrentPoint.getLatitude())
                .thenReturn(mCurrentPoint.getLatitude());
        when(location.getLongitude())
                .thenReturn(firstComputedStart.getLongitude())
                .thenReturn(firstComputedStart.getLongitude())
                .thenReturn(firstComputedEnd.getLongitude())
                .thenReturn(firstComputedEnd.getLongitude())
                .thenReturn(firstComputedMiddle.getLongitude())
                .thenReturn(firstComputedMiddle.getLongitude())
                .thenReturn(mCurrentPoint.getLongitude())
                .thenReturn(mCurrentPoint.getLongitude());

        Route result = mInteractor.getNearestRoute(location);
        assertEquals(mComputedRoutesList.get(0), result);

        result = mInteractor.getNearestRoute(location);
        assertEquals(mComputedRoutesList.get(0), result);

        result = mInteractor.getNearestRoute(location);
        assertEquals(mComputedRoutesList.get(0), result);

        result = mInteractor.getNearestRoute(location);
        assertEquals(mComputedRoutesList.get(3), result);
    }

    @Test
    public void getNearestRoute_fromNullOrEmpty_returnsNull() {
        Route result = mInteractor.getNearestRoute(null);

        assertNull(result);

        result = mInteractor.getNearestRoute(mMockLocation);

        assertNull(result);
    }

    @Test
    public void getNearestRoute_fromRealLocation_obtainComputedRoutes() {
        PointerInteractorImpl mock = mock(PointerInteractorImpl.class);
        when(mock.getNearestRoute(any(Point.class))).thenCallRealMethod();

        Route result = mock.getNearestRoute(mMockRealLocation);

        verify(mock).getComputedRoutes(anyInt());
    }

    @Test
    public void getNearestRoute_withNullRoutePointsOrRouteSizeSmallerTwo_returnsNull() {
        Route result = mInteractor.getNearestRoute(new Point());

        assertNull(result);
    }

    @Test
    public void getNearestRoute_withoutRoutesInRepo_returnsNull() {
        PointerInteractorImpl mock = mock(PointerInteractorImpl.class);
        when(mock.getComputedRoutes(anyInt()))
                .thenReturn(null)
                .thenReturn(new ArrayList<Route>());

        Route result = mock.getNearestRoute(mMockRealLocation);

        assertNull(result);

        result = mock.getNearestRoute(mMockRealLocation);

        assertNull(result);
    }

    //TODO Double check
    @Test
    public void getNearestRoute_fromMockLocation_returnsNearestRouteModel() {
        when(mInteractor.getComputedRoutes(anyInt()))
                .thenReturn(mRoutesList);


        Route result = mInteractor.getNearestRoute(mPointsList.get(1));

        assertEquals(mRoutesList.get(0), result);
    }

    @Ignore
    @Test
    public void isStillCurrentRoute_fromLocationOnCurrentRoute_returnTrue() {
        Route model = mComputedRoutesList.get(0);
        when(mInteractor.getCurrentRoute(any(Point.class))).thenReturn(model);
        mInteractor.setCurrentRoute(model);

        boolean result = mInteractor.isStillCurrentRoute(model.getRoutePoints().get(0));

        assertTrue(result);
    }

    @Test
    public void isStillCurrentRoute_fromLocationNotOnRoute_returnFalse() {
        boolean result = mInteractor.isStillCurrentRoute(mCurrentPoint);

        assertFalse(result);
    }

    @Test
    public void getNearestPoint_fromOneRoutePointAndCurrentPoint_returnThisPoint() {
        List<Point> listWithPoint = new ArrayList<>();
        listWithPoint.add(mNearPoint);

        Point result = mInteractor.getNearestPoint(listWithPoint, mCurrentPoint);

        assertEquals(mNearPoint, result);
    }

    @Test
    public void getNearestPoint_fromTwoRoutePointAndCurrentPoint_returnNearestPoint() {
        List<Point> listWithPoint = new ArrayList<>();
        listWithPoint.add(mNearPoint);
        listWithPoint.add(mFarPoint);

        Point result = mInteractor.getNearestPoint(listWithPoint, mCurrentPoint);

        assertEquals(mNearPoint, result);
    }

    @Ignore
    @Test
    public void getNearestPoint_fromManyRoutePointAndCurrentPoint_returnNearestPoint() {
        List<Point> listWithPoints = new ArrayList<>(mPointsList);
        Point resultFromOddList = mInteractor.getNearestPoint(listWithPoints, mCurrentPoint);
        assertEquals(mNearPoint, resultFromOddList);

        listWithPoints.remove(5);
        Point resultFromEvenList = mInteractor.getNearestPoint(listWithPoints, mCurrentPoint);
        assertEquals(mNearPoint, resultFromEvenList);

        Point resultWithFarPoint = mInteractor.getNearestPoint(listWithPoints, mFarPoint);
        assertEquals(mFarPoint, resultWithFarPoint);

        Point point = new Point(49d, 32d);
        for (Route model : mRoutesList) {
            List<Point> points = model.getRoutePoints();
            Point result = mInteractor.getNearestPoint(points, point);

            assertEquals(points.get(0), result);
        }

        List<Point> computedPoints = mComputedRoutesList.get(0).getRoutePoints();
        Point resultFromRealData = mInteractor.getNearestPoint(
                computedPoints,
                mHarvestingRoute.get(mHarvestingRoute.size() - 1));

        assertEquals(computedPoints.get(computedPoints.size() - 1), resultFromRealData);

    }

    @Test
    public void getNearAndNextPoints_FromEmptyRoute_returnEmptyList() {
        List<Point> result = mInteractor.getNearAndNextPoints(new ArrayList<Point>(), mNearPoint);

        assertTrue(result.isEmpty());
    }

    @Test
    public void getNearAndNextPoints_FromOnePointRoute_returnThisPoint() {
        List<Point> onePointList = new ArrayList<>();
        onePointList.add(mNearPoint);
        List<Point> result = mInteractor.getNearAndNextPoints(onePointList, mNearPoint);

        assertNotNull(result);

        assertEquals(mNearPoint, result.get(0));
    }

    @Test
    public void getNearAndNextPoints_FromRouteAndPointAtStartOfTheRoute_returnThisPointAndTheNextOne() {
        List<Point> result = mInteractor.getNearAndNextPoints(mPointsList, mNearPoint);

        assertNotNull(result);

        assertEquals(mNearPoint, result.get(0));
        assertEquals(mPointsList.get(mPointsList.indexOf(mNearPoint) + 1), result.get(1));

        List<Point> computedRoutePoints = mComputedRoutesList.get(0).getRoutePoints();
        result = mInteractor.getNearAndNextPoints(
                computedRoutePoints,
                mHarvestingRoute.get(0));

        assertEquals(computedRoutePoints.get(1), result.get(0));
        assertEquals(computedRoutePoints.get(2), result.get(1));
    }

    @Ignore
    @Test
    public void getNearAndNextPoints_FromRouteAndPointAtEndOfTheRoute_returnThisPoint() {
        List<Point> result = mInteractor.getNearAndNextPoints(mPointsList, mFarPoint);

        assertNotNull(result);

        assertEquals(mFarPoint, result.get(0));

        List<Point> computedRoutePoints = mComputedRoutesList.get(0).getRoutePoints();
        result = mInteractor.getNearAndNextPoints(
                computedRoutePoints,
                mHarvestingRoute.get(mHarvestingRoute.size() - 1));

        assertEquals(computedRoutePoints.get(computedRoutePoints.size() - 1), result.get(0));
    }

    @Test
    public void getNearAndNextPoints_FromRouteAndPointAtCenterOfTheRoute_returnThisPointAndTheNextOne() {
        List<Point> result = mInteractor.getNearAndNextPoints(mPointsList, mPointsList.get(5));

        assertEquals(mPointsList.get(5), result.get(0));
        assertEquals(mPointsList.get(6), result.get(1));

        List<Point> computedRoutePoints = mComputedRoutesList.get(0).getRoutePoints();
        result = mInteractor.getNearAndNextPoints(
                computedRoutePoints,
                mHarvestingRoute.get(mHarvestingRoute.size() / 2));

        Point expected1 = computedRoutePoints.get(computedRoutePoints.size() / 2 + 1);
        Point expected2 = computedRoutePoints.get(computedRoutePoints.size() / 2 + 2);

        assertEquals(expected1, result.get(0));
        assertEquals(expected2, result.get(1));
    }

    private static List<Route> computeRouteModels(Route fieldBuildingRoute) {
        //TODO Normal check
        if (fieldBuildingRoute == null)
            return null;

        List<Route> result = new ArrayList<>();
        result.add(fieldBuildingRoute);

        List<Point> fieldBuildingPoints = fieldBuildingRoute.getRoutePoints();
        Point start = fieldBuildingPoints.get(0);
        Point end = fieldBuildingPoints.get(fieldBuildingPoints.size() - 1);

        double arrowHeading = -90;

        double computedHeading = MapUtils.computeHeading(start, end);
        double normalHeading = computedHeading + arrowHeading;

        for (int i = 0; i < 4; i++) {
            List<Point> routePoints = computeNewPath(result.get(i).getRoutePoints(),
                    WIDTH_METERS,
                    normalHeading);

            Route route = new Route(i + 1,
                    0, Route.Type.COMPUTED,
                    routePoints);

            result.add(route);
        }
        result.remove(0);
        return result;
    }

    private static List<Point> computeNewPath(List<Point> route, double width, double heading) {
        List<Point> result = new ArrayList<>(route.size());

        for (Point point : route) {
            Point newPoint = MapUtils.computeOffset(point, width, heading);
            result.add(newPoint);
        }

        return result;
    }

    private static void fillLists() {
        mHarvestingRoute.add(new Point(50.421403047796304, 30.425471959874532));
        mHarvestingRoute.add(new Point(50.421492947796295, 30.425563359499005));
        mHarvestingRoute.add(new Point(50.4215796477963, 30.425650459136847));
        mHarvestingRoute.add(new Point(50.4216630477963, 30.42568115878848));
        mHarvestingRoute.add(new Point(50.4217475477963, 30.42573765843551));
        mHarvestingRoute.add(new Point(50.42183264779631, 30.42579085808003));
        mHarvestingRoute.add(new Point(50.421915947796315, 30.425823957732078));
        mHarvestingRoute.add(new Point(50.42199704779629, 30.4258847573933));
        mHarvestingRoute.add(new Point(50.422077147796294, 30.425945157058703));
        mHarvestingRoute.add(new Point(50.42215504779629, 30.42599855673329));
        mHarvestingRoute.add(new Point(50.42242494779629, 30.42616205560584));
        mHarvestingRoute.add(new Point(50.422517047796305, 30.426176555221105));
        mHarvestingRoute.add(new Point(50.4226024477963, 30.42622365486436));
        mHarvestingRoute.add(new Point(50.422682847796295, 30.42625355452849));
        mHarvestingRoute.add(new Point(50.4227546477963, 30.426359654228563));
        mHarvestingRoute.add(new Point(50.4228218477963, 30.426372253947832));
        mHarvestingRoute.add(new Point(50.4228876477963, 30.426372153672965));
        mHarvestingRoute.add(new Point(50.422950947796274, 30.426433853408525));
        mHarvestingRoute.add(new Point(50.423012447796296, 30.42652715315161));
        mHarvestingRoute.add(new Point(50.42306504779629, 30.426528752931883));
        mHarvestingRoute.add(new Point(50.42311024779629, 30.426541052743065));
        mHarvestingRoute.add(new Point(50.42315084779628, 30.426602752573455));
        mHarvestingRoute.add(new Point(50.423222247796296, 30.42662315227518));
        mHarvestingRoute.add(new Point(50.42324924779629, 30.42659715216239));
        mHarvestingRoute.add(new Point(50.42326574779629, 30.42663665209346));
        mHarvestingRoute.add(new Point(50.42327484779629, 30.42663695205544));
        mHarvestingRoute.add(new Point(50.423281180329916, 30.426680696850383));

        mPointsList.add(mNearPoint);
        mPointsList.add(new Point(51.1d, 31.0d));
        mPointsList.add(new Point(51.2d, 31.0d));
        mPointsList.add(new Point(51.3d, 31.0d));
        mPointsList.add(new Point(51.4d, 31.0d));
        mPointsList.add(new Point(51.5d, 31.0d));
        mPointsList.add(new Point(51.6d, 31.0d));
        mPointsList.add(new Point(51.7d, 31.0d));
        mPointsList.add(mFarPoint);

        mBaseRoutePointsList.add(new Point(50.421355, 30.4256428));
        mBaseRoutePointsList.add(new Point(50.4214449, 30.4256972));
        mBaseRoutePointsList.add(new Point(50.4215316, 30.4257533));
        mBaseRoutePointsList.add(new Point(50.421615, 30.425807));
        mBaseRoutePointsList.add(new Point(50.4216995, 30.4258595));
        mBaseRoutePointsList.add(new Point(50.4217846, 30.4259127));
        mBaseRoutePointsList.add(new Point(50.4218679, 30.4259648));
        mBaseRoutePointsList.add(new Point(50.421949, 30.4260166));
        mBaseRoutePointsList.add(new Point(50.4220291, 30.426066));
        mBaseRoutePointsList.add(new Point(50.422107, 30.4261144));
        mBaseRoutePointsList.add(new Point(50.4223769, 30.4262649));
        mBaseRoutePointsList.add(new Point(50.422469, 30.4263184));
        mBaseRoutePointsList.add(new Point(50.4225544, 30.4263715));
        mBaseRoutePointsList.add(new Point(50.4226348, 30.4264244));
        mBaseRoutePointsList.add(new Point(50.4227066, 30.4264715));
        mBaseRoutePointsList.add(new Point(50.4227738, 30.4265161));
        mBaseRoutePointsList.add(new Point(50.4228396, 30.426558));
        mBaseRoutePointsList.add(new Point(50.4229029, 30.4265987));
        mBaseRoutePointsList.add(new Point(50.4229644, 30.426637));
        mBaseRoutePointsList.add(new Point(50.423017, 30.4266696));
        mBaseRoutePointsList.add(new Point(50.4230622, 30.4266969));
        mBaseRoutePointsList.add(new Point(50.4231028, 30.4267206));
        mBaseRoutePointsList.add(new Point(50.4231742, 30.42676));
        mBaseRoutePointsList.add(new Point(50.4232012, 30.426774));
        mBaseRoutePointsList.add(new Point(50.4232177, 30.4267845));
        mBaseRoutePointsList.add(new Point(50.4232268, 30.4267898));
        mBaseRoutePointsList.add(new Point(50.42323, 30.4267916));
    }
}