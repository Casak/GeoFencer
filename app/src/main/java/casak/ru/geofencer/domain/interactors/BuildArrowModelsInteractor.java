package casak.ru.geofencer.domain.interactors;


import casak.ru.geofencer.domain.interactors.base.Interactor;
import casak.ru.geofencer.domain.model.Arrow;


public interface BuildArrowModelsInteractor extends Interactor {

    interface Callback {
        void onArrowsBuildFinished(long fieldId);
        void onArrowsBuildFailed(long fieldId);
    }

    Arrow getLeftArrow();
    Arrow getRightArrow();
}
