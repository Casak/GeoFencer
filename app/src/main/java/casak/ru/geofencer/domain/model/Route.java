package casak.ru.geofencer.domain.model;

import java.util.ArrayList;
import java.util.List;

public class Route {
    public enum Type {
        BASE,
        COMPUTED,
    }
    private long id;
    private long fieldId;
    private Type type;
    private List<Point> routePoints;

    public Route(long id, long fieldId, Type type){
        this.id = id;
        this.type = type;
        this.fieldId = fieldId;
        routePoints = new ArrayList<>();
    }

    public Route(long id, long fieldId, Type type, List<Point> routePoints){
        this(id, fieldId, type);
        this.routePoints = routePoints;
    }

    public long getId() {
        return id;
    }

    public long getFieldId() {
        return fieldId;
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
