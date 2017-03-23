package org.nearbyshops.enduserapp.Shops.MapsFragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.nearbyshops.enduserapp.Model.Shop;
import org.nearbyshops.enduserapp.R;
import org.nearbyshops.enduserapp.Shops.Interfaces.GetDataset;
import org.nearbyshops.enduserapp.Shops.Interfaces.NotifyDatasetChanged;
import org.nearbyshops.enduserapp.Shops.Interfaces.NotifyListItemClick;
import org.nearbyshops.enduserapp.Utility.UtilityGeneral;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

/**
 * Created by sumeet on 1/11/16.
 */

public class ShopMapFragment extends Fragment implements OnMapReadyCallback ,NotifyListItemClick, NotifyDatasetChanged{


    @State
    ArrayList<Shop> dataset = new ArrayList<>();


    private GoogleMap mMap;
    List<Marker> markerList;

    SupportMapFragment mapFragment;


    @Bind(R.id.horizontal_list)
    RecyclerView reviewsList;

    LinearLayoutManager linearLayoutManager;
    AdapterHorizontal adapter;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_shops_map, container, false);
        ButterKnife.bind(this,rootView);


        if(savedInstanceState==null)
        {
            if(getActivity() instanceof GetDataset)
            {
                dataset = ((GetDataset)getActivity()).getDataset();
            }

        }
        else
        {
            onViewStateRestored(savedInstanceState);
        }


        setupRecyclerView();

/*
        if(mapFragment==null)
        {

        }*/



        if(getActivity().getSupportFragmentManager().findFragmentByTag("map_fragment")==null)
        {
            mapFragment = new SupportMapFragment();

            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.map,mapFragment,"map_fragment")
                    .commit();
        }
        else
        {

            if(getActivity().getSupportFragmentManager().findFragmentByTag("map_fragment") instanceof SupportMapFragment)
            {

            }

            mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                    .findFragmentByTag("map_fragment");
        }


        mapFragment.getMapAsync(this);


        return rootView;
    }



    void setupRecyclerView()
    {

        adapter = new AdapterHorizontal(dataset,getActivity(),this);
        reviewsList.setAdapter(adapter);

        linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        reviewsList.setLayoutManager(linearLayoutManager);


        SnapHelper helper = new LinearSnapHelper();
//        helper.onFling(10,10);
        helper.attachToRecyclerView(reviewsList);

        reviewsList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(dataset!=null && dataset.size()>0)
                {
                    int position = linearLayoutManager.findFirstVisibleItemPosition();

                    if(position!=RecyclerView.NO_POSITION)
                    {
                        notifyListItemClick(position);
                    }

                }

            }
        });

    }





    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Icepick.restoreInstanceState(this,savedInstanceState);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Icepick.saveInstanceState(this,outState);
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.


            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},2);

            return;
        }




        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        Location location = new Location("xyz");

        location.setLatitude(UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LAT_CENTER_KEY,0));
        location.setLongitude(UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LON_CENTER_KEY,0));


        if(dataset!=null && dataset.size()>0)
        {
            Shop meetup = dataset.get(0);
            LatLng latLng = new LatLng(meetup.getLatCenter(),meetup.getLonCenter());
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,12));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,12));
//            Log.d("dataset","Dataset Size Map Ready : " + String.valueOf(dataset.size()));

        }else
        {
            if(location !=null)
            {
                LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,12));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,12));
            }

            Log.d("dataset","Dataset null : " + String.valueOf(dataset.size()));
        }


        markerList = new ArrayList<>();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                reviewsList.smoothScrollToPosition(markerList.indexOf(marker));

                marker.showInfoWindow();

                return true;
            }
        });


        loadMarkers();
    }



    void loadMarkers()
    {

        if(markerList==null)
        {
            return;
        }


        markerList.clear();
        mMap.clear();

        for(Shop shop: dataset)
        {
            LatLng latLng = new LatLng(shop.getLatCenter(),shop.getLonCenter());

//            Drawable drawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_low_priority_black_24px, getTheme());



            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .snippet(shop.getShopAddress())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                    .title(shop.getShopName()));

            //                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_group_black_24dp))
//            marker.showInfoWindow();

            markerList.add(marker);
        }
    }










    @Override
    public void notifyListItemClick(int position) {

        if(mMap == null)
        {
            return;
        }

        Shop shop = dataset.get(position);
        LatLng latLng = new LatLng(shop.getLatCenter(),shop.getLonCenter());

        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        if(markerList!=null){

            if(markerList.size()<dataset.size())
            {
                loadMarkers();
            }

            markerList.get(position).showInfoWindow();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ButterKnife.unbind(this);
    }


    @Override
    public void notifyDatasetChanged() {
        adapter.notifyDataSetChanged();
        loadMarkers();
    }
}
