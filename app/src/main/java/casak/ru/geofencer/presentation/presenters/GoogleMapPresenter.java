package casak.ru.geofencer.presentation.presenters;

import android.util.LongSparseArray;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Map;

import casak.ru.geofencer.domain.interactors.CreateFieldInteractor;
import casak.ru.geofencer.domain.interactors.LoadFieldInteractor;
import casak.ru.geofencer.domain.interactors.LocationInteractor;
import casak.ru.geofencer.domain.model.Arrow;
import casak.ru.geofencer.presentation.presenters.base.BasePresenter;

/**
 * Created on 09.02.2017.
 */

public interface GoogleMapPresenter extends BasePresenter, CreateFieldInteractor.Callback,
        LoadFieldInteractor.Callback, LocationInteractor.Callback,
        GoogleMap.OnPolylineClickListener {
    interface View {
        void changeCamera(CameraUpdate update);

        void changeMapType(int type);

        Polyline showPolyline(PolylineOptions options);

        Polygon showPolygon(PolygonOptions options);

        //For debugging
        Circle showCircle(CircleOptions options);

        CameraPosition getCurrentCameraPosition();

        int getCurrentMapType();
    }

    boolean isFieldBuilding();

    void startBuildField();

    void finishBuildField();

    void onZoomMore();

    void onZoomLess();

    void changeZoom(float to);

    void onTiltMore();

    void onTiltLess();

    void changeTilt(float tilt);

    void changeMapType();

    void onFieldLoad(int fieldId);

    int getCurrentFieldId();

    //For debugging
    LongSparseArray<Polyline> getRoutes();

    Map<Arrow, Polyline> getArrows();
}