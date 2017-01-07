package org.nearbyshops.enduser.ShopItemByItem.NewCarts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.nearbyshops.enduser.DaggerComponentBuilder;
import org.nearbyshops.enduser.Login.LoginDialog;
import org.nearbyshops.enduser.Login.NotifyAboutLogin;
import org.nearbyshops.enduser.Model.Item;
import org.nearbyshops.enduser.ModelEndPoints.ShopItemEndPoint;
import org.nearbyshops.enduser.ModelRoles.EndUser;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.RetrofitRESTContract.ShopItemService;
import org.nearbyshops.enduser.ShopItemByItem.Interfaces.NotifyFillCartsChanged;
import org.nearbyshops.enduser.ShopItemByItem.Interfaces.NotifyNewCartsChanged;
import org.nearbyshops.enduser.Shops.UtilityLocation;
import org.nearbyshops.enduser.ShopsByCategoryOld.Interfaces.NotifySort;
import org.nearbyshops.enduser.ShopsByCategoryOld.Interfaces.NotifyTitleChanged;
import org.nearbyshops.enduser.Utility.UtilityGeneral;
import org.nearbyshops.enduser.Utility.UtilityLogin;
import org.nearbyshops.enduser.UtilitySort.UtilitySortShopItems;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewCartsFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener,
        AdapterNewCarts.NotifyAddToCart, NotifyFillCartsChanged, NotifyAboutLogin,
        NotifySort {



    Item item;

    @Inject
    ShopItemService shopItemService;

    RecyclerView recyclerView;
    AdapterNewCarts adapter;

    @State
    ArrayList<Object> dataset = new ArrayList<>();

    GridLayoutManager layoutManager;
    SwipeRefreshLayout swipeContainer;

//    NotifyPagerAdapter notifyPagerAdapter;

    boolean isDestroyed;




//    TextView itemDescription;
//    TextView itemName;
//    ImageView itemImage;
//    TextView priceRange;
//    TextView shopCount;
//
//
//    @Bind(R.id.item_rating)
//    TextView itemRating;
//
//    @Bind(R.id.rating_count)
//    TextView ratingCount;



    private int limit = 10;
    @State int offset = 0;
    @State int item_count = 0;



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

        ButterKnife.bind(this,rootView);


        item = getArguments().getParcelable("item");
        dataset.add(0,item);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);


        swipeContainer = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeContainer);

        if(swipeContainer!=null) {

            swipeContainer.setOnRefreshListener(this);
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }


        // bindings for Item
//        itemDescription = (TextView) rootView.findViewById(R.id.itemDescription);
//        itemName = (TextView) rootView.findViewById(R.id.itemName);
//        itemImage = (ImageView) rootView.findViewById(R.id.itemImage);
//        priceRange = (TextView) rootView.findViewById(R.id.price_range);
//        shopCount = (TextView) rootView.findViewById(R.id.shop_count);

//        bindItem();


        if(savedInstanceState == null)
        {

            makeRefreshNetworkCall();
        }
        else
        {
            onViewStateRestored(savedInstanceState);
        }

        setupRecyclerView();
        return rootView;
    }


    void setupRecyclerView()
    {
        adapter = new AdapterNewCarts(dataset,getActivity(),this,item,(AppCompatActivity) getActivity(),this);

        recyclerView.setAdapter(adapter);

        layoutManager = new GridLayoutManager(getActivity(),1);
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);

        //recyclerView.addItemDecoration(
        //        new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST)
        //);

        //recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL_LIST));

        //itemCategoriesList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

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

                if(layoutManager.findLastVisibleItemPosition()==dataset.size())
                {
                    // trigger fetch next page

                    if(dataset.size()== previous_position)
                    {
                        return;
                    }


                    // trigger fetch next page

                    if((offset+limit)<=item_count)
                    {
                        offset = offset + limit;
                        makeNetworkCall(false, false);
                    }

                    previous_position = dataset.size();

                }
            }
        });
    }


    int previous_position = -1;



    @Override
    public void onRefresh() {

//        dataset.clear();
        offset=0;
        makeNetworkCall(false, true);
    }



    void makeRefreshNetworkCall()
    {

        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);

                try {

                    onRefresh();

                } catch (IllegalArgumentException ex)
                {
                    ex.printStackTrace();

                }
            }
        });

    }



    void makeNetworkCall(final boolean notifyChange, final boolean clearDataset)
    {

            EndUser endUser = UtilityLogin.getEndUser(getActivity());

            Integer endUserID = null;


            if(endUser != null)
            {
                endUserID = endUser.getEndUserID();
            }




        String current_sort = "";

        current_sort = UtilitySortShopItems.getSort(getContext())
                + " " + UtilitySortShopItems.getAscending(getContext());


        // Network Available
            Call<ShopItemEndPoint> call = shopItemService.getShopItemEndpoint(
                    null,null,item.getItemID(),
                    UtilityLocation.getLatitude(getActivity()),
                    UtilityLocation.getLongitude(getActivity()),
                    null, null,
                    null,
                    endUserID,
                    false,
                    null,null,null,null,
                    null,current_sort,
                    limit,offset,null,true
            );

            call.enqueue(new Callback<ShopItemEndPoint>() {
                @Override
                public void onResponse(Call<ShopItemEndPoint> call, Response<ShopItemEndPoint> response) {


                    if(isDestroyed)
                    {
                        return;
                    }

                    if(response.body()!=null)
                    {

                        if(clearDataset)
                        {
                            dataset.clear();
                            dataset.add(0,item);
                        }
                        dataset.addAll(response.body().getResults());
                        item_count = response.body().getItemCount();


                        if(notifyChange)
                        {
                            if(getActivity() instanceof NotifyNewCartsChanged)
                            {
                                ((NotifyNewCartsChanged)getActivity()).notifyNewCartsChanged();
                            }
                        }



                    }

                    /*else
                    {
//                        dataset.clear();
                        adapter.notifyDataSetChanged();

                        if(notifyChange)
                        {
                            if(getActivity() instanceof NotifyNewCartsChanged)
                            {
                                ((NotifyNewCartsChanged)getActivity()).notifyNewCartsChanged();
                            }

                        }


                    }
*/

                    adapter.notifyDataSetChanged();
                    notifyTitleChanged();
                    swipeContainer.setRefreshing(false);


                }

                @Override
                public void onFailure(Call<ShopItemEndPoint> call, Throwable t) {


                    if(isDestroyed)
                    {
                        return;
                    }

                    showToastMessage("Network Request failed !");
                    swipeContainer.setRefreshing(false);
                }
            });


    }



    private void showLoginDialog()
    {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        LoginDialog loginDialog = new LoginDialog();
        loginDialog.show(fm,"serviceUrl");
    }




    void showToastMessage(String message)
    {
        if(getActivity()!=null)
        {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    public void notifyAddToCart() {

        // change to true
//        dataset.clear();

        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);


                offset = 0;
                makeNetworkCall(true,true);

            }
        });
    }

    @Override
    public void notifyFilledCartsChanged() {
//        onRefresh();

//        dataset.clear();
//        makeNetworkCall(false);
        makeRefreshNetworkCall();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this,outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Icepick.restoreInstanceState(this,savedInstanceState);
        notifyTitleChanged();
    }



    void notifyTitleChanged()
    {
        if(getActivity() instanceof NotifyTitleChanged)
        {
            ((NotifyTitleChanged) getActivity())
                    .NotifyTitleChanged(
                            " New Carts ("
                                    + String.valueOf(dataset.size()-1) + "/" + item_count + ")",1
                    );
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isDestroyed = true;
        ButterKnife.unbind(this);
    }

    @Override
    public void NotifyLogin() {

        makeRefreshNetworkCall();
    }

    @Override
    public void notifySortChanged() {

        makeRefreshNetworkCall();
    }

    public int getItemCount() {
        return item_count;
    }






}
