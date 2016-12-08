package casak.ru.geofencer.presenter;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;

import java.util.LinkedList;
import java.util.List;

import casak.ru.geofencer.Util.MapsUtils;
import casak.ru.geofencer.model.Field;
import casak.ru.geofencer.presenter.interfaces.IMapPresenter;

/**
 * Created by Casak on 08.12.2016.
 */

public class MapPresenter implements IMapPresenter{
    private Context context;

    public MapPresenter (Context context){
        this.context = context;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        //Combine width
        double width = 20;

        LatLng latLng1 = new LatLng(50.097119d, 30.124142d);
        LatLng latLng2 = new LatLng(50.099563d, 30.127152d);
        LatLng latLng3 = new LatLng(50.098466d, 30.125510d);

        Polygon polygon = googleMap.addPolygon(MapsUtils.createFieldPolygonOptions(latLng1, latLng2, width)
                .strokeColor(0x7FFF0000)
                .fillColor(0x7F00FF00)
                .geodesic(true));
        //TODO Normalize the direction
        Polyline polyline = googleMap.addPolyline(MapsUtils.createPolylineOptions(new LatLng[]{latLng1, latLng3, latLng2})
                .color(0x7F000000)
                .geodesic(true));

        List<Polyline> polylines = new LinkedList<>();
        polylines.add(polyline);


        for (int i = 0; i < 49; i++) {

            Polyline polyline1 = polylines.get(i);
            List<LatLng> oldPoints = polyline1.getPoints();
            LatLng[] points =
                    MapsUtils.computeNewPath(polyline1, width).toArray(new LatLng[oldPoints.size()]);

            polylines.add(
                    googleMap.addPolyline(MapsUtils.createPolylineOptions(points)
                            .color(0x7FFF00FF)
                            .width(5)
                            .geodesic(true)
                    ));
        }

        Field field = new Field(polygon, polylines);

        googleMap.moveCamera(MapsUtils.polygonToCameraUpdate(polygon));
    }




}
