package casak.ru.geofencer.domain.repository;

import android.support.annotation.Nullable;

import java.util.List;

import casak.ru.geofencer.domain.model.ArrowModel;

public interface ArrowRepository {

    @Nullable
    List<ArrowModel> getArrows(long fieldId);
    @Nullable
    ArrowModel getLeftArrow(long fieldId);
    @Nullable
    ArrowModel getRightArrow(long fieldId);
    void addArrow(ArrowModel arrow, long fieldId);

    void deleteArrows(long fieldId);

}
