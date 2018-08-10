package org.nearbyshops.enduserapp.OrderHistoryHD.OrderHistoryHD.Complete;

import android.content.Intent;
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


import org.nearbyshops.enduserapp.DaggerComponentBuilder;
import org.nearbyshops.enduserapp.LoginNew.Login;
import org.nearbyshops.enduserapp.Model.Shop;
import org.nearbyshops.enduserapp.ModelCartOrder.Endpoints.OrderEndPoint;
import org.nearbyshops.enduserapp.ModelCartOrder.Order;
import org.nearbyshops.enduserapp.ModelRoles.User;
import org.nearbyshops.enduserapp.OrderDetail.OrderDetail;
import org.nearbyshops.enduserapp.OrderDetail.PrefOrderDetail;
import org.nearbyshops.enduserapp.OrderHistoryHD.OrderHistoryHD.Interfaces.RefreshFragment;
import org.nearbyshops.enduserapp.OrderHistoryHD.OrderHistoryHD.OrderHistoryHD;
import org.nearbyshops.enduserapp.OrderHistoryHD.OrderHistoryHD.SlidingLayerSort.UtilitySortOrdersHD;
import org.nearbyshops.enduserapp.R;
import org.nearbyshops.enduserapp.RetrofitRESTContract.OrderService;
import org.nearbyshops.enduserapp.Interfaces.NotifySearch;
import org.nearbyshops.enduserapp.ShopsByCategoryOld.Interfaces.NotifySort;
import org.nearbyshops.enduserapp.ShopsByCategoryOld.Interfaces.NotifyTitleChanged;
import org.nearbyshops.enduserapp.Utility.PrefLogin;
import org.nearbyshops.enduserapp.Utility.UtilityShopHome;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import icepick.State;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CompleteOrdersFragment extends Fragment implements AdapterComplete.NotifyConfirmOrder, SwipeRefreshLayout.OnRefreshListener ,NotifySort,NotifySearch ,RefreshFragment{


//    @Inject
//    OrderServicePFS orderService;

    @Inject
    OrderService orderServiceShopStaff;

    RecyclerView recyclerView;
    AdapterComplete adapter;

    public List<Order> dataset = new ArrayList<>();

    GridLayoutManager layoutManager;
    SwipeRefreshLayout swipeContainer;


    final private int limit = 5;
    @State int offset = 0;
    @State int item_count = 0;
    boolean isDestroyed;



    public CompleteOrdersFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);

    }


    public static CompleteOrdersFragment newInstance() {
        CompleteOrdersFragment fragment = new CompleteOrdersFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_home_delivery_placed_orders, container, false);


        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        swipeContainer = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeContainer);


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

        adapter = new AdapterComplete(dataset,this,this);

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


                if(offset + limit > layoutManager.findLastVisibleItemPosition()+1-1)
                {
                    return;
                }


                if(layoutManager.findLastVisibleItemPosition()==dataset.size()-1+1)
                {
                    // trigger fetch next page

//                    if(layoutManager.findLastVisibleItemPosition() == previous_position)
//                    {
//                        return;
//                    }


                    if((offset+limit)<=item_count)
                    {
                        offset = offset + limit;
                        makeNetworkCall(false);
                    }

//                    previous_position = layoutManager.findLastVisibleItemPosition();

                }

            }
        });
    }



//    int previous_position = -1;



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


    void makeNetworkCall(final boolean clearDataset)
    {

        String current_sort = "";
        current_sort = UtilitySortOrdersHD.getSort(getContext()) + " " + UtilitySortOrdersHD.getAscending(getContext());


        User endUser = PrefLogin.getUser(getActivity());
        if(endUser==null)
        {
            showLoginDialog();

            swipeContainer.setRefreshing(false);
            return;
        }



        Integer shopID = null;

        if(getActivity().getIntent().getBooleanExtra(OrderHistoryHD.IS_FILTER_BY_SHOP,false))
        {
            Shop shop = UtilityShopHome.getShop(getActivity());

            if(shop!=null)
            {
                shopID = shop.getShopID();
            }
        }


        Call<OrderEndPoint> call = orderServiceShopStaff.getOrders(
                    PrefLogin.getAuthorizationHeaders(getActivity()),
                    null,shopID,false,
                    null,null,null,
                    null,null,
                    null,null,
                    false,searchQuery,
                    current_sort,limit,offset,null);


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


    @Override
    public void onResume() {
        super.onResume();
        notifyTitleChanged();
        isDestroyed=false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed=true;
    }



    void showToastMessage(String message)
    {
        if(getActivity()!=null)
        {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        }

    }




    void notifyTitleChanged()
    {

        if(getActivity() instanceof NotifyTitleChanged)
        {
            ((NotifyTitleChanged)getActivity())
                    .NotifyTitleChanged(
                            "Complete (" + String.valueOf(dataset.size())
                                    + "/" + String.valueOf(item_count) + ")",1);


        }
    }





    // Refresh the Confirmed PlaceholderFragment

    private static String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }



    void refreshConfirmedFragment()
    {
        Fragment fragment = getActivity().getSupportFragmentManager()
                .findFragmentByTag(makeFragmentName(R.id.container,1));

        if(fragment instanceof RefreshFragment)
        {
            ((RefreshFragment)fragment).refreshFragment();
        }
    }

    @Override
    public void notifyOrderSelected(Order order) {
        PrefOrderDetail.saveOrder(order,getActivity());
        getActivity().startActivity(new Intent(getActivity(),OrderDetail.class));
    }

    @Override
    public void notifySortChanged() {
        makeRefreshNetworkCall();
    }




    String searchQuery = null;

    @Override
    public void search(final String searchString) {
        searchQuery = searchString;
        makeRefreshNetworkCall();
    }

    @Override
    public void endSearchMode() {
        searchQuery = null;
        makeRefreshNetworkCall();
    }




//    @Override
//    public void notifyCancelOrder(final Order order) {
//
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//        builder.setTitle("Confirm Cancel Order !")
//                .setMessage("Are you sure you want to cancel this order !")
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        cancelOrder(order);
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        showToastMessage(" Not Cancelled !");
//                    }
//                })
//                .show();
//    }


//    private void cancelOrder(Order order) {
//
//
////        Call<ResponseBody> call = orderService.cancelOrderByShop(order.getOrderID());
//
//        Call<ResponseBody> call = orderServiceShopStaff.cancelledByEndUser(
//                UtilityLogin.getAuthorizationHeaders(getActivity()),
//                order.getOrderID()
//        );
//
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                if(response.code() == 200 )
//                {
//                    showToastMessage("Successful");
//                    makeRefreshNetworkCall();
//                }
//                else if(response.code() == 304)
//                {
//                    showToastMessage("Not Cancelled !");
//                }
//                else
//                {
//                    showToastMessage("Server Error");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                showToastMessage("Network Request Failed. Check your internet connection !");
//            }
//        });
//
//    }



    public static final String TAG_LOGIN_DIALOG = "tag_login_dialog";

    private void showLoginDialog()
    {
//        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(TAG_LOGIN_DIALOG);
//
//        if(getActivity().getSupportFragmentManager().findFragmentByTag(TAG_LOGIN_DIALOG)==null)
//        {
//            FragmentManager fm = getActivity().getSupportFragmentManager();
//            LoginDialog loginDialog = new LoginDialog();
//            loginDialog.show(fm,TAG_LOGIN_DIALOG);
//        }

        Intent intent = new Intent(getActivity(),Login.class);
        startActivity(intent);
    }



    @Override
    public void refreshFragment() {
        makeRefreshNetworkCall();
    }




}
