package casak.ru.geofencer.threading.impl;

import android.os.Handler;
import android.os.Looper;

import casak.ru.geofencer.threading.MainThread;

/**
 * This class makes sure that the runnable we provide will be run on the main UI thread.
 */
public class MainThreadImpl implements MainThread {

    private Handler mHandler;

    public MainThreadImpl() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void post(Runnable runnable) {
        mHandler.post(runnable);
    }
}