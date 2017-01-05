package casak.ru.geofencer.domain.interactors;


import casak.ru.geofencer.domain.interactors.base.Interactor;


public interface BuildArrowsInteractor extends Interactor {

    interface Callback {
        void onArrowsBuildFinished();

        void onArrowsBuildFailed();
    }

}
