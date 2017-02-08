package casak.ru.geofencer.domain.repository;

import android.support.annotation.Nullable;

import java.util.List;

import casak.ru.geofencer.domain.model.Arrow;

public interface ArrowRepository {

    @Nullable
    List<Arrow> getArrows(long fieldId);
    @Nullable
    Arrow getLeftArrow(long fieldId);
    @Nullable
    Arrow getRightArrow(long fieldId);
    void addArrow(Arrow arrow, long fieldId);

    void deleteArrows(long fieldId);

}
