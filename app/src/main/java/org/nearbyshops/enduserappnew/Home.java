package org.nearbyshops.enduserappnew;


import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.widget.Toast;


import com.mapzen.android.lost.api.LocationRequest;
import com.mapzen.android.lost.api.LocationServices;
import com.mapzen.android.lost.api.LocationSettingsRequest;
import com.mapzen.android.lost.api.LocationSettingsResult;
import com.mapzen.android.lost.api.LocationSettingsStates;
import com.mapzen.android.lost.api.LostApiClient;
import com.mapzen.android.lost.api.PendingResult;
import com.mapzen.android.lost.api.SettingsApi;
import com.mapzen.android.lost.api.Status;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.nearbyshops.enduserappnew.AndroidServices.LocationUpdateServiceGoogle;
import org.nearbyshops.enduserappnew.Carts.CartsList.CartsListFragment;
import org.nearbyshops.enduserappnew.Interfaces.ShowFragment;
import org.nearbyshops.enduserappnew.ItemsByCategoryTypeSimple.Interfaces.NotifyBackPressed;
import org.nearbyshops.enduserappnew.ItemsByCategoryTypeSimple.ItemCategoriesFragmentSimple;
import org.nearbyshops.enduserappnew.Login.NotifyAboutLogin;
import org.nearbyshops.enduserappnew.LoginPlaceholder.FragmentSignInMessage;
import org.nearbyshops.enduserappnew.OneSignal.PrefOneSignal;
import org.nearbyshops.enduserappnew.OneSignal.UpdateOneSignalID;
import org.nearbyshops.enduserappnew.OrderHistoryNew.PendingOrdersFragmentNew;
import org.nearbyshops.enduserappnew.Shops.ListFragment.FragmentShopNew;
import org.nearbyshops.enduserappnew.TabProfile.ProfileFragment;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;

import java.util.ArrayList;


public class Home extends AppCompatActivity implements ShowFragment,NotifyAboutLogin {


    public static final String TAG_LOGIN = "tag_login";
    public static final String TAG_PROFILE = "tag_profile_fragment";

    public static final String TAG_ITEMS_FRAGMENT = "tag_items_fragment";
    public static final String TAG_SHOPS_FRAGMENT = "tag_shops_fragment";
    public static final String TAG_CARTS_FRAGMENT = "tag_carts_fragment";
    public static final String TAG_ORDERS_FRAGMENT = "tag_orders_fragment";


    private static final int REQUEST_CHECK_SETTINGS = 100;


    BottomBar bottomBar;



    // fragments
    ItemCategoriesFragmentSimple itemsFragment;


    boolean isFirstLaunch = true;



    public Home() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_new);

        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
//        bottomBar.setDefaultTab(R.id.tab_search);


        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);





        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {

            @Override
            public void onTabSelected(@IdRes int tabId) {

//
//                if(tabId==R.id.tab_local)
//                {
//                    showLocal();
//                }
//                else





                if(tabId == R.id.tab_items)
                {
                    showItemsFragment();
                }
                else if(tabId == R.id.tab_shops)
                {

//                    bottomBar.getTabWithId(R.id.tab_).setBadgeCount(0);
//                    PrefBadgeCount.saveBadgeCountTripRequests(0,HomeNew.this);

                    showShopsFragment();
                }
                else if(tabId == R.id.tab_cart)
                {
//                    bottomBar.getTabWithId(R.id.tab_trips).setBadgeCount(0);
//                    PrefBadgeCount.saveBadgeCountCurrentTrips(0,HomeNew.this);

                    showCartFragment();
                }

                else if(tabId == R.id.tab_orders)
                {
//                    bottomBar.getTabWithId(R.id.tab_trips).setBadgeCount(0);
//                    PrefBadgeCount.saveBadgeCountCurrentTrips(0,HomeNew.this);

                    showOrdersFragment();
                }
                else if (tabId == R.id.tab_profile) {
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.


//                    bottomBar.getTabWithId(R.id.tab_requests).setBadgeCount(2);
                        showProfileFragment();
                }
            }
        });












        int screenToOpen = getIntent().getIntExtra("screen_to_open",-1);



        if(screenToOpen==1)
        {
            bottomBar.selectTabWithId(R.id.tab_items);
        }
        else if(screenToOpen==2)
        {
            bottomBar.selectTabWithId(R.id.tab_shops);
        }
        else if(screenToOpen==3)
        {
            bottomBar.selectTabWithId(R.id.tab_cart);
        }
        else if(screenToOpen==4)
        {

            bottomBar.selectTabWithId(R.id.tab_orders);
        }
        else if(screenToOpen==5)
        {

            bottomBar.selectTabWithId(R.id.tab_profile);
        }






        startSettingsCheck();

//        stopService(new Intent(this,LocationUpdateServiceLOST.class));
        startService(new Intent(this,LocationUpdateServiceGoogle.class));




        if(PrefOneSignal.getToken(this)!=null)
        {
            startService(new Intent(getApplicationContext(), UpdateOneSignalID.class));
        }
    }








    void showLogMessage(String message)
    {
        Log.d("log_home_screen",message);
    }


    @Override
    public void loginSuccess() {

        showProfileFragment();
        bottomBar.selectTabWithId(R.id.tab_profile);
    }




    @Override
    public void showLoginFragment()
    {

        if(getSupportFragmentManager().findFragmentByTag(TAG_LOGIN)==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,new FragmentSignInMessage(),TAG_LOGIN)
                    .commit();
        }
    }















    @Override
    public void showProfileFragment()
    {

        if(PrefLogin.getUser(getBaseContext())==null)
        {
            showLoginFragment();

            return;
        }



        if(getSupportFragmentManager().findFragmentByTag(TAG_PROFILE)==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,new ProfileFragment(),TAG_PROFILE)
                    .commit();
        }
    }





    @Override
    public void showOrdersFragment() {



        if(PrefLogin.getUser(getBaseContext())==null)
        {
            showLoginFragment();

            return;
        }


        if(getSupportFragmentManager().findFragmentByTag(TAG_ORDERS_FRAGMENT)==null)
        {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,new PendingOrdersFragmentNew(), TAG_ORDERS_FRAGMENT)
                    .commit();
        }
    }



    @Override
    public void showCartFragment() {


        if(PrefLogin.getUser(getBaseContext())==null)
        {
            showLoginFragment();
            return;
        }


        if(getSupportFragmentManager().findFragmentByTag(TAG_CARTS_FRAGMENT)==null)
        {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,new CartsListFragment(),TAG_CARTS_FRAGMENT)
                    .commit();
        }
    }




    @Override
    public void showShopsFragment() {


        if(getSupportFragmentManager().findFragmentByTag(TAG_SHOPS_FRAGMENT)==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, FragmentShopNew.newInstance(false), TAG_SHOPS_FRAGMENT)
                    .commit();
        }


    }






    @Override
    public void showItemsFragment() {


        if(getSupportFragmentManager().findFragmentByTag(TAG_ITEMS_FRAGMENT)==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ItemCategoriesFragmentSimple(), TAG_ITEMS_FRAGMENT)
                    .commit();
        }

    }







    void showToastMessage(String message)
    {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }


    boolean isDestroyed = false;








    @Override
    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag(TAG_ITEMS_FRAGMENT);



        if(fragment==null)
        {
            fragment = getSupportFragmentManager()
                    .findFragmentByTag(TAG_SHOPS_FRAGMENT);
        }

        //notifyBackPressed !=null

        if(fragment instanceof NotifyBackPressed)
        {
//            showLogMessage("Fragment Instanceof NotifyBackPressed !");

            if(((NotifyBackPressed) fragment).backPressed())
            {
                super.onBackPressed();
            }
        }
        else
        {
            super.onBackPressed();
        }
    }








    @Override
    protected void onPause() {
        super.onPause();
//        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        EventBus.getDefault().register(this);
    }







    LocationRequest mLocationRequest;
    Status requestStatus;
    PendingResult<LocationSettingsResult> result;

    void startSettingsCheck()
    {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

//
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    2);

            return;
        }


//        mLocationRequest = LocationRequest.create();
//        mLocationRequest.setSmallestDisplacement(100);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        ArrayList<LocationRequest> requests = new ArrayList<>();

        LocationRequest highAccuracy = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        requests.add(highAccuracy);


        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .addAllLocationRequests(requests)
                .build();


        LostApiClient apiClient = new LostApiClient.Builder(this).build();
        apiClient.connect();


        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(apiClient, request);



        LocationSettingsResult locationSettingsResult = result.await();
        LocationSettingsStates states = locationSettingsResult.getLocationSettingsStates();
        Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case Status.SUCCESS:
                // All location settings are satisfied. The client can make location requests here.
                break;
            case Status.RESOLUTION_REQUIRED:
                // Location requirements are not satisfied. Redirect user to system settings for resolution.
                try {
                    status.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
                break;
            case Status.INTERNAL_ERROR:
            case Status.INTERRUPTED:
            case Status.TIMEOUT:
            case Status.CANCELLED:
                // Location settings are not satisfied and cannot be resolved.
                break;
            default:
                break;
        }

//        result = LocationServices.SettingsApi.checkLocationSettings(apiClient, request);


//        LocationSettingsResult locationSettingsResult = result.await();

//        LocationSettingsStates states = locationSettingsResult.getLocationSettingsStates();
//        if (states != null) {
//
//        }


    }





    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {


//
//        if(requestCode==2)
//        {
//            // If request is cancelled, the result arrays are empty.
//
//

        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            // permission was granted, yay! Do the
            // contacts-related task you need to do.

//                startService(new Intent(this,LocationUpdateServiceLocal.class));


            showToastMessage("Permission Granted !");




//            stopService(new Intent(this,LocationUpdateServiceLOST.class));
            startService(new Intent(this,LocationUpdateServiceGoogle.class));


//                onConnected(null);


//            if(requestCode==2)
//            {
//
//
//            }


            startSettingsCheck();


        }
        else
        {

            // permission denied, boo! Disable the
            // functionality that depends on this permission.

            showToastMessage("Permission Rejected");
        }


        //        }



        return;
        // other 'case' lines to check for other
        // permissions this app might request
    }





//
//    private void resolveLocationSettings() {
//        try {
//            requestStatus.startResolutionForResult(Home.this, REQUEST_CHECK_SETTINGS);
//        } catch (IntentSender.SendIntentException e) {
//            e.printStackTrace();
//        }
//    }





//    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//
//            case REQUEST_CHECK_SETTINGS:
//                startSettingsCheck();
//                break;
//            default:
//                break;
//        }
//    }










    private void resolveLocationSettings() {
        try {
            requestStatus.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }


}