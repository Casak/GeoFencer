package com.smartagrodriver.core.presentation.presenters.impl;


import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created on 13.01.2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class MapPresenterTest {
//TODO Enable
/*
    static final int FIELD_ID = 1;

    @Mock
    public Location mMockLocation;
    @Mock
    public MainThread mMockMainThread;
    @Mock
    public MapPresenter mMapPresenter;
    @Mock
    public RouteRepository mMockRouteRepository;
    @Mock
    public static Location mMockRealLocation;

    public static Route fieldBuildingRoute;
    public static List<Route> routes;
    public static List<Route> computedRoutes;
    public static List<Point> listWithPoint;
    public static List<Point> routeBuildingRoutePoints;
    public static List<Point> harvestingRoute;

    public static Point currentPoint = new Point(50.42, 30.42);
    public static Point nearPoint = new Point(51.0d, 31.0d);
    public static Point farPoint = new Point(51.8d, 31.0d);

    @BeforeClass
    public static void setUpClass() {
        routes = new ArrayList<>();
        listWithPoint = new ArrayList<>();
        harvestingRoute = new ArrayList<>();
        routeBuildingRoutePoints = new ArrayList<>();

        harvestingRoute.add(new Point(50.421403047796304, 30.425471959874532));
        harvestingRoute.add(new Point(50.421492947796295, 30.425563359499005));
        harvestingRoute.add(new Point(50.4215796477963, 30.425650459136847));
        harvestingRoute.add(new Point(50.4216630477963, 30.42568115878848));
        harvestingRoute.add(new Point(50.4217475477963, 30.42573765843551));
        harvestingRoute.add(new Point(50.42183264779631, 30.42579085808003));
        harvestingRoute.add(new Point(50.421915947796315, 30.425823957732078));
        harvestingRoute.add(new Point(50.42199704779629, 30.4258847573933));
        harvestingRoute.add(new Point(50.422077147796294, 30.425945157058703));
        harvestingRoute.add(new Point(50.42215504779629, 30.42599855673329));
        harvestingRoute.add(new Point(50.42242494779629, 30.42616205560584));
        harvestingRoute.add(new Point(50.422517047796305, 30.426176555221105));
        harvestingRoute.add(new Point(50.4226024477963, 30.42622365486436));
        harvestingRoute.add(new Point(50.422682847796295, 30.42625355452849));
        harvestingRoute.add(new Point(50.4227546477963, 30.426359654228563));
        harvestingRoute.add(new Point(50.4228218477963, 30.426372253947832));
        harvestingRoute.add(new Point(50.4228876477963, 30.426372153672965));
        harvestingRoute.add(new Point(50.422950947796274, 30.426433853408525));
        harvestingRoute.add(new Point(50.423012447796296, 30.42652715315161));
        harvestingRoute.add(new Point(50.42306504779629, 30.426528752931883));
        harvestingRoute.add(new Point(50.42311024779629, 30.426541052743065));
        harvestingRoute.add(new Point(50.42315084779628, 30.426602752573455));
        harvestingRoute.add(new Point(50.423222247796296, 30.42662315227518));
        harvestingRoute.add(new Point(50.42324924779629, 30.42659715216239));
        harvestingRoute.add(new Point(50.42326574779629, 30.42663665209346));
        harvestingRoute.add(new Point(50.42327484779629, 30.42663695205544));
        harvestingRoute.add(new Point(50.423281180329916, 30.426680696850383));

        listWithPoint.add(nearPoint);
        listWithPoint.add(new Point(51.1d, 31.0d));
        listWithPoint.add(new Point(51.2d, 31.0d));
        listWithPoint.add(new Point(51.3d, 31.0d));
        listWithPoint.add(new Point(51.4d, 31.0d));
        listWithPoint.add(new Point(51.5d, 31.0d));
        listWithPoint.add(new Point(51.6d, 31.0d));
        listWithPoint.add(new Point(51.7d, 31.0d));
        listWithPoint.add(farPoint);

        routeBuildingRoutePoints.add(new Point(50.421355, 30.4256428));
        routeBuildingRoutePoints.add(new Point(50.4214449, 30.4256972));
        routeBuildingRoutePoints.add(new Point(50.4215316, 30.4257533));
        routeBuildingRoutePoints.add(new Point(50.421615, 30.425807));
        routeBuildingRoutePoints.add(new Point(50.4216995, 30.4258595));
        routeBuildingRoutePoints.add(new Point(50.4217846, 30.4259127));
        routeBuildingRoutePoints.add(new Point(50.4218679, 30.4259648));
        routeBuildingRoutePoints.add(new Point(50.421949, 30.4260166));
        routeBuildingRoutePoints.add(new Point(50.4220291, 30.426066));
        routeBuildingRoutePoints.add(new Point(50.422107, 30.4261144));
        routeBuildingRoutePoints.add(new Point(50.4223769, 30.4262649));
        routeBuildingRoutePoints.add(new Point(50.422469, 30.4263184));
        routeBuildingRoutePoints.add(new Point(50.4225544, 30.4263715));
        routeBuildingRoutePoints.add(new Point(50.4226348, 30.4264244));
        routeBuildingRoutePoints.add(new Point(50.4227066, 30.4264715));
        routeBuildingRoutePoints.add(new Point(50.4227738, 30.4265161));
        routeBuildingRoutePoints.add(new Point(50.4228396, 30.426558));
        routeBuildingRoutePoints.add(new Point(50.4229029, 30.4265987));
        routeBuildingRoutePoints.add(new Point(50.4229644, 30.426637));
        routeBuildingRoutePoints.add(new Point(50.423017, 30.4266696));
        routeBuildingRoutePoints.add(new Point(50.4230622, 30.4266969));
        routeBuildingRoutePoints.add(new Point(50.4231028, 30.4267206));
        routeBuildingRoutePoints.add(new Point(50.4231742, 30.42676));
        routeBuildingRoutePoints.add(new Point(50.4232012, 30.426774));
        routeBuildingRoutePoints.add(new Point(50.4232177, 30.4267845));
        routeBuildingRoutePoints.add(new Point(50.4232268, 30.4267898));
        routeBuildingRoutePoints.add(new Point(50.42323, 30.4267916));

        double lat = 50.0d;
        double lng = 30.0d;
        routes.add(new Route(0, FIELD_ID, Route.Type.COMPUTED, listWithPoint));
        for (int i = 0; i < 5; i++) {
            List<Point> points = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                points.add(new Point(lat + j, lng + i));
            }
            routes.add(new Route(i, FIELD_ID, Route.Type.COMPUTED, points));
        }

        fieldBuildingRoute =
                new Route(0, 0, Route.Type.BASE, routeBuildingRoutePoints);
        computedRoutes = computeRouteModels(fieldBuildingRoute, true);

    }

    @Before
    public void setUp() {
        when(mMockRealLocation.getLatitude()).thenReturn(50.0d);
        when(mMockRealLocation.getLongitude()).thenReturn(30.0d);

        when(mMapPresenter.getComputedRoutes(anyInt())).thenReturn(computedRoutes);
        when(mMapPresenter.isStillCurrentRoute(any(Point.class))).thenCallRealMethod();

        when(mMapPresenter.getNearestPoint(any(Point.class), any(Point.class), any(Point.class)))
                .thenCallRealMethod();
        when(mMapPresenter.getNearestRoute(any(Location.class))).thenCallRealMethod();
        when(mMapPresenter.getNearestPoint(anyList(), any(Point.class))).thenCallRealMethod();
    }

    @Ignore
    @Test
    public void getNearestRoute_mockedRealDataAndNearLocation_nearestRouteModel() {
        Point firstComputedStart = harvestingRoute.get(0);
        Point firstComputedEnd = harvestingRoute.get(harvestingRoute.size() - 1);
        Point firstComputedMiddle = harvestingRoute.get(harvestingRoute.size() / 2);

        Location location = mock(Location.class);
        when(location.getLatitude())
                .thenReturn(firstComputedStart.getLatitude())
                .thenReturn(firstComputedStart.getLatitude())
                .thenReturn(firstComputedEnd.getLatitude())
                .thenReturn(firstComputedEnd.getLatitude())
                .thenReturn(firstComputedMiddle.getLatitude())
                .thenReturn(firstComputedMiddle.getLatitude())
                .thenReturn(currentPoint.getLatitude())
                .thenReturn(currentPoint.getLatitude());
        when(location.getLongitude())
                .thenReturn(firstComputedStart.getLongitude())
                .thenReturn(firstComputedStart.getLongitude())
                .thenReturn(firstComputedEnd.getLongitude())
                .thenReturn(firstComputedEnd.getLongitude())
                .thenReturn(firstComputedMiddle.getLongitude())
                .thenReturn(firstComputedMiddle.getLongitude())
                .thenReturn(currentPoint.getLongitude())
                .thenReturn(currentPoint.getLongitude());

        Route result = mMapPresenter.getNearestRoute(location);
        assertEquals(computedRoutes.get(0), result);

        result = mMapPresenter.getNearestRoute(location);
        assertEquals(computedRoutes.get(0), result);

        result = mMapPresenter.getNearestRoute(location);
        assertEquals(computedRoutes.get(0), result);

        result = mMapPresenter.getNearestRoute(location);
        assertEquals(computedRoutes.get(3), result);
    }

    @Test
    public void getNearestRoute_fromNullOrEmpty_returnsNull() {
        Route result = mMapPresenter.getNearestRoute(null);

        assertNull(result);

        result = mMapPresenter.getNearestRoute(mMockLocation);

        assertNull(result);
    }

    @Test
    public void getNearestRoute_fromRealLocation_obtainComputedRoutes() {
        Route result = mMapPresenter.getNearestRoute(mMockRealLocation);

        verify(mMapPresenter).getComputedRoutes(anyInt());
    }

    @Test
    public void getNearestRoute_withNullRoutePointsOrRouteSizeSmallerTwo_returnsNull() {
        List<Route> list = new ArrayList<>();
        list.add(new Route(0, FIELD_ID, Route.Type.COMPUTED));
        when(mMapPresenter.getComputedRoutes(anyInt()))
                .thenReturn(list);

        Route result = mMapPresenter.getNearestRoute(mMockRealLocation);

        assertNull(result);
    }

    @Test
    public void getNearestRoute_withoutRoutesInRepo_returnsNull() {
        when(mMapPresenter.getComputedRoutes(anyInt()))
                .thenReturn(null)
                .thenReturn(new ArrayList<Route>());

        Route result = mMapPresenter.getNearestRoute(mMockRealLocation);

        assertNull(result);

        result = mMapPresenter.getNearestRoute(mMockRealLocation);

        assertNull(result);
    }

    @Test
    public void getNearestRoute_fromMockLocation_returnsNearestRouteModel() {
        when(mMapPresenter.getComputedRoutes(anyInt())).thenReturn(routes);

        Route result = mMapPresenter.getNearestRoute(mMockRealLocation);

        assertEquals(result, routes.get(1));
    }

    @Ignore
    @Test
    public void isStillCurrentRoute_fromLocationOnCurrentRoute_returnTrue() {
        Route model = computedRoutes.get(0);
        when(mMapPresenter.getCurrentRoute(any(Location.class))).thenReturn(model);

        boolean result = mMapPresenter.isStillCurrentRoute(model.getRoutePoints().get(0));

        assertTrue(result);
    }

    @Test
    public void isStillCurrentRoute_fromLocationNotOnRoute_returnFalse() {
        when(mMapPresenter.getCurrentRoute(any(Location.class))).thenReturn(computedRoutes.get(0));

        boolean result = mMapPresenter.isStillCurrentRoute(currentPoint);

        assertFalse(result);
    }

    @Test
    public void getNearestPoint_fromOneRoutePointAndCurrentPoint_returnThisPoint() {
        List<Point> listWithPoint = new ArrayList<>();
        listWithPoint.add(nearPoint);

        Point result = mMapPresenter.getNearestPoint(listWithPoint, currentPoint);

        assertEquals(result, nearPoint);
    }

    @Test
    public void getNearestPoint_fromTwoRoutePointAndCurrentPoint_returnNearestPoint() {
        List<Point> listWithPoint = new ArrayList<>();
        listWithPoint.add(nearPoint);
        listWithPoint.add(farPoint);

        Point result = mMapPresenter.getNearestPoint(listWithPoint, currentPoint);

        assertEquals(result, nearPoint);
    }

    @Ignore
    @Test
    public void getNearestPoint_fromManyRoutePointAndCurrentPoint_returnNearestPoint() {
        List<Point> listWithPoints = new ArrayList<>(listWithPoint);
        Point resultFromOddList = mMapPresenter.getNearestPoint(listWithPoints, currentPoint);
        assertEquals(resultFromOddList, nearPoint);

        listWithPoints.remove(5);
        Point resultFromEvenList = mMapPresenter.getNearestPoint(listWithPoints, currentPoint);
        assertEquals(resultFromEvenList, nearPoint);

        Point resultWithFarPoint = mMapPresenter.getNearestPoint(listWithPoints, farPoint);
        assertEquals(resultWithFarPoint, farPoint);

        Point point = new Point(49d, 32d);
        for (Route model : routes) {
            List<Point> points = model.getRoutePoints();
            Point result = mMapPresenter.getNearestPoint(points, point);

            assertEquals(result, points.get(0));
        }

        List<Point> computedPoints = computedRoutes.get(0).getRoutePoints();
        Point resultFromRealData = mMapPresenter.getNearestPoint(
                computedPoints,
                harvestingRoute.get(harvestingRoute.size()-1));

        assertEquals(resultFromRealData, computedPoints.get(computedPoints.size() - 1));

    }

    @Test
    public void getNearAndNextPoints_FromEmptyRoute_returnEmptyList() {
        List<Point> result = mMapPresenter.getNearAndNextPoints(new ArrayList<Point>(), nearPoint);

        assertTrue(result.isEmpty());
    }

    @Ignore
    @Test
    public void getNearAndNextPoints_FromOnePointRoute_returnThisPoint() {
        List<Point> onePointList = new ArrayList<>();
        onePointList.add(nearPoint);
        List<Point> result = mMapPresenter.getNearAndNextPoints(onePointList, nearPoint);

        assertNotNull(result);

        assertEquals(result.get(0), nearPoint);
    }

    @Ignore
    @Test
    public void getNearAndNextPoints_FromRouteAndPointAtStartOfTheRoute_returnThisPointAndTheNextOne() {
        List<Point> result = mMapPresenter.getNearAndNextPoints(listWithPoint, nearPoint);

        assertNotNull(result);

        assertEquals(result.get(0), nearPoint);
        assertEquals(result.get(1), listWithPoint.get(listWithPoint.indexOf(nearPoint)+1));

        List<Point> computedRoutePoints = computedRoutes.get(0).getRoutePoints();
        result = mMapPresenter.getNearAndNextPoints(
                computedRoutePoints,
                harvestingRoute.get(0));

        assertEquals(result.get(0), computedRoutePoints.get(0));
        assertEquals(result.get(1), computedRoutePoints.get(1));
    }

    @Ignore
    @Test
    public void getNearAndNextPoints_FromRouteAndPointAtEndOfTheRoute_returnThisPoint() {
        List<Point> result = mMapPresenter.getNearAndNextPoints(listWithPoint, farPoint);

        assertNotNull(result);

        assertEquals(result.get(0), farPoint);

        List<Point> computedRoutePoints = computedRoutes.get(0).getRoutePoints();
        result = mMapPresenter.getNearAndNextPoints(
                computedRoutePoints,
                harvestingRoute.get(harvestingRoute.size()-1));

        assertEquals(result.get(0), computedRoutePoints.get(computedRoutePoints.size()-1));
    }

    @Ignore
    @Test
    public void getNearAndNextPoints_FromRouteAndPointAtCenterOfTheRoute_returnThisPointAndTheNextOne() {
        List<Point> result = mMapPresenter.getNearAndNextPoints(listWithPoint, listWithPoint.get(5));

        assertEquals(result.get(0), listWithPoint.get(5));
        assertEquals(result.get(1), listWithPoint.get(6));

        List<Point> computedRoutePoints = computedRoutes.get(0).getRoutePoints();
        result = mMapPresenter.getNearAndNextPoints(
                computedRoutePoints,
                harvestingRoute.get(harvestingRoute.size()/2));

        assertEquals(result.get(0), computedRoutePoints.get(computedRoutePoints.size()/2));
        assertEquals(result.get(1), computedRoutePoints.get(computedRoutePoints.size()/2+1));
    }



    private static List<Route> computeRouteModels(Route fieldBuildingRoute, boolean toLeft) {
        //TODO Normal check
        if (fieldBuildingRoute == null)
            return null;

        List<Route> result = new ArrayList<>();
        result.add(fieldBuildingRoute);

        List<Point> fieldBuildingPoints = fieldBuildingRoute.getRoutePoints();
        Point start = fieldBuildingPoints.get(0);
        Point end = fieldBuildingPoints.get(fieldBuildingPoints.size() - 1);

        double arrowHeading = toLeft ? Constants.HEADING_TO_LEFT : Constants.HEADING_TO_RIGHT;

        double computedHeading = MapUtils.computeHeading(start, end);
        double normalHeading = computedHeading + arrowHeading;

        for (int i = 0; i < 4; i++) {

            List<Point> routePoints = computeNewPath(result.get(i).getRoutePoints(),
                    Constants.WIDTH_METERS,
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


    private void randomizeRoutePoints() {
        List<Point> newList = new ArrayList<>();
        Random random = new Random(5330);
        for (Point p : routeBuildingRoutePoints) {
            p.setLongitude(p.getLongitude() +
                    Double.parseDouble("0.0000" + random.nextInt(100)));
            newList.add(MapUtils.computeOffset(p, 15, MapUtils.computeHeading(routeBuildingRoutePoints.get(0), routeBuildingRoutePoints.get(routeBuildingRoutePoints.size() - 1)) - 90));
        }

        StringBuilder sb = new StringBuilder();
        for (Point point : newList)
            sb.append(point.getLatitude() + "," + point.getLongitude() + "\r\n");

        assertEquals("", sb.toString());
    }
    */
}
