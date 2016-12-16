package casak.ru.geofencer.presenter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;

import java.util.List;

import casak.ru.geofencer.R;
import casak.ru.geofencer.model.FieldModel;
import casak.ru.geofencer.model.HarvesterModel;
import casak.ru.geofencer.service.LocationService;
import casak.ru.geofencer.presenter.interfaces.IMapPresenter;
import casak.ru.geofencer.util.MapsUtils;

/**
 * Created by Casak on 08.12.2016.
 */

public class MapPresenter implements IMapPresenter, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    private static final String TAG = MapPresenter.class.getSimpleName();

    private Context context;
    private GoogleMap mGoogleMap;
    private static GoogleApiClient mGoogleApiClient;
    private LocationService locationService;
    private View.OnClickListener onClickListener;
    private LocationListener mapLocationListener;

    //TODO Inject
    private FieldModel field;
    private HarvesterModel harvester;

    public MapPresenter(Context context) {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }

        this.context = context;

        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        LatLng currentLatLng = null;
        if (currentLocation != null)
            currentLatLng = new LatLng(currentLocation.getLatitude(),
                    currentLocation.getLongitude());

        harvester = new HarvesterModel(this, currentLatLng);
        field = new FieldModel(this, harvester);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        locationService.onActivityResult(requestCode, resultCode, data, mGoogleApiClient);
    }

    @Override
    public void onStart() {
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Alert or dialog
            return;
        }
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(context, context.getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            locationService = new LocationService(context, mGoogleApiClient, getLocationListener());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Location client connected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Location client connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Location client connection failed");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mGoogleMap.setOnPolylineClickListener(field.getPolylineClickListener());
        LatLng geoCentrUkraine = new LatLng(48.9592699d, 32.8723257d);
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(geoCentrUkraine, 6f));
    }

    public void finishCreatingRoute(List<LatLng> route) {
        field.initBuildingField(route);
    }

    @Nullable
    public Marker showMarker(MarkerOptions options) {
        return options == null ? null : mGoogleMap.addMarker(options);
    }

    @Nullable
    public Polyline showPolyline(PolylineOptions options) {
        return options == null ? null : mGoogleMap.addPolyline(options);
    }

    @Nullable
    public Polygon showPolygon(PolygonOptions options) {
        return options == null ? null : mGoogleMap.addPolygon(options);
    }

    @Nullable
    public TileOverlay showTileOverlay(TileOverlayOptions options) {
        return options == null ? null : mGoogleMap.addTileOverlay(options);
    }

    public void removeMarker(Marker marker) {
        marker.setVisible(false);
        marker.remove();
    }

    public void removePolyline(Polyline polyline) {
        polyline.setVisible(false);
        polyline.remove();
    }

    public void removePolygon(Polygon polygon) {
        polygon.setVisible(false);
        polygon.remove();
    }

    public void removeTileOverlay(TileOverlay tileOverlay) {
        tileOverlay.setVisible(false);
        tileOverlay.remove();
    }

    public void moveCamera(CameraUpdate cameraUpdate) {
        mGoogleMap.moveCamera(cameraUpdate);
    }

    public void animateCamera(CameraUpdate cameraUpdate) {
        mGoogleMap.animateCamera(cameraUpdate);
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener == null ? onClickListener = new OnClickListener() : onClickListener;
    }

    public LocationListener getLocationListener() {
        return mapLocationListener == null ? mapLocationListener = new MapLocationListener() : mapLocationListener;
    }

    private class OnClickListener implements View.OnClickListener {
        boolean firstClick = true;

        @Override
        public void onClick(View view) {
            if (firstClick) {
                firstClick = false;
                harvester.startFieldRouteBuilding();
                MapsUtils.startMockingLocations(getLocationListener());
            } else {
                firstClick = true;
                harvester.finishFieldRouteBuilding();
            }
        }
    }

    private class MapLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            LatLng currentLocation = new LatLng(location.getLatitude(),
                    location.getLongitude());

            harvester.updateCurrentLocation(currentLocation);

            /*Log.d("TAG", "Current location = " + location.getLatitude() +
                    ", " + location.getLongitude());
            Toast.makeText(context, "Current location = " + location.getLatitude() +
                    ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();*/
        }
    }
}
