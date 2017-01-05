package casak.ru.geofencer.domain.interactors.impl;

import casak.ru.geofencer.domain.model.Point;

public class MapUtils {
    public static Point computeOffset(Point from, double distance, double heading) {
        distance /= 6371009.0D;
        heading = Math.toRadians(heading);
        double fromLat = Math.toRadians(from.getLatitude());
        double fromLng = Math.toRadians(from.getLongitude());
        double cosDistance = Math.cos(distance);
        double sinDistance = Math.sin(distance);
        double sinFromLat = Math.sin(fromLat);
        double cosFromLat = Math.cos(fromLat);
        double sinLat = cosDistance * sinFromLat + sinDistance * cosFromLat * Math.cos(heading);
        double dLng = Math.atan2(sinDistance * cosFromLat * Math.sin(heading), cosDistance - sinFromLat * sinLat);
        return new Point(Math.toDegrees(Math.asin(sinLat)), Math.toDegrees(fromLng + dLng));
    }

    public static double computeHeading(Point from, Point to) {
        double fromLat = Math.toRadians(from.getLatitude());
        double fromLng = Math.toRadians(from.getLongitude());
        double toLat = Math.toRadians(to.getLatitude());
        double toLng = Math.toRadians(to.getLongitude());
        double dLng = toLng - fromLng;
        double heading = Math.atan2(Math.sin(dLng) * Math.cos(toLat), Math.cos(fromLat) * Math.sin(toLat) - Math.sin(fromLat) * Math.cos(toLat) * Math.cos(dLng));
        return wrap(Math.toDegrees(heading), -180.0D, 180.0D);
    }

    public static double computeDistanceBetween(Point from, Point to) {
        return computeAngleBetween(from, to) * 6371009.0D;
    }

    static double computeAngleBetween(Point from, Point to) {
        return distanceRadians(Math.toRadians(from.getLatitude()), Math.toRadians(from.getLongitude()),
                Math.toRadians(to.getLatitude()), Math.toRadians(to.getLongitude()));
    }

    static double wrap(double n, double min, double max) {
        return n >= min && n < max?n:mod(n - min, max - min) + min;
    }

    static double mod(double x, double m) {
        return (x % m + m) % m;
    }

    private static double distanceRadians(double lat1, double lng1, double lat2, double lng2) {
        return arcHav(havDistance(lat1, lat2, lng1 - lng2));
    }

    static double hav(double x) {
        double sinHalf = Math.sin(x * 0.5D);
        return sinHalf * sinHalf;
    }

    static double arcHav(double x) {
        return 2.0D * Math.asin(Math.sqrt(x));
    }

    static double havDistance(double lat1, double lat2, double dLng) {
        return hav(lat1 - lat2) + hav(dLng) * Math.cos(lat1) * Math.cos(lat2);
    }
}
