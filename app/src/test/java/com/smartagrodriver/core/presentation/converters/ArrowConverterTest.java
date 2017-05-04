package com.smartagrodriver.core.presentation.converters;

import com.google.android.gms.maps.model.PolylineOptions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import com.smartagrodriver.core.domain.model.Arrow;
import com.smartagrodriver.core.domain.model.Point;

import static org.junit.Assert.*;

/**
 * Created on 16.02.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class ArrowConverterTest {

    private Arrow arrow;
    private List<Point> points;

    @Before
    public void setUpArrow() {
        points = new ArrayList<>();
        points.add(new Point());
        points.add(new Point());

        arrow = new Arrow(points, Arrow.Type.LEFT);
    }

    @Test
    public void convertToPresentationModel_fromArrowModel_returnPolylineOptions() {
        PolylineOptions result = ArrowConverter.convertToPresentationModel(arrow);

        assertTrue(result.isClickable());
        assertTrue(result.isGeodesic());
        assertEquals(points.size(), result.getPoints().size());
    }

}