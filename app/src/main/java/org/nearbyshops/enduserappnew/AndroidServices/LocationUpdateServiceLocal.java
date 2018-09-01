//package org.nearbyshops.enduserappnew.AndroidServices;
//
//import android.Manifest;
//import android.app.IntentService;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.LocalBroadcastManager;
//import android.util.Log;
//
//import com.google.android.gms.location.LocationAvailability;
//import com.google.android.gms.location.LocationCallback;
//import com.google.android.gms.location.LocationListener;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationResult;
//import com.google.android.gms.location.LocationServices;
//
//import org.greenrobot.eventbus.EventBus;
//import org.nearbyshops.enduserappnew.AndroidServices.NonStopService.NonStopIntentService;
//import org.nearbyshops.enduserappnew.Preferences.PrefLocation;
//
//
///**
// * Created by sumeet on 10/4/17.
// */
//
//
//
//public class LocationUpdateServiceLocal extends IntentService implements LocationListener {
//
//
//
////    GoogleApiClient mGoogleApiClient;
//    LocationRequest mLocationRequest;
//    Location mLastLocation;
//
//
//    public static final String ACTION_LOCATION_UPDATED_OFFLINE = "com.taxireferral.locationupdate.offline";
//    public static final String ACTION_FAILED_TO_UPDATE_LOCATION = "com.taxireferral.locationupdate.failed";
//
//
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
////
////        // Create an instance of GoogleAPIClient.
////        if (mGoogleApiClient == null) {
////            mGoogleApiClient = new GoogleApiClient.Builder(this)
////                    .addConnectionCallbacks(this)
////                    .addOnConnectionFailedListener(this)
////                    .addApi(LocationServices.API)
////                    .build();
////        }
//
//        // Location_ code ends
//    }
//
//
//
//
//
//
//    public LocationUpdateServiceLocal(String name) {
//        super(name);
//    }
//
//
//    public LocationUpdateServiceLocal() {
//        super("name");
//
//    }
//
//
//
//
//
//
//
//
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//
//        requestLocationUpdates();
//
//        logMessage("LocationUpdateServiceLocal : onHandleIntent");
//    }
//
//
//    LocationCallback locationCallback;
//
//
//
//
//
//    public void requestLocationUpdates() {
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//
//            stopSelf();
//            return;
//        }
//
//
//
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(10000);
//        mLocationRequest.setSmallestDisplacement(100);
//        mLocationRequest.setFastestInterval(5000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//
//
//
//        locationCallback = new LocationCallback(){
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                super.onLocationResult(locationResult);
//
//
//                                        locationResult.getLastLocation().getLatitude();
//                                        locationResult.getLastLocation().getLongitude();
//
//            }
//
//            @Override
//            public void onLocationAvailability(LocationAvailability locationAvailability) {
//                super.onLocationAvailability(locationAvailability);
//            }
//        };
//
//
//        if(mLocationRequest!=null)
//        {
//                LocationServices.getFusedLocationProviderClient(getApplicationContext()).requestLocationUpdates(
//                         mLocationRequest, locationCallback,null);
//        }
//
//    }
//
//
//    @Override
//    public void onStart(Intent intent, int startId) {
//        super.onStart(intent, startId);
//
//    }
//
//
//
//
//
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        stopLocationUpdates();
//    }
//
//
//
//    @Override
//    public void onLocationChanged(Location location) {
//
//        PrefLocation.saveLatLonCurrent(location.getLatitude(),location.getLongitude(),getApplicationContext());
//
//        EventBus.getDefault().post(location);
//
//        logMessage("Location Updated : Lat : " + location.getLatitude() + " Lon : " + location.getLongitude());
//        stopLocationUpdates();
//    }
//
//
//
//
//    void logMessage(String message)
//    {
//        Log.d("location_service",message);
//    }
//
//
//
//
//
//
//
//
//
//
//
//    protected void stopLocationUpdates() {
//
//
//        if(locationCallback!=null)
//        {
//            LocationServices.getFusedLocationProviderClient(getApplicationContext()).removeLocationUpdates(locationCallback);
//        }
//
//        stopSelf();
//    }
//
//
//}
