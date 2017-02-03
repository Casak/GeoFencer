package casak.ru.geofencer.storage.converters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import casak.ru.geofencer.domain.model.FieldModel;
import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.model.RouteModel;
import casak.ru.geofencer.storage.model.Field;

import static org.junit.Assert.*;

/**
 * Created on 02.02.2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class FieldConverterTest {

    @Test
    public void convertToStorageModel_fromEmptyModel_returnNull() {
        Field result = FieldConverter.convertToStorageModel(null);

        assertNull(result);
    }

    //TODO Finish test and implementation
    @Test
    public void convertToStorageModel_fromDomainModel_returnStorageModel() {
        List<Point> points = Util.stringToPoints("50.0,30.0;51.0,31.0;");
        RouteModel route1 = new RouteModel(1, RouteModel.Type.COMPUTED, 1, points);
        RouteModel route2 = new RouteModel(1, RouteModel.Type.COMPUTED, 1, points);
        RouteModel route3 = new RouteModel(1, RouteModel.Type.COMPUTED, 1, points);

        FieldModel model = new FieldModel(1, points);
        model.addComputedRoute(route1);
        model.addComputedRoute(route2);
        model.addComputedRoute(route3);

        Field result = FieldConverter.convertToStorageModel(model);

        assertNotNull(result);
        assertEquals(model.getId(), result.id);
    }
}