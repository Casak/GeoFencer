package casak.ru.geofencer.storage.converters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

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
        RouteModel model = new RouteModel(1, RouteModel.Type.COMPUTED, 1);

        Route result = RouteConverter.convertToStorageModel(model);

        assertNotNull(result);
        assertEquals(model.getId(), result.id);
        assertEquals(model.getFieldId(), result.fieldId);
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
        RouteModel result = RouteConverter.convertToDomainModel(route);

        assertNotNull(result);
        assertEquals(route.id, result.getId());
        assertEquals(route.fieldId, result.getFieldId());
        assertEquals(RouteModel.Type.COMPUTED, result.getType());
    }


}