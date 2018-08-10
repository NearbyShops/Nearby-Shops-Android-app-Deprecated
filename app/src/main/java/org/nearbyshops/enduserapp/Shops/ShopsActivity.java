package org.nearbyshops.enduserapp.Shops;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wunderlist.slidinglayer.SlidingLayer;

import org.nearbyshops.enduserapp.Model.Shop;
import org.nearbyshops.enduserapp.Notifications.NonStopService.LocationUpdateService;
import org.nearbyshops.enduserapp.R;
import org.nearbyshops.enduserapp.Shops.Interfaces.GetDataset;
import org.nearbyshops.enduserapp.Shops.Interfaces.NotifyDatasetChanged;
import org.nearbyshops.enduserapp.Interfaces.NotifySearch;
import org.nearbyshops.enduserapp.Shops.ListFragment.FragmentShopTwo;
import org.nearbyshops.enduserapp.Shops.MapsFragment.ShopMapFragment;
import org.nearbyshops.enduserapp.ShopsByCategoryOld.Interfaces.NotifySort;
import org.nearbyshops.enduserapp.ShopsByCategoryOld.Interfaces.NotifyTitleChanged;
import org.nearbyshops.enduserapp.Shops.SlidingLayerSort.SlidingLayerSortShops;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;

public class ShopsActivity extends AppCompatActivity implements NotifyTitleChanged,
        NotifySort , GetDataset{

    @State
    boolean isMapView = false;

    @State
    ArrayList<Shop> dataset = new ArrayList<>();

    @BindView(R.id.shop_count_indicator)
    TextView shopsCount;

    @BindView(R.id.slidingLayer)
    SlidingLayer slidingLayer;

//    FragmentShopTwo fragmentShopTwo;
//    private ShopMapFragment shopMapFragment;

    public static final String TAG_SHOP_FRAGMENT = "shop_fragment";
    public static final String TAG_MAP_FRAGMENT = "map_tag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shops);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportFragmentManager().findFragmentByTag(TAG_SHOP_FRAGMENT)==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, FragmentShopTwo.newInstance(false),TAG_SHOP_FRAGMENT)
                    .commit();

        }




        if(savedInstanceState != null)
        {
            onRestoreInstanceState(savedInstanceState);

            Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_SHOP_FRAGMENT);

            if(fragment instanceof NotifyDatasetChanged)
            {
                ((NotifyDatasetChanged)fragment).notifyDatasetChanged();
            }
        }




        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupSlidingLayer();


        if(isMapView)
        {
            appBarLayout.setExpanded(false,true);
        }


        startService(new Intent(this, LocationUpdateService.class));


    }



    void setupSlidingLayer()
    {

        ////slidingLayer.setShadowDrawable(R.drawable.sidebar_shadow);
        //slidingLayer.setShadowSizeRes(R.dimen.shadow_size);



        if(slidingLayer!=null) {

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            slidingLayer.setChangeStateOnTap(true);
            slidingLayer.setSlidingEnabled(true);
//            slidingLayer.setPreviewOffsetDistance(55);
            slidingLayer.setOffsetDistance(30); //(int)(20 * metrics.density)
            slidingLayer.setStickTo(SlidingLayer.STICK_TO_RIGHT);

            //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(250, ViewGroup.LayoutParams.MATCH_PARENT);

            //slidingContents.setLayoutParams(layoutParams);

            //slidingContents.setMinimumWidth(metrics.widthPixels-50);


            if (getSupportFragmentManager().findFragmentByTag("sliding_layer")==null)
            {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.slidinglayerfragment,new SlidingLayerSortShops(),"sliding_layer")
                        .commit();
            }

        }
    }



    @OnClick({R.id.icon_sort,R.id.text_sort})
    void sortClick()
    {
        slidingLayer.openLayer(true);
    }


    @BindView(R.id.icon_list)
    ImageView mapIcon;

    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;

    @OnClick({R.id.icon_list,R.id.text_list})
    void listMapClick()
    {

        if(isMapView)
        {

            Drawable icon = VectorDrawableCompat
                    .create(getResources(),
                            R.drawable.ic_map_white_24px, getTheme());

            mapIcon.setImageDrawable(icon);


            /*if(fragmentShopTwo == null)
            {
                fragmentShopTwo = FragmentShopTwo.newInstance(true);
            }*/

//            fragmentShopTwo = FragmentShopTwo.newInstance();

            /*getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack("name")
                    .replace(R.id.fragment_container, new FragmentShopTwo().newInstance(true),"shop_fragment")
                    .commit();*/

//            getSupportFragmentManager().popBackStackImmediate();



            if(getSupportFragmentManager().findFragmentByTag(TAG_SHOP_FRAGMENT)==null)
            {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container, FragmentShopTwo.newInstance(false),TAG_SHOP_FRAGMENT)
                        .commit();

            }
            else
            {
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(getSupportFragmentManager().findFragmentByTag("map_tag"))
                        .commit();


                getSupportFragmentManager()
                        .beginTransaction()
                        .show(getSupportFragmentManager().findFragmentByTag("shop_fragment"))
                        .commit();
            }



            appBarLayout.setExpanded(true,true);
            isMapView = false;
        }
        else
        {


            Drawable icon = VectorDrawableCompat
                    .create(getResources(),
                            R.drawable.ic_view_list_white_24px, getTheme());



            mapIcon.setImageDrawable(icon);



            /*if(shopMapFragment == null)
            {

            }*/

//            shopMapFragment = new ShopMapFragment();


            if(getSupportFragmentManager().findFragmentByTag("map_tag")==null)
            {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container, new ShopMapFragment(),"map_tag")
                        .commit();

            }
            else
            {

                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(getSupportFragmentManager().findFragmentByTag("shop_fragment"))
                        .commit();


                getSupportFragmentManager()
                        .beginTransaction()
                        .show(getSupportFragmentManager().findFragmentByTag("map_tag"))
                        .commit();

            }


            //.addToBackStack("map")

            appBarLayout.setExpanded(false,true);
            isMapView = true;
        }

    }








    @Override
    public void notifySortChanged() {

//        Fragment fragment = (Fragment) mPagerAdapter.instantiateItem(mViewPager,mViewPager.getCurrentItem());

//        Fragment fragment = getSupportFragmentManager().findFragmentByTag("shop_fragment");


        Fragment fragment = getSupportFragmentManager().findFragmentByTag("shop_fragment");

        if(fragment instanceof NotifySort)
        {
            ((NotifySort)fragment).notifySortChanged();
        }




    }


    @Override
    public void NotifyTitleChanged(String title, int tabPosition) {
        shopsCount.setText(title);
    }

    @Override
    public void titleChanged(int i, int size, int item_count) {

    }




    /*@Override
    public void setDataset(ArrayList<Shop> dataset) {

        this.dataset = dataset;
    }*/


    @Override
    public ArrayList<Shop> getDataset() {

        return dataset;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this,outState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_shops_screen, menu);


        // Associate searchable configuration with the SearchView
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.action_search), new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {

                Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_SHOP_FRAGMENT);

                if(fragment instanceof NotifySearch)
                {
                    ((NotifySearch) fragment).endSearchMode();
                }

//                Toast.makeText(ShopsActivity.this, "onCollapsed Called ", Toast.LENGTH_SHORT).show();

                return true;
            }
        });





//        searchView.setSubmitButtonEnabled(true);
//        searchView.onActionViewCollapsed();

        searchManager.setOnCancelListener(new SearchManager.OnCancelListener() {
            @Override
            public void onCancel() {

                Toast.makeText(ShopsActivity.this, "cancelled Called ", Toast.LENGTH_SHORT).show();
            }
        });

        searchManager.setOnDismissListener(new SearchManager.OnDismissListener() {
            @Override
            public void onDismiss() {

                Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_SHOP_FRAGMENT);

                if(fragment instanceof NotifySearch)
                {
                    ((NotifySearch) fragment).endSearchMode();
                }

                Toast.makeText(ShopsActivity.this, "onDismiss Called ", Toast.LENGTH_SHORT).show();

            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {



                Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_SHOP_FRAGMENT);

                if(fragment instanceof NotifySearch)
                {
                    ((NotifySearch) fragment).endSearchMode();
                }

                Toast.makeText(ShopsActivity.this, "Search Closed", Toast.LENGTH_SHORT).show();

                return false;
            }
        });


        return true;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleIntent(intent);
    }


    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
//            Toast.makeText(this,query,Toast.LENGTH_SHORT).show();

            Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_SHOP_FRAGMENT);

            if(fragment instanceof NotifySearch)
            {
                ((NotifySearch) fragment).search(query);
            }
        }
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Icepick.restoreInstanceState(this,savedInstanceState);
    }




    // broadcast location service


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, LocationUpdateService.class));
    }










    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter(LocationUpdateService.ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(testReceiver, filter);

    }






    // Define the callback for what to do when data is received
    private BroadcastReceiver testReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

//            int resultCode = intent.getIntExtra("resultCode", RESULT_CANCELED);
//            if (resultCode == RESULT_OK) {
//                String resultValue = intent.getStringExtra("resultValue");
//                Toast.makeText(getActivity(), "Location Updated ", Toast.LENGTH_SHORT).show();
//            }

//            if(isDestroyed)
//            {
//                return;
//            }

            Toast.makeText(ShopsActivity.this, "Location Updated ", Toast.LENGTH_SHORT).show();
        }
    };




}
