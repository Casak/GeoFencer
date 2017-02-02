package casak.ru.geofencer.storage.converters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.model.RouteModel;
import casak.ru.geofencer.storage.model.Route;

import static org.junit.Assert.*;

/**
 * Created on 02.02.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class RouteConverterTest {

    @Test
    public void convertToStorageModel_fromEmptyModel_returnsNull() {
        Route result = RouteConverter.convertToStorageModel(null);

        assertNull(result);
    }

    @Test
    public void convertToStorageModel_fromNonEmptyModel_returnsConvertedModel() {
        List<Point> points = Util.stringToPoints("50.0,30.0;51.0,31.0;");

        RouteModel model = new RouteModel(1, RouteModel.Type.COMPUTED, 1, points);

        Route result = RouteConverter.convertToStorageModel(model);

        assertNotNull(result);
        assertEquals(model.getId(), result.id);
        assertEquals(model.getFieldId(), result.fieldId);
        assertEquals(model.getRoutePoints(), Util.stringToPoints(result.points));
        assertEquals(2, result.type);
    }

    @Test
    public void convertToDomainModel_fromEmptyModel_returnsNull() {
        RouteModel result = RouteConverter.convertToDomainModel(null);

        assertNull(result);
    }

    @Test
    public void convertToDomainModel_fromNonEmptyModel_returnsConvertedModel() {
        Route route = new Route();
        route.id = 1;
        route.fieldId = 1;
        route.type = 2;
        route.points = "50.0,30.0;51.0,31.0;";
        RouteModel result = RouteConverter.convertToDomainModel(route);

        assertNotNull(result);
        assertEquals(route.id, result.getId());
        assertEquals(route.fieldId, result.getFieldId());
        assertEquals(Util.stringToPoints(route.points), result.getRoutePoints());
        assertEquals(RouteModel.Type.COMPUTED, result.getType());
    }


}