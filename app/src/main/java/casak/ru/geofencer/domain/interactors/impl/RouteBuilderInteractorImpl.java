package casak.ru.geofencer.domain.interactors.impl;

import android.util.Log;

import casak.ru.geofencer.domain.executor.Executor;
import casak.ru.geofencer.domain.executor.MainThread;
import casak.ru.geofencer.domain.interactors.RouteBuilderInteractor;
import casak.ru.geofencer.domain.interactors.base.AbstractInteractor;
import casak.ru.geofencer.domain.model.Point;
import casak.ru.geofencer.domain.model.RouteModel;
import casak.ru.geofencer.domain.repository.LocationRepository;
import casak.ru.geofencer.domain.repository.RouteRepository;

/**
 * Created on 05.01.2017.
 */
public class RouteBuilderInteractorImpl extends AbstractInteractor implements RouteBuilderInteractor {

    private static final String TAG = RouteBuilderInteractorImpl.class.getSimpleName();

    private static Thread routeDaemon;

    private static RouteModel mFieldBuildingRouteModel;

    private boolean isBuildingRoute;

    private RouteBuilderInteractor.Callback mCallback;
    private LocationRepository mLocationRepository;
    private RouteRepository mRouteRepository;
    private int fieldId;

    public RouteBuilderInteractorImpl(Executor threadExecutor, MainThread mainThread,
                                      RouteBuilderInteractor.Callback callback, LocationRepository locationRepository,
                                      RouteRepository routeRepository, int fieldId) {
        super(threadExecutor, mainThread);
        this.fieldId = fieldId;

        mCallback = callback;
        mLocationRepository = locationRepository;
        mRouteRepository = routeRepository;
    }

    @Override
    public void run() {
        mFieldBuildingRouteModel = mRouteRepository.createRouteModel(fieldId, RouteModel.Type.FIELD_BUILDING);

        routeDaemon = new LocationChangeListenerDaemon();
        routeDaemon.setDaemon(true);
        //TODO Move to resources; recreate thread if down
        routeDaemon.setName("LocationChangeListenerDaemon");
        routeDaemon.start();

        isBuildingRoute = true;
    }

    @Override
    public void finish() {
        ((LocationChangeListenerDaemon)routeDaemon).cancel();
        isBuildingRoute = false;
        mRouteRepository.addRouteModel(mFieldBuildingRouteModel);
    }

    private void newPoint(Point point) {
        if (mFieldBuildingRouteModel != null && isBuildingRoute)
            mFieldBuildingRouteModel.addRoutePoint(point);
    }

    private class LocationChangeListenerDaemon extends Thread {
        private volatile boolean running = true;
        Point currentPosition;

        @Override
        public void run() {
            while (running) {
                Point retrievedPosition = mLocationRepository.getLastLocation();
                currentPosition = acceptPoint(retrievedPosition);
                if (currentPosition != null && !retrievedPosition.equals(currentPosition))
                    newPoint(currentPosition);
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    Log.e(TAG, "from LocationChangeListenerDaemon:\n " + "Interrupted while sleeping", e);
                }
            }
        }

        public void cancel(){
            running = false;
        }

        //TODO Implement this shit, dude
        private Point acceptPoint(Point newPoint) {
            return newPoint == currentPosition ? currentPosition : newPoint;
        }
    }
}
