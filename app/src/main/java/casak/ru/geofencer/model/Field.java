package casak.ru.geofencer.model;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;

import java.util.List;

import casak.ru.geofencer.Utils;

public class Field {

    private GoogleMap map;
    Polygon field;
    //TODO 0 - for start position
    List<Polyline> routes;
    //TODO Define field types
    Integer fieldType;

    public Field(Polygon field, List<Polyline> routes){
        this.field = field;
        this.routes = routes;
    }

}
