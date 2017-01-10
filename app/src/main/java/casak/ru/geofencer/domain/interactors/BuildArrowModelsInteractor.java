package casak.ru.geofencer.domain.interactors;


import casak.ru.geofencer.domain.interactors.base.Interactor;
import casak.ru.geofencer.domain.model.ArrowModel;


public interface BuildArrowModelsInteractor extends Interactor {

    interface Callback {
        void onArrowsBuildFinished(int fieldId);
        void onArrowsBuildFailed(int fieldId);
    }

    ArrowModel getLeftArrow();
    ArrowModel getRightArrow();
}
