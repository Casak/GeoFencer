package casak.ru.geofencer.domain.interactors;


import casak.ru.geofencer.domain.interactors.base.Interactor;
import casak.ru.geofencer.domain.model.ArrowModel;


public interface BuildFieldInteractor extends Interactor {

    interface Callback {
        void onFieldBuildFinish();

        void onFieldBuildFail();

        void removeArrow(ArrowModel model);
    }



}
