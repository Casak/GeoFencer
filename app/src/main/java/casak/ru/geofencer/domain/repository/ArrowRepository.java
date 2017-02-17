package casak.ru.geofencer.domain.repository;

import android.support.annotation.Nullable;

import java.util.List;

import casak.ru.geofencer.domain.model.Arrow;

public interface ArrowRepository {

    @Nullable
    List<Arrow> get(long fieldId);

    @Nullable
    Arrow getLeft(long fieldId);

    @Nullable
    Arrow getRight(long fieldId);

    void add(Arrow model, long fieldId);

    void delete(long fieldId);

}
