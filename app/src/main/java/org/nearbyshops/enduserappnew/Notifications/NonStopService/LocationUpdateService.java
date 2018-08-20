package org.nearbyshops.enduserappnew.Notifications.NonStopService;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLocation;

/**
 * Created by sumeet on 10/4/17.
 */



public class LocationUpdateService extends NonStopIntentService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener ,LocationListener{


    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;


    public static final String ACTION = "com.codepath.example.servicesdemo.MyTestService";



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



    public LocationUpdateService(String name) {
        super(name);
    }


    public LocationUpdateService() {
        super("name");
    }









    @Override
    protected void onHandleIntent(Intent intent) {



    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }


        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
//        mLocationRequest.setSmallestDisplacement(100);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if(mLocationRequest!=null)
        {
            if(mGoogleApiClient.isConnected())
            {
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, this);
            }
        }

//        createLocationRequest();

//        if(mLocationRequest!=null)
//        {
//            startLocationUpdates();
//        }
    }

    private static final int REQUEST_CHECK_SETTINGS = 3;

    protected void createLocationRequest() {




//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                .addLocationRequest(mLocationRequest);


//        PendingResult<LocationSettingsResult> result =
//                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

//
//        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
//            @Override
//            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
//
//                final Status status = locationSettingsResult.getStatus();
//                final LocationSettingsStates states = locationSettingsResult.getLocationSettingsStates();
//
//                switch (status.getStatusCode()) {
//                    case LocationSettingsStatusCodes.SUCCESS:
//                        // All location settings are satisfied. The client can
//                        // initialize location requests here.
//                        break;
//                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                        // Location settings are not satisfied, but this can be fixed
//                        // by showing the user a dialog.
//                        try {
//                            // Show the dialog by calling startResolutionForResult(),
//                            // and check the result in onActivityResult().
//                            status.startResolutionForResult(
//                                    this,
//                                    REQUEST_CHECK_SETTINGS);
//
//                        } catch (IntentSender.SendIntentException e) {
//                            // Ignore the error.
//                        }
//                        break;
//
//                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                        // Location settings are not satisfied. However, we have no way
//                        // to fix the settings so we won't show the dialog.
//                        // ...
//                        break;
//
//                }
//            }
//
//        });
    }


    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

//
//            ActivityCompat.requestPermissions(
//                    this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
//                    2);

            return;
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


        stopLocationUpdates();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onLocationChanged(Location location) {





        saveLocation(location);

        System.out.println("Location Updated : Lat : " + location.getLatitude() + " Lon : " + location.getLongitude());

    }



    void saveLocation(Location location)
    {
        PrefGeneral.saveInSharedPrefFloat(PrefGeneral.LAT_CENTER_KEY,(float)location.getLatitude());
        PrefGeneral.saveInSharedPrefFloat(PrefGeneral.LON_CENTER_KEY,(float)location.getLongitude());

        PrefLocation.saveLatitude((float) location.getLatitude(),this);
        PrefLocation.saveLongitude((float) location.getLongitude(),this);

        broadcastMessage();

//        stopLocationUpdates();
    }



    void broadcastMessage()
    {

        // Construct an Intent tying it to the ACTION (arbitrary event namespace)
        Intent in = new Intent(ACTION);
        // Put extras into the intent as usual
//        in.putExtra("resultCode", Activity.RESULT_OK);
//        in.putExtra("resultValue", "My Result Value. Passed in: " + val);
        // Fire the broadcast with intent packaged
        LocalBroadcastManager.getInstance(this).sendBroadcast(in);
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
