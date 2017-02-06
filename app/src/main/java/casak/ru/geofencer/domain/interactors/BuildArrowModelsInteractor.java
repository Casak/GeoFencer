package casak.ru.geofencer.domain.interactors;


import casak.ru.geofencer.domain.interactors.base.Interactor;
import casak.ru.geofencer.domain.model.ArrowModel;


public interface BuildArrowModelsInteractor extends Interactor {

    interface Callback {
        void onArrowsBuildFinished(long fieldId);
        void onArrowsBuildFailed(long fieldId);
    }

    ArrowModel getLeftArrow();
    ArrowModel getRightArrow();
}
