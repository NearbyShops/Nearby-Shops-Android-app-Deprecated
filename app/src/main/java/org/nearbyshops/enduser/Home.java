package org.nearbyshops.enduser;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.rangebar.IRangeBarFormatter;
import com.appyvet.rangebar.RangeBar;
import com.wunderlist.slidinglayer.SlidingLayer;


import org.nearbyshops.enduser.Carts.CartsListActivity;
import org.nearbyshops.enduser.ItemCategories.ItemCategories;
import org.nearbyshops.enduser.Utility.UtilityGeneral;
import org.nearbyshops.enduser.aaSamples.NavigationDrawerSample;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Home extends AppCompatActivity
        implements View.OnClickListener, RangeBar.OnRangeBarChangeListener, LocationListener, NavigationView.OnNavigationItemSelectedListener {

    // views for navigation drawer


    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;



    // Views
    @Bind(R.id.itemCategories)
    TextView itemCategories;
    @Bind(R.id.rangebarDeliveryRange)
    RangeBar rangeBarDeliveryRange;
    @Bind(R.id.rangebarProximity)
    RangeBar rangeBarProximity;
    @Bind(R.id.textMax)
    TextView textMax;
    @Bind(R.id.textMin)
    TextView textMin;
    @Bind(R.id.shopsNearby)
    TextView shopsNearby;

    @Bind(R.id.itemsNearby)
    TextView itemsNearby;

    @Bind(R.id.setLocation)
    TextView setLocation;

    //Unbinder unbinder;

    SlidingLayer slidingLayer;

    IRangeBarFormatter rangeBarFormatter = new IRangeBarFormatter() {

        @Override
        public String format(String value) {

            value = value + "" + " Km";

            return value;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_nav_layout);

        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //itemCategories = (TextView) findViewById(R.id.itemCategories);

        //if(itemCategories!=null)
        //{
        //  itemCategories.setOnClickListener(this);
        //}


        // setup Range Bars
        rangeBarDeliveryRange.setFormatter(rangeBarFormatter);
        rangeBarProximity.setFormatter(rangeBarFormatter);

        rangeBarDeliveryRange.setOnRangeBarChangeListener(this);
        rangeBarProximity.setOnRangeBarChangeListener(this);

        updateRangeBars();


        requestLocation();


        // sliding layer setup


        slidingLayer = (SlidingLayer)findViewById(R.id.slidingLayer);


        ////slidingLayer.setShadowDrawable(R.drawable.sidebar_shadow);
        //slidingLayer.setShadowSizeRes(R.dimen.shadow_size);
        //slidingLayer.setOffsetDistanceRes(R.dimen.offset_distance);
        //slidingLayer.setPreviewOffsetDistanceRes(R.dimen.preview_offset_distance);
        //slidingLayer.setStickTo(SlidingLayer.STICK_TO_LEFT);
        slidingLayer.setChangeStateOnTap(true);
        slidingLayer.setSlidingEnabled(true);
        slidingLayer.setPreviewOffsetDistance(50);
        slidingLayer.setOffsetDistance(20);
        slidingLayer.setStickTo(SlidingLayer.STICK_TO_RIGHT);




        // navigation drawer setup
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        setupNavigationDrawer();

    } // onCreate() Ends



    void setupNavigationDrawer()
    {

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

    }





    @OnClick(R.id.setLocation)
    void setLocationClick(View view)
    {
        if(slidingLayer.isClosed())
        {
            slidingLayer.openLayer(true);
        }else
        {
            slidingLayer.closeLayer(true);
        }

    }

    @OnClick(R.id.shopsNearby)
    public void shopsButtonClick() {

    }

    @OnClick(R.id.itemsNearby)
    public void itemsNearbyClick() {
        //Intent intent = new Intent(this, SettingsActivity.class);

        //startActivity(intent);
        //savePreferences();

        Intent intent = new Intent(this, NavigationDrawerSample.class);

        startActivity(intent);


        //Intent intent = new Intent(this, Categories.class);

        //startActivity(intent);
    }

    @OnClick(R.id.itemCategories)
    public void itemCategoriesClick() {

        savePreferences();

        Intent intent = new Intent(this, ItemCategories.class);
        startActivity(intent);

    }


    @Override
    public void onClick(View v) {

        //Intent intent = new Intent(this,GeolocationTest.class);

        //startActivity(intent);

        //Intent intent = new Intent(this, ItemCategories.class);

        //startActivity(intent);


    }


    float deliveryRangeMax = 0;
    float deliveryRangeMin= 0;
    float proximity = 0;
    boolean isDeliveryRangeUpdated = false;
    boolean isProximityUpdated = false;

    void updateRangeBars()
    {
        if(UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MAX_KEY)>30)
        {
            return;
        }

        rangeBarDeliveryRange.setRangePinsByValue(
                UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MIN_KEY),
                UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MAX_KEY));

        rangeBarProximity.setSeekPinByValue(
                UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.PROXIMITY_KEY));

    }

    void savePreferences()
    {
        if(isDeliveryRangeUpdated)
        {
            if(deliveryRangeMax > 30 )
            {
                deliveryRangeMax = 30;
            }

            UtilityGeneral.saveInSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MIN_KEY,deliveryRangeMin);
            UtilityGeneral.saveInSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MAX_KEY,deliveryRangeMax);
        }

        if(isProximityUpdated)
        {
            UtilityGeneral.saveInSharedPrefFloat(UtilityGeneral.PROXIMITY_KEY,proximity);
        }
    }

    @Override
    public void onRangeChangeListener(
            RangeBar rangeBar, int leftPinIndex,
            int rightPinIndex, String leftPinValue, String rightPinValue) {


        switch (rangeBar.getId()) {
            case R.id.rangebarDeliveryRange:


                deliveryRangeMax = Float.parseFloat(rightPinValue);
                deliveryRangeMin = Float.parseFloat(leftPinValue);
                isDeliveryRangeUpdated = true;

                //Toast.makeText(this,"RangeBarFired",Toast.LENGTH_SHORT).show();

                break;

            case R.id.rangebarProximity:

                proximity = Float.parseFloat(rightPinValue);
                isProximityUpdated = true;

                //Toast.makeText(this,"RangeBarFired",Toast.LENGTH_SHORT).show();

                break;

            default:

                break;

        }


        if (rangeBar.getId() == R.id.rangebarDeliveryRange) {
            if (rightPinIndex > rangeBarProximity.getTickInterval()) {
                rangeBarProximity.setTickEnd(rightPinIndex);
                rangeBarProximity.setFormatter(rangeBarFormatter);
            }

            //textMax.setText("Max: " + rightPinValue + " Km");
            //textMin.setText("Min: " + leftPinValue + " Km");
        } else if (rangeBar.getId() == R.id.rangebarProximity) {
            textMax.setText("Max: " + rightPinValue + " Km");
            textMin.setText("Min: " + leftPinValue + " Km");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        //unbinder.unbind();
        ButterKnife.unbind(this);
    }



    /*
        The following code is for getting the current location
     */

    final int MY_PERMISSIONS_REQUEST_READ_LOCATION = 1;

    LocationManager mlocationManager;

    public void requestLocation() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


            /// / TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_LOCATION);

            return;
        }

        mlocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5, this);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_LOCATION:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {

                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }

                    mlocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    mlocationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 2000, 5, this);

                }
                break;
        }
    }


    @Override
    public void onLocationChanged(Location location) {

        if(location!=null)
        {

            UtilityGeneral.saveInSharedPrefFloat(UtilityGeneral.LAT_CENTER_KEY,(float)location.getLatitude());
            UtilityGeneral.saveInSharedPrefFloat(UtilityGeneral.LON_CENTER_KEY,(float)location.getLongitude());


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission
                    (this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {


                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                return;
            }

            mlocationManager.removeUpdates(this);
        }

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


    @Override
    protected void onStop() {
        super.onStop();


        if (mlocationManager != null) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                            this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            mlocationManager.removeUpdates(this);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        int id = item.getItemId();

        if (id == R.id.nav_about) {
            // Handle the camera action

            showToastMessage("about");


        } else if (id == R.id.nav_settings) {

            showToastMessage("Settings");

        } else if (id == R.id.nav_carts) {

            Intent intent = new Intent(this, CartsListActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }




}



