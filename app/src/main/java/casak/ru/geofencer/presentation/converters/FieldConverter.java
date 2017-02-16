package casak.ru.geofencer.presentation.converters;

import com.google.android.gms.maps.model.PolygonOptions;

import casak.ru.geofencer.R;
import casak.ru.geofencer.domain.model.Field;

/**
 * Created on 16.02.2017.
 */

public class FieldConverter {
    public static PolygonOptions convertToPresentation(Field field){
        PolygonOptions result = new PolygonOptions();

        result.geodesic(true);
        result.clickable(false);
        result.fillColor(R.color.field_fill);
        result.strokeColor(R.color.field_stroke);

        result.addAll(LatLngConverter.convertToLatLng(field.getPoints()));

        return result;
    }
}
