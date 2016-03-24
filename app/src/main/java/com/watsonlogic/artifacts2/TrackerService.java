package com.watsonlogic.artifacts2;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

public class TrackerService extends Service implements LocationListener {
    private static final long MIN_DELTA_DISTANCE = 0; //no minimum change in distance req'd
    private static final long MIN_TIME_BW_UPDATES = 0; //no delay between updates req'd
    private final String TAG = "TrackerService";
    private final String BROADCAST_ACTION = "TrackerService";
    protected LocationManager locMgr;
    protected Location loc;
    boolean GPSReady, networkReady = false;
    private double latitude, longitude;
    private Looper mLooper;

    public TrackerService() {
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Location l = getLocation();
                Log.d(TAG, l.getLatitude() + "," + l.getLongitude());
                //TODO: Broadcast location to TabsActivity
            }
        }).start();
        /*locMgr = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        try{
            locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
            loc = locMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } catch(SecurityException e){
            e.printStackTrace();
        }*/

        return Service.START_STICKY;
    }


    public Location getLocation() {
        locMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        GPSReady = locMgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
        networkReady = locMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        String provider = null;
        if (GPSReady) {
            provider = LocationManager.GPS_PROVIDER;
            Log.d(TAG, "GPS Enabled");
        } else if (networkReady) {
            provider = LocationManager.NETWORK_PROVIDER;
            Log.d(TAG, "Network Enabled");
        }
        if (loc == null) {
            try {
                locMgr.requestLocationUpdates(provider, MIN_TIME_BW_UPDATES, MIN_DELTA_DISTANCE, this);
                if (locMgr != null) {
                    loc = locMgr.getLastKnownLocation(provider);
                    if (loc != null) {
                        latitude = loc.getLatitude();
                        longitude = loc.getLongitude();
                    }
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
        return loc;
    }

    public void stopTracker() {
        if (locMgr != null) {
            try {
                locMgr.removeUpdates(TrackerService.this);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    public double getLatitude() {
        if (loc != null) {
            latitude = loc.getLatitude();
        }
        return latitude;
    }


    public double getLongitude() {
        if (loc != null) {
            longitude = loc.getLongitude();
        }
        return longitude;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("OnLocationChanged", "Changed");
        Log.d(TAG, location.getLatitude() + "," + location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
