package casak.ru.geofencer.presentation.presenters;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import casak.ru.geofencer.presentation.presenters.base.BasePresenter;

/**
 * Created on 09.02.2017.
 */

public interface GoogleMapPresenter extends BasePresenter, OnMapReadyCallback {
    interface View{
        void setMap(GoogleMap googleMap);
        Polyline showPolyline(PolylineOptions options);


    }

    boolean isFieldBuilding();
    void startBuildField();
    void finishBuildField();
}
