package org.nearbyshops.enduserappnew.AndroidServices;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


import org.greenrobot.eventbus.EventBus;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Preferences.PrefLocation;

import javax.inject.Inject;


/**
 * Created by sumeet on 10/4/17.
 */



public class LocationUpdateServiceGoogle extends IntentService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener ,LocationListener {


    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mLastLocation;

    public static final String ACTION = "com.codepath.example.servicesdemo.MyTestService";
    public static final String ACTION_LOCATION_UPDATED_ONLINE = "com.taxireferral.locationupdate.online";
    public static final String ACTION_LOCATION_UPDATED_OFFLINE = "com.taxireferral.locationupdate.offline";
    public static final String ACTION_FAILED_TO_UPDATE_LOCATION = "com.taxireferral.locationupdate.failed";




    @Override
    public void onCreate() {
        super.onCreate();


        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        // Location code ends
    }






    public LocationUpdateServiceGoogle(String name) {
        super(name);
    }


    public LocationUpdateServiceGoogle() {
        super("name");

    }









    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d("location_update","on handle intent ");

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            stopSelf();
            return;
        }




        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setSmallestDisplacement(100);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        if(mLocationRequest!=null && mGoogleApiClient.isConnected())
        {
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, this);
        }

    }


    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mGoogleApiClient.connect();
    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mGoogleApiClient != null) {

            mGoogleApiClient.disconnect();
        }

    }


    @Override
    public void onLocationChanged(Location location) {


//        LocationWithAddress locationCurrent = new LocationWithAddress(
//                location.getLatitude(),
//                location.getLongitude()
//        );


        EventBus.getDefault().post(location);

        PrefLocation.saveLatLonCurrent(location.getLatitude(),location.getLongitude(),getApplicationContext());

        logMessage("Location Updated : Lat : " + location.getLatitude() + " Lon : " + location.getLongitude());
        stopLocationUpdates();
    }



    void logMessage(String message)
    {
        Log.d("location_service",message);
    }





    protected void stopLocationUpdates() {

        if(mGoogleApiClient.isConnected())
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }

        stopSelf();
    }



}
