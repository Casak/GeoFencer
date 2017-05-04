package com.smartagrodriver.core.presentation.converters;

import android.content.res.Resources;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.PolygonOptions;

import java.util.List;

import com.smartagrodriver.core.AndroidApplication;
import com.smartagrodriver.core.R;
import com.smartagrodriver.core.domain.model.Field;
import com.smartagrodriver.core.domain.model.Point;

/**
 * Created on 16.02.2017.
 */

public class FieldConverter {
    @Nullable
    public static PolygonOptions convertToPresentation(Field field) {
        Resources resources = AndroidApplication.getComponent().getContext().getResources();

        List<Point> points = field.getPoints();

        if (points.size() == 0) {
            return null;
        }

        PolygonOptions result = new PolygonOptions();

        result.geodesic(true);
        result.clickable(false);
        result.fillColor(resources.getColor(R.color.field_fill));
        result.strokeColor(resources.getColor(R.color.field_stroke));

        result.addAll(LatLngConverter.convertToLatLng(points));

        return result;
    }
}