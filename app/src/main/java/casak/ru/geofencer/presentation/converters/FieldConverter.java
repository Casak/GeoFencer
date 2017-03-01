package casak.ru.geofencer.presentation.converters;

import android.content.res.Resources;

import com.google.android.gms.maps.model.PolygonOptions;

import casak.ru.geofencer.AndroidApplication;
import casak.ru.geofencer.R;
import casak.ru.geofencer.domain.model.Field;

/**
 * Created on 16.02.2017.
 */

public class FieldConverter {
    public static PolygonOptions convertToPresentation(Field field) {
        Resources resources = AndroidApplication.getComponent().getContext().getResources();

        PolygonOptions result = new PolygonOptions();

        result.geodesic(true);
        result.clickable(false);
        result.fillColor(resources.getColor(R.color.field_fill));
        result.strokeColor(resources.getColor(R.color.field_stroke));

        result.addAll(LatLngConverter.convertToLatLng(field.getPoints()));

        return result;
    }
}
