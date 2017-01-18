package casak.ru.geofencer.domain.interactors.impl;

import java.util.Iterator;
import java.util.List;

import casak.ru.geofencer.domain.model.Point;

import static java.lang.Math.*;

public class MapUtils {

    public static boolean isLocationOnPath(Point point, List<Point> route,
                                           boolean geodesic, double tolerance) {
        return isLocationOnEdgeOrPath(point, route, false, geodesic, tolerance);
    }

    private static boolean isLocationOnEdgeOrPath(Point point, List<Point> poly, boolean closed, boolean geodesic, double toleranceEarth) {
        int size = poly.size();
        if(size == 0) {
            return false;
        } else {
            double tolerance = toleranceEarth / 6371009.0D;
            double havTolerance = hav(tolerance);
            double lat3 = Math.toRadians(point.getLatitude());
            double lng3 = Math.toRadians(point.getLongitude());
            Point prev = poly.get(closed ? size - 1 : 0);
            double lat1 = Math.toRadians(prev.getLatitude());
            double lng1 = Math.toRadians(prev.getLongitude());
            double maxAcceptable;
            double y1;
            if(geodesic) {
                for(Iterator minAcceptable = poly.iterator(); minAcceptable.hasNext(); lng1 = y1) {
                    Point point2 = (Point)minAcceptable.next();
                    maxAcceptable = Math.toRadians(point2.getLatitude());
                    y1 = Math.toRadians(point2.getLongitude());
                    if(isOnSegmentGC(lat1, lng1, maxAcceptable, y1, lat3, lng3, havTolerance)) {
                        return true;
                    }

                    lat1 = maxAcceptable;
                }
            } else {
                double var60 = lat3 - tolerance;
                maxAcceptable = lat3 + tolerance;
                y1 = mercator(lat1);
                double y3 = mercator(lat3);
                double[] xTry = new double[3];

                double y2;
                for(Iterator var29 = poly.iterator(); var29.hasNext(); y1 = y2) {
                    Point point21 = (Point)var29.next();
                    double lat2 = Math.toRadians(point21.getLatitude());
                    y2 = mercator(lat2);
                    double lng2 = Math.toRadians(point21.getLongitude());
                    if(Math.max(lat1, lat2) >= var60 && Math.min(lat1, lat2) <= maxAcceptable) {
                        double x2 = wrap(lng2 - lng1, -3.141592653589793D, 3.141592653589793D);
                        double x3Base = wrap(lng3 - lng1, -3.141592653589793D, 3.141592653589793D);
                        xTry[0] = x3Base;
                        xTry[1] = x3Base + 6.283185307179586D;
                        xTry[2] = x3Base - 6.283185307179586D;
                        double[] var41 = xTry;
                        int var42 = xTry.length;

                        for(int var43 = 0; var43 < var42; ++var43) {
                            double x3 = var41[var43];
                            double dy = y2 - y1;
                            double len2 = x2 * x2 + dy * dy;
                            double t = len2 <= 0.0D?0.0D:clamp((x3 * x2 + (y3 - y1) * dy) / len2, 0.0D, 1.0D);
                            double xClosest = t * x2;
                            double yClosest = y1 + t * dy;
                            double latClosest = inverseMercator(yClosest);
                            double havDist = havDistance(lat3, latClosest, x3 - xClosest);
                            if(havDist < havTolerance) {
                                return true;
                            }
                        }
                    }

                    lat1 = lat2;
                    lng1 = lng2;
                }
            }

            return false;
        }
    }

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

    public static double distanceToLine(final Point p, final Point start, final Point end) {
        if (start.equals(end)) {
            computeDistanceBetween(end, p);
        }

        final double s0lat = toRadians(p.getLatitude());
        final double s0lng = toRadians(p.getLongitude());
        final double s1lat = toRadians(start.getLatitude());
        final double s1lng = toRadians(start.getLongitude());
        final double s2lat = toRadians(end.getLatitude());
        final double s2lng = toRadians(end.getLongitude());

        double s2s1lat = s2lat - s1lat;
        double s2s1lng = s2lng - s1lng;
        final double u = ((s0lat - s1lat) * s2s1lat + (s0lng - s1lng) * s2s1lng)
                / (s2s1lat * s2s1lat + s2s1lng * s2s1lng);
        if (u <= 0) {
            return computeDistanceBetween(p, start);
        }
        if (u >= 1) {
            return computeDistanceBetween(p, end);
        }
        Point sa = new Point(p.getLatitude() - start.getLatitude(),
                p.getLongitude() - start.getLongitude());
        Point sb = new Point(u * (end.getLatitude() - start.getLatitude()),
                u * (end.getLongitude() - start.getLongitude()));
        return computeDistanceBetween(sa, sb);
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

    private static boolean isOnSegmentGC(double lat1, double lng1, double lat2, double lng2,
                                                double lat3, double lng3, double havTolerance) {
        double havDist13 = havDistance(lat1, lat3, lng1 - lng3);
        if (havDist13 <= havTolerance) {
            return true;
        }
        double havDist23 = havDistance(lat2, lat3, lng2 - lng3);
        if (havDist23 <= havTolerance) {
            return true;
        }
        double sinBearing = sinDeltaBearing(lat1, lng1, lat2, lng2, lat3, lng3);
        double sinDist13 = sinFromHav(havDist13);
        double havCrossTrack = havFromSin(sinDist13 * sinBearing);
        if (havCrossTrack > havTolerance) {
            return false;
        }
        double havDist12 = havDistance(lat1, lat2, lng1 - lng2);
        double term = havDist12 + havCrossTrack * (1 - 2 * havDist12);
        if (havDist13 > term || havDist23 > term) {
            return false;
        }
        if (havDist12 < 0.74) {
            return true;
        }
        double cosCrossTrack = 1 - 2 * havCrossTrack;
        double havAlongTrack13 = (havDist13 - havCrossTrack) / cosCrossTrack;
        double havAlongTrack23 = (havDist23 - havCrossTrack) / cosCrossTrack;
        double sinSumAlongTrack = sinSumFromHav(havAlongTrack13, havAlongTrack23);
        return sinSumAlongTrack > 0;  // Compare with half-circle == PI using sign of sin().
    }

    static double havFromSin(double x) {
        double x2 = x * x;
        return x2 / (1 + sqrt(1 - x2)) * .5;
    }

    static double sinFromHav(double h) {
        return 2 * sqrt(h * (1 - h));
    }

    static double sinSumFromHav(double x, double y) {
        double a = sqrt(x * (1 - x));
        double b = sqrt(y * (1 - y));
        return 2 * (a + b - 2 * (a * y + b * x));
    }

    private static double sinDeltaBearing(double lat1, double lng1, double lat2, double lng2,
                                          double lat3, double lng3) {
        double sinLat1 = sin(lat1);
        double cosLat2 = cos(lat2);
        double cosLat3 = cos(lat3);
        double lat31 = lat3 - lat1;
        double lng31 = lng3 - lng1;
        double lat21 = lat2 - lat1;
        double lng21 = lng2 - lng1;
        double a = sin(lng31) * cosLat3;
        double c = sin(lng21) * cosLat2;
        double b = sin(lat31) + 2 * sinLat1 * cosLat3 * hav(lng31);
        double d = sin(lat21) + 2 * sinLat1 * cosLat2 * hav(lng21);
        double denom = (a * a + b * b) * (c * c + d * d);
        return denom <= 0 ? 1 : (a * d - b * c) / sqrt(denom);
    }

    static double mercator(double lat) {
        return log(tan(lat * 0.5 + PI/4));
    }

    static double clamp(double x, double low, double high) {
        return x < low ? low : (x > high ? high : x);
    }

    static double inverseMercator(double y) {
        return 2 * atan(exp(y)) - PI / 2;
    }
}
