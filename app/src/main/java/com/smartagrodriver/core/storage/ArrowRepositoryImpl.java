package com.smartagrodriver.core.storage;

import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.smartagrodriver.core.domain.model.Arrow;
import com.smartagrodriver.core.domain.repository.ArrowRepository;

/**
 * Created on 09.01.2017.
 */
public class ArrowRepositoryImpl implements ArrowRepository {
    private Map<Long, List<Arrow>> warehouse = new HashMap<>();

    public static class SingletonHolder {
        public static final ArrowRepository HOLDER_INSTANCE = new ArrowRepositoryImpl() {
        };
    }

    public static ArrowRepository getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    @Override
    public List<Arrow> get(long fieldId) {
        return warehouse.get(fieldId);
    }

    @Override
    public Arrow getLeft(long fieldId) {
        return getArrow(fieldId, Arrow.Type.LEFT);
    }

    @Override
    public Arrow getRight(long fieldId) {
        return getArrow(fieldId, Arrow.Type.RIGHT);
    }

    @Override
    public void add(Arrow arrow, long fieldId) {
        List<Arrow> arrows = warehouse.get(fieldId);
        if (arrows == null)
            arrows = new LinkedList<>();
        arrows.add(arrow);
        warehouse.put(fieldId, arrows);
    }

    @Override
    public void delete(long fieldId) {
        warehouse.remove(fieldId);
    }

    @Nullable
    private Arrow getArrow(long fieldId, Arrow.Type type) {
        List<Arrow> arrowList = warehouse.get(fieldId);

        if (arrowList == null || arrowList.size() == 0)
            return null;

        Arrow result = warehouse.get(fieldId).get(0);
        if (result != null && result.getType() == type)
            return result;
        if (arrowList.size() > 1)
            result = arrowList.get(1);
        if (result != null && result.getType() == type)
            return result;
        return null;
    }
}
