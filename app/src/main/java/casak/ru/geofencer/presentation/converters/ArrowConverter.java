package casak.ru.geofencer.presentation.converters;

import android.graphics.Color;

import com.google.android.gms.maps.model.PolylineOptions;

import casak.ru.geofencer.R;
import casak.ru.geofencer.domain.model.Arrow;

/**
 * Created on 15.02.2017.
 */

public class ArrowConverter {
    public static PolylineOptions convertToPresentationModel(Arrow arrow) {
        PolylineOptions result = new PolylineOptions();

        result.clickable(true);
        result.color(R.color.arrow);
        result.geodesic(true);

        result.addAll(LatLngConverter.convertToLatLng(arrow.getArrowPoints()));

        return result;
    }
}
