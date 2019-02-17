package org.nearbyshops.enduserappnew;


import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.nearbyshops.enduserappnew.Carts.CartsList.CartsListFragment;
import org.nearbyshops.enduserappnew.Interfaces.NotifySearch;
import org.nearbyshops.enduserappnew.Interfaces.ShowFragment;
import org.nearbyshops.enduserappnew.ItemsByCategoryTypeSimple.Interfaces.NotifyBackPressed;
import org.nearbyshops.enduserappnew.ItemsByCategoryTypeSimple.ItemCategoriesFragmentSimple;
import org.nearbyshops.enduserappnew.Login.NotifyAboutLogin;
import org.nearbyshops.enduserappnew.LoginPlaceholder.FragmentSignInMessage;
import org.nearbyshops.enduserappnew.OneSignal.PrefOneSignal;
import org.nearbyshops.enduserappnew.OneSignal.UpdateOneSignalID;
import org.nearbyshops.enduserappnew.OrderHistoryNew.OrdersFragmentNew;
import org.nearbyshops.enduserappnew.Shops.ListFragment.FragmentShopNew;
import org.nearbyshops.enduserappnew.TabProfile.ProfileFragment;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;




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


                if (tabId == R.id.tab_items) {
                    showItemsFragment();
                } else if (tabId == R.id.tab_shops) {

//                    bottomBar.getTabWithId(R.id.tab_).setBadgeCount(0);
//                    PrefBadgeCount.saveBadgeCountTripRequests(0,HomeNew.this);

                    showShopsFragment();
                } else if (tabId == R.id.tab_cart) {
//                    bottomBar.getTabWithId(R.id.tab_trips).setBadgeCount(0);
//                    PrefBadgeCount.saveBadgeCountCurrentTrips(0,HomeNew.this);

                    showCartFragment();
                } else if (tabId == R.id.tab_orders) {
//                    bottomBar.getTabWithId(R.id.tab_trips).setBadgeCount(0);
//                    PrefBadgeCount.saveBadgeCountCurrentTrips(0,HomeNew.this);

                    showOrdersFragment();
                } else if (tabId == R.id.tab_profile) {
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.


//                    bottomBar.getTabWithId(R.id.tab_requests).setBadgeCount(2);
                    showProfileFragment();
                }
            }
        });


        int screenToOpen = getIntent().getIntExtra("screen_to_open", -1);


        if (screenToOpen == 1) {
            bottomBar.selectTabWithId(R.id.tab_items);
        } else if (screenToOpen == 2) {
            bottomBar.selectTabWithId(R.id.tab_shops);
        } else if (screenToOpen == 3) {
            bottomBar.selectTabWithId(R.id.tab_cart);
        } else if (screenToOpen == 4) {

            bottomBar.selectTabWithId(R.id.tab_orders);
        } else if (screenToOpen == 5) {

            bottomBar.selectTabWithId(R.id.tab_profile);
        }


//        startSettingsCheck();

        checkPermissions();


        if (PrefOneSignal.getToken(this) != null) {
            startService(new Intent(getApplicationContext(), UpdateOneSignalID.class));
        }
    }


    void showLogMessage(String message) {
        Log.d("log_home_screen", message);
    }


    @Override
    public void loginSuccess() {

        showProfileFragment();
        bottomBar.selectTabWithId(R.id.tab_profile);
    }


    @Override
    public void showLoginFragment() {

        if (getSupportFragmentManager().findFragmentByTag(TAG_LOGIN) == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new FragmentSignInMessage(), TAG_LOGIN)
                    .commit();
        }
    }







    void checkPermissions() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    2);
            return;
        }

    }




    public interface PermissionGranted
    {
        void permissionGranted();
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

            Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_ITEMS_FRAGMENT);

            if (fragment instanceof PermissionGranted) {
                ((PermissionGranted) fragment).permissionGranted();
            }

        } else {
            // permission denied, boo! Disable the
            // functionality that depends on this permission.
            showToastMessage("Permission Rejected");
        }


    }




        @Override
        public void showProfileFragment ()
        {

            if (PrefLogin.getUser(getBaseContext()) == null) {
                showLoginFragment();

                return;
            }


            if (getSupportFragmentManager().findFragmentByTag(TAG_PROFILE) == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new ProfileFragment(), TAG_PROFILE)
                        .commit();
            }
        }


        @Override
        public void showOrdersFragment () {


            if (PrefLogin.getUser(getBaseContext()) == null) {
                showLoginFragment();

                return;
            }


            if (getSupportFragmentManager().findFragmentByTag(TAG_ORDERS_FRAGMENT) == null) {

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new OrdersFragmentNew(), TAG_ORDERS_FRAGMENT)
                        .commit();
            }
        }


        @Override
        public void showCartFragment () {


            if (PrefLogin.getUser(getBaseContext()) == null) {
                showLoginFragment();
                return;
            }


            if (getSupportFragmentManager().findFragmentByTag(TAG_CARTS_FRAGMENT) == null) {

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new CartsListFragment(), TAG_CARTS_FRAGMENT)
                        .commit();
            }
        }


        @Override
        public void showShopsFragment () {


            if (getSupportFragmentManager().findFragmentByTag(TAG_SHOPS_FRAGMENT) == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, FragmentShopNew.newInstance(false), TAG_SHOPS_FRAGMENT)
                        .commit();
            }


        }


        @Override
        public void showItemsFragment () {


            if (getSupportFragmentManager().findFragmentByTag(TAG_ITEMS_FRAGMENT) == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new ItemCategoriesFragmentSimple(), TAG_ITEMS_FRAGMENT)
                        .commit();
            }

        }


        void showToastMessage (String message)
        {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }


        boolean isDestroyed = false;


        @Override
        public void onBackPressed () {

            Fragment fragment = getSupportFragmentManager()
                    .findFragmentByTag(TAG_ITEMS_FRAGMENT);


            if (fragment == null) {
                fragment = getSupportFragmentManager()
                        .findFragmentByTag(TAG_SHOPS_FRAGMENT);
            }

            //notifyBackPressed !=null

            if (fragment instanceof NotifyBackPressed) {
//            showLogMessage("Fragment Instanceof NotifyBackPressed !");

                if (((NotifyBackPressed) fragment).backPressed()) {
                    super.onBackPressed();
                }
            } else {
                super.onBackPressed();
            }
        }


        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            super.onCreateOptionsMenu(menu);
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_items_by_cat_simple, menu);


            // Associate searchable configuration with the SearchView
            SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


            MenuItem item = menu.findItem(R.id.action_search);

            item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {

                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {

                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

                    if (fragment instanceof NotifySearch) {
                        ((NotifySearch) fragment).endSearchMode();
                    }

//                Toast.makeText(Home.this, "onCollapsed Called ", Toast.LENGTH_SHORT).show();

                    return true;
                }
            });


            return true;
        }


        void showToast (String message)
        {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }


        @Override
        protected void onNewIntent (Intent intent){
            super.onNewIntent(intent);

            handleIntent(intent);
        }


        private void handleIntent (Intent intent){

            if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
                String query = intent.getStringExtra(SearchManager.QUERY);
                //use the query to search your data somehow

//            Toast.makeText(this,query,Toast.LENGTH_SHORT).show();


                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);


                if (fragment instanceof NotifySearch) {
                    ((NotifySearch) fragment).search(query);
                }
            }
        }



}
