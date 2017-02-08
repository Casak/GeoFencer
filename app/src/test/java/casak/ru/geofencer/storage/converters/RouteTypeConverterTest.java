package casak.ru.geofencer.storage.converters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import casak.ru.geofencer.domain.model.Route;
import casak.ru.geofencer.storage.model.RouteType;

import static org.junit.Assert.*;

/**
 * Created on 02.02.2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class RouteTypeConverterTest {

    @Test
    public void convertToStorageModel_fromEmptyModel_returnsNull () {
        RouteType result = RouteTypeConverter.convertToStorageModel(null);

        assertNull(result);
    }

    @Test
    public void convertToStorageModel_fromNonEmptyModel_returnsConvertedModel () {
        RouteType result = RouteTypeConverter.convertToStorageModel(Route.Type.BASE);

        assertNotNull(result);
        assertEquals(1, result.id);
        assertEquals(Route.Type.BASE.toString(), result.type);

        result = RouteTypeConverter.convertToStorageModel(Route.Type.COMPUTED);

        assertNotNull(result);
        assertEquals(2, result.id);
        assertEquals(Route.Type.COMPUTED.toString(), result.type);
    }

    @Test
    public void convertToDomainModel_fromEmptyModel_returnsNull () {
        Route.Type result = RouteTypeConverter.convertToDomainModel(null);

        assertNull(result);
    }

    @Test
    public void convertToDomainModel_fromNonEmptyModel_returnsConvertedModel () {
        RouteType type = new RouteType();
        type.id = 1;
        type.type = Route.Type.BASE.toString();
        Route.Type result = RouteTypeConverter.convertToDomainModel(type);

        assertNotNull(result);
        assertEquals(Route.Type.BASE, result);

        type.id = 2;
        type.type = Route.Type.COMPUTED.toString();
        result = RouteTypeConverter.convertToDomainModel(type);

        assertNotNull(result);
        assertEquals(Route.Type.COMPUTED, result);
    }

}