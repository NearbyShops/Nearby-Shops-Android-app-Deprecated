//package org.nearbyshops.enduserappnew.PlacePickerGoogle;//package org.taxireferralservice.outstation.SelectLocationGoogle;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.ActivityInfo;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.core.app.ActivityCompat;
//import androidx.fragment.app.Fragment;
//
//import com.google.android.gms.location.LocationAvailability;
//import com.google.android.gms.location.LocationCallback;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationResult;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.libraries.places.api.model.Place;
//import com.google.android.libraries.places.api.model.RectangularBounds;
//import com.google.android.libraries.places.widget.Autocomplete;
//import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.gson.Gson;
//
//
//import org.nearbyshops.enduserappnew.Preferences.PrefLocation;
//import org.nearbyshops.enduserappnew.R;
//
//import java.util.Arrays;
//import java.util.List;
//
//import javax.inject.Inject;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//import static android.app.Activity.RESULT_OK;
//
///**
// * Created by sumeet on 4/1/18.
// */
//
//public class PlacePickerWithMapFragment extends Fragment {
//
//    @Inject Gson gson;
//
//    String searchText;
//
//
//    private GoogleMap googleMapInstance;
//
//
//    private LocationRequest mLocationRequest;
//    private LocationWithAddress selectedLocation = new LocationWithAddress();
//    private LocationWithAddress selectedFromGeocoder = new LocationWithAddress();
//
////    private LocationWithAddress selectedLocation = new LocationWithAddress();
//
//
//
////    boolean isPickupSelected = false;
//
//    private boolean isMapCentered = false;
//
//    private double latCurrent = -1;
//    private double lonCurrent = -1;
//
//
//    @BindView(R.id.fab_current)
//    FloatingActionButton fabCurrent;
////    @BindView(R.id.progress_bar_selected) ProgressBar progressBarSelected;
//
//    @BindView(R.id.instructions) TextView instructionsText;
//    @BindView(R.id.instructions_box) LinearLayout instructionsBox;
//
//
//    /* Variables for text search */
//
////    @BindView(R.id.swipe_container) SwipeRefreshLayout swipeContainer;
////    @BindView(R.id.recycler_view) RecyclerView recyclerView;
//
////    GridLayoutManager layoutManager;
////    AdapterOpenNames listAdapter;
//
////    ArrayList<Object> dataset = new ArrayList<>();
//
////    @BindView(R.id.search_text) EditText searchBox;
//
//
//    private boolean isDestroyed;
//
//
//    @BindView(R.id.pickup) TextView pickupAddress;
////    @BindView(R.id.destination) TextView destinationAddress;
//    @BindView(R.id.progress_bar_pickup) ProgressBar progressPickup;
////    @BindView(R.id.progress_bar_destination) ProgressBar progressDestination;
//
//    @BindView(R.id.distance_from_pickup) TextView distanceFromPickup;
//
//    @BindView(R.id.done_button) TextView doneButton;
//
//
//    private boolean fetchingAddress = false;
//
//    private LatLng cameraPositionCurrent;
//
//
//
//    public PlacePickerWithMapFragment() {
//
//        DaggerComponentBuilder.getInstance()
//                .getNetComponent()
//                .Inject(this);
//    }
//
//
//
//
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
//
//        setRetainInstance(true);
//        View rootView = inflater.inflate(R.layout.fragment_place_picker_google, container, false);
//        ButterKnife.bind(this,rootView);
//
//
//
////        initializeMap();
//        initializeMapGoogle();
//
//
//
//        return rootView;
//    }
//
//
//
//
//
//
//    private void initializeMapGoogle()
//    {
//        SupportMapFragment mapFragment = (SupportMapFragment)
//                getChildFragmentManager().findFragmentById(R.id.mapview);
//
//
//        if (mapFragment != null) {
//
//
//
//            mapFragment.getMapAsync(new com.google.android.gms.maps.OnMapReadyCallback() {
//                @Override
//                public void onMapReady(GoogleMap googleMap) {
//
//                    googleMapInstance = googleMap;
//
//
//                    double lat = 0;
//                    double lon = 0;
//
//
//                    lat = getActivity().getIntent().getDoubleExtra("latitude",0);
//                    lon = getActivity().getIntent().getDoubleExtra("longitude",0);
//
//    //                LocationWithAddress pickUpLocation = null;
//
//    //                pickUpLocation = PreferenceTaxiFiltersLocal.getPickUpLocation(getActivity());
//
//
//
//    //
//    //                if(pickUpLocation!=null)
//    //                {
//    //                    lat = pickUpLocation.getLatitude();
//    //                    lon = pickUpLocation.getLongitude();
//    //                }
//    //                else
//    //                {
//    //                    lat = PrefLocation.getLatitideCurrent(getActivity());
//    //                    lon = PrefLocation.getLongitudeCurrent(getActivity());
//    //                }
//
//
//
//
//                    if(lat==0 && lon == 0)
//                    {
//                        lat = PrefLocation.getLatitude(getActivity());
//                        lon = PrefLocation.getLongitude(getActivity());
//                    }
//                    else
//                    {
//                        selectedFromGeocoder.setLatitude(lat);
//                        selectedFromGeocoder.setLongitude(lon);
//                        selectedFromGeocoder.setLocationName(getActivity().getIntent().getStringExtra("name"));
//                        selectedFromGeocoder.setLocationAddress(getActivity().getIntent().getStringExtra("address"));
//
//                    }
//
//
//
//
//
//
//
//
//
//                    if(lat!=0 && lon!=0)
//                    {
//
//
//                        googleMap.animateCamera(
//                                CameraUpdateFactory.newLatLngZoom(
//                                        new LatLng(lat,lon),17
//                                )
//                        );
//
//                    }
//                    else
//                    {
//
//                        requestLocationUpdates();
//
//                    }
//
//
//
//
//
//                    googleMapInstance.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
//                        @Override
//                        public void onCameraMove() {
//
//
//                            fetchingAddress=true;
//
//
//                            cameraPositionCurrent = googleMapInstance.getCameraPosition().target;
//
//
//
//                            selectedLocation.setLatitude(cameraPositionCurrent.latitude);
//                            selectedLocation.setLongitude(cameraPositionCurrent.longitude);
//
//
//
//
//                            if(selectedLocation.distanceFrom(selectedFromGeocoder.getLatitude(),selectedFromGeocoder.getLongitude())<300)
//                            {
//
//                                selectedLocation.setLocationName(selectedFromGeocoder.getLocationName());
//                                selectedLocation.setLocationAddress(selectedFromGeocoder.getLocationAddress());
//
//                            }
//                            else
//                            {
//                                selectedLocation.setLocationName("Selected Location : ");
//
//
//                                selectedLocation.setLocationAddress("" +
//                                        "Lat : " + String.format("%.4f",cameraPositionCurrent.latitude)
//                                        + " | Lon : " + String.format("%.4f",cameraPositionCurrent.longitude)
//                                );
//                            }
//
//
//    //                        pickupLocation.setLocationAddress("Selected Pickup Location");
//
//
//
//    //                        pickupAddress.setText(pickupLocation.getLocationName());
//                            pickupAddress.setText(selectedLocation.getLocationName() + "\n" + selectedLocation.getLocationAddress());
//
//
//                        }
//                    });
//
//
//                }
//            });
//        }
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
//    @OnClick(R.id.done_button)
//    void finishButton()
//    {
//
////        if(fetchingAddress)
////        {
////            showToastMessage("Please wait we are fetching address !");
////            return;
////        }
//
//
//
//
////        PreferenceTaxiFiltersLocal.savePickUpLocation(selectedLocation,getActivity());
////        PreferenceTaxiFiltersLocal.savePickupSetByUser(getActivity(),true);
//
//
//        Intent intentData = new Intent();
//        intentData.putExtra("latSelected",selectedLocation.getLatitude());
//        intentData.putExtra("lonSelected",selectedLocation.getLongitude());
//        intentData.putExtra("locationName",selectedLocation.getLocationName());
//        intentData.putExtra("address",selectedLocation.getLocationAddress());
//
//
//        if(getActivity()!=null)
//        {
//            getActivity().setResult(6,intentData);
//            getActivity().finish();
//        }
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
//    private LocationCallback locationCallback;
//    private void requestLocationUpdates()
//    {
//
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
//        {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//
//
//
//
//        mLocationRequest = LocationRequest.create();
//        mLocationRequest.setInterval(10000);
//        mLocationRequest.setSmallestDisplacement(10);
//        mLocationRequest.setFastestInterval(5000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//
////        locationCallback = new MyLocationCallback();
//
//        locationCallback = new LocationCallback(){
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                super.onLocationResult(locationResult);
//
//
////                        if(locationResult.getLastLocation().getAccuracy()>17)
////                        {
//                latCurrent = locationResult.getLastLocation().getLatitude();
//                lonCurrent = locationResult.getLastLocation().getLongitude();
//
//
//                LocationWithAddress pickUpLocation = PreferenceTaxiFiltersOutstation.getPickUpLocation(getActivity());
//
//
//                if(pickUpLocation==null)
//                {
//
//                    if(googleMapInstance!=null)
//                    {
//                        if(!isMapCentered)
//                        {
//
//                            googleMapInstance.animateCamera(
//                                    CameraUpdateFactory.newLatLngZoom(
//                                            new LatLng(locationResult.getLastLocation().getLatitude(),locationResult.getLastLocation().getLongitude())
//                                            ,17
//                                    )
//                            );
//
//
//                            isMapCentered=true;
//                        }
//
//
//
//                    }
//
//                }
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
//        LocationServices.getFusedLocationProviderClient(getActivity())
//                .requestLocationUpdates(mLocationRequest,locationCallback, null);
//
//    }
//
//
//
//    void showToastMessage(String message)
//    {
//        Toast.makeText(getActivity(), message,Toast.LENGTH_SHORT).show();
//    }
//
//
//
//    void showlog(String message)
//    {
//        Log.d("place_picker_map",message);
//    }
//
//
//
//
//
//
//
//    @OnClick(R.id.fab_current)
//    void myLocation()
//    {
//
//        if(googleMapInstance!=null)
//        {
//
//
//
//            LatLng latLng = new LatLng(PrefLocation.getLatitideCurrent(getActivity()),
//                    PrefLocation.getLongitudeCurrent(getActivity()));
//
//            googleMapInstance.animateCamera(
//                        CameraUpdateFactory.newLatLng(
//                        latLng)
//            );
//        }
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
//    private CountDownTimer countDownTimerPickup = new CountDownTimer(1000, 1000) {
//
//        public void onTick(long millisUntilFinished) {
//
//        }
//
//        public void onFinish() {
//
////            reverseGeocodeMapzenPickup();
//
//        }
//    };
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
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        if(getActivity()!=null)
//        {
//            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }
//
//
//        isDestroyed = false;
//    }
//
//
//
//
//
//    @Override
//    public void onPause() {
//        super.onPause();
//
//        if(getActivity()!=null)
//        {
//            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
//        }
//    }
//
//
//
//
//
//
//
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//
//        if(locationCallback!=null)
//        {
//            LocationServices.getFusedLocationProviderClient(getActivity()).removeLocationUpdates(locationCallback);
//        }
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
//
//        isDestroyed = true;
//
//        if(locationCallback!=null)
//        {
//            LocationServices.getFusedLocationProviderClient(getActivity()).removeLocationUpdates(locationCallback);
//        }
//    }
//
//
//
//
//
//
//
//    private Marker selectedPoint;
//
//
//
//
//    void showLogMessage(String logMessage)
//    {
//        Log.d("place_picker",logMessage);
//    }
//
//
//
//
//
//
//
//    private void updateMapPosition(double lat, double lon)
//    {
//        fetchingAddress = true;
//
////        swipeContainer.setVisibility(View.GONE);
//
//
//        if(selectedPoint !=null)
//        {
//            selectedPoint.remove();
//            selectedPoint=null;
//        }
//
//
//
//        LatLng point = new LatLng(
//                lat,
//                lon
//        );
//
//
//
//        selectedPoint = googleMapInstance.addMarker(
//                new MarkerOptions()
//                        .position(point)
//                        .title("Pickup")
//
//        );
//
//        googleMapInstance.animateCamera(
//                CameraUpdateFactory.newLatLngZoom(point,17)
//        );
//
//    }
//
//
//
//
//
////
////
////    @OnClick(R.id.clear_search_text)
////    void clearSearchText()
////    {
////        searchBox.setText("");
////    }
////
//
//
//
////    @OnTextChanged(R.id.search_text)
////    void searchBoxChange(CharSequence newText)
////    {
////
////        int PLACE_PICKER_REQUEST = 57;
////
////        try {
////            Intent intent =
////                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
////                            .build(getActivity());
////
////            startActivityForResult(intent, PLACE_PICKER_REQUEST);
////        } catch (GooglePlayServicesRepairableException e) {
//
////        } catch (GooglePlayServicesNotAvailableException e) {
////        }
////    }
//
//
//
//
//
//
//    private int AUTOCOMPLETE_REQUEST_CODE = 1;
//
//
//
//    @OnClick(R.id.search_text_wrapper)
//    void searchBoxClick()
//    {
////        int PLACE_PICKER_REQUEST = 57;
////
////        try {
////
////            com.google.android.gms.maps.model.LatLng pointOne = new com.google.android.gms.maps.model.LatLng(PrefLocation.getLatitideCurrent(getActivity())+1,PrefLocation.getLongitudeCurrent(getActivity())+1);
////            com.google.android.gms.maps.model.LatLng pointTwo = new com.google.android.gms.maps.model.LatLng(PrefLocation.getLatitideCurrent(getActivity())-1,
////                    PrefLocation.getLongitudeCurrent(getActivity())-1);
////
////            LatLngBounds bounds  = LatLngBounds.builder().include(pointOne)
////                    .include(pointTwo)
////                    .build();
////
////            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
////                    .setCountry("IN")
////                    .build();
////
////            Intent intent =
////                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
////                            .setBoundsBias(bounds)
////                            .setFilter(typeFilter)
////                            .build(getActivity());
////
////
////            startActivityForResult(intent, PLACE_PICKER_REQUEST);
////
////        } catch (GooglePlayServicesRepairableException e) {
////            // TODO: Handle the error.
////        } catch (GooglePlayServicesNotAvailableException e) {
////            // TODO: Handle the error.
////        }
//
//
//
//
//
//        List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS, Place.Field.NAME,Place.Field.LAT_LNG);
//
//// Start the autocomplete intent.
//        Intent intent = new Autocomplete.IntentBuilder(
//                AutocompleteActivityMode.OVERLAY, fields)
//                .setCountry("IN")
//                .setLocationBias(RectangularBounds.newInstance(
//                        new com.google.android.gms.maps.model.LatLng(PrefLocation.getLatitideCurrent(getActivity()),PrefLocation.getLongitudeCurrent(getActivity())),
//                        new com.google.android.gms.maps.model.LatLng(PrefLocation.getLatitideCurrent(getActivity()),PrefLocation.getLongitudeCurrent(getActivity()))
//                ))
//                .build(getActivity());
//
//
//
//
//
//
//        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
//
//
//
//
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
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode==AUTOCOMPLETE_REQUEST_CODE)
//        {
//
//            // place picker pickup address
//            if (resultCode == RESULT_OK) {
//
//                Place place = Autocomplete.getPlaceFromIntent(data);
////                Place place = PlacePicker.getPlace(getActivity(), data);
////                String toastMsg = String.format("Place: %s", place.getName());
////                Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_LONG).show();
//
//
////                selectedLocation = new LocationWithAddress();
//                selectedFromGeocoder.setLocationName(place.getName().toString());
//                selectedFromGeocoder.setLocationAddress(place.getAddress().toString());
//                selectedFromGeocoder.setLatitude(place.getLatLng().latitude);
//                selectedFromGeocoder.setLongitude(place.getLatLng().longitude);
//
//                selectedLocation.setLocationName(place.getName().toString());
//                selectedLocation.setLocationAddress(place.getAddress().toString());
//                selectedLocation.setLatitude(place.getLatLng().latitude);
//                selectedLocation.setLongitude(place.getLatLng().longitude);
//
//
//
////                PreferenceTaxiFiltersLocal.savePickUpLocation(pickupAddressLocal,getActivity());
////                PreferenceTaxiFiltersLocal.savePickupSetByUser(getActivity(),true);
//
//                displayPickupLocation();
//
//
//
//
//                updateMapPosition(selectedFromGeocoder.getLatitude(),selectedFromGeocoder.getLongitude());
//
//
//            }
//        }
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
//    private void displayPickupLocation()
//    {
//        if(selectedLocation!=null)
//        {
////            pickupAddress.setText(selectedLocation.getLocationName());
//            pickupAddress.setText(selectedLocation.getLocationAddress());
////
//        }
//        else
//        {
//            pickupAddress.setText("Location not Selected");
//        }
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
//}
