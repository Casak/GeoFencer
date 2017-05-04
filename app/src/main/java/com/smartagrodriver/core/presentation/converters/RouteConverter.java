package com.smartagrodriver.core.presentation.converters;

import android.content.res.Resources;
import android.graphics.Color;

import com.google.android.gms.maps.model.PolylineOptions;

import com.smartagrodriver.core.AndroidApplication;
import com.smartagrodriver.core.R;
import com.smartagrodriver.core.domain.model.Route;

/**
 * Created on 15.02.2017.
 */

public class RouteConverter {
    public static PolylineOptions convertToPresentation(Route route) {
        Resources resources = AndroidApplication.getComponent().getContext().getResources();

        PolylineOptions result = new PolylineOptions();

        result.clickable(false);
        result.geodesic(true);

        switch (route.getType()) {
            case COMPUTED:
                result.color(resources.getColor(R.color.route_computed));
                break;
            case BASE:
                result.color(resources.getColor(R.color.route_base));
                break;
            default:
                result.color(Color.BLACK);
        }

        result.addAll(LatLngConverter.convertToLatLng(route.getRoutePoints()));

        return result;
    }

}
