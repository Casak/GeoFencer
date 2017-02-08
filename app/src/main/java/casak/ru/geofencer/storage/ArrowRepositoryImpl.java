package casak.ru.geofencer.storage;

import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import casak.ru.geofencer.domain.model.Arrow;
import casak.ru.geofencer.domain.repository.ArrowRepository;

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
    public List<Arrow> getArrows(long fieldId) {
        return warehouse.get(fieldId);
    }

    @Override
    public Arrow getLeftArrow(long fieldId) {
        return getArrow(fieldId, Arrow.Type.LEFT);
    }

    @Override
    public Arrow getRightArrow(long fieldId) {
        return getArrow(fieldId, Arrow.Type.RIGHT);
    }

    @Override
    public void addArrow(Arrow arrow, long fieldId) {
        List<Arrow> arrows = warehouse.get(fieldId);
        if (arrows == null)
            arrows = new LinkedList<>();
        arrows.add(arrow);
        warehouse.put(fieldId, arrows);
    }

    @Override
    public void deleteArrows(long fieldId) {
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
