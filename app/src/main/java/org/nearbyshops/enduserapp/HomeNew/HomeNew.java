package org.nearbyshops.enduserapp.HomeNew;


import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.widget.Toast;


import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.nearbyshops.enduserapp.Carts.CartsList.CartsListFragment;
import org.nearbyshops.enduserapp.DaggerComponentBuilder;
import org.nearbyshops.enduserapp.Interfaces.ShowFragment;
import org.nearbyshops.enduserapp.ItemsByCategoryTypeSimple.Interfaces.NotifyBackPressed;
import org.nearbyshops.enduserapp.ItemsByCategoryTypeSimple.ItemCategoriesFragmentSimple;
import org.nearbyshops.enduserapp.LoginNew.NotifyAboutLogin;
import org.nearbyshops.enduserapp.LoginPlaceholders.FragmentSignInMessage;
import org.nearbyshops.enduserapp.OrderHistoryNew.PendingOrdersFragmentNew;
import org.nearbyshops.enduserapp.R;
import org.nearbyshops.enduserapp.Shops.ListFragment.FragmentShopNew;
import org.nearbyshops.enduserapp.TabProfile.ProfileFragment;
import org.nearbyshops.enduserapp.Utility.PrefLogin;







public class HomeNew extends AppCompatActivity implements ShowFragment,NotifyAboutLogin {


    public static final String TAG_LOGIN = "tag_login";
    public static final String TAG_PROFILE = "tag_profile_fragment";

    public static final String TAG_ITEMS_FRAGMENT = "tag_items_fragment";
    public static final String TAG_SHOPS_FRAGMENT = "tag_shops_fragment";
    public static final String TAG_CARTS_FRAGMENT = "tag_carts_fragment";
    public static final String TAG_ORDERS_FRAGMENT = "tag_orders_fragment";



    BottomBar bottomBar;




    // fragments
    ItemCategoriesFragmentSimple itemsFragment;


    boolean isFirstLaunch = true;



    public HomeNew() {

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












//        int screenToOpen = getIntent().getIntExtra("screen_to_open",-1);



//        if(screenToOpen==1)
//        {
//            bottomBar.selectTabWithId(R.id.tab_search);
//        }
//        else if(screenToOpen==2)
//        {
//            bottomBar.selectTabWithId(R.id.tab_requests);
//        }
//        else if(screenToOpen==3)
//        {
//            bottomBar.selectTabWithId(R.id.tab_trips);
//        }
//        else if(screenToOpen==4)
//        {
//
//            bottomBar.selectTabWithId(R.id.tab_profile);
//        }






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
}



