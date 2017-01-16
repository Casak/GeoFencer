package casak.ru.geofencer.presentation.presenters.impl;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.media.MediaMetadataCompat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.domain.interactors.impl.MapUtils;
import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.model.RouteModel;
import casak.ru.geofencer.domain.repository.RouteRepository;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Created on 13.01.2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class MapPresenterTest {
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

    public static List<RouteModel> routeModels;
    public static List<Point> listWithPoint;

    public static Point currentPoint = new Point(50.42, 30.42);
    public static Point nearPoint = new Point(51.0d, 31.0d);
    public static Point farPoint = new Point(51.8d, 31.0d);

    @BeforeClass
    public static void setUpClass() {
        routeModels = new ArrayList<>();
        listWithPoint = new ArrayList<>();

        listWithPoint.add(nearPoint);
        listWithPoint.add(new Point(51.1d, 31.0d));
        listWithPoint.add(new Point(51.2d, 31.0d));
        listWithPoint.add(new Point(51.3d, 31.0d));
        listWithPoint.add(new Point(51.4d, 31.0d));
        listWithPoint.add(new Point(51.5d, 31.0d));
        listWithPoint.add(new Point(51.6d, 31.0d));
        listWithPoint.add(new Point(51.7d, 31.0d));
        listWithPoint.add(farPoint);
        routeModels.add(new RouteModel(0, RouteModel.Type.COMPUTED, FIELD_ID, listWithPoint));


        double lat = 50.0d;
        double lng = 30.0d;
        for (int i = 0; i < 5; i++) {
            List<Point> points = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                points.add(new Point(lat + j, lng + i));
            }
            routeModels.add(new RouteModel(i, RouteModel.Type.COMPUTED, FIELD_ID, points));
        }

    }

    @Before
    public void setUp() {
        when(mMockRouteRepository.getAllRoutes(FIELD_ID, RouteModel.Type.COMPUTED)).thenReturn(routeModels);
        when(mMockRealLocation.getLatitude()).thenReturn(50.0d);
        when(mMockRealLocation.getLongitude()).thenReturn(30.0d);
        when(mMapPresenter.computePointer(any(Location.class))).thenCallRealMethod();
        when(mMapPresenter.getNearestRoute(any(Location.class))).thenCallRealMethod();
        when(mMapPresenter.getNearestPoint(anyList(), any(Point.class))).thenCallRealMethod();
        when(mMapPresenter.getNearestPoints(anyList(), any(Point.class))).thenCallRealMethod();
        when(mMapPresenter.computingFirstApproach(anyList(), any(Point.class))).thenCallRealMethod();
        when(mMapPresenter.computingSecondApproach(anyList(), any(Point.class))).thenCallRealMethod();
        when(mMapPresenter.computeAngleBetweenPointAndLine(any(Point.class), any(Point.class), any(Point.class)))
                .thenCallRealMethod();
    }

    @Test
    public void computePointer_fromNull_returnZero() {
        double result = mMapPresenter.computePointer(null);

        assertEquals(result, 0, 0);
    }

    @Test
    public void computePointer_fromMockLocation_returnZero() {
        double result = mMapPresenter.computePointer(mMockLocation);

        assertEquals(result, 0, 0);
    }

    @Test
    public void computePointer_fromRealLocation_callsGetNearestRoute() {
        when(mMapPresenter.getComputedRoutes(anyInt())).thenReturn(routeModels);

        mMapPresenter.computePointer(mMockRealLocation);

        verify(mMapPresenter).getNearestRoute(any(Location.class));
    }

    //TODO Rename
    @Test
    public void computePointer_fromRealLocationAndRouteModels_returnComputedValues() {
        when(mMapPresenter.getComputedRoutes(anyInt())).thenReturn(routeModels);

        double pointer = mMapPresenter.computePointer(mMockRealLocation);

        assertEquals(0, pointer, 0);
    }


    @Test
    public void getNearestRoute_fromNullOrEmpty_returnsNull() {
        RouteModel result = mMapPresenter.getNearestRoute(null);

        assertNull(result);

        result = mMapPresenter.getNearestRoute(mMockLocation);

        assertNull(result);
    }

    @Test
    public void getNearestRoute_fromRealLocation_callGetComputedRoutes() {
        RouteModel result = mMapPresenter.getNearestRoute(mMockRealLocation);

        verify(mMapPresenter).getComputedRoutes(anyInt());
    }

    @Test
    public void getNearestRoute_withNullRoutePointsOrRouteSizeSmallerTwo_returnsNull() {
        List<RouteModel> list = new ArrayList<>();
        list.add(new RouteModel(0, RouteModel.Type.COMPUTED, FIELD_ID));
        when(mMapPresenter.getComputedRoutes(anyInt()))
                .thenReturn(list);

        RouteModel result = mMapPresenter.getNearestRoute(mMockRealLocation);

        assertNull(result);
    }


    @Test
    public void getNearestRoute_withoutRoutesInRepo_returnsNull() {
        when(mMapPresenter.getComputedRoutes(anyInt()))
                .thenReturn(null)
                .thenReturn(new ArrayList<RouteModel>());

        RouteModel result = mMapPresenter.getNearestRoute(mMockRealLocation);

        assertNull(result);

        result = mMapPresenter.getNearestRoute(mMockRealLocation);

        assertNull(result);
    }

    @Test
    public void getNearestRoute_fromRealLocation_returnsNearestRouteModel() {
        when(mMapPresenter.getComputedRoutes(anyInt())).thenReturn(routeModels);

        RouteModel result = mMapPresenter.getNearestRoute(mMockRealLocation);

        assertEquals(result, routeModels.get(1));
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
        for (RouteModel model : routeModels) {
            List<Point> points = model.getRoutePoints();
            Point result = mMapPresenter.getNearestPoint(points, point);

            assertEquals(result, points.get(0));
        }
    }

    @Test
    public void getNearestPoints_FromValidData_returnListOfSurroundingPoints() {
        List<Point> result = mMapPresenter.getNearestPoints(listWithPoint, nearPoint);

        assertNotNull(result);

        assertEquals(result.get(0), nearPoint);
        assertEquals(result.get(1), nearPoint);
        assertEquals(result.get(2), listWithPoint.get(1));

        result = mMapPresenter.getNearestPoints(listWithPoint, farPoint);

        assertEquals(result.get(0), listWithPoint.get(7));
        assertEquals(result.get(1), farPoint);

        result = mMapPresenter.getNearestPoints(listWithPoint, listWithPoint.get(5));

        assertEquals(result.get(0), listWithPoint.get(4));
        assertEquals(result.get(1), listWithPoint.get(5));
        assertEquals(result.get(2), listWithPoint.get(6));
    }

    @Test
    public void getNearestPoints_FromValidData_returnNearestPoints() {

    }

//TODO Refactor
    @Test
    public void computingSecondApproach_withValidData() {
        double routeHeading = MapUtils.computeHeading(listWithPoint.get(0), listWithPoint.get(1));
        double currentHeading = MapUtils.computeHeading(currentPoint, listWithPoint.get(0));
        double expected = currentHeading - routeHeading;
        double result = mMapPresenter.computingSecondApproach(listWithPoint, currentPoint);

        assertEquals(result, expected, 0);

        Point newCurrent = new Point(51.4d,31.4d);
        routeHeading = MapUtils.computeHeading(listWithPoint.get(3), listWithPoint.get(4));
        currentHeading = MapUtils.computeHeading(newCurrent, listWithPoint.get(4));
        expected = currentHeading - routeHeading;
        result = mMapPresenter.computingSecondApproach(listWithPoint, newCurrent);

        assertEquals(result, expected, 0);

        routeHeading = MapUtils.computeHeading(listWithPoint.get(7), listWithPoint.get(8));
        currentHeading = MapUtils.computeHeading(farPoint, listWithPoint.get(8));
        expected = currentHeading - routeHeading;
        result = mMapPresenter.computingSecondApproach(listWithPoint, farPoint);

        assertEquals(result, expected, 0);
    }


    @Test
    public void computeAngleBetweenTwoSegments_FromTwoSegments_return90(){
        Point current = new Point(51d, 31d);
        Point start = new Point(50d, 30d);
        Point end = new Point(51d, 30d);

        double result = mMapPresenter.computeAngleBetweenPointAndLine(start, end, current);

        assertEquals(90, result, 1);


        current =  new Point(49d, 31d);

        result = mMapPresenter.computeAngleBetweenPointAndLine(start, end, current);

        assertEquals(17, result, 1);



        current =  new Point(52d, 31d);

        result = mMapPresenter.computeAngleBetweenPointAndLine(start, end, current);

        assertEquals(147, result, 1);


    }


}
