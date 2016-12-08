package casak.ru.geofencer.model;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;

import java.util.List;

public class Field {

    private GoogleMap map;
    Polygon field;
    //TODO 0 - for start position
    List<Polyline> allRoutes;
    Polyline currentRoute;
    List<Polyline> finishedRoutes;
    //TODO Define field types
    Integer fieldType;

    double headingStartEnd;


    public Field(Polygon field, List<Polyline> routes){
        this.field = field;
        this.allRoutes = routes;
    }

}
