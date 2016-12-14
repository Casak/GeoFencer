package casak.ru.geofencer.model;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.LinkedList;
import java.util.List;

import casak.ru.geofencer.Constants;
import casak.ru.geofencer.presenter.MapPresenter;
import casak.ru.geofencer.util.MapsUtils;

public class FieldModel {
    private Polygon field;
    private List<LatLng> currentRoute;
    private Polyline leftArrow;
    private Polyline rightArrow;
    private List<Polyline> notHarvestedRoutes;
    private Polyline harvestedPolyline;
    private Polygon harvestedPolygon;
    private TileOverlay heatMap;
    private MapPresenter mapPresenter;
    private PolylineClickListener polylineClickListener;


    public FieldModel(MapPresenter presenter) {
        mapPresenter = presenter;
        currentRoute = new LinkedList<>();
    }

    public void initBuildingField(List<LatLng> route) {
        //currentRoute = route;

        LatLng latLng1 = new LatLng(50.097119d, 30.124142d);
        LatLng latLng2 = new LatLng(50.098466d, 30.125510d);
        LatLng latLng3 = new LatLng(50.099563d, 30.127152d);
        currentRoute.add(latLng1);
        currentRoute.add(latLng2);
        currentRoute.add(latLng3);

        leftArrow = mapPresenter.showPolyline(createArrow(currentRoute, true));
        rightArrow = mapPresenter.showPolyline(createArrow(currentRoute, false));

        harvestedPolyline = mapPresenter.showPolyline(MapsUtils.createPolylineOptions(currentRoute));

        harvestedPolygon = mapPresenter
                .showPolygon(MapsUtils.harvestedPolygonOptions(harvestedPolyline)
                        .fillColor(Color.BLUE)
                        .strokeColor(Color.BLUE));

        heatMap = mapPresenter.showTileOverlay(createHeatMap(harvestedPolyline));

        mapPresenter.moveCamera(MapsUtils.polygonToCameraUpdate(harvestedPolygon));
    }

    public PolylineClickListener getPolylineClickListener() {
        if (polylineClickListener == null) {
            return polylineClickListener = new PolylineClickListener();
        } else
            return polylineClickListener;
    }

    private PolylineOptions createArrow(List<LatLng> route, boolean toLeft) {
        if (route.size() > 1) {
            LatLng start = route.get(0);
            LatLng end = route.get(route.size() - 1);

            double distanceBetween = SphericalUtil
                    .computeDistanceBetween(start, end);
            double heading = SphericalUtil.computeHeading(start, end);
            LatLng routeCenter = SphericalUtil.computeOffset(start, distanceBetween / 2, heading);

            return MapsUtils.createArrow(routeCenter, distanceBetween, heading, toLeft);
        }
        //TODO Normal error handling
        else
            return null;
    }

    private List<Polyline> createComputedPolylines(Polyline oldPolyline, double heading) {
        List<Polyline> routes = new LinkedList<>();
        routes.add(oldPolyline);

        List<LatLng> oldPolylineList = oldPolyline.getPoints();
        //TODO Normal check
        LatLng start = oldPolylineList.get(0);
        LatLng end = oldPolylineList.get(oldPolylineList.size() - 1);

        double computedHeading = SphericalUtil.computeHeading(start, end);
        double normalHeading = computedHeading + heading;
        double backwardHeading = computedHeading + 180;

        int transparentColor = 0x9FFF00FF;
        boolean first = true;
        for (int i = 0; i < 4; i++) {
            transparentColor = transparentColor - 0x20000000;

            Polyline path = routes.get(i);

            List<LatLng> resultPoints = MapsUtils.computeNewPath(path,
                    Constants.WIDTH_METERS,
                    normalHeading);

            List<LatLng> points = new LinkedList<>();
            if (first)
                points.add(SphericalUtil.computeOffset(resultPoints.get(0),
                        Constants.WIDTH_METERS * 2,
                        backwardHeading));

            points.addAll(resultPoints);
            if (first) {
                first = false;
                points.add(SphericalUtil.computeOffset(resultPoints.get(resultPoints.size() - 1),
                        Constants.WIDTH_METERS * 2,
                        computedHeading));
            }

            routes.add(
                    mapPresenter.showPolyline(MapsUtils.createPolylineOptions(points)
                            .color(transparentColor)
                            .width(5)
                            .geodesic(true)
                    ));
        }
        return routes;
    }

    private Polygon createField(LatLng start, LatLng end, boolean toLeft) {
        return mapPresenter.showPolygon(MapsUtils.createFieldPolygonOptions(start,
                end,
                Constants.WIDTH_METERS,
                toLeft));
    }

    private TileOverlayOptions createHeatMap(Polyline path) {
        List<LatLng> list = path.getPoints();
        HeatmapTileProvider mProvider = new HeatmapTileProvider.Builder()
                .data(list)
                .radius(10)
                .build();
        return new TileOverlayOptions().tileProvider(mProvider);
    }

    //TODO Destruct?
    private void removeArrow(Polyline arrow) {
        mapPresenter.removePolyline(arrow);
    }

    private class PolylineClickListener implements GoogleMap.OnPolylineClickListener {
        @Override
        public void onPolylineClick(Polyline polyline) {
            LatLng start;
            LatLng end;
            if (leftArrow == null && rightArrow == null)
                return;
            else {
                start = currentRoute.get(0);
                end = currentRoute.get(currentRoute.size() - 1);
            }

            if (polyline.equals(leftArrow)) {
                field = createField(start, end, true);
                notHarvestedRoutes = createComputedPolylines(harvestedPolyline,
                        Constants.HEADING_TO_LEFT);
                mapPresenter.moveCamera(MapsUtils.polygonToCameraUpdate(field));
            }
            if (polyline.equals(rightArrow)) {
                field = createField(start, end, false);
                notHarvestedRoutes = createComputedPolylines(harvestedPolyline,
                        Constants.HEADING_TO_RIGHT);
                mapPresenter.moveCamera(MapsUtils.polygonToCameraUpdate(field));
            }

            removeArrow(leftArrow);
            removeArrow(rightArrow);
            leftArrow = null;
            rightArrow = null;
        }
    }
}
