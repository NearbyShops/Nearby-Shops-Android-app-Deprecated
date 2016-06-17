package org.nearbyshops.enduser.ShopsForItems;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.nearbyshops.enduser.DaggerComponentBuilder;
import org.nearbyshops.enduser.Model.Item;
import org.nearbyshops.enduser.Model.ShopItem;
import org.nearbyshops.enduser.MyApplication;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.RetrofitRESTContract.ShopItemService;
import org.nearbyshops.enduser.Utility.UtilityGeneral;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewCartsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, Callback<List<ShopItem>>,AdapterNewCarts.NotifyCallbacks{

    Item item;

    @Inject
    ShopItemService shopItemService;

    RecyclerView recyclerView;

    AdapterNewCarts adapter;

    public List<ShopItem> dataset = new ArrayList<>();

    GridLayoutManager layoutManager;

    SwipeRefreshLayout swipeContainer;



    NotifyPagerAdapter notifyPagerAdapter;


    public NewCartsFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }

    public static NewCartsFragment newInstance(Item item) {

        NewCartsFragment fragment = new NewCartsFragment();
        Bundle args = new Bundle();
        args.putParcelable("item",item);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        View rootView = inflater.inflate(R.layout.fragment_new_carts, container, false);

        item = getArguments().getParcelable("item");

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);

        setupRecyclerView();

        swipeContainer = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeContainer);

        if(swipeContainer!=null) {

            swipeContainer.setOnRefreshListener(this);
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }


        return rootView;
    }


    void setupRecyclerView()
    {
        adapter = new AdapterNewCarts(dataset,getActivity(),this,item);

        recyclerView.setAdapter(adapter);

        layoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);

        //recyclerView.addItemDecoration(
        //        new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST)
        //);

        //recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL_LIST));

        //itemCategoriesList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        layoutManager.setSpanCount(metrics.widthPixels/400);

    }




    @Override
    public void onRefresh() {

        makeNetworkCall();
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

        if(UtilityGeneral.isNetworkAvailable(MyApplication.getAppContext()))
        {
            // Network Available
            Call<List<ShopItem>> call = shopItemService.getShopItems(
                    0,item.getItemID(),
                    UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LAT_CENTER_KEY),
                    UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LON_CENTER_KEY),
                    UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MAX_KEY),
                    UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MIN_KEY),
                    UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.PROXIMITY_KEY),
                    UtilityGeneral.getEndUserID(MyApplication.getAppContext()),
                    false);

            call.enqueue(this);

        }
        else
        {
            showToastMessage("No network. Application is Offline !");
            swipeContainer.setRefreshing(false);
        }
    }



    void showToastMessage(String message)
    {
        if(getActivity()!=null)
        {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    public void onResponse(Call<List<ShopItem>> call, Response<List<ShopItem>> response) {

        if(response.body()!=null)
        {
            dataset.clear();
            dataset.addAll(response.body());
            adapter.notifyDataSetChanged();

            if(notifyPagerAdapter!=null)
            {
                notifyPagerAdapter.notifyNewCartsChanged();
            }


        }else
        {
            dataset.clear();
            adapter.notifyDataSetChanged();

            if(notifyPagerAdapter!=null)
            {
                notifyPagerAdapter.notifyNewCartsChanged();
            }
        }

        swipeContainer.setRefreshing(false);
    }

    @Override
    public void onFailure(Call<List<ShopItem>> call, Throwable t) {

        showToastMessage("Network Request failed !");
        swipeContainer.setRefreshing(false);
    }

    @Override
    public void notifyAddToCart() {

        makeNetworkCall();

    }


    public NotifyPagerAdapter getNotifyPagerAdapter() {
        return notifyPagerAdapter;
    }

    public void setNotifyPagerAdapter(NotifyPagerAdapter notifyPagerAdapter) {
        this.notifyPagerAdapter = notifyPagerAdapter;
    }



    public interface NotifyPagerAdapter
    {
        void notifyNewCartsChanged();
    }



}
