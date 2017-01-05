package casak.ru.geofencer.domain.model;

import java.util.List;

public class RouteModel {
    private int id;

    public RouteModel(int id){
        this.id = id;
    }

    private List<Point> routePoints;
    private Type routeType;

    public enum Type {
        FROM_BASE,
        FIELD_BUILDING,
        COMPUTED,
        CULTIVATED
    }

    public int getId() {
        return id;
    }

    public List<Point> getRoutePoints() {
        return routePoints;
    }

    public void setRoutePoints(List<Point> routePoints) {
        this.routePoints = routePoints;
    }
}
