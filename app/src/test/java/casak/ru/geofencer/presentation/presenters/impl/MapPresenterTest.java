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

    public Point currentPoint = new Point(50.4236812,30.4266095);
    public Point nearPoint = new Point(50.0d, 30.0d);
    public Point farPoint = new Point(60.0d, 10.0d);

    @BeforeClass
    public static void setUpClass() {
        routeModels = new ArrayList<>();
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
        when(mMapPresenter.getNearestPoints(anyList(), any(Point.class), anyInt())).thenCallRealMethod();
        when(mMapPresenter.computingSecondApproach(anyList(), any(Point.class))).thenCallRealMethod();
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

        assertEquals(result, routeModels.get(0));
    }

    @Test
    public void computingSecondApproach_FromValidInput_callGetNearestPoints() {
        double result = mMapPresenter.computingSecondApproach(routeModels.get(0).getRoutePoints(), currentPoint);

        verify(mMapPresenter).getNearestPoints(anyList(), any(Point.class), anyInt());
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
        List<Point> listWithPoint = new ArrayList<>();
        listWithPoint.add(nearPoint);
        listWithPoint.add(new Point(51.0d,31.0d));
        listWithPoint.add(new Point(51.0d,31.0d));
        listWithPoint.add(new Point(51.0d,31.0d));
        listWithPoint.add(new Point(51.0d,31.0d));
        listWithPoint.add(new Point(51.0d,31.0d));
        listWithPoint.add(new Point(51.0d,31.0d));
        listWithPoint.add(new Point(51.0d,31.0d));
        listWithPoint.add(farPoint);

        Point resultFromOddList = mMapPresenter.getNearestPoint(listWithPoint, currentPoint);

        assertEquals(resultFromOddList, nearPoint);

        listWithPoint.remove(5);

        Point resultFromEvenList = mMapPresenter.getNearestPoint(listWithPoint, currentPoint);

        assertEquals(resultFromEvenList, nearPoint);


        Point resultWithFarPoint = mMapPresenter.getNearestPoint(listWithPoint, farPoint);

        assertEquals(resultWithFarPoint, farPoint);

        Point point = new Point(49d,32d);
        for(RouteModel model : routeModels) {
            List<Point> points = model.getRoutePoints();
            Point result = mMapPresenter.getNearestPoint(points, point);

            assertEquals(result, points.get(0));
        }
    }

    @Test
    public void getNearestPoints_FromValidData_returnExactlyCountOfPoints(){
        List<Point> listWithPoint = new ArrayList<>();
        listWithPoint.add(nearPoint);
        listWithPoint.add(new Point(51.0d,31.0d));
        listWithPoint.add(new Point(51.0d,31.0d));
        listWithPoint.add(new Point(51.0d,31.0d));
        listWithPoint.add(new Point(51.0d,31.0d));
        listWithPoint.add(new Point(51.0d,31.0d));
        listWithPoint.add(new Point(51.0d,31.0d));
        listWithPoint.add(new Point(51.0d,31.0d));
        listWithPoint.add(farPoint);

        for (int count = 0; count < 5; count++) {
            List<Point> result = mMapPresenter.getNearestPoints(listWithPoint, currentPoint, count);
            assertEquals(result.size(), count);
        }
    }

    @Test
    public void getNearestPoints_FromValidData_returnNearestPoints(){

    }
}
