package org.nearbyshops.enduserapp;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.ResultReceiver;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.GsonBuilder;


import org.apache.commons.validator.routines.UrlValidator;
import org.nearbyshops.enduserapp.Carts.CartsListActivity;
import org.nearbyshops.enduserapp.DeliveryAddress.DeliveryAddressActivity;
import org.nearbyshops.enduserapp.EditProfileEndUser.EditEndUserFragment;
import org.nearbyshops.enduserapp.EditProfileEndUser.EditProfileEndUser;
import org.nearbyshops.enduserapp.FilterShops.FilterShops;
import org.nearbyshops.enduserapp.ItemsByCategoryTypeSimple.ItemCategoriesSimple;
import org.nearbyshops.enduserapp.Items.ItemsActivity;
import org.nearbyshops.enduserapp.Login.LoginDialog;
import org.nearbyshops.enduserapp.Login.NotifyAboutLogin;
import org.nearbyshops.enduserapp.ModelRoles.EndUser;
import org.nearbyshops.enduserapp.ModelServiceConfig.ServiceConfigurationLocal;
import org.nearbyshops.enduserapp.Notifications.SSEIntentServiceUser;
import org.nearbyshops.enduserapp.OrdersHomePickFromShop.OrdersHomePickFromShop;
import org.nearbyshops.enduserapp.RetrofitRESTContract.ServiceConfigurationService;
import org.nearbyshops.enduserapp.Services.ServicesActivity;
import org.nearbyshops.enduserapp.Settings.SettingsCustom;
import org.nearbyshops.enduserapp.SharedPreferences.UtilityLocationOld;
import org.nearbyshops.enduserapp.Shops.ShopsActivity;
import org.nearbyshops.enduserapp.Shops.UtilityLocation;
import org.nearbyshops.enduserapp.ShopsByCatSimple.ShopsByCat;
import org.nearbyshops.enduserapp.Utility.UtilityLogin;
import org.nearbyshops.enduserapp.UtilityGeocoding.Constants;
import org.nearbyshops.enduserapp.UtilityGeocoding.FetchAddressIntentService;
import org.nearbyshops.enduserapp.OrdersHomeDelivery.OrderHome;
import org.nearbyshops.enduserapp.Utility.UtilityGeneral;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscription;


public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,  GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener , NotifyAboutLogin{

    // views for navigation drawer

    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;

//    @Bind(R.id.shop_filters)
//    LinearLayout shopFilters;

    // Views
//    @Bind(R.id.option_item_categories)
//    RelativeLayout itemCategories;

    @Bind(R.id.serviceURL)
    EditText serviceURL;

    @Bind(R.id.text_input_service_url) TextInputLayout textInputServiceURL;

    UrlValidator urlValidator;


    // location variables
    //private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;

    @Bind(R.id.text_lat_lon)
    TextView text_lat_longitude;

    // location variables ends

    int delivery_range_current_min = ServiceConstants.DELIVERY_RANGE_CITY_MIN;
    int delivery_range_current_max = ServiceConstants.DELIVERY_RANGE_CITY_MAX;


    Subscription editTextSub;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_nav_layout);

        ButterKnife.bind(this);

        // Location Code

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        // Location code ends



        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);





        // navigation drawer setup
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        setupNavigationDrawer();


        if(UtilityGeneral.getServiceURL(this).equals("http://nearbyshops.org"))
        {
//            showLoginDialog();
//            showLoginDialog();
        }


        String[] schemes = {"http", "https"};

        urlValidator = new UrlValidator(schemes);

        serviceURL.setText(UtilityGeneral.getServiceURL(this));

        serviceURL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (urlValidator.isValid(s.toString())) {
                    UtilityGeneral.saveServiceURL(s.toString(),Home.this);
                    textInputServiceURL.setError(null);
                    textInputServiceURL.setErrorEnabled(false);
                    updateStatusLight();
                }
                else
                {
//                    serviceURL.setError("URL Invalid");
                    textInputServiceURL.setErrorEnabled(true);
                    textInputServiceURL.setError("Invalid URL");

                    UtilityGeneral.saveServiceLightStatus(Home.this,STATUS_LIGHT_RED);
                    setStatusLight();
                }

            }
        });



//        setlabelLogin();

        setStatusLight();

    } // onCreate() Ends



    /*void bindEditTextServiceURL()
    {

        editTextSub = RxTextView
                .textChanges(serviceURL)
                .debounce(700, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence value) {

                        // do some work with new text


                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        System.out.println(throwable.toString());

                    }
                });
    }*/





    void updateStatusLight()
    {

        GsonBuilder gsonBuilder = new GsonBuilder();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .baseUrl(UtilityGeneral.getServiceURL(MyApplication.getAppContext()))
                .build();

        ServiceConfigurationService service = retrofit.create(ServiceConfigurationService.class);

        Call<ServiceConfigurationLocal> call = service.getServiceConfiguration(
                UtilityLocation.getLatitude(this),
                UtilityLocation.getLongitude(this)
        );


        call.enqueue(new Callback<ServiceConfigurationLocal>() {
            @Override
            public void onResponse(Call<ServiceConfigurationLocal> call, Response<ServiceConfigurationLocal> response) {

                if(response.code()==200)
                {
                    if(response.body()!=null)
                    {
                        ServiceConfigurationLocal configurationLocal = response.body();
                        UtilityGeneral.saveConfiguration(configurationLocal,Home.this);


//                        if(UtilityLocation.getLongitude(Home.this)!=null && UtilityLocation.getLatitude(Home.this)!=null)
//                        {

//                            Location locationUser = new Location("user");
//                            locationUser.setLongitude(UtilityLocation.getLongitude(Home.this));
//                            locationUser.setLatitude(UtilityLocation.getLatitude(Home.this));
//
//                            Location locationProvider = new Location("provider");
//                            locationProvider.setLatitude(configurationLocal.getLatCenter());
//                            locationProvider.setLongitude(configurationLocal.getLonCenter());


//                            float distance  = locationProvider.distanceTo(locationUser);


                            if(configurationLocal.getRt_distance()<=configurationLocal.getServiceRange())
                            {
                                UtilityGeneral.saveServiceLightStatus(Home.this,STATUS_LIGHT_GREEN);
                                setStatusLight();
                            }
                            else
                            {
                                UtilityGeneral.saveServiceLightStatus(Home.this,STATUS_LIGHT_YELLOW);
                                setStatusLight();
                            }
//                        }
//                        else
//                        {
//                            UtilityGeneral.saveServiceLightStatus(Home.this,STATUS_LIGHT_YELLOW);
//                            setStatusLight();
//                        }


                    }
                    else
                    {
                        UtilityGeneral.saveServiceLightStatus(Home.this,STATUS_LIGHT_RED);
                        setStatusLight();
                    }
                }
                else
                {
                    UtilityGeneral.saveServiceLightStatus(Home.this,STATUS_LIGHT_RED);
                    setStatusLight();
                }
            }

            @Override
            public void onFailure(Call<ServiceConfigurationLocal> call, Throwable t) {


                UtilityGeneral.saveServiceLightStatus(Home.this,STATUS_LIGHT_RED);
                setStatusLight();

            }
        });

    }


    @Bind(R.id.status_indicator_one) TextView statusLight;
    public static final int STATUS_LIGHT_GREEN = 1;
    public static final int STATUS_LIGHT_YELLOW = 2;
    public static final int STATUS_LIGHT_RED = 3;


    void setStatusLight()
    {
        int status = UtilityGeneral.getServiceLightStatus(this);

        if(status == STATUS_LIGHT_GREEN)
        {
            statusLight.setBackgroundColor(ContextCompat.getColor(this,R.color.gplus_color_1));
        }
        else if(status == STATUS_LIGHT_YELLOW)
        {
            statusLight.setBackgroundColor(ContextCompat.getColor(this,R.color.gplus_color_2));
        }
        else if(status == STATUS_LIGHT_RED)
        {
            statusLight.setBackgroundColor(ContextCompat.getColor(this,R.color.deepOrange900));
        }

    }


    private void showLoginDialog()
    {
        FragmentManager fm = getSupportFragmentManager();
        LoginDialog loginDialog = new LoginDialog();
        loginDialog.show(fm,"serviceUrl");
    }







    @SuppressWarnings("RestrictedApi")
    void setupNavigationDrawer()
    {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

    }



//    @OnClick(R.id.option_item_categories)
//    public void itemCategoriesClick() {
//
//        Intent intent = new Intent(this, ShopItemSwipeView.class);
//
//        Intent intent = new Intent(this, ItemsByCategory.class);
//        startActivity(intent);
//    }


    @OnClick(R.id.option_shops_by_category)
    public void shopsByCategoryClick() {

//        Intent intent = new Intent(this, ShopsByCategory.class);
//        startActivity(intent);

        startActivity(new Intent(this, ShopsByCat.class));
    }


    @OnClick(R.id.option_shops_nearby)
    public void shopsNearby()
    {
        Intent intent = new Intent(this, ShopsActivity.class);
        startActivity(intent);
    }




    @OnClick(R.id.option_items)
    public void optionItemsClick()
    {
        Intent intent = new Intent(this, ItemsActivity.class);
        startActivity(intent);
    }



//    @OnClick(R.id.option_items_by_category_format_two)
//    public void optionFormatTwo()
//    {
//        Intent intent = new Intent(this,ItemsByCatS2.class);
//        startActivity(intent);
//    }


    @OnClick(R.id.option_items_by_category)
    void optionItemCatSimple()
    {
        Intent intent = new Intent(this, ItemCategoriesSimple.class);
        startActivity(intent);
    }




    boolean location_block_visible = false;

    @Bind(R.id.location_settings_block)
    RelativeLayout locationBlock;


    @OnClick(R.id.show_hide_location_settings)
    public void showHideLocationBlock(View view)
    {
        if(location_block_visible)
        {
            locationBlock.setVisibility(View.GONE);
            location_block_visible = false;

        }else
        {
            locationBlock.setVisibility(View.VISIBLE);
            location_block_visible = true;
        }
    }



    boolean market_information_visible = false;

    @Bind(R.id.market_information)
    CardView market_information_block;

    @OnClick(R.id.market_information_label)
    void showHideMarketInformation(View view)
    {
        if(market_information_visible)
        {

            market_information_block.setVisibility(View.GONE);
            market_information_visible = false;
        }
        else
        {
            market_information_block.setVisibility(View.VISIBLE);
            market_information_visible = true;
        }
    }






    @Override
    protected void onDestroy() {
        super.onDestroy();

        //unbinder.unbind();
        ButterKnife.unbind(this);


        if(editTextSub!=null && !editTextSub.isUnsubscribed())
        {
            editTextSub.unsubscribe();
        }
    }



    void updateLoginMenuVisibility()
    {
        if(UtilityLogin.getEndUser(this)==null)
        {
            // logged out
            navigationView.getMenu().findItem(R.id.nav_edit_profile).setVisible(false);
        }
        else
        {
            navigationView.getMenu().findItem(R.id.nav_edit_profile).setVisible(true);
        }
    }




    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        int id = item.getItemId();

        if (id == R.id.nav_about_service) {
            // Handle the camera action
            showToastMessage("about");

        } else if (id == R.id.nav_settings) {

            startActivity(new Intent(this, SettingsCustom.class));

        } else if (id == R.id.nav_carts) {

            Intent intent = new Intent(this, CartsListActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_orders) {

            startActivity(new Intent(this, OrderHome.class));

        }
        else if(id == R.id.nav_orders_pick_from_shop)
        {
            startActivity(new Intent(this, OrdersHomePickFromShop.class));
        }
        else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        else if(id==R.id.nav_edit_profile)
        {

            Intent intent = new Intent(this, EditProfileEndUser.class);
            intent.putExtra(EditEndUserFragment.EDIT_MODE_INTENT_KEY,EditEndUserFragment.MODE_UPDATE);
            startActivity(intent);

        }
        else if(id == R.id.nav_login) {

            loginClick(item);
        }
        else if(id == R.id.nav_delivery_address)
        {
            startActivity(new Intent(this, DeliveryAddressActivity.class));
        }

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    void loginClick(MenuItem item)
    {
        if(UtilityLogin.getEndUser(this)==null)
        {
            showLoginDialog();
            navigationView.getMenu().findItem(R.id.nav_edit_profile).setVisible(true);
        }
        else
        {
            UtilityLogin.saveEndUser(null,this);
            UtilityLogin.saveCredentials(this,null,null);

            item.setTitle("Login");

            showToastMessage("You are logged out !");
            navigationView.getMenu().findItem(R.id.nav_edit_profile).setVisible(false);
        }

    }


    @SuppressWarnings("RestrictedApi")
    void setlabelLogin()
    {
        if(UtilityLogin.getEndUser(this)==null)
        {
            navigationView.getMenu().getItem(0).setTitle("Login");
        }
        else
        {
            navigationView.getMenu().getItem(0).setTitle("Logout");
        }
    }



    @Override
    @SuppressWarnings("RestrictedApi")
    public void NotifyLogin() {

        navigationView.getMenu().getItem(0).setTitle("Logout");

        showToastMessage("You are logged In !");
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




    // location code begins

    // location code


    @Override
    protected void onStart() {

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

//        showToastMessage("onStart");

        super.onStart();
    }


    @Override
    protected void onStop() {

        super.onStop();

        if (mGoogleApiClient != null) {

            mGoogleApiClient.disconnect();
        }

    }




    @Override
    protected void onResume() {
        super.onResume();

        setupNotifications();

        setlabelLogin();
        updateLoginMenuVisibility();
//        bindEditTextServiceURL();
    }


    @Override
    protected void onPause() {
        super.onPause();


        stopLocationUpdates();
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

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);


            return;
        }


        if (mGoogleApiClient == null) {

            return;
        }


        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        if (mLastLocation != null) {

            saveLocation(mLastLocation);


        }else
        {

            // if getlastlocation does not work then request the device to get the current location.
            createLocationRequest();


            if(mLocationRequest!=null)
            {
                startLocationUpdates();
            }

        }
    }




    private static final int REQUEST_CHECK_SETTINGS = 3;


    protected void createLocationRequest() {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());


        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {

                final Status status = locationSettingsResult.getStatus();
                final LocationSettingsStates states = locationSettingsResult.getLocationSettingsStates();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    Home.this,
                                    REQUEST_CHECK_SETTINGS);

                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        // ...
                        break;

                }
            }

        });
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHECK_SETTINGS) {

            if (resultCode == RESULT_OK) {


                showToastMessage("Permission granted !");

                onConnected(null);

            } else {


                showToastMessage("Permission not granted !");
            }
        }
    }




    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.d("applog","Google api client connection failed !");

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


            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    2);

            return;
        }


        if(mGoogleApiClient.isConnected())
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }

    }



    protected void stopLocationUpdates() {

        if(mGoogleApiClient.isConnected())
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }

    }


    @Override
    public void onLocationChanged(Location location) {

        saveLocation(location);
        stopLocationUpdates();
    }




    void saveLocation(Location location)
    {

        text_lat_longitude.setText("Latitude    : " + String.format("%.4f",location.getLatitude())
                + "\nLongitude : " + String.format("%.4f",location.getLongitude()));

        startIntentService(location);

        UtilityGeneral.saveInSharedPrefFloat(UtilityGeneral.LAT_CENTER_KEY,(float)location.getLatitude());
        UtilityGeneral.saveInSharedPrefFloat(UtilityGeneral.LON_CENTER_KEY,(float)location.getLongitude());

        org.nearbyshops.enduserapp.Shops.UtilityLocation.saveLatitude((float) location.getLatitude(),this);
        org.nearbyshops.enduserapp.Shops.UtilityLocation.saveLongitude((float) location.getLongitude(),this);

        UtilityLocationOld.saveCurrentLocation(this,location);

    }


    @OnClick(R.id.text_update)
    void updateLocationClick(View view)
    {
        // if getlastlocation does not work then request the device to get the current location.
        createLocationRequest();


        if(mLocationRequest!=null)
        {
            startLocationUpdates();
        }

    }


    // location code Ends



    // address resolution code

    @Bind(R.id.text_address)
    TextView text_address;


    private AddressResultReceiver mResultReceiver = new AddressResultReceiver();

    protected void startIntentService(Location location) {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }



    @SuppressLint("ParcelCreator")
    @SuppressWarnings("RestrictedApi")
    class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver() {

            super(new Handler());
        }



        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string
            // or an error message sent from the intent service.

            String mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);

            if(mAddressOutput!=null && text_address!=null)
            {
                text_address.setText(mAddressOutput);
            }



            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                //showToastMessage(getString(R.string.address_found));
            }

        }
    }

    // address resolution code ends




    // handle results for permission request


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case 1:

                if(grantResults.length>0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                    onConnected(null);

                }
                else
                {
                    showToastMessage("Permission denied cant access location !");
                }


                break;


            case 2:

                if(grantResults.length>0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                    startLocationUpdates();

                }
                else
                {
                    showToastMessage("Permission denied cant access location !");
                }

            default:

            break;

        }
    }



    @OnClick(R.id.filter_shops)
    void showHideFilters()
    {
//        FragmentManager fm = getSupportFragmentManager();
//        FilterShopsDialogMain filterShopsDialog = new FilterShopsDialogMain();
//        filterShopsDialog.show(fm,"serviceUrl");

        Intent intent = new Intent(this, FilterShops.class);
        startActivity(intent);
    }



    @OnClick(R.id.discover_services_button)
    void discoverServicesClick()
    {
        startActivity(new Intent(this, ServicesActivity.class));
    }


    @OnClick(R.id.paste_url_button)
    void pasteURLClick()
    {
        ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);

        if(clipboard.getPrimaryClip()!=null)
        {
            serviceURL.setText(clipboard.getPrimaryClip().getItemAt(0).getText());
        }
    }






    // setup Notifications for the User using SSE (Server Sent Events)



    @OnClick(R.id.experimental_features)
    void setupNotifications()
    {
        EndUser endUser = UtilityLogin.getEndUser(this);

        System.out.println("Setup Notifications !");

        if(endUser!=null)
        {
            System.out.println("Setup Notifications : End USER ID : " + String.valueOf(endUser.getEndUserID()));

            Intent intent = new Intent(this, SSEIntentServiceUser.class);
            intent.putExtra(SSEIntentServiceUser.END_USER_ID, endUser.getEndUserID());
            startService(intent);
        }
    }


}