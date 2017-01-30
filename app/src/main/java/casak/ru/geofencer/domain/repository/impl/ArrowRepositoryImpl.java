package casak.ru.geofencer.domain.repository.impl;

import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import casak.ru.geofencer.domain.model.ArrowModel;
import casak.ru.geofencer.domain.repository.ArrowRepository;

/**
 * Created on 09.01.2017.
 */
public class ArrowRepositoryImpl implements ArrowRepository {
    private Map<Integer, List<ArrowModel>> warehouse = new HashMap<>();

    public static class SingletonHolder {
        public static final ArrowRepository HOLDER_INSTANCE = new ArrowRepositoryImpl() {
        };
    }

    public static ArrowRepository getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    @Override
    public List<ArrowModel> getArrows(Integer fieldId) {
        return warehouse.get(fieldId);
    }

    @Override
    public ArrowModel getLeftArrow(Integer fieldId) {
        return getArrow(fieldId, ArrowModel.Type.LEFT);
    }

    @Override
    public ArrowModel getRightArrow(Integer fieldId) {
        return getArrow(fieldId, ArrowModel.Type.RIGHT);
    }

    @Override
    public void addArrow(ArrowModel arrow, Integer fieldId) {
        List<ArrowModel> arrows = warehouse.get(fieldId);
        if (arrows == null)
            arrows = new LinkedList<>();
        arrows.add(arrow);
        warehouse.put(fieldId, arrows);
    }

    @Override
    public void deleteArrows(Integer fieldId) {
        warehouse.remove(fieldId);
    }

    @Nullable
    private ArrowModel getArrow(Integer fieldId, ArrowModel.Type type) {
        List<ArrowModel> arrowModelList = warehouse.get(fieldId);

        if (arrowModelList == null || arrowModelList.size() == 0)
            return null;

        ArrowModel result = warehouse.get(fieldId).get(0);
        if (result != null && result.getType() == type)
            return result;
        if (arrowModelList.size() > 1)
            result = arrowModelList.get(1);
        if (result != null && result.getType() == type)
            return result;
        return null;
    }
}
