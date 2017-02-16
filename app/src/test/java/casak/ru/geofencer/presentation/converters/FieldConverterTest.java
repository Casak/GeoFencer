package casak.ru.geofencer.presentation.converters;

import com.google.android.gms.maps.model.PolygonOptions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import casak.ru.geofencer.domain.model.Field;
import casak.ru.geofencer.domain.model.Point;

import static org.junit.Assert.*;

/**
 * Created on 16.02.2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class FieldConverterTest {

    private Field field;
    private List<Point> points;

    @Before
    public void createField(){
        points = new ArrayList<>();
        points.add(new Point());
        points.add(new Point(99,99));

        field = new Field(99, points);
    }

    @Test
    public void convertToPresentation_fromDomainField_returnPolygonOption() {
        PolygonOptions result = FieldConverter.convertToPresentation(field);

        assertTrue(result.isGeodesic());
        assertFalse(result.isClickable());
        assertEquals(points.size(), result.getPoints().size());
    }

}