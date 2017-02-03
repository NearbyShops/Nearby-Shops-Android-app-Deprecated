package org.nearbyshops.enduserapp.DeliveryAddress;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.nearbyshops.enduserapp.R;
import org.nearbyshops.enduserapp.SharedPreferences.UtilityLocationOld;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class PickLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    Marker currentMarker;

//    Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_location);

        ButterKnife.bind(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//         mapFragment.getMapAsync(this);

        SupportMapFragment mapFragment1 = new SupportMapFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.map,mapFragment1).commit();

        mapFragment1.getMapAsync(this);

    }


    @OnClick(R.id.confirm_selected_location_button)
    void confirmButtonClick()
    {

        if(currentMarker==null)
        {
            Toast.makeText(this, R.string.toast_location_not_selected, Toast.LENGTH_SHORT).show();

            return;
        }

        Intent data = new Intent();
        data.putExtra("latitude",currentMarker.getPosition().latitude);
        data.putExtra("longitude",currentMarker.getPosition().longitude);
        setResult(RESULT_OK,data);
        finish();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},2);

            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);

//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(,14));

        Location currentLocation = UtilityLocationOld.getCurrentLocation(this);

        if(currentLocation!=null)
        {
            LatLng latLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));
        }



        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

//                Toast.makeText(FilterShops.this,"Long Click : " + latLng.toString(), Toast.LENGTH_SHORT).show();


                if(currentMarker!=null)
                {
                    currentMarker.remove();
                }


                currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).snippet(latLng.toString()).title("Selected Location"));
//                mMap.moveCamera();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                currentMarker.showInfoWindow();

            }
        });


        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().zIndex(13).position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        /*if(unbinder!=null)
        {
            unbinder.unbind();
        }*/


        ButterKnife.unbind(this);
    }
}
