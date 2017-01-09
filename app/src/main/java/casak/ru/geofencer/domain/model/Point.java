package casak.ru.geofencer.domain.model;

public class Point{
    private double mTime = 0.0d;
    private double mLatitude = 0.0d;
    private double mLongitude = 0.0d;
    private double mAltitude = 0.0d;
    private float mSpeed = 0.0f;
    private float mBearing = 0.0f;
    private float mAccuracy = 0.0f;

    public Point(){}

    public Point(double latitude, double longitude){
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public double getTime() {
        return mTime;
    }

    public void setTime(double mTime) {
        this.mTime = mTime;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public double getAltitude() {
        return mAltitude;
    }

    public void setAltitude(double mAltitude) {
        this.mAltitude = mAltitude;
    }

    public float getSpeed() {
        return mSpeed;
    }

    public void setSpeed(float mSpeed) {
        this.mSpeed = mSpeed;
    }

    public float getBearing() {
        return mBearing;
    }

    public void setBearing(float mBearing) {
        this.mBearing = mBearing;
    }

    public float getAccuracy() {
        return mAccuracy;
    }

    public void setAccuracy(float mAccuracy) {
        this.mAccuracy = mAccuracy;
    }

    //TODO computeHash
    @Override
    public boolean equals(Object obj) {
        if(obj == null || obj instanceof Point)
            return false;
        Point point = (Point) obj;
        if (point.mTime != mTime || point.mLatitude != mLatitude ||
                point.mLongitude != mLongitude || point.mAltitude != mAltitude ||
                point.mSpeed != mSpeed || point.mBearing != mBearing ||
                point.mAccuracy != mAccuracy)
            return false;
        return true;
    }
}
