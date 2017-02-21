package casak.ru.geofencer.bluetooth;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import casak.ru.geofencer.domain.interactors.LocationInteractor;
import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.storage.converters.Util;

/**
 * Created on 17.02.2017.
 */

public class AntennaDataProvider implements AntennaDataObservable {
    private static final String TAG = AntennaDataProvider.class.getSimpleName();

    private List<LocationInteractor.OnLocationChangedListener> observers;

    public AntennaDataProvider() {
        observers = new LinkedList<>();
    }

    @Override
    public void registerObserver(LocationInteractor.OnLocationChangedListener observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(LocationInteractor.OnLocationChangedListener observer) {
        observers.remove(observer);
    }

    @Override
    public void passLocation(Point point) {
        notifyObservers(point);
    }

    private void notifyObservers(Point point) {
        for (LocationInteractor.OnLocationChangedListener observer : observers)
            observer.onChange(point);
    }


    String locationsString = "50.421355,30.4256428;" +
            "50.4214449,30.4256972;" +
            "50.4215316,30.4257533;" +
            "50.421615,30.425807;" +
            "50.4216995,30.4258595;" +
            "50.4217846,30.4259127;" +
            "50.4218679,30.4259648;" +
            "50.421949,30.4260166;" +
            "50.4220291,30.426066;" +
            "50.422107,30.4261144;" +
            "50.4223769,30.4262649;" +
            "50.422469,30.4263184;" +
            "50.4225544,30.4263715;" +
            "50.4226348,30.4264244;" +
            "50.4227066,30.4264715;" +
            "50.4227738,30.4265161;" +
            "50.4228396,30.426558;" +
            "50.4229029,30.4265987;" +
            "50.4229644,30.426637;" +
            "50.423017,30.4266696;" +
            "50.4230622,30.4266969;" +
            "50.4231028,30.4267206;" +
            "50.4231742,30.42676;" +
            "50.4232012,30.426774;" +
            "50.4232177,30.4267845;" +
            "50.4232268,30.4267898;" +
            "50.42323,30.4267916";

    public void startPassingRouteBuildingPoints() {
        String[] locationArray = locationsString.split(";");
        for (String aLocationArray : locationArray) {
            String[] locationSting = aLocationArray.split(",");
            Point location = new Point(Double.parseDouble(locationSting[0]),
                    Double.parseDouble(locationSting[1]));
            passLocation(location);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "Fake locations passed");
    }

    public void startHarvesting() {
        List<Point> list15 = offsetList(15);
        List<Point> list45 = reverseLatLngList(offsetList(45));
        List<Point> list25 = offsetList(25);
        List<Point> list35 = reverseLatLngList(offsetList(35));

        final List<Point> list = new ArrayList<>();
        list.addAll(list35);
        list.addAll(list25);
        list.addAll(list45);
        list.addAll(list15);

        new MockLocationAsyncTask(this, 1000).execute(list.toArray(new Point[list.size()]));
    }

    private static class MockLocationAsyncTask extends AsyncTask<Point, Point, String> {
        List<Point> locations = new LinkedList<>();
        AntennaDataProvider listener;
        int timeout;

        public MockLocationAsyncTask(AntennaDataProvider dataProvider, int timeout) {
            listener = dataProvider;
            this.timeout = timeout;
        }

        @Override
        protected String doInBackground(Point... locationArray) {
            for (Point point : locationArray) {
                publishProgress(point);
                try {
                    Thread.sleep(timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Point... point) {
            listener.passLocation(point[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("All mocked locations ", "are passed to the app");
        }
    }

    private List<Point> reverseLatLngList(List<Point> list) {
        List<Point> result = new LinkedList<>();
        for (int i = list.size() - 1; i >= 0; i--)
            result.add(list.get(i));

        return result;
    }

    public List<Point> offsetList(double offset) {
        List<Point> newList = new ArrayList<>();
        Random random = new Random(Double.doubleToLongBits(offset));

        List<Point> old = Util.stringToPoints(locationsString);

        for (Point p : old) {
            Point point = new Point(p.getLatitude(), p.getLongitude() +
                    Double.parseDouble("0.0000" + random.nextInt(100)));
            newList.add(computeOffset(point, offset,
                    computeHeading(old.get(0),
                            old.get(old.size() - 1)) - 90));
        }
        return newList;
    }

    static double mod(double x, double m) {
        return (x % m + m) % m;
    }

    static double wrap(double n, double min, double max) {
        return n >= min && n < max ? n : mod(n - min, max - min) + min;
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


}
