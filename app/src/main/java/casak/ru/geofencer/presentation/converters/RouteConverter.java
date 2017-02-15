package casak.ru.geofencer.presentation.converters;

import android.graphics.Color;

import com.google.android.gms.maps.model.PolylineOptions;

import casak.ru.geofencer.domain.model.Route;

/**
 * Created on 15.02.2017.
 */

public class RouteConverter {
    public static PolylineOptions convertToPresentation(Route route) {
        PolylineOptions result = new PolylineOptions();

        result.clickable(false);
        result.geodesic(true);
        result.color(Color.CYAN);

        result.addAll(LatLngConverter.convertToLatLng(route.getRoutePoints()));

        return result;
    }

}
