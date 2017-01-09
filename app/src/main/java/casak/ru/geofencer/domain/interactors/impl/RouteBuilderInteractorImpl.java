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
    private boolean isBuildingFieldRoute;

    private RouteBuilderInteractor.Callback mCallback;
    private LocationRepository mLocationRepository;
    private RouteRepository mRouteRepository;

    public RouteBuilderInteractorImpl(Executor threadExecutor, MainThread mainThread,
                                      RouteBuilderInteractor.Callback callback, LocationRepository locationRepository,
                                      RouteRepository routeRepository) {
        super(threadExecutor, mainThread);
        mCallback = callback;
        mLocationRepository = locationRepository;
        mRouteRepository = routeRepository;

        RouteModel tmp = mRouteRepository.getRoute(RouteModel.Type.FIELD_BUILDING);
        if (tmp == null)
            tmp = mRouteRepository.createRoute(RouteModel.Type.FIELD_BUILDING);
        mFieldBuildingRouteModel = tmp;

        routeDaemon = new LocationChangeListenerDaemon();
        routeDaemon.setDaemon(true);
        //TODO Move to resources; recreate thread if down
        routeDaemon.setName("LocationChangeListenerDaemon");
        routeDaemon.start();
    }

    @Override
    public void run() {

    }

    @Override
    public void newPoint(Point point) {
        if (isBuildingFieldRoute)
                mFieldBuildingRouteModel.addRoutePoint(point);
    }

    @Override
    public RouteModel startBuildRoute(RouteModel.Type type) {
        if(type == RouteModel.Type.FIELD_BUILDING)
            isBuildingFieldRoute = true;
        return mFieldBuildingRouteModel;
    }

    @Override
    public void finishBuildRoute(RouteModel routeModel) {
        if(routeModel.getType() == RouteModel.Type.FIELD_BUILDING)
            isBuildingFieldRoute = false;
    }

    private class LocationChangeListenerDaemon extends Thread {
        Point currentPosition;

        @Override
        public void run() {
            while (true) {
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

        //TODO Implement this shit, dude
        private Point acceptPoint(Point newPoint) {
            return newPoint == currentPosition ? currentPosition : newPoint;
        }

        public Point getCurrentPosition(){
            return currentPosition;
        }
    }
}
