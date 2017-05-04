package com.smartagrodriver.core.domain.model;

public class Point{
    private long mDate = 0;
    private double mLatitude = 0.0d;
    private double mLongitude = 0.0d;
    private double mAltitude = 0.0d;
    private double mSpeed = 0.0f;
    private double mBearing = 0.0f;
    private double mAccuracy = 0.0f;

    public Point(){}

    public Point(double latitude, double longitude){
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public long getDate() {
        return mDate;
    }

    public void setDate(long date) {
        this.mDate = date;
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

    public double getSpeed() {
        return mSpeed;
    }

    public void setSpeed(double mSpeed) {
        this.mSpeed = mSpeed;
    }

    public double getBearing() {
        return mBearing;
    }

    public void setBearing(double mBearing) {
        this.mBearing = mBearing;
    }

    public double getAccuracy() {
        return mAccuracy;
    }

    public void setAccuracy(double mAccuracy) {
        this.mAccuracy = mAccuracy;
    }

    //TODO computeHash
    @Override
    public boolean equals(Object obj) {
        if(obj == null || ! (obj instanceof Point))
            return false;
        Point point = (Point) obj;
        if (point.mDate != mDate || point.mLatitude != mLatitude ||
                point.mLongitude != mLongitude || point.mAltitude != mAltitude ||
                point.mSpeed != mSpeed || point.mBearing != mBearing ||
                point.mAccuracy != mAccuracy)
            return false;
        return true;
    }
}
