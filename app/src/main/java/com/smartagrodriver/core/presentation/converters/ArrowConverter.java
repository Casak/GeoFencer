package com.smartagrodriver.core.presentation.converters;

import android.content.res.Resources;

import com.google.android.gms.maps.model.PolylineOptions;

import com.smartagrodriver.core.AndroidApplication;
import com.smartagrodriver.core.R;
import com.smartagrodriver.core.domain.model.Arrow;

/**
 * Created on 15.02.2017.
 */

public class ArrowConverter {
    public static PolylineOptions convertToPresentationModel(Arrow arrow) {
        Resources resources = AndroidApplication.getComponent().getContext().getResources();

        PolylineOptions result = new PolylineOptions();

        result.clickable(true);
        result.color(resources.getColor(R.color.arrow));
        result.geodesic(true);

        result.addAll(LatLngConverter.convertToLatLng(arrow.getArrowPoints()));

        return result;
    }
}
