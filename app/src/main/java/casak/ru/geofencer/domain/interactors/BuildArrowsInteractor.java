package casak.ru.geofencer.domain.interactors;


import casak.ru.geofencer.domain.interactors.base.Interactor;
import casak.ru.geofencer.domain.model.ArrowModel;


public interface BuildArrowsInteractor extends Interactor {

    interface Callback {
        void onArrowsBuildFinished();

        void onArrowsBuildFailed();
    }

    ArrowModel getLeftArrow();
    ArrowModel getRightArrow();
}
