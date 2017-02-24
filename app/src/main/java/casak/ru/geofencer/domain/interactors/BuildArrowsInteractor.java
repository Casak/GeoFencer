package casak.ru.geofencer.domain.interactors;

import casak.ru.geofencer.domain.interactors.base.Interactor;
import casak.ru.geofencer.domain.model.Arrow;
import casak.ru.geofencer.domain.model.Route;

public interface BuildArrowsInteractor extends Interactor {
    void init(Route baseRoute, int fieldId);

    void onArrowClick(Arrow model);
}