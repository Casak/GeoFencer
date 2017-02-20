package casak.ru.geofencer.bluetooth;

import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import casak.ru.geofencer.domain.interactors.LocationInteractor;
import casak.ru.geofencer.domain.model.Point;

/**
 * Created on 17.02.2017.
 */

public class AntennaDataProvider implements AntennaDataObservable {
    private static final String TAG = AntennaDataProvider.class.getSimpleName();

    private List<LocationInteractor.OnLocationChanged> observers;

    @Inject
    public AntennaDataProvider() {
        observers = new LinkedList<>();
    }

    @Override
    public void registerObserver(LocationInteractor.OnLocationChanged observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(LocationInteractor.OnLocationChanged observer) {
        observers.remove(observer);
    }

    @Override
    public void passLocation(Point point) {
        notifyObservers(point);
    }

    private void notifyObservers(Point point) {
        for (LocationInteractor.OnLocationChanged observer : observers)
            observer.onChange(point);
    }

    public void startPassingRouteBuildingPoints(){
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
}
