package casak.ru.geofencer.domain.repository;

import android.support.annotation.Nullable;

import java.util.List;

import casak.ru.geofencer.domain.model.ArrowModel;

public interface ArrowRepository {

    @Nullable
    List<ArrowModel> getArrows(Integer fieldId);
    @Nullable
    ArrowModel getLeftArrow(Integer fieldId);
    @Nullable
    ArrowModel getRightArrow(Integer fieldId);
    void addArrow(ArrowModel arrow, Integer fieldId);

    void deleteArrows(Integer fieldId);

}
