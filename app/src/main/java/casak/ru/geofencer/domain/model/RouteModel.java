package casak.ru.geofencer.domain.model;

import java.util.ArrayList;
import java.util.List;

public class RouteModel {
    public enum Type {
        FROM_BASE,
        FIELD_BUILDING,
        COMPUTED,
        CULTIVATED
    }
    private int id;
    private Type type;
    private List<Point> routePoints;
    private Type routeType;

    public RouteModel(int id, Type type){
        this.id = id;
        this.type = type;
        routePoints = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public List<Point> getRoutePoints() {
        return routePoints;
    }

    public void setRoutePoints(List<Point> routePoints) {
        this.routePoints = routePoints;
    }

    public void addRoutePoint(Point newPoint){
        routePoints.add(newPoint);
    }
}
