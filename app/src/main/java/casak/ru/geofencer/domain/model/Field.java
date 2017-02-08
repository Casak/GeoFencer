package casak.ru.geofencer.domain.model;

import java.util.ArrayList;
import java.util.List;

//TODO Do I need harvestedRoute? Check converter test if do
public class Field {
    private int id;
    private List<Point> points;
    private Route currentRoute;
    private List<Route> routes;

    private Field(){
        routes = new ArrayList<>();
    }

    public Field(int id) {
        this();
        this.id = id;
    }

    public Field(int id, List<Point> points) {
        this();
        this.id = id;
        this.points = points;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public Route getCurrentRoute() {
        return currentRoute;
    }

    public void setCurrentRoute(Route currentRoute) {
        this.currentRoute = currentRoute;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> computedRoutes) {
        this.routes = computedRoutes;
    }

    public void addRoute(Route model){
        routes.add(model);
    }

    public int getId() {
        return id;
    }
}
