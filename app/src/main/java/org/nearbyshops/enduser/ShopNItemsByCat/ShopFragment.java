package org.nearbyshops.enduser.ShopNItemsByCat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wunderlist.slidinglayer.SlidingLayer;

import org.nearbyshops.enduser.DaggerComponentBuilder;
import org.nearbyshops.enduser.Model.ItemCategory;
import org.nearbyshops.enduser.Model.Shop;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.StandardInterfaces.DataProviderShop;
import org.nearbyshops.enduser.StandardInterfacesGeneric.DataSubscriber;
import org.nearbyshops.enduser.Utility.DividerItemDecoration;
import org.nearbyshops.enduser.Utility.UtilityGeneral;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by sumeet on 25/5/16.
 */
public class ShopFragment extends Fragment implements DataSubscriber<Shop>, SwipeRefreshLayout.OnRefreshListener {



        ItemCategory itemCategory;

        @Inject
        DataProviderShop shopDataProvider;

        RecyclerView recyclerView;

        ShopAdapter adapter;

        List<Shop> dataset = new ArrayList<>();

        GridLayoutManager layoutManager;

        SwipeRefreshLayout swipeContainer;


        SlidingLayer slidingLayer ;

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";


    public ShopFragment() {
        // inject dependencies through dagger
        DaggerComponentBuilder.getInstance()
                .getDataComponent().Inject(this);
    }

    /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ShopFragment newInstance(int sectionNumber,ItemCategory itemCategory) {

            ShopFragment fragment = new ShopFragment();
            Bundle args = new Bundle();
            args.putParcelable("itemCat",itemCategory);
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_shops, container, false);
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);

            itemCategory = getArguments().getParcelable("itemCat");

            recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

            setupRecyclerView();

            swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);

            if(swipeContainer!=null) {

                swipeContainer.setOnRefreshListener(this);
                swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light);
            }





            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

            /*
            textView.setText(String.valueOf(UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LAT_CENTER_KEY))
                    + ":" + String.valueOf(UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LON_CENTER_KEY))
                    + " \nProximity:" + String.valueOf(UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.PROXIMITY_KEY))
                    + " \nDeliveryRange:" + String.valueOf(UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MIN_KEY))
                    + ":" + String.valueOf(UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MAX_KEY)));

            */

            return rootView;
        }


    void slidingLayerSetup()
    {

    }

    void setupRecyclerView()
    {

        adapter = new ShopAdapter(dataset,getActivity());

        recyclerView.setAdapter(adapter);

        layoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST)
        );

        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(),DividerItemDecoration.HORIZONTAL_LIST)
        );

        //itemCategoriesList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        layoutManager.setSpanCount(metrics.widthPixels/350);
    }


    @Override
    public void onResume() {
        super.onResume();


        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);

                try {

                    makeNetworkCall();

                } catch (IllegalArgumentException ex)
                {
                    ex.printStackTrace();

                }

                adapter.notifyDataSetChanged();
            }
        });


    }

    void makeNetworkCall()
    {
        if(UtilityGeneral.isNetworkAvailable(getActivity()))
        {
            // Network Available

            shopDataProvider.readMany(0,
                    itemCategory.getItemCategoryID(),
                    UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LAT_CENTER_KEY),
                    UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LON_CENTER_KEY),
                    UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MAX_KEY),
                    UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MIN_KEY),
                    UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.PROXIMITY_KEY),this);
        }
        else

        {
            showToastMessage("No network. Application is Offline !");
            swipeContainer.setRefreshing(false);
        }
    }

    @Override
    public void readManyCallback(boolean isOffline, boolean isSuccessful, int httpStatusCode, List<Shop> shopList) {

        if(!isOffline)
        {
            // Online
            if(isSuccessful)
            {

                // Successful

                dataset.clear();
                if(shopList!=null)
                {
                    dataset.addAll(shopList);
                }

                adapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
                //showToastMessage(String.valueOf(dataset.size()));

            }else
            {
                // UnSuccessful
                swipeContainer.setRefreshing(false);
            }


        }else
        {
            // offline
            showToastMessage("Application Offline !");

            swipeContainer.setRefreshing(false);
        }


    }



    @Override
    public void createCallback(boolean isOffline, boolean isSuccessful, int httpStatusCode, Shop shop) {

    }

    @Override
    public void readCallback(boolean isOffline, boolean isSuccessful, int httpStatusCode, Shop shop) {

    }


    @Override
    public void updateCallback(boolean isOffline, boolean isSuccessful, int httpStatusCode) {

    }

    @Override
    public void deleteShopCallback(boolean isOffline, boolean isSuccessful, int httpStatusCode) {

    }


    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {

        makeNetworkCall();
    }
}
