package org.nearbyshops.enduser.ItemCategoryOption;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wunderlist.slidinglayer.SlidingLayer;

import org.nearbyshops.enduser.ItemCategoryOption.Interfaces.NotifyCategoryChanged;
import org.nearbyshops.enduser.ItemCategoryOption.Interfaces.NotifyTitleChanged;
import org.nearbyshops.enduser.ModelEndPoints.ShopEndPoint;
import org.nearbyshops.enduser.DaggerComponentBuilder;
import org.nearbyshops.enduser.Model.ItemCategory;
import org.nearbyshops.enduser.Model.Shop;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.RetrofitRESTContract.ShopService;
import org.nearbyshops.enduser.Utility.DividerItemDecoration;
import org.nearbyshops.enduser.Utility.UtilityGeneral;

import java.util.ArrayList;

import javax.inject.Inject;

import icepick.Icepick;
import icepick.State;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 25/5/16.
 */
public class FragmentShopOld extends Fragment implements SwipeRefreshLayout.OnRefreshListener, NotifyCategoryChanged {


//        ItemCategory itemCategory;

        @State
        ItemCategory notifiedCurrentCategory;

        @State boolean isSaved = false;


        @Inject
        ShopService shopService;

        RecyclerView recyclerView;

        AdapterShop adapter;

        ArrayList<Shop> dataset = new ArrayList<>();

        GridLayoutManager layoutManager;

        SwipeRefreshLayout swipeContainer;


        SlidingLayer slidingLayer ;

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";


    public FragmentShopOld() {
        // inject dependencies through dagger
        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);


        Log.d("applog","Shop Fragment Constructor");

    }

    /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static FragmentShopOld newInstance(int sectionNumber, ItemCategory itemCategory) {

            FragmentShopOld fragment = new FragmentShopOld();
            Bundle args = new Bundle();
            args.putParcelable("itemCat",itemCategory);
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_shop_item_by_shop, container, false);
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);

//            itemCategory = getArguments().getParcelable("itemCat");

            recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

            setupRecyclerView();

            swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);

            setupSwipeContainer();


            if(getActivity() instanceof ShopItemSwipeView)
            {
                /*
                if(!((ShopItemSwipeView) getActivity()).getNotifyCategoryChangedList().contains(this))
                {
                    ((ShopItemSwipeView) getActivity()).getNotifyCategoryChangedList().add(this);
                }*/

                ((ShopItemSwipeView) getActivity()).notifyCategoryChangedShopFragment = this;
            }




                if(!isSaved)
                {
                    if(notifiedCurrentCategory==null)
                    {
                        this.notifiedCurrentCategory = ((ShopItemSwipeView)getActivity()).notifiedCurrentCategory;
                        makeRefreshNetworkCall();
                    }

                    isSaved = true;
                }



            return rootView;
        }



    void setupSwipeContainer()
    {
        if(swipeContainer!=null) {

            swipeContainer.setOnRefreshListener(this);
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }

    }



    final private int limit = 30;
    @State int offset = 0;
    @State int item_count = 0;


    void setupRecyclerView()
    {

        adapter = new AdapterShop(dataset,getActivity());

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





    private void makeRefreshNetworkCall() {

        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);

                try {

                    offset = 0; // reset the offset
                    dataset.clear();
                    makeNetworkCall();

                } catch (IllegalArgumentException ex)
                {
                    ex.printStackTrace();

                }
            }
        });

    }



    @Override
    public void onRefresh() {

        offset = 0; // reset the offset
        dataset.clear();
        makeNetworkCall();
    }







    private void makeNetworkCall()
    {

        if(notifiedCurrentCategory==null)
        {
            swipeContainer.setRefreshing(false);

            return;
        }

        Call<ShopEndPoint> callEndpoint = shopService.filterShopsByItemCategory(

                notifiedCurrentCategory.getItemCategoryID(),
                null,
                (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LAT_CENTER_KEY),
                (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LON_CENTER_KEY),
                (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MAX_KEY),
                (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MIN_KEY),
                (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.PROXIMITY_KEY),
                null,limit,offset,false
        );

/*

        Call<List<Shop>> call = shopService.getShops(null,
                notifiedCurrentCategory.getItemCategoryID(),
                (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LAT_CENTER_KEY),
                (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LON_CENTER_KEY),
                (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MAX_KEY),
                (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MIN_KEY),
                (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.PROXIMITY_KEY));
*/



        callEndpoint.enqueue(new Callback<ShopEndPoint>() {
            @Override
            public void onResponse(Call<ShopEndPoint> call, Response<ShopEndPoint> response) {


//                dataset.clear();

                if(response.body()!=null)
                {
                    dataset.addAll(response.body().getResults());

                    if(response.body().getItemCount()!=null)
                    {
                        item_count = response.body().getItemCount();
                    }
                }


                if(getActivity() instanceof  NotifyTitleChanged)
                {

                    ((NotifyTitleChanged)getActivity()).titleChanged(2,dataset.size(),item_count);

                }

                adapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<ShopEndPoint> call, Throwable t) {

                showToastMessage(getString(R.string.network_request_failed));
                swipeContainer.setRefreshing(false);
            }
        });

    }



    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }


    @Override
    public void categoryChanged(ItemCategory currentCategory, boolean isBackPressed) {


        this.notifiedCurrentCategory = currentCategory;
        makeRefreshNetworkCall();


        Log.d("applog", "Fragment Shop: Category Changed");
    }


    @Override
    public void notifySwipeToRight() {

    }



    // apply ice pack


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Icepick.saveInstanceState(this, outState);
        outState.putParcelableArrayList("dataset",dataset);
    }



    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);


        Icepick.restoreInstanceState(this, savedInstanceState);

        if (savedInstanceState != null) {

            ArrayList<Shop> tempList = savedInstanceState.getParcelableArrayList("dataset");

            dataset.clear();
            dataset.addAll(tempList);

            if(getActivity() instanceof  NotifyTitleChanged)
            {
                ((NotifyTitleChanged)getActivity()).titleChanged(2,dataset.size(),item_count);
            }


            adapter.notifyDataSetChanged();
        }
    }



}
