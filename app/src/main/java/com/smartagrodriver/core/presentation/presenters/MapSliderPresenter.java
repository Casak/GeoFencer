package com.smartagrodriver.core.presentation.presenters;

import com.smartagrodriver.core.presentation.presenters.base.BasePresenter;


/**
 * Created on 09.02.2017.
 */

public interface MapSliderPresenter extends BasePresenter {
    interface View {
        void startCloseAnimation();

        void startOpenAnimation();
    }

}