package com.smartagrodriver.core.presentation.converters;

import android.content.res.Resources;
import android.graphics.Color;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolylineOptions;

import com.smartagrodriver.core.AndroidApplication;
import com.smartagrodriver.core.R;
import com.smartagrodriver.core.domain.model.Route;

import java.util.Arrays;
import java.util.List;

/**
 * Created on 15.02.2017.
 */

public class RouteConverter {
    private static final int TRACTOR_ICON_SIZE = 40;
    private static final int ROUTE_WIDTH_PX = 5;
    private static final int PATTERN_GAP_LENGTH_PX = 10;
    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    private static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);

    public static PolylineOptions convertToPresentation(Route route) {
        Resources resources = AndroidApplication.getComponent().getContext().getResources();

        PolylineOptions result = new PolylineOptions();

        result.clickable(false);
        result.geodesic(true);
        result.width(ROUTE_WIDTH_PX);
        result.pattern(PATTERN_POLYGON_ALPHA);

        switch (route.getType()) {
            case COMPUTED:
                result.color(resources.getColor(R.color.route_computed));
                break;
            case BASE:
                result.color(resources.getColor(R.color.route_base));
                break;
            case SESSION:
                result.endCap(new CustomCap(BitmapDescriptorFactory.fromResource(R.drawable.tractor_icon), TRACTOR_ICON_SIZE));
                result.color(resources.getColor(R.color.route_session));
                break;
            default:
                result.color(Color.BLACK);
        }

        result.addAll(LatLngConverter.convertToLatLng(route.getRoutePoints()));

        return result;
    }

}
