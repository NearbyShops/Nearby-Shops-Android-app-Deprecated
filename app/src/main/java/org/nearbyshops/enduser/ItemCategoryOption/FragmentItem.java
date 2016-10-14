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

import org.nearbyshops.enduser.ItemCategoryOption.Interfaces.NotifyCategoryChanged;
import org.nearbyshops.enduser.ItemCategoryOption.Interfaces.NotifyTitleChanged;
import org.nearbyshops.enduser.ModelEndPoints.ItemEndPoint;
import org.nearbyshops.enduser.DaggerComponentBuilder;
import org.nearbyshops.enduser.Model.Item;
import org.nearbyshops.enduser.Model.ItemCategory;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.RetrofitRESTContract.ItemService;
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
public class FragmentItem extends Fragment implements SwipeRefreshLayout.OnRefreshListener, NotifyCategoryChanged{



//        ItemCategory itemCategory;

        @State ItemCategory notifiedCurrentCategory;

        @State boolean isSaved = false;

        @Inject
        ItemService itemService;

        RecyclerView recyclerView;
        AdapterItem adapter;

        ArrayList<Item> dataset = new ArrayList<>();

        GridLayoutManager layoutManager;
        SwipeRefreshLayout swipeContainer;


        private static final String ARG_SECTION_NUMBER = "section_number";





    private int limit = 30;
    @State int offset = 0;

    @State int item_count = 0;


    // Interface References

    NotifyTitleChanged notifyTitleChanged;

    // Interface References Ends


    public FragmentItem() {
        // inject dependencies through dagger
        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

        Log.d("applog","Item Fragment Constructor");
    }

    /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static FragmentItem newInstance(ItemCategory itemCategory) {

            FragmentItem fragment = new FragmentItem();
            Bundle args = new Bundle();
            args.putParcelable("itemCat",itemCategory);
            fragment.setArguments(args);
            return fragment;
        }




        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_items, container, false);

//            itemCategory = getArguments().getParcelable("itemCat");

            recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

            swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);

            setupRecyclerView();
            setupSwipeContainer();


            if(getActivity() instanceof NotifyTitleChanged)
            {
                notifyTitleChanged = (NotifyTitleChanged) getActivity();
            }


            if(getActivity() instanceof ShopItemSwipeView)
            {

                /*
                if(!((ShopItemSwipeView) getActivity()).getNotifyCategoryChangedList().contains(this))
                {
                    ((ShopItemSwipeView) getActivity()).getNotifyCategoryChangedList().add(this);
                }
                    */

                ((ShopItemSwipeView) getActivity()).notifyCategoryChangedItemFragemnt = this;

            }


            if(!isSaved)
            {
                if(notifiedCurrentCategory==null)
                {
                    this.notifiedCurrentCategory = ((ShopItemSwipeView)getActivity()).notifiedCurrentCategory;
                }

                makeRefreshNetworkCall();

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


    void setupRecyclerView()
    {

        adapter = new AdapterItem(dataset,getActivity());

        recyclerView.setAdapter(adapter);

        layoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST)
        );

        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(),DividerItemDecoration.HORIZONTAL_LIST)
        );


        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        layoutManager.setSpanCount(metrics.widthPixels/350);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(layoutManager.findLastVisibleItemPosition()==dataset.size()-1)
                {
                    // trigger fetch next page

                    if((offset+limit)<=item_count)
                    {
                        offset = offset + limit;
                        makeNetworkCall();
                    }

                }
            }
        });
    }





    void makeRefreshNetworkCall()
    {
        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);

                try {

                    dataset.clear();
                    offset = 0 ; // reset offset
                    makeNetworkCall();

                } catch (IllegalArgumentException ex)
                {
                    ex.printStackTrace();

                }
            }
        });
    }




    void makeNetworkCall()
    {

        if(notifiedCurrentCategory==null)
        {
            swipeContainer.setRefreshing(false);

            return;
        }
/*
            Call<List<Item>> call = itemService.getItems(itemCategory.getItemCategoryID(),null,
                    (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LAT_CENTER_KEY),
                    (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LON_CENTER_KEY),
                    (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MAX_KEY),
                    (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MIN_KEY),
                    (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.PROXIMITY_KEY)
            );*/


        Call<ItemEndPoint> endPointCall = itemService.getItemsEndpoint(notifiedCurrentCategory.getItemCategoryID(),
                null,
                (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LAT_CENTER_KEY),
                (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LON_CENTER_KEY),
                (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MAX_KEY),
                (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MIN_KEY),
                (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.PROXIMITY_KEY),
                null, limit,offset,null);



        endPointCall.enqueue(new Callback<ItemEndPoint>() {
            @Override
            public void onResponse(Call<ItemEndPoint> call, Response<ItemEndPoint> response) {

//                dataset.clear();

                if(response.body()!=null)
                {
                    dataset.addAll(response.body().getResults());

                    if(response.body().getItemCount()!=null)
                    {
                        item_count = response.body().getItemCount();
                    }


                    notifyTitleChanged.titleChanged(1,dataset.size(),item_count);
                }

                adapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<ItemEndPoint> call, Throwable t) {

                swipeContainer.setRefreshing(false);

                showToastMessage("Network request failed. Please check your connection !");

            }
        });

    }


    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {

        dataset.clear();
        offset = 0; // reset the offset
        makeNetworkCall();
    }

    @Override
    public void categoryChanged(ItemCategory currentCategory, boolean isBackPressed) {

        this.notifiedCurrentCategory = currentCategory;
        makeRefreshNetworkCall();
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

            ArrayList<Item> tempList = savedInstanceState.getParcelableArrayList("dataset");

            dataset.clear();
            dataset.addAll(tempList);



            if(notifyTitleChanged !=null)
            {
                notifyTitleChanged.titleChanged(1,dataset.size(),item_count);
            }

            adapter.notifyDataSetChanged();
        }

    }



}
