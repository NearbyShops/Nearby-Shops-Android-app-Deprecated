package org.nearbyshops.enduserapp.FilterShops;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.nearbyshops.enduserapp.DaggerComponentBuilder;
import org.nearbyshops.enduserapp.Model.Shop;
import org.nearbyshops.enduserapp.ModelEndPoints.ShopEndPoint;
import org.nearbyshops.enduserapp.R;
import org.nearbyshops.enduserapp.RetrofitRESTContract.ShopService;
import org.nearbyshops.enduserapp.Shops.UtilityLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import icepick.State;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FilterShops extends FragmentActivity implements OnMapReadyCallback, SeekBar.OnSeekBarChangeListener {

    private GoogleMap mMap;

    @Bind(R.id.seekbar) SeekBar seekbar;
    @Bind(R.id.shop_count) TextView shopCount;
    @Bind(R.id.item_count) TextView itemCount;
//    @Bind(R.id.delivery_range_header) TextView deliveryRangeHeader;

    public FilterShops() {
        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }


    //    Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_shops);

        ButterKnife.bind(this);

        Fragment mapFragment = getSupportFragmentManager().findFragmentByTag("tag_map");

        if(mapFragment == null)
        {

            mapFragment = new SupportMapFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.map,mapFragment,"tag_map")
                    .commit();
        }


        if(mapFragment instanceof SupportMapFragment)
        {
            ((SupportMapFragment) mapFragment).getMapAsync(this);
        }


        seekbar.setOnSeekBarChangeListener(this);
        seekBarProximity.setOnSeekBarChangeListener(this);
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


        Double lat = UtilityLocation.getLatitude(this);
        Double lon = UtilityLocation.getLongitude(this);

        if(lat!=null && lon!=null)
        {
            LatLng latLng = new LatLng(lat,lon);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));
        }


        makeNetworkCall();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                marker.showInfoWindow();

                return false;
            }
        });


        initializeProximityFilter();
        initializeDeliveryRange();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
        ButterKnife.unbind(this);
    }


    Circle circleProximity;


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

        if(seekBar.getId() == R.id.seekbar_proximity)
        {
            drawProximityCircle();
        }
        else if(seekBar.getId()== R.id.seekbar)
        {
            drawDeliveryRangeCircle();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

        if(seekBar.getId() == R.id.seekbar_proximity)
        {
            drawProximityCircle();
        }
        else if(seekBar.getId() == R.id.seekbar)
        {
            drawDeliveryRangeCircle();
        }

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        if(seekBar.getId()==R.id.seekbar_proximity)
        {
            if(seekBar.getVisibility()==View.GONE)
            {
                UtilityLocation.saveProximity(null,this);
            }
            else if(seekBar.getVisibility() == View.VISIBLE)
            {
                mMap.clear();
                drawProximityCircle();
                makeNetworkCall();
            }
        }
        else if(seekBar.getId()==R.id.seekbar)
        {

            if(seekBar.getVisibility()==View.GONE)
            {
                UtilityLocation.saveDeliveryRangeMax(null,this);
            }
            else if(seekBar.getVisibility() == View.VISIBLE)
            {
                mMap.clear();
                drawDeliveryRangeCircle();
                makeNetworkCall();
            }
        }


    }


    void drawProximityCircle()
    {
        float proximity  = ((float)seekBarProximity.getProgress()/1000);
        proximityHeader.setText("Proximity : " + String.valueOf(proximity) + " Km");
        UtilityLocation.saveProximity(proximity,this);

        Double lat = UtilityLocation.getLatitude(this);
        Double lon = UtilityLocation.getLongitude(this);
        LatLng latLng = new LatLng(lat,lon);

        if(circleProximity!=null)
        {
            circleProximity.remove();
//            circleProximity.setVisible(false);
        }

        circleProximity = mMap.addCircle(
                new CircleOptions()
                        .center(latLng)
//                                .fillColor(0x11000000)
                        .radius(seekBarProximity.getProgress())
                        .strokeWidth(6)
                        .strokeColor(0x66036a8c)
        );


        circleDeliveryRange = mMap.addCircle(
                new CircleOptions()
                        .center(latLng)
//                                .fillColor(0x11000000)
                        .radius(seekbar.getProgress())
                        .strokeWidth(6)
                        .strokeColor(0x66ff6600)
        );


        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, getZoomLevel(circleProximity)));
    }


    Circle circleDeliveryRange;

    void drawDeliveryRangeCircle()
    {
        float delivery  = ((float)seekbar.getProgress()/1000);
        deliveryRangeHeader.setText("Delivery Range Max : " + String.valueOf(delivery) + " Km");
        UtilityLocation.saveDeliveryRangeMax(delivery,this);


        Double lat = UtilityLocation.getLatitude(this);
        Double lon = UtilityLocation.getLongitude(this);
        LatLng latLng = new LatLng(lat,lon);

        if(circleDeliveryRange!=null)
        {
            circleDeliveryRange.remove();
//            circleProximity.setVisible(false);
        }

        circleDeliveryRange = mMap.addCircle(
                new CircleOptions()
                        .center(latLng)
//                                .fillColor(0x11000000)
                        .radius(seekbar.getProgress())
                        .strokeWidth(6)
                        .strokeColor(0x66ff6600)
        );


        circleProximity = mMap.addCircle(
                new CircleOptions()
                        .center(latLng)
//                                .fillColor(0x11000000)
                        .radius(seekBarProximity.getProgress())
                        .strokeWidth(6)
                        .strokeColor(0x66036a8c)
        );


        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, getZoomLevel(circleDeliveryRange)));
    }


    @OnCheckedChanged(R.id.checkbox_proximity)
    void filterProximityCheckChange()
    {
        if(checkBoxProximity.isChecked())
        {

            proximityHeader.setVisibility(View.VISIBLE);
            seekBarProximity.setVisibility(View.VISIBLE);

            if(UtilityLocation.getProximity(this)==null)
            {
                UtilityLocation.saveProximity((float)5,this);
                seekBarProximity.setProgress(5000);
            }

            mMap.clear();
            drawProximityCircle();
            makeNetworkCall();

        }
        else
        {
            proximityHeader.setVisibility(View.GONE);
            seekBarProximity.setVisibility(View.GONE);
//            circleProximity.remove();
            circleProximity.setVisible(false);
            UtilityLocation.saveProximity(null,this);

            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
            mMap.clear();
            makeNetworkCall();
        }
    }


    @OnCheckedChanged(R.id.checkbox_delivery_range)
    void filterDeliveryRange()
    {
        if(checkBoxDeliveryRange.isChecked())
        {

            deliveryRangeHeader.setVisibility(View.VISIBLE);
            seekbar.setVisibility(View.VISIBLE);

            if(UtilityLocation.getDeliveryRangeMax(this)==null)
            {
                UtilityLocation.saveDeliveryRangeMax((float)7,this);
                seekbar.setProgress(7000);
            }

            mMap.clear();
            drawDeliveryRangeCircle();
            makeNetworkCall();
        }
        else
        {
            deliveryRangeHeader.setVisibility(View.GONE);
            seekbar.setVisibility(View.GONE);

            circleDeliveryRange.setVisible(false);
            UtilityLocation.saveDeliveryRangeMax(null,this);

            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
            mMap.clear();
            makeNetworkCall();
        }
    }


    void initializeProximityFilter()
    {
        if(UtilityLocation.getProximity(this)==null)
        {
            proximityHeader.setVisibility(View.GONE);
            seekBarProximity.setVisibility(View.GONE);
            checkBoxProximity.setChecked(false);
        }
        else
        {
            proximityHeader.setVisibility(View.VISIBLE);
            seekBarProximity.setVisibility(View.VISIBLE);

            double proximity = UtilityLocation.getProximity(this);
            seekBarProximity.setProgress((int) (proximity*1000));

            checkBoxProximity.setChecked(true);
            drawProximityCircle();
        }
    }



    void initializeDeliveryRange()
    {
        if(UtilityLocation.getDeliveryRangeMax(this)==null)
        {
            deliveryRangeHeader.setVisibility(View.GONE);
            seekbar.setVisibility(View.GONE);
            checkBoxDeliveryRange.setChecked(false);
        }
        else
        {
            deliveryRangeHeader.setVisibility(View.VISIBLE);
            seekbar.setVisibility(View.VISIBLE);

            double deliveryRange = UtilityLocation.getDeliveryRangeMax(this);
            seekbar.setProgress((int)(deliveryRange*1000));

            checkBoxDeliveryRange.setChecked(true);
            drawDeliveryRangeCircle();
        }

    }




    @Bind(R.id.checkbox_proximity) CheckBox checkBoxProximity;
    @Bind(R.id.proximity_header) TextView proximityHeader;
    @Bind(R.id.seekbar_proximity) SeekBar seekBarProximity;

    @Bind(R.id.delivery_range_header) TextView deliveryRangeHeader;
    @Bind(R.id.checkbox_delivery_range) CheckBox checkBoxDeliveryRange;

    Map<Integer,Circle> circleMap = new HashMap<>();
    Map<Integer,Marker> markerMap = new HashMap<>();

//    boolean filterByProximity = false;
//    boolean filterByRange = false;


//    Marker currentMarker;
//    Circle circle = null;

    void drawShops()
    {

        for(Shop shop: dataset)
        {
            LatLng latLng = new LatLng(
                    shop.getLatCenter(),
                    shop.getLonCenter()
            );

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .snippet(shop.getShopAddress() + "\n" + shop.getCity())
                    .title(shop.getShopName())
            );

//            marker.showInfoWindow();

            Circle circle = mMap.addCircle(
                    new CircleOptions()
                            .center(latLng)
                            .fillColor(0x11000000)
                            .radius(shop.getDeliveryRange()*1000)
                            .strokeWidth(1)
            );

            circleMap.put(shop.getShopID(),circle);
            markerMap.put(shop.getShopID(),marker);

        }

    }




    public int getZoomLevel(Circle circle)
    {
        int zoomLevel = 11;
        if (circle != null)
        {
            double radius = circle.getRadius();
            double scale = radius / 500;
            zoomLevel = (int) Math.floor((16 - Math.log(scale) / Math.log(2)));
        }
        return zoomLevel ;
    }


    ArrayList<Shop> dataset = new ArrayList<>();
    boolean isDestroyed;

//    final private int limit = 10;
//    @State int offset = 0;
    @State int item_count = 0;
    @Inject ShopService shopService;


    private void makeNetworkCall()
    {

//        String current_sort = "";
//        current_sort = UtilitySortShopsByCategory.getSort(this) + " " + UtilitySortShopsByCategory.getAscending(getContext());

//        Call<ShopEndPoint> callEndpoint = shopService.getShops(
//                null,
//                null,
//                UtilityLocationOld.getLatitude(this),
//                UtilityLocationOld.getLongitude(this),
//                null, null,
//                UtilityLocationOld.getProximity(this),
//                null, null,120,0,false
//        );



        Call<ShopEndPoint> callEndpoint = shopService.getShopForFilters(
                UtilityLocation.getLatitude(this),
                UtilityLocation.getLongitude(this),
                UtilityLocation.getDeliveryRangeMax(this),0.00,
                UtilityLocation.getProximity(this),
                null
        );




        callEndpoint.enqueue(new Callback<ShopEndPoint>() {
            @Override
            public void onResponse(Call<ShopEndPoint> call, Response<ShopEndPoint> response) {

                if(isDestroyed)
                {
                    return;
                }

                if(response.body()!=null)
                {

                    dataset.clear();
                    dataset.addAll(response.body().getResults());

                    if(response.body().getItemCount()!=null)
                    {
                        item_count = response.body().getItemCount();
                        shopCount.setText("Shop Count : " + String.valueOf(item_count));
                    }

                    // draw shops
                    drawShops();
                }

            }

            @Override
            public void onFailure(Call<ShopEndPoint> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }

                showToastMessage("No Internet ! Please try later.");
            }
        });

    }



    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }




    @Override
    protected void onStop() {
        super.onStop();
        isDestroyed = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isDestroyed = false;
    }
}
