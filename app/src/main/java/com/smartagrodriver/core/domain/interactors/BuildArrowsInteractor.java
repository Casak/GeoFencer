package com.smartagrodriver.core.domain.interactors;

import com.smartagrodriver.core.domain.interactors.base.Interactor;
import com.smartagrodriver.core.domain.model.Arrow;
import com.smartagrodriver.core.domain.model.Route;

public interface BuildArrowsInteractor extends Interactor {
    void init(Route baseRoute, int fieldId);

    void onArrowClick(Arrow model);
}