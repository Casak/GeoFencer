package casak.ru.geofencer;

import android.graphics.Color;

import com.google.android.gms.maps.model.Polyline;
import com.google.maps.android.SphericalUtil;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Casak on 06.12.2016.
 */

public class Utils {

    static final double FIELD_HEIGHT_METERS = 1000;
    static final double EARTH_RADIUS_METERS = 6371.008 * 1000;


    static PolylineOptions createPolylineOptions(LatLng... latLngs) {
        return new PolylineOptions().add(latLngs);
    }

    //TODO rename signature
    static PolygonOptions createFieldPolygonOptions(LatLng start, LatLng end, double width) {
        double offset = width == 0 ? 0d : width / 2;
        return new PolygonOptions().add(computeCorners(start, end, offset).toArray(new LatLng[4]));
    }

    static List<LatLng> computeCorners(LatLng start, LatLng end, double offset) {
        double heading = SphericalUtil.computeHeading(start, end);

        List<LatLng> result = new ArrayList<>(4);

        LatLng cornerSouthWest = SphericalUtil.computeOffset(start, offset, heading + 90);
        LatLng cornerNorthWest = SphericalUtil.computeOffset(cornerSouthWest, FIELD_HEIGHT_METERS, heading - 90);
        LatLng cornerSouthEast = SphericalUtil.computeOffset(end, offset, heading + 90);
        LatLng cornerNorthEast = SphericalUtil.computeOffset(cornerSouthEast, FIELD_HEIGHT_METERS, heading - 90);

        result.add(cornerNorthWest);
        result.add(cornerSouthWest);
        result.add(cornerSouthEast);
        result.add(cornerNorthEast);
        return result;
    }

//TODO rename
    static List<LatLng> computeNewPath(Polyline polyline, double width) {
        List<LatLng> points = polyline.getPoints();
        List<LatLng> result = new ArrayList<>(points.size());
        for (LatLng point : points)
            result.add(SphericalUtil.computeOffset(point, width, Constants.HEADING));
        return result;
    }


    static List<LatLng> computeHarvested(LatLng start, LatLng end, double width) {
        double heading = SphericalUtil.computeHeading(start, end);
        double offset = width == 0 ? 0d : width / 2;
        List<LatLng> result = new ArrayList<>(4);

        LatLng cornerSouthWest = SphericalUtil.computeOffset(start, offset - 1, heading + 90);
        LatLng cornerNorthWest = SphericalUtil.computeOffset(cornerSouthWest, width - 1, heading - 90);
        LatLng cornerSouthEast = SphericalUtil.computeOffset(end, offset - 1, heading + 90);
        LatLng cornerNorthEast = SphericalUtil.computeOffset(cornerSouthEast, width - 1, heading - 90);

        result.add(cornerNorthWest);
        result.add(cornerSouthWest);
        result.add(cornerSouthEast);
        result.add(cornerNorthEast);
        return result;
    }


    static private double computeCourse(LatLng start, LatLng end) {
        double lat1 = toRadians(start.latitude);
        double lng1 = toRadians(start.longitude);

        double lat2 = toRadians(end.latitude);
        double lng2 = toRadians(end.longitude);

        double x = Math.sin(lng2 - lng1) * Math.cos(lat2);
        double y = Math.cos(lat1) * Math.sin(lat2) -
                Math.sin(lat1) * Math.cos(lat2) * Math.cos(lng2 - lng1);
        return mod(Math.atan2(x, y), Math.PI * 2);
    }

    static double distanceBetween(LatLng start, LatLng end) {
        double R = EARTH_RADIUS_METERS / 1000;
        double φ1 = toRadians(start.latitude), λ1 = toRadians(start.longitude);
        double φ2 = toRadians(end.latitude), λ2 = toRadians(end.longitude);
        double Δφ = φ2 - φ1;
        double Δλ = λ2 - λ1;

        double a = Math.sin(Δφ / 2) * Math.sin(Δφ / 2)
                + Math.cos(φ1) * Math.cos(φ2)
                * Math.sin(Δλ / 2) * Math.sin(Δλ / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    static LatLng destinationPoint(LatLng start, double bearing) {
        double radius = EARTH_RADIUS_METERS;

        // sinφ2 = sinφ1⋅cosδ + cosφ1⋅sinδ⋅cosθ
        // tanΔλ = sinθ⋅sinδ⋅cosφ1 / cosδ−sinφ1⋅sinφ2
        // see http://williams.best.vwh.net/avform.htm#LL

        double δ = FIELD_HEIGHT_METERS / radius; // angular distance in radians
        double θ = toRadians(bearing);

        double φ1 = toRadians(start.latitude), λ1 = toRadians(start.longitude);

        double sinφ1 = Math.sin(φ1), cosφ1 = Math.cos(φ1);
        double sinδ = Math.sin(δ), cosδ = Math.cos(δ);
        double sinθ = Math.sin(θ), cosθ = Math.cos(θ);

        double sinφ2 = sinφ1 * cosδ + cosφ1 * sinδ * cosθ;
        double φ2 = Math.asin(sinφ2);
        double y = sinθ * sinδ * cosφ1;
        double x = cosδ - sinφ1 * sinφ2;
        double λ2 = λ1 + Math.atan2(y, x);

        return new LatLng(toDegrees(φ2), (toDegrees(λ2) + 540) % 360 - 180);
    }

    static double bearindBetween(LatLng start, LatLng end) {
        double φ1 = toRadians(start.latitude), φ2 = toRadians(end.latitude);
        double Δλ = toRadians(end.longitude - start.longitude);

        // see http://mathforum.org/library/drmath/view/55417.html
        double y = Math.sin(Δλ) * Math.cos(φ2);
        double x = Math.cos(φ1) * Math.sin(φ2) -
                Math.sin(φ1) * Math.cos(φ2) * Math.cos(Δλ);
        double θ = Math.atan2(y, x);

        return (toDegrees(θ) + 360) % 360;
    }

    static double bearindFinalBetween(LatLng start, LatLng end) {
        return (bearindBetween(start, end) + 180) % 360;
    }

    private static double mod(double x, double y) {
        return y - x * Math.floor(y / x);
    }

    private static double toRadians(double point) {
        return point * (Math.PI / 180);
    }

    private static double toDegrees(double point) {
        return point * (180 / Math.PI);
    }
}
