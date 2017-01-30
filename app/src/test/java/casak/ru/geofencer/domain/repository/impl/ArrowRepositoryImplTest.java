package casak.ru.geofencer.domain.repository.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import casak.ru.geofencer.domain.model.ArrowModel;
import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.repository.ArrowRepository;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Created on 30.01.2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class ArrowRepositoryImplTest {
    public static ArrowRepository repository;

    public static Integer fieldId = 1;

    public static ArrowModel leftArrow = new ArrowModel(new ArrayList<Point>(), ArrowModel.Type.LEFT);
    public static ArrowModel rightArrow = new ArrowModel(new ArrayList<Point>(), ArrowModel.Type.RIGHT);

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
        List<ArrowModel> result = repository.getArrows(fieldId);

        assertEquals(2, result.size());
    }

    @Test
    public void getArrows_afterSetUp_returnLeftAndRightArrows() {
        List<ArrowModel> result = repository.getArrows(fieldId);

        assertEquals(leftArrow, result.get(0));
        assertEquals(rightArrow, result.get(1));
    }

    @Test
    public void getArrows_fromIncorrectFiledId_returnNull() {
        List<ArrowModel> result = repository.getArrows(-1);

        assertNull(result);
    }

    @Test
    public void getLeftOrRightArrow_fromIncorrectFieldId_returnNull() {
        ArrowModel leftResult = repository.getLeftArrow(-1);
        ArrowModel rightResult = repository.getRightArrow(-1);

        assertNull(leftResult);
        assertNull(rightResult);
    }

    @Test
    public void getLeftOrRightArrow_fromCorrectFieldId_returnDefiniteArrowModel() {
        ArrowModel leftResult = repository.getLeftArrow(fieldId);
        ArrowModel rightResult = repository.getRightArrow(fieldId);

        assertEquals(leftArrow, leftResult);
        assertEquals(rightArrow, rightResult);
    }

    @Test
    public void deleteArrows_fromCorrectFieldId_deleteMapEntry() {
        repository.deleteArrows(fieldId);

        List<ArrowModel> result = repository.getArrows(fieldId);

        assertNull(result);
    }
}