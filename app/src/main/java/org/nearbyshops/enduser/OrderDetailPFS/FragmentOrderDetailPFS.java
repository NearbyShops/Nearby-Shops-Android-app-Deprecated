package org.nearbyshops.enduser.OrderDetailPFS;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.nearbyshops.enduser.DaggerComponentBuilder;
import org.nearbyshops.enduser.ItemDetail.ItemDetail;
import org.nearbyshops.enduser.Model.Item;
import org.nearbyshops.enduser.Model.Shop;
import org.nearbyshops.enduser.ModelPickFromShop.OrderItemEndPointPFS;
import org.nearbyshops.enduser.ModelPickFromShop.OrderPFS;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.RetrofitRESTContract.ShopService;
import org.nearbyshops.enduser.RetrofitRESTContractPFS.OrderServicePFS;
import org.nearbyshops.enduser.Shops.UtilityLocation;
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
 * Created by sumeet on 15/11/16.
 */

public class FragmentOrderDetailPFS extends Fragment implements SwipeRefreshLayout.OnRefreshListener ,AdapterOrderDetailPFS.NotifyItemClick{

    OrderPFS order;

    @Inject OrderServicePFS orderItemService;

    RecyclerView recyclerView;
    AdapterOrderDetailPFS adapter;

    public List<Object> dataset = new ArrayList<>();

    GridLayoutManager layoutManager;
    SwipeRefreshLayout swipeContainer;



    final private int limit = 5;
    @State int offset = 0;
    @State int item_count = 0;

    boolean isDestroyed;



    public FragmentOrderDetailPFS() {
        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_order_detail_screen, container, false);



        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        swipeContainer = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeContainer);

        order = UtilityOrderDetailPFS.getOrder(getActivity());


        if(savedInstanceState==null)
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

        adapter = new AdapterOrderDetailPFS(dataset,getActivity(),this);
        layoutManager = new GridLayoutManager(getActivity(),1);


/*
        // add order to the dataset
        if(!dataset.contains(order))
        {
            dataset.add(0,order);
            adapter.notifyItemChanged(0);
        }*/


//        layoutManager.setSpanCount(metrics.widthPixels/400);





//        if(spanCount==0){
//            spanCount = 1;
//        }

//        layoutManager.setSpanCount(spanCount);

//        final int finalSpanCount = spanCount;

        /*layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                System.out.println("Position : " + position);


//                if(adapter.getItemViewType(position)==AdapterOrderDetailPFS.TAG_VIEW_HOLDER_ORDER_ITEM)
//                {
//                    return 1;
//                }
//                else if(adapter.getItemViewType(position)==AdapterOrderDetailPFS.TAG_VIEW_HOLDER_ORDER)
//                {
//


//                    DisplayMetrics metrics = new DisplayMetrics();
//                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
//                    int spanCount = (int) (metrics.widthPixels / (230 * metrics.density));
//
//                    if (spanCount == 0) {
//                        return 1;
//                    } else {
//                        return spanCount;
//                    }

//                    return 2;
//                }



                if (dataset.get(position) instanceof OrderItem) {

                    return 2;

                }
                else if (dataset.get(position) instanceof Order)
                {

//                    DisplayMetrics metrics = new DisplayMetrics();
//                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
//                    int spanCount = (int) (metrics.widthPixels / (230 * metrics.density));
//
//                    if (spanCount == 0) {
//                        return 1;
//                    } else {
//                        return spanCount;
//                    }

                    return 4;
                }

                return 4;
            }
        });
*/

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL));



        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(layoutManager.findLastVisibleItemPosition()==dataset.size())
                {
                    // trigger fetch next page

                    if(layoutManager.findLastVisibleItemPosition() == previous_position)
                    {
                        return;
                    }


                    if((offset+limit)<=item_count)
                    {
                        offset = offset + limit;
                        makeNetworkCall(false);
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
        makeNetworkCallShop();
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


    @Inject
    ShopService shopService;

    void makeNetworkCall(final boolean clearDataset)
    {

        Shop currentShop = UtilityShopHome.getShop(getContext());

        Call<OrderItemEndPointPFS> call = orderItemService.getOrderItemPFS(
                UtilityLogin.getAuthorizationHeaders(getActivity()),
                order.getOrderID(),null,null,null,limit,offset,null);


        call.enqueue(new Callback<OrderItemEndPointPFS>() {
            @Override
            public void onResponse(Call<OrderItemEndPointPFS> call, Response<OrderItemEndPointPFS> response) {

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
                        dataset.add(0,order);
                    }

                    dataset.addAll(response.body().getResults());
                    adapter.notifyDataSetChanged();
//                    notifyTitleChanged();

                }

                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<OrderItemEndPointPFS> call, Throwable t) {
                if(isDestroyed)
                {
                    return;
                }

                showToastMessage("Network Request failed !");
                swipeContainer.setRefreshing(false);

            }
        });

    }


    void makeNetworkCallShop()
    {
        Call<Shop> call = shopService.getShop(
          order.getShopID(),
                UtilityLocation.getLatitude(getActivity()),
                UtilityLocation.getLongitude(getActivity())
        );


        call.enqueue(new Callback<Shop>() {
            @Override
            public void onResponse(Call<Shop> call, Response<Shop> response) {

                if(response.code()==200 && response.body()!=null)
                {
                    order.setShop(response.body());
                    adapter.notifyItemChanged(0);
                }
            }

            @Override
            public void onFailure(Call<Shop> call, Throwable t) {

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
    public void onDestroy() {
        super.onDestroy();
        isDestroyed=true;
    }

    @Override
    public void notifyItemClicked(Item item) {
        Intent intent = new Intent(getActivity(), ItemDetail.class);
        intent.putExtra(ItemDetail.ITEM_DETAIL_INTENT_KEY,item);
        getActivity().startActivity(intent);
    }
}
