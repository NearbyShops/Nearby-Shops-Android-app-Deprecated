package org.nearbyshops.enduser.CancelledOrders.CancelledByEndUser;

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


import org.nearbyshops.enduser.CancelledOrders.CancelledOrdersHomeDelivery;
import org.nearbyshops.enduser.DaggerComponentBuilder;
import org.nearbyshops.enduser.Model.Shop;
import org.nearbyshops.enduser.ModelCartOrder.Endpoints.OrderEndPoint;
import org.nearbyshops.enduser.ModelCartOrder.Order;
import org.nearbyshops.enduser.ModelStatusCodes.OrderStatusHomeDelivery;
import org.nearbyshops.enduser.OrderHistoryHD.OrderHistoryHD.OrderHistoryHD;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.RetrofitRESTContract.OrderService;
import org.nearbyshops.enduser.ShopsByCategoryOld.Interfaces.NotifyTitleChanged;
import org.nearbyshops.enduser.Utility.UtilityLogin;
import org.nearbyshops.enduser.Utility.UtilityShopHome;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import icepick.State;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 13/6/16.
 */


public class FragmentCancelledByUser extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener,AdapterCancelledByUser.NotifyCancelHandover {


    @Inject
    OrderService orderService;

    RecyclerView recyclerView;
    AdapterCancelledByUser adapter;

    public List<Order> dataset = new ArrayList<>();

    GridLayoutManager layoutManager;
    SwipeRefreshLayout swipeContainer;



//    NotificationReceiver notificationReceiver;

//    DeliveryGuySelf deliveryGuySelf;



    final private int limit = 5;
    @State int offset = 0;
    @State int item_count = 0;
    boolean isDestroyed;




    public FragmentCancelledByUser() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);

    }



    public static FragmentCancelledByUser newInstance() {
        FragmentCancelledByUser fragment = new FragmentCancelledByUser();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_delivery_cancelled_by_shop, container, false);


        setRetainInstance(true);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        swipeContainer = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeContainer);




        if(savedInstanceState!=null)
        {
            // restore instance state
//            deliveryGuySelf = savedInstanceState.getParcelable("savedVehicle");
        }
        else
        {
            makeRefreshNetworkCall();
        }



        setupRecyclerView();
        setupSwipeContainer();

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

        adapter = new AdapterCancelledByUser(dataset,this);

        recyclerView.setAdapter(adapter);

        layoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

//        layoutManager.setSpanCount(metrics.widthPixels/400);



        int spanCount = (int) (metrics.widthPixels/(230 * metrics.density));

        if(spanCount==0){
            spanCount = 1;
        }

        layoutManager.setSpanCount(spanCount);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(layoutManager.findLastVisibleItemPosition()==dataset.size()-1)
                {
                    // trigger fetch next page

                    if(layoutManager.findLastVisibleItemPosition() == previous_position)
                    {
                        return;
                    }


                    if((offset+limit)<=item_count)
                    {
                        offset = offset + limit;


                        swipeContainer.post(new Runnable() {
                            @Override
                            public void run() {

                                swipeContainer.setRefreshing(true);

                                makeNetworkCall(false);
                            }
                        });

                    }

                    previous_position = layoutManager.findLastVisibleItemPosition();
                }
            }
        });
    }


    int previous_position = -1;

    @Override
    public void onRefresh() {

        offset = 0;
        makeNetworkCall(true);
    }


    void makeRefreshNetworkCall()
    {

        swipeContainer.post(new Runnable() {
            @Override
            public void run() {

                swipeContainer.setRefreshing(true);
                onRefresh();
            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();
        notifyTitleChanged();
    }


    void makeNetworkCall(final boolean clearDataset)
    {

//
//        if(deliveryGuySelf ==null)
//        {
//            return;
//        }


        Integer shopID = null;

        if(getActivity().getIntent().getBooleanExtra(CancelledOrdersHomeDelivery.IS_FILTER_BY_SHOP,false))
        {
            Shop shop = UtilityShopHome.getShop(getActivity());

            if(shop!=null)
            {
                shopID = shop.getShopID();
            }
        }


//        Shop currentShop = UtilityShopHome.getShop(getContext());


            Call<OrderEndPoint> call = orderService
                    .getOrders(
                            UtilityLogin.getAuthorizationHeaders(getActivity()),
                            null,
                            shopID,false, OrderStatusHomeDelivery.CANCELLED_BY_USER,
                            null,null,null,null,null,null,null,null,null,
                            limit,offset,null
                    );


            call.enqueue(new Callback<OrderEndPoint>() {
                @Override
                public void onResponse(Call<OrderEndPoint> call, Response<OrderEndPoint> response) {

                    if(isDestroyed)
                    {
                        return;
                    }

                    if(response.body()!= null)
                    {
                        item_count = response.body().getItemCount();

                        if(clearDataset)
                        {
                            dataset.clear();
                        }

                        dataset.addAll(response.body().getResults());
                        adapter.notifyDataSetChanged();
                        notifyTitleChanged();

                    }

                    swipeContainer.setRefreshing(false);

                }

                @Override
                public void onFailure(Call<OrderEndPoint> call, Throwable t) {
                    if(isDestroyed)
                    {
                        return;
                    }

                    showToastMessage("Network Request failed !");
                    swipeContainer.setRefreshing(false);
                }
            });

    }



    void showToastMessage(String message)
    {
        if(getActivity()!=null)
        {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    public void notifyCancelHandover(Order order) {




        /*order.setStatusHomeDelivery(OrderStatusHomeDelivery.ORDER_PACKED);
        order.setDeliveryVehicleSelfID(null);

        Call<ResponseBody> call = orderService.putOrder(order.getOrderID(),order);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    showToastMessage("Handover cancelled !");
                    makeRefreshNetworkCall();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Network Request Failed. Try again !");

            }
        });*/
    }

/*

    public DeliveryGuySelf getDeliveryGuySelf() {
        return deliveryGuySelf;
    }

    public void setDeliveryGuySelf(DeliveryGuySelf deliveryGuySelf) {
        this.deliveryGuySelf = deliveryGuySelf;
    }
*/

    /*public interface NotificationReceiver
    {
        void notifyPendingAcceptChanged();
    }
    */

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putParcelable("savedVehicle", deliveryGuySelf);
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
    }



    void notifyTitleChanged()
    {

        if(getActivity() instanceof NotifyTitleChanged)
        {
            ((NotifyTitleChanged)getActivity())
                    .NotifyTitleChanged(
                            "Cancelled By User (" + String.valueOf(dataset.size())
                                    + "/" + String.valueOf(item_count) + ")",2);


        }
    }


}
