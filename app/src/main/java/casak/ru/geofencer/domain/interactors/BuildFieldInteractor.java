package casak.ru.geofencer.domain.interactors;


import casak.ru.geofencer.domain.interactors.base.Interactor;


public interface BuildFieldInteractor extends Interactor {

    interface Callback {
        void onFieldBuildFinish();

        void onFieldBuildFail();
    }



}
