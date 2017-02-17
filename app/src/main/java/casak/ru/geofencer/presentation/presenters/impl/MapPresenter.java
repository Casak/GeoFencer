package casak.ru.geofencer.presentation.presenters.impl;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import casak.ru.geofencer.bluetooth.BluetoothAntennaLocationSource;
import casak.ru.geofencer.domain.executor.Executor;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.presentation.presenters.base.AbstractPresenter;

/**
 * Created on 08.12.2016.
 */

public class MapPresenter extends AbstractPresenter {

    private static final String TAG = MapPresenter.class.getSimpleName();

    private LocationSource locationSource;
    private LocationListener mapLocationListener;


    public MapPresenter(Context context, Executor executor, MainThread mainThread) {
        super(executor, mainThread);

        locationSource = new BluetoothAntennaLocationSource();

    }

    public LocationListener getLocationListener() {
        return mapLocationListener == null ? mapLocationListener = new MapLocationListener() : mapLocationListener;
    }


    private List<Location> locations = new LinkedList<>();

    private class MapLocationListener implements LocationListener {
        private Location previous = new Location(LocationManager.GPS_PROVIDER);

        @Override
        public void onLocationChanged(Location location) {
            if (location.equals(previous))
                return;
            else {
                locations.add(location);
                previous = location;
            }
            BluetoothAntennaLocationSource.getListener().onLocationChanged(location);
        }
    }

    public void onPolylineClick(Polyline polyline) {

        List<LatLng> list = new ArrayList<>();

        list.add(new LatLng(50.422912, 30.425952));
        list.add(new LatLng(50.422601, 30.425775));
        list.add(new LatLng(50.422061, 30.425292));
        list.add(new LatLng(50.421608, 30.424369));
        list.add(new LatLng(50.421454, 30.424938));
        list.add(new LatLng(50.421403047796304, 30.425471959874532));
        list.add(new LatLng(50.421492947796295, 30.425563359499005));
        list.add(new LatLng(50.4215796477963, 30.425650459136847));
        list.add(new LatLng(50.4216630477963, 30.42568115878848));
        list.add(new LatLng(50.4217475477963, 30.42573765843551));
        list.add(new LatLng(50.42183264779631, 30.42579085808003));
        list.add(new LatLng(50.421915947796315, 30.425823957732078));
        list.add(new LatLng(50.42199704779629, 30.4258847573933));
        list.add(new LatLng(50.422077147796294, 30.425945157058703));
        list.add(new LatLng(50.42215504779629, 30.42599855673329));
        list.add(new LatLng(50.42242494779629, 30.42616205560584));
        list.add(new LatLng(50.422517047796305, 30.426176555221105));
        list.add(new LatLng(50.4226024477963, 30.42622365486436));
        list.add(new LatLng(50.422682847796295, 30.42625355452849));
        list.add(new LatLng(50.4227546477963, 30.426359654228563));
        list.add(new LatLng(50.4228218477963, 30.426372253947832));
        list.add(new LatLng(50.4228876477963, 30.426372153672965));
        list.add(new LatLng(50.422950947796274, 30.426433853408525));
        list.add(new LatLng(50.423012447796296, 30.42652715315161));
        list.add(new LatLng(50.42306504779629, 30.426528752931883));
        list.add(new LatLng(50.42311024779629, 30.426541052743065));
        list.add(new LatLng(50.42315084779628, 30.426602752573455));
        list.add(new LatLng(50.423222247796296, 30.42662315227518));
        list.add(new LatLng(50.42324924779629, 30.42659715216239));
        list.add(new LatLng(50.42326574779629, 30.42663665209346));
        list.add(new LatLng(50.42327484779629, 30.42663695205544));
        list.add(new LatLng(50.423281180329916, 30.426680696850383));


        //MapsUtils.mockLocations(getLocationListener(),
        //        list.toArray(new LatLng[list.size()]));

    }

}
