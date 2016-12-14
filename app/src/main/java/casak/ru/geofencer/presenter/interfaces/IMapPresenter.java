package casak.ru.geofencer.presenter.interfaces;

import android.content.Intent;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * Created by Casak on 08.12.2016.
 */

public interface IMapPresenter extends OnMapReadyCallback {

    void onMapReady(GoogleMap googleMap);
    void onActivityResult(int requestCode, int resultCode, Intent data);
    View.OnClickListener getOnClickListener();
    void onStart();
    void onStop();

}
