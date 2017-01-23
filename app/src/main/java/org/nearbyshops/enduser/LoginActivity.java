package org.nearbyshops.enduser;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.commons.validator.routines.UrlValidator;
import org.nearbyshops.enduser.ModelRoles.EndUser;
import org.nearbyshops.enduser.ModelServiceConfig.ServiceConfigurationLocal;
import org.nearbyshops.enduser.Utility.UtilityLogin;
import org.nearbyshops.enduser.UtilityGeocoding.Constants;
import org.nearbyshops.enduser.UtilityGeocoding.FetchAddressIntentService;
import org.nearbyshops.enduser.Model.Service;
import org.nearbyshops.enduser.RetrofitRESTContract.EndUserService;
import org.nearbyshops.enduser.RetrofitRESTContract.ServiceConfigurationService;
import org.nearbyshops.enduser.Utility.UtilityGeneral;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Target, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {


    @Bind(R.id.serviceURLEditText)
    EditText serviceUrlEditText;

    @Bind(R.id.distributorIDEdittext)
    EditText distributorIDEditText;

    @Bind(R.id.distributorPassword)
    EditText password;

    @Bind(R.id.loginButton)
    Button loginButton;

    @Bind(R.id.signUpButton)
    Button signUpButton;


    @Bind(R.id.inputLayoutServiceURL)
    TextInputLayout inputLayoutServiceURL;


    @Bind(R.id.urlValidText)
    TextView urlValidText;


    @Bind(R.id.login_backdrop)
    ImageView loginBackdrop;

    @Bind(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Bind(R.id.serviceURLBar)
    LinearLayout serviceBar;


    // location variables
    //private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;



//    ServiceConfigurationService configurationService;

    Toolbar toolbar;

    UrlValidator urlValidator = null;

    public LoginActivity() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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


        serviceUrlEditText.setText(UtilityGeneral.getServiceURL(MyApplication.getAppContext()));
        //distributorIDEditText.setText(String.valueOf(UtilityGeneral.getEndUserID(MyApplication.getAppContext())));

        //makeServiceConfigCall();

        String[] schemes = {"http", "https"};

        urlValidator = new UrlValidator(schemes);


//        makeServiceConfigCall();


        serviceUrlEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //UtilityGeneral.saveServiceURL(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

                //URLUtil.isValidUrl(s.toString())

                if (urlValidator.isValid(s.toString())) {
                    UtilityGeneral.saveServiceURL(s.toString());


                    //inputLayoutServiceURL.setErrorEnabled(false);

                    //inputLayoutServiceURL.setError(null);
                    //inputLayoutServiceURL.setErrorEnabled(false);
                    urlValidText.setVisibility(View.GONE);
//                    makeServiceConfigCall();

                } else {
                    //showToastMessage("Invalid URL");

                    //inputLayoutServiceURL.setErrorEnabled(true);
                    //inputLayoutServiceURL.setError("Invalid URL");

                    //Log.d("applog","Invalid URL");

                    urlValidText.setVisibility(View.VISIBLE);
                    urlValidText.setText("Invalid URL: Please Enter a valid URL");

                }

            }
        });


    }// onCreate Ends


    /*void makeServiceConfigCall() {


        if (!urlValidator.isValid(UtilityGeneral.getServiceURL(MyApplication.getAppContext()))) {
            return;
        }


        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(UtilityGeneral.getServiceURL(MyApplication.getAppContext()))
                .build();


        ServiceConfigurationService service = retrofit.create(ServiceConfigurationService.class);


        Call<ServiceConfigurationLocal> call = service.getServiceConfiguration();

        call.enqueue(new Callback<ServiceConfigurationLocal>() {
            @Override
            public void onResponse(Call<Service> call, Response<Service> response) {

                if (response.body() != null) {
                    Service service = response.body();

                    loadImage(service.getImagePath());
                }
            }

            @Override
            public void onFailure(Call<Service> call, Throwable t) {

//                showToastMessage("Network request failed !");

            }
        });
    }*/


    void loadImage(String serviceImagePath) {

        String imagePath = UtilityGeneral.getConfigImageEndpointURL(MyApplication.getAppContext())
                + serviceImagePath;

        if(loginBackdrop!=null)
        {
            Picasso.with(this)
                    .load(imagePath)
                    .placeholder(R.drawable.images)
                    .into(loginBackdrop);
        }


        Picasso.with(this)
                .load(imagePath)
                .placeholder(R.drawable.images)
                .into(this);

    }


    // obtaining colors dynamically from the backdrop image poster
    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {


        Palette palette = Palette.from(bitmap).generate();

        int color = 323235;
        int vibrant = palette.getVibrantColor(color);
        int vibrantLight = palette.getLightVibrantColor(color);
        int vibrantDark = palette.getDarkVibrantColor(color);
        int muted = palette.getMutedColor(color);
        int mutedLight = palette.getLightMutedColor(color);
        int mutedDark = palette.getDarkMutedColor(color);

        Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();

        //if(vibrantSwatch!=null) {
        //  originalTitle.setTextColor(vibrantSwatch.getTitleTextColor());
        //}


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(vibrantDark);


        }

        inputLayoutServiceURL.setBackgroundColor(vibrantDark);
        serviceBar.setBackgroundColor(muted);

//        if(fab!=null && vibrantDark!=0) {
//
//            fab.setBackgroundTintList(ColorStateList.valueOf(vibrantDark));
//
//        }//fab.setBackgroundColor(vibrantDark);


        if (collapsingToolbarLayout != null) {

            collapsingToolbarLayout.setContentScrimColor(vibrant);

        }

    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }


    void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.loginButton:

                startActivity(new Intent(this, Home.class));

                break;

        }

    }


    @OnClick(R.id.loginButton)
    public void login() {
        makeLoginRequest();
    }


    void makeLoginRequest() {


        if (!urlValidator.isValid(UtilityGeneral.getServiceURL(MyApplication.getAppContext()))) {
            return;
        }


        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(UtilityGeneral.getServiceURL(MyApplication.getAppContext()))
                .build();


        EndUserService service = retrofit.create(EndUserService.class);


        final String username = distributorIDEditText.getText().toString();
        final String passwordstr = password.getText().toString();


        if (!username.equals("") && !passwordstr.equals("")) {

            Call<EndUser> call = service.EndUserLogin(UtilityLogin.baseEncoding(username,passwordstr));


            call.enqueue(new Callback<EndUser>() {
                @Override
                public void onResponse(Call<EndUser> call, Response<EndUser> response) {


                    if (response.code() == 200) {
                        showToastMessage("LoginActivity Successful");


                        if (response.body() != null) {


                            UtilityLogin.saveCredentials(LoginActivity.this,username,passwordstr);
//                            UtilityGeneral.saveEndUserID(response.body().getEndUserID());

//                            showToastMessage(response.body().getEndUserName() + " : " + response.body().getEndUserID());

                            startActivity(new Intent(LoginActivity.this, Home.class));
                        }


                    } else if (response.code() == 401) {
                        showToastMessage("Username or password is incorrect");
                    } else {
                        showToastMessage("Server Error !");
                    }


                }

                @Override
                public void onFailure(Call<EndUser> call, Throwable t) {

                    showToastMessage("Network request failed !");
                }
            });

        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }






    // location code


    @Override
    protected void onStart() {

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

        super.onStart();
    }


    @Override
    protected void onStop() {

        if (mGoogleApiClient != null) {

            mGoogleApiClient.disconnect();
        }


        super.onStop();
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (mGoogleApiClient.isConnected()) {

            startLocationUpdates();
        }


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
                                    LoginActivity.this,
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


                showToastMessage("Not granted !");
            }


        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
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

        UtilityGeneral.saveInSharedPrefFloat(UtilityGeneral.LAT_CENTER_KEY,(float)location.getLatitude());
        UtilityGeneral.saveInSharedPrefFloat(UtilityGeneral.LON_CENTER_KEY,(float)location.getLongitude());
    }



    // location code





    // address resolution code



    private AddressResultReceiver mResultReceiver = new AddressResultReceiver();

    protected void startIntentService(Location location) {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }




    @SuppressLint("ParcelCreator")
    class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver() {

            super(new Handler());
        }



        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string
            // or an error message sent from the intent service.

            String mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);

//            locationdata.setText(mAddressOutput);


            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {

                showToastMessage(getString(R.string.address_found));

            }

        }
    }



    /*

    void displayResult(Location location,String message)
    {

        locationdata.setText( "Message : " + message +
                            "\nAccuracy : " + String.valueOf(mLastLocation.getAccuracy()) + " meters"
                        + "\nProvider : " + String.valueOf(mLastLocation.getProvider())
                        + "\nLatitude : " + String.valueOf(mLastLocation.getLatitude())
                        + "\nLongitude" + String.valueOf(mLastLocation.getLongitude()));

    }

    */

    // address resolution code ends




}


