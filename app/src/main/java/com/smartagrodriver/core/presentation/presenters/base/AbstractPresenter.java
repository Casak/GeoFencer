package com.smartagrodriver.core.presentation.presenters.base;


import com.smartagrodriver.core.threading.Executor;
import com.smartagrodriver.core.threading.MainThread;

/**
 * This is a base class for all presenters which are communicating with interactors. This base class will hold a
 * reference to the Executor and MainThread objects that are needed for running interactors in a background thread.
 */
public abstract class AbstractPresenter {
    protected Executor mExecutor;
    protected MainThread mMainThread;

    public AbstractPresenter(Executor executor, MainThread mainThread) {
        mExecutor = executor;
        mMainThread = mainThread;
    }
}