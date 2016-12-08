package casak.ru.geofencer.presenter.interfaces;

import android.content.Intent;
import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;


/**
 * Created by Casak on 08.12.2016.
 */

public interface ILocationPresenter {
    boolean isConnected();
    Location currentLocation();
    boolean isMoving();
    boolean enableLocationUpdates(GoogleApiClient googleApiClient);

}
