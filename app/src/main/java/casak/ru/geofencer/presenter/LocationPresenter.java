package casak.ru.geofencer.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import casak.ru.geofencer.R;
import casak.ru.geofencer.presenter.interfaces.ILocationPresenter;

/**
 * Created by Casak on 08.12.2016.
 */

public class LocationPresenter implements ILocationPresenter, ResultCallback<LocationSettingsResult>,
        LocationListener {

    private static final int LOCATION_UPDATE_INTERVAL_SECONDS = 30 * 1000;
    private static final int LOCATION_FASTEST_UPDATE_INTERVAL_SECONDS = 5 * 1000;


    private boolean isEnabledGPS = false;
    private Location currentLocation;
    private Context mContext;
    private LocationRequest mLocationRequest;

    public LocationPresenter(final Context context, GoogleApiClient googleApiClient) throws Exception {
        this.mContext = context;

        if (ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION) !=  PackageManager.PERMISSION_GRANTED) {
            // TODO: Alert or dialog, new Exception type
            throw new Exception();
        }

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(LOCATION_UPDATE_INTERVAL_SECONDS);
        mLocationRequest.setFastestInterval(LOCATION_FASTEST_UPDATE_INTERVAL_SECONDS);

        checkGPS(googleApiClient);
        enableLocationUpdates(googleApiClient);
        }

    @Override
    public boolean enableLocationUpdates(GoogleApiClient googleApiClient) {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, mLocationRequest, this);
        return false;
    }

    private void checkGPS(GoogleApiClient googleApiClient) {
        if ((googleApiClient != null && !googleApiClient.isConnected()) || mContext == null) {
            //TODO create something
            return;
        }

        PackageManager pm = mContext.getPackageManager();

        if (pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)) {
            LocationManager manager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                        .addLocationRequest(mLocationRequest);
                builder.setAlwaysShow(true);

                PendingResult<LocationSettingsResult> result =
                        LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
                result.setResultCallback(this);
            }
            else{
                isEnabledGPS = true;
            }
        }
        else{
            Toast.makeText(mContext, "You don`t have GPS inside ", Toast.LENGTH_LONG).show();
        }
    }



    @Override
    public boolean isConnected() {
        return isEnabledGPS;
    }

    @Override
    public Location currentLocation() {
        return currentLocation;
    }

    @Override
    public boolean isMoving() {
        return false;
    }
    //TODO listeners
    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                isEnabledGPS = true;
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                try {//TODO Create constant instead of '1000'
                    status.startResolutionForResult((Activity) mContext, 1000);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(mContext.getString(R.string.location_manager));
                builder.setMessage(mContext.getString(R.string.gps_enable_question));
                builder.setPositiveButton(mContext.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mContext.startActivity(i);
                    }
                });
                builder.setNegativeButton(mContext.getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO should finish?
                        ((Activity) mContext).finish();
                    }
                });
                builder.create().show();
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        Log.d("TAG", "Current location = " + location.getLatitude() +
                ", " + location.getLongitude());
        Toast.makeText(mContext, "Current location = " + location.getLatitude() +
                ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data, GoogleApiClient googleApiClient) {
        if(requestCode == 1000 && resultCode == Activity.RESULT_OK){
            checkGPS(googleApiClient);
        }
    }
}
