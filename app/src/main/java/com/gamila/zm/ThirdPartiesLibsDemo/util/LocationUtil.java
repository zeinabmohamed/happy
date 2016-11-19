package com.gamila.zm.ThirdPartiesLibsDemo.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import com.gamila.zm.ThirdPartiesLibsDemo.AppApplication;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

/**
 * Created by zeinab on 4/30/2016.
 */
public class LocationUtil implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    private static LocationUtil mLocationUtil;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private ArrayList<LocationListenerUtil> locationListenerList = new ArrayList<>();

    public static LocationUtil getInstance() {

        if (mLocationUtil == null) {

            mLocationUtil = new LocationUtil();
        }
        return mLocationUtil;
    }

    public void connect() {
        getGoogleApiClient().connect();

    }

    private GoogleApiClient getGoogleApiClient() {

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(AppApplication.getInstance())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        return mGoogleApiClient;

    }


    public Location getCurrentLocation(Activity activity) {
        if (mLastLocation == null) {
            if (ActivityCompat.checkSelfPermission(AppApplication.getInstance(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.
                    checkSelfPermission(AppApplication.getInstance(),
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                /// in case if not granted

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                            , Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                }
                return null;
            }
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(getGoogleApiClient());


        }
        return mLastLocation;

    }

    public double getDistanceInKm(Activity activity, Location destinationLocation) {
        double distance = 0;
        if (getGoogleApiClient().isConnected()) {
            if (mLastLocation == null) {

                getCurrentLocation(activity);

              //  distance =  mLastLocation.distanceTo(destinationLocation)/1000;

            } else {
                distance = mLastLocation.distanceTo(destinationLocation) / 1000;
            }
        } else {
            getGoogleApiClient().connect();
        }
        return distance;
    }

    public void activateLocationAccess(final Context context) {
        final LocationManager manager = (LocationManager) context.
                getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            new AlertDialog.Builder(context)
                    .setTitle(" Activate location access ")
                    .setMessage("Please activate location access for better performance Click ok to goto settings else exit.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            context.startActivity(intent);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
//                            System.exit(0);
                            dialog.dismiss();
                        }
                    })
                    .show();

        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        /* getCurrentLocation();*/
        for (LocationListenerUtil locationListener : getLocationListenerList()) {

            locationListener.onConnect();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        getGoogleApiClient().connect();
    }

    public void disconnect() {

        getGoogleApiClient().disconnect();
    }

    public ArrayList<LocationListenerUtil> getLocationListenerList() {
        return locationListenerList;
    }

    public void addLocationListener(LocationListenerUtil locationListener) {
        getLocationListenerList().add(locationListener);
    }

    public void removeLocationListener(LocationListenerUtil locationListener) {
        getLocationListenerList().remove(locationListener);
    }

    public interface LocationListenerUtil {
        void onConnect();
    }
}
