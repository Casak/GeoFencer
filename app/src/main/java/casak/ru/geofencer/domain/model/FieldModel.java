package casak.ru.geofencer.domain.model;

import java.util.List;

public class FieldModel {
    private List<Point> points;
    private RouteModel currentRoute;
    private List<RouteModel> computedRoutes;
    private List<RouteModel> harvestedRoutes;

    public FieldModel() {
    }

    public FieldModel(List<Point> points) {
        this.points = points;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public RouteModel getCurrentRoute() {
        return currentRoute;
    }

    public void setCurrentRoute(RouteModel currentRoute) {
        this.currentRoute = currentRoute;
    }

    public List<RouteModel> getComputedRoutes() {
        return computedRoutes;
    }

    public void setComputedRoutes(List<RouteModel> computedRoutes) {
        this.computedRoutes = computedRoutes;
    }

    public List<RouteModel> getHarvestedRoutes() {
        return harvestedRoutes;
    }

    public void setHarvestedRoutes(List<RouteModel> harvestedRoutes) {
        this.harvestedRoutes = harvestedRoutes;
    }

    public void addComputedRoute(RouteModel model){
        computedRoutes.add(model);
    }






















/*

    private Polygon field;
    private List<LatLng> currentRoute;
    private Polyline leftArrow;
    private Polyline rightArrow;
    private List<Polyline> notHarvestedRoutes;
    private MapPresenter mapPresenter;
    private PolylineClickListener polylineClickListener;
    private HarvesterModel harvester;


    public FieldModel(MapPresenter presenter, HarvesterModel harvesterModel) {
        harvester = harvesterModel;
        mapPresenter = presenter;
        currentRoute = new LinkedList<>();
    }

    public void initBuildingField(List<LatLng> route) {
        currentRoute = route;
        leftArrow = mapPresenter.showPolyline(createArrow(currentRoute, true));
        rightArrow = mapPresenter.showPolyline(createArrow(currentRoute, false));

        CameraUpdate cameraUpdate = MapsUtils.harvestedPolygonToCameraUpdate(harvester.getHarvestedPolygon());
        if (cameraUpdate != null)
            mapPresenter.animateCamera(cameraUpdate);
    }

    public PolylineClickListener getPolylineClickListener() {
        if (polylineClickListener == null) {
            return polylineClickListener = new PolylineClickListener();
        } else
            return polylineClickListener;
    }

    //TODO Think about moving to @HarvesterModel
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

        int transparentColor = Constants.COMPUTED_ROUTE_COLOR;
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
                            .zIndex(Constants.ROUTE_INDEX)
                    ));
        }
        return routes;
    }

    private Polygon createField(LatLng start, LatLng end, boolean toLeft) {
        return mapPresenter.showPolygon(MapsUtils.createFieldPolygonOptions(start,
                end,
                Constants.WIDTH_METERS,
                toLeft)
                .zIndex(Constants.FIELD_INDEX));
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
                notHarvestedRoutes = createComputedPolylines(harvester.getHarvestedPolyline(),
                        Constants.HEADING_TO_LEFT);
            }
            if (polyline.equals(rightArrow)) {
                field = createField(start, end, false);
                notHarvestedRoutes = createComputedPolylines(harvester.getHarvestedPolyline(),
                        Constants.HEADING_TO_RIGHT);
            }
            if (polyline.equals(leftArrow) || polyline.equals(rightArrow)) {
                //TODO Delete this mock
                List<LatLng> points = notHarvestedRoutes.get(1).getPoints();
                points.remove(0);
                points.remove(1);
                points.remove(points.size()-1);
                points.remove(points.size()-2);
                MapsUtils.mockLocations(mapPresenter.getLocationListener(),
                        points.toArray(new LatLng[points.size()]));

                CameraUpdate cameraUpdate = MapsUtils.fieldPolygonToCameraUpdate(field);
                if (cameraUpdate != null)
                    mapPresenter.animateCamera(cameraUpdate);
            }

            removeArrow(leftArrow);
            removeArrow(rightArrow);
            leftArrow = null;
            rightArrow = null;
        }
    }

    */
}
