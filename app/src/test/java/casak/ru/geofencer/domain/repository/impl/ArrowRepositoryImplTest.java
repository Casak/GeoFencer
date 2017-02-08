package casak.ru.geofencer.domain.repository.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import casak.ru.geofencer.domain.model.Arrow;
import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.repository.ArrowRepository;
import casak.ru.geofencer.storage.ArrowRepositoryImpl;

import static org.junit.Assert.*;

/**
 * Created on 30.01.2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class ArrowRepositoryImplTest {
    public static ArrowRepository repository;

    public static Integer fieldId = 1;

    public static Arrow leftArrow = new Arrow(new ArrayList<Point>(), Arrow.Type.LEFT);
    public static Arrow rightArrow = new Arrow(new ArrayList<Point>(), Arrow.Type.RIGHT);

    @BeforeClass
    public static void setUpClass() {
        repository = ArrowRepositoryImpl.getInstance();
    }

    @Before
    public void setUp(){
        repository.addArrow(leftArrow, fieldId);
        repository.addArrow(rightArrow, fieldId);
    }

    @After
    public void tearDown(){
        repository.deleteArrows(fieldId);
    }

    @Test
    public void getArrows_afterSetUp_TwoArrowsInRepo() {
        List<Arrow> result = repository.getArrows(fieldId);

        assertEquals(2, result.size());
    }

    @Test
    public void getArrows_afterSetUp_returnLeftAndRightArrows() {
        List<Arrow> result = repository.getArrows(fieldId);

        assertEquals(leftArrow, result.get(0));
        assertEquals(rightArrow, result.get(1));
    }

    @Test
    public void getArrows_fromIncorrectFiledId_returnNull() {
        List<Arrow> result = repository.getArrows(-1);

        assertNull(result);
    }

    @Test
    public void getLeftOrRightArrow_fromIncorrectFieldId_returnNull() {
        Arrow leftResult = repository.getLeftArrow(-1);
        Arrow rightResult = repository.getRightArrow(-1);

        assertNull(leftResult);
        assertNull(rightResult);
    }

    @Test
    public void getLeftOrRightArrow_fromCorrectFieldId_returnDefiniteArrowModel() {
        Arrow leftResult = repository.getLeftArrow(fieldId);
        Arrow rightResult = repository.getRightArrow(fieldId);

        assertEquals(leftArrow, leftResult);
        assertEquals(rightArrow, rightResult);
    }

    @Test
    public void deleteArrows_fromCorrectFieldId_deleteMapEntry() {
        repository.deleteArrows(fieldId);

        List<Arrow> result = repository.getArrows(fieldId);

        assertNull(result);
    }
}