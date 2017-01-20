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
    private int fieldId;
    private Type type;
    private List<Point> routePoints;

    public RouteModel(int id, Type type, int fieldId){
        this.id = id;
        this.type = type;
        this.fieldId = fieldId;
        routePoints = new ArrayList<>();
    }

    public RouteModel(int id, Type type, int fieldId, List<Point> routePoints){
        this(id, type, fieldId);
        this.routePoints = routePoints;
    }

    public int getId() {
        return id;
    }

    public int getFieldId() {
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
