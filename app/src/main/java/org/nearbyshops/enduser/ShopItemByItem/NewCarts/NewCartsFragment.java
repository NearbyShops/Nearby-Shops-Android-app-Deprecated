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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduser.DaggerComponentBuilder;
import org.nearbyshops.enduser.Login.LoginDialog;
import org.nearbyshops.enduser.Login.NotifyAboutLogin;
import org.nearbyshops.enduser.Model.Item;
import org.nearbyshops.enduser.Model.ShopItem;
import org.nearbyshops.enduser.ModelEndPoints.ShopItemEndPoint;
import org.nearbyshops.enduser.ModelRoles.EndUser;
import org.nearbyshops.enduser.ModelStats.ItemStats;
import org.nearbyshops.enduser.MyApplication;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.RetrofitRESTContract.ShopItemService;
import org.nearbyshops.enduser.ShopItemByItem.Interfaces.NotifyFillCartsChanged;
import org.nearbyshops.enduser.ShopItemByItem.Interfaces.NotifyNewCartsChanged;
import org.nearbyshops.enduser.ShopsByCategory.Interfaces.NotifySort;
import org.nearbyshops.enduser.ShopsByCategory.Interfaces.NotifyTitleChanged;
import org.nearbyshops.enduser.Utility.UtilityGeneral;
import org.nearbyshops.enduser.Utility.UtilityLogin;
import org.nearbyshops.enduser.UtilitySort.UtilitySortShopItems;

import java.util.ArrayList;

import javax.inject.Inject;

import icepick.Icepick;
import icepick.State;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewCartsFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener,
        AdapterNewCarts.NotifyAddToCart, NotifyFillCartsChanged, NotifyAboutLogin,
        NotifySort{



    Item item;

    @Inject
    ShopItemService shopItemService;

    RecyclerView recyclerView;
    AdapterNewCarts adapter;

    @State
    ArrayList<ShopItem> dataset = new ArrayList<>();

    GridLayoutManager layoutManager;
    SwipeRefreshLayout swipeContainer;

//    NotifyPagerAdapter notifyPagerAdapter;

    boolean isDestroyed;




    TextView itemDescription;
    TextView itemName;
    ImageView itemImage;
    TextView priceRange;
    TextView shopCount;



    private int limit = 30;
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
        item = getArguments().getParcelable("item");
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
        itemDescription = (TextView) rootView.findViewById(R.id.itemDescription);
        itemName = (TextView) rootView.findViewById(R.id.itemName);
        itemImage = (ImageView) rootView.findViewById(R.id.itemImage);
        priceRange = (TextView) rootView.findViewById(R.id.price_range);
        shopCount = (TextView) rootView.findViewById(R.id.shop_count);

        bindItem();


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
        adapter = new AdapterNewCarts(dataset,getActivity(),this,item,(AppCompatActivity) getActivity());

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
//                        makeRequestRetrofit(false,false);
                        makeNetworkCall(false);
                    }

                }
            }


        });


    }




    @Override
    public void onRefresh() {

        dataset.clear();
        makeNetworkCall(false);
    }



    void makeRefreshNetworkCall()
    {

        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);

                try {

                    dataset.clear();
                    makeNetworkCall(false);

                } catch (IllegalArgumentException ex)
                {
                    ex.printStackTrace();

                }

                adapter.notifyDataSetChanged();
            }
        });
    }



    void makeNetworkCall(final boolean notifyChange)
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
                    (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LAT_CENTER_KEY),
                    (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LON_CENTER_KEY),
                    (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MAX_KEY),
                    (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MIN_KEY),
                    (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.PROXIMITY_KEY),
                    endUserID,
                    false,
                    null,null,null,null,
                    current_sort,
                    limit,offset,null
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
//                        dataset.clear();
                        dataset.addAll(response.body().getResults());
                        adapter.notifyDataSetChanged();


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
        dataset.clear();
        makeNetworkCall(true);

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
                                    + String.valueOf(dataset.size()) + "/" + item_count + ")",1
                    );
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        isDestroyed = true;
    }

    @Override
    public void NotifyLogin() {

        makeRefreshNetworkCall();
    }

    @Override
    public void notifySortChanged() {

        makeRefreshNetworkCall();
    }




    void bindItem()
    {
        itemName.setText(item.getItemName());

        itemDescription.setText(item.getItemDescription());

        if(item.getItemStats()!=null)
        {
            ItemStats itemStats = item.getItemStats();

            String shop = "Shops";

            if(itemStats.getShopCount()==1)
            {
                shop = "Shop";
            }

            shopCount.setText("In " + String.valueOf(itemStats.getShopCount()) + " " + shop);
            priceRange.setText( "Rs: "
                    + String.valueOf(itemStats.getMin_price())
                    + " - "
                    + String.valueOf(itemStats.getMax_price())
                    + " per " + item.getQuantityUnit()
            );


//            Log.d("applog","Item Stats :" + dataset.get(position).getItemStats().getShopCount());
        }


        String imagePath = UtilityGeneral.getImageEndpointURL(MyApplication.getAppContext())
                + item.getItemImageURL();

        Picasso.with(getActivity())
                .load(imagePath)
                .placeholder(R.drawable.nature_people)
                .into(itemImage);
    }

}
