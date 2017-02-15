package casak.ru.geofencer.presentation.presenters;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import casak.ru.geofencer.domain.interactors.CreateFieldInteractor;
import casak.ru.geofencer.presentation.presenters.base.BasePresenter;

/**
 * Created on 09.02.2017.
 */

public interface GoogleMapPresenter extends BasePresenter, CreateFieldInteractor.Callback, OnMapReadyCallback {
    interface View {
        void setMap(GoogleMap googleMap);

        void changeCamera(CameraUpdate update);

        void changeMapType(int type);

        Polyline showPolyline(PolylineOptions options);

        Polygon showPolygon(PolygonOptions options);

        CameraPosition getCurrentCameraPosition();

        int getCurrentMapType();
    }

    boolean isFieldBuilding();

    void startBuildField();

    void finishBuildField();

    void onZoomMore();

    void onZoomLess();

    void onTiltMore();

    void onTiltLess();

    void changeTilt(float tilt);

    void changeMapType();
}
