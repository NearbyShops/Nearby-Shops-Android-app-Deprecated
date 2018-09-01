//package org.nearbyshops.enduserappnew.AndroidServices;
//
//import android.Manifest;
//import android.app.IntentService;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.support.v4.app.ActivityCompat;
//
//import com.mapzen.android.lost.api.LocationListener;
//import com.mapzen.android.lost.api.LocationRequest;
//import com.mapzen.android.lost.api.LocationServices;
//import com.mapzen.android.lost.api.LostApiClient;
//
//import org.greenrobot.eventbus.EventBus;
//import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
//import org.nearbyshops.enduserappnew.Preferences.PrefLocation;
//
//
//
//    /**
//     * Created by sumeet on 10/4/17.
//     */
//
//
//
//public class LocationUpdateServiceLOST  extends IntentService implements LostApiClient.ConnectionCallbacks,LocationListener {
//
//
//        LostApiClient lostApiClient;
//        Location mLastLocation;
//        LocationRequest mLocationRequest;
//
//
////    public static final String ACTION = "com.codepath.example.servicesdemo.MyTestService";
//
//
//
//
//
//
//
//        @Override
//        public void onCreate() {
//            super.onCreate();
//
//            lostApiClient = new LostApiClient.Builder(this)
//                    .addConnectionCallbacks(this)
//                    .build();
//
//            lostApiClient.connect();
//
//        }
//
//
//
//
//
//        @Override public void onConnected() {
//            // Client is connected and ready for use
//            startLocationUpdates();
//        }
//
//        @Override public void onConnectionSuspended() {
//            // Fused location provider service has been suspended
//        }
//        // Location code ends
//
//
//
//
//
//
//
//
//        public LocationUpdateServiceLOST(String name) {
//            super(name);
//        }
//
//
//        public LocationUpdateServiceLOST() {
//            super("name");
//        }
//
//
//
//
//
//
//
//
//
//        @Override
//        protected void onHandleIntent(Intent intent) {
//
//        }
//
//
//
//
//
//
//
//        protected void startLocationUpdates() {
//
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//
////
////            ActivityCompat.requestPermissions(
////                    this,
////                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
////                    2);
//
//                stopSelf();
//
//                return;
//            }
//
//
//
//
//
//            LocationRequest request = LocationRequest.create()
//                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                    .setInterval(5000)
//                    .setSmallestDisplacement(100);
//
////            LocationListener listener = new LocationListener() {
////                @Override
////                public void onLocationChanged(Location location) {
////                    // Do stuff
////                }
////            };
//
//
//
//
//
//            LocationServices.FusedLocationApi
//                    .requestLocationUpdates(
//                            lostApiClient,
//                            request, this
//                    );
//
//        }
//
//
//
//
//
//
//
//        @Override
//        public void onStart(Intent intent, int startId) {
//            super.onStart(intent, startId);
//
//            if (lostApiClient != null) {
//                lostApiClient.connect();
//            }
//        }
//
//
//
//
//        @Override
//        public void onDestroy() {
//            super.onDestroy();
//
//
//            stopLocationUpdates();
//
//            if (lostApiClient != null) {
//                lostApiClient.disconnect();
//            }
//        }
//
//
//
//
//
//
//
//
//        @Override
//        public void onLocationChanged(Location location) {
//
//
//            EventBus.getDefault().post(location);
//
//            saveLocation(location);
////            System.out.println("Location Updated : Lat : " + location.getLatitude() + " Lon : " + location.getLongitude());
//        }
//
//
//
//
//
//
//        void saveLocation(Location location)
//        {
////            PrefGeneral.saveInSharedPrefFloat(PrefGeneral.LAT_CENTER_KEY,(float)location.getLatitude());
////            PrefGeneral.saveInSharedPrefFloat(PrefGeneral.LON_CENTER_KEY,(float)location.getLongitude());
//
//
//
////            PrefLocation.saveLatitude((float) location.getLatitude(),this);
////            PrefLocation.saveLongitude((float) location.getLongitude(),this);
//
//            PrefLocation.saveLatLonCurrent(
//                    (float) location.getLatitude(),
//                    (float) location.getLongitude(),this
//            );
//
//
////        broadcastMessage();
//
//            stopLocationUpdates();
//        }
//
//
//
//
//
//
//
//        protected void stopLocationUpdates() {
//
//            if(lostApiClient.isConnected())
//            {
//                LocationServices.FusedLocationApi.removeLocationUpdates(
//                        lostApiClient, this);
//            }
//
//            stopSelf();
//        }
//
//}
