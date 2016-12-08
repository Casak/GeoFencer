package casak.ru.geofencer.presenter.interfaces;

import android.content.Intent;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * Created by Casak on 08.12.2016.
 */

public interface IMapPresenter extends OnMapReadyCallback {

    void onMapReady(GoogleMap googleMap);
    void startCreatingRoute();
    void finishCreatingRoute();
    void onActivityResult(int requestCode, int resultCode, Intent data);
    void onStart();
    void onStop();

}
