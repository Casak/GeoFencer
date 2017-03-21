package casak.ru.geofencer.presentation.converters;

import android.content.res.Resources;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.PolygonOptions;

import java.util.List;

import casak.ru.geofencer.AndroidApplication;
import casak.ru.geofencer.R;
import casak.ru.geofencer.domain.model.Field;
import casak.ru.geofencer.domain.model.Point;

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