package org.nearbyshops.enduserappnew.ShopItemByItemNew.ShopItemFragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.ItemDetailNew.ItemDetailFragment;
import org.nearbyshops.enduserappnew.ItemDetailNew.ItemDetailNew;
import org.nearbyshops.enduserappnew.Login.Login;
import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.Model.ShopItem;
import org.nearbyshops.enduserappnew.ModelEndPoints.ShopItemEndPoint;
import org.nearbyshops.enduserappnew.ModelRoles.User;
import org.nearbyshops.enduserappnew.ModelStats.ItemStats;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLocation;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Preferences.PrefShopHome;
import org.nearbyshops.enduserappnew.Preferences.UtilityFunctions;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.ShopItemService;
import org.nearbyshops.enduserappnew.ShopHome.ShopHome;
import org.nearbyshops.enduserappnew.ShopItemByItemNew.SlidingLayerSort.PrefSortShopItems;
import org.nearbyshops.enduserappnew.ShopsByCategory.Interfaces.NotifySort;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

//import org.nearbyshops.enduser.ItemsByCategoryTypeSimple.AdapterSimple;

public class ShopItemFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        AdapterShopItem.NotifyFilledCart, NotifySort
{

    Item item;

    @Inject
    ShopItemService shopItemService;


    ArrayList<ShopItem> dataset = new ArrayList<>();

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;

    AdapterShopItem adapter;
    GridLayoutManager layoutManager;


    boolean isDestroyed;


    // bindings for item
    @BindView(R.id.itemName) TextView categoryName;
//        TextView categoryDescription;

    //    @BindView(R.id.items_list_item) CardView itemCategoryListItem;
    @BindView(R.id.itemImage) ImageView categoryImage;
    @BindView(R.id.price_range) TextView priceRange;
    @BindView(R.id.price_average) TextView priceAverage;
    @BindView(R.id.shop_count) TextView shopCount;
    @BindView(R.id.item_rating) TextView itemRating;
    @BindView(R.id.rating_count) TextView ratingCount;




    boolean clearDataset = true;

    private int limit = 10;
    int offset = 0;
    int item_count = 0;

    /*
    * Terminologies and concepts
    *
    * Each Shop has a seperate cart. If a customer buys 2 items from two shops that will create two separate carts. Two Shops do not share one cart.
    *
    *
    * Filled Carts : Carts in which items are already added
    * New Carts : Carts in which no items are added
    *
    *
    * */





    public ShopItemFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }






    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        View rootView = inflater.inflate(R.layout.fragment_shop_item, container, false);
        ButterKnife.bind(this,rootView);
        setRetainInstance(true);


//        item = getArguments().getParcelable("item");

        String jsonString = getActivity().getIntent().getStringExtra("item_json");
        Gson gson = UtilityFunctions.provideGson();
        item = gson.fromJson(jsonString,Item.class);


//        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
//        swipeContainer = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeContainer);

        if(swipeContainer!=null) {

            swipeContainer.setOnRefreshListener(this);
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }






        if(savedInstanceState==null)
        {
            setupRecyclerView();
            swipeRefresh();
        }
        else
        {
//            onViewStateRestored(savedInstanceState);
            setupRecyclerView();
            adapter.getCartStats();
        }



        bindItem();


        return rootView;
    }







    void setupRecyclerView()
    {
        adapter = new AdapterShopItem(dataset,getActivity(),item,this);

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

//        layoutManager.setSpanCount(metrics.widthPixels/350);


//        int spanCount = (int) (metrics.widthPixels/(230 * metrics.density));
//
//        if(spanCount==0){
//            spanCount = 1;
//        }




        layoutManager.setSpanCount(1);



        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


                if(offset + limit > layoutManager.findLastVisibleItemPosition())
                {
                    return;
                }




                if(layoutManager.findLastVisibleItemPosition()==dataset.size()-1)
                {



                    // trigger fetch next page

                    if((offset + limit)<=item_count)
                    {
                        offset = offset + limit;

//                        makeRequestItem(false,false);

                        fetchNewCartItems();

                    }


                }
            }

        });



    }





    void swipeRefresh()
    {

        swipeContainer.post(new Runnable() {
            @Override
            public void run() {

                swipeContainer.setRefreshing(true);
                onRefresh();

            }
        });
    }








    void fetchNewCartItems()
    {
        // fetch shop items from shops with carts not filled




        User endUser = PrefLogin.getUser(getActivity());


//        if(endUser == null)
//        {
//            swipeContainer.setRefreshing(false);
//            return;
//        }


        if(item==null)
        {
            showToastMessage("Item null !");
            swipeContainer.setRefreshing(false);
            return;
        }



        Integer endUserID = null;


        if(endUser!=null)
        {
            endUserID = endUser.getUserID();
        }




        String current_sort = "";

        current_sort = PrefSortShopItems.getSort(getActivity())
                + " " + PrefSortShopItems.getAscending(getActivity());


        // Network Available
        Call<ShopItemEndPoint> call = shopItemService.getShopItemEndpoint(
                null,null,item.getItemID(),
                PrefLocation.getLatitude(getActivity()),
                PrefLocation.getLongitude(getActivity()),
                null, null,
                null,
                endUserID,
                false,
                null,null,null,null,
                null,true,current_sort,
                limit,offset,null,true
        );

        call.enqueue(new Callback<ShopItemEndPoint>() {
            @Override
            public void onResponse(Call<ShopItemEndPoint> call, Response<ShopItemEndPoint> response) {


                if(isDestroyed)
                {
                    return;
                }


                if(response.code()==200)
                {
                    if(response.body()!=null)
                    {

                        if(clearDataset)
                        {
                            dataset.clear();
                            clearDataset = false;
                        }


                        dataset.addAll(response.body().getResults());
                        item_count = response.body().getItemCount();

                    }
                }
                else
                {
                    showToastMessage("Response Code : " + String.valueOf(response.code()));
                }




                adapter.notifyDataSetChanged();
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






    @Override
    public void onRefresh() {
        clearDataset = true;
        fetchFilledCartItems();
        fetchNewCartItems();
    }





    void fetchFilledCartItems()
    {
            // fetch shop items from shops with filled carts


            User endUser = PrefLogin.getUser(getActivity());


            if(endUser == null)
            {
                swipeContainer.setRefreshing(false);
                return;
            }

            if(item==null)
            {
                swipeContainer.setRefreshing(false);
                return;
            }





        String current_sort = "";

        current_sort = PrefSortShopItems.getSort(getActivity()) + " " + PrefSortShopItems.getAscending(getActivity());



        Call<ShopItemEndPoint> callEndpoint = shopItemService.getShopItemEndpoint(
                    null,null,item.getItemID(),
                    PrefLocation.getLatitude(getActivity()),
                    PrefLocation.getLongitude(getActivity()),
                    null, null,
                    null,
                    endUser.getUserID(),
                    true,
                    null,null,null,null,
                    null,
                    true, current_sort,
                    null,null,null,
                    true
            );


            callEndpoint.enqueue(new Callback<ShopItemEndPoint>() {
                @Override
                public void onResponse(Call<ShopItemEndPoint> call, Response<ShopItemEndPoint> response) {

                    if (isDestroyed) {
                        return;
                    }


                    if(response.code()==200)
                    {

                        if (response.body() != null)
                        {

                            if(clearDataset)
                            {
                                dataset.clear();
                                clearDataset=false;
                            }

                            dataset.addAll(0,response.body().getResults());

                            adapter.getCartStats();
                            adapter.notifyDataSetChanged();
                            swipeContainer.setRefreshing(false);


                        }
                    }
                    else
                    {
                        showToastMessage("Failed code : " + String.valueOf(response.code()));
                    }



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




    void showToastMessage(String message)
    {
        if(getActivity()!=null)
        {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        }
    }






    @Override
    public void notifyCartDataChanged() {

//        swipeRefresh();
    }



    @Override
    public void notifyShopLogoClick(Shop shop) {

        Intent shopHomeIntent = new Intent(getActivity(), ShopHome.class);
        PrefShopHome.saveShop(shop,getActivity());
        startActivity(shopHomeIntent);
    }






    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isDestroyed = true;
    }





//    @Override
//    public void notifyNewCartsChanged() {
////        swipeRefresh();
//        makeNetworkCall(false);
//    }


    @Override
    public void notifySortChanged() {
        onRefresh();
    }



    @OnClick(R.id.include)
    void listItemClick()
    {

        Intent intent = new Intent(getActivity(), ItemDetailNew.class);
//        intent.putExtra(ItemDetail.ITEM_DETAIL_INTENT_KEY,item);

        String itemJson = UtilityFunctions.provideGson().toJson(item);
        intent.putExtra(ItemDetailFragment.TAG_JSON_STRING,itemJson);

        getActivity().startActivity(intent);


    }








    void bindItem()
    {

        if(item==null)
        {
            return;
        }


        categoryName.setText(item.getItemName());

        ItemStats itemStats = item.getItemStats();

        if(itemStats!=null)
        {
            String currency = "";
            currency = PrefGeneral.getCurrencySymbol(getActivity());

            priceRange.setText("Price Range :\n" + currency + " " + itemStats.getMin_price() + " - " + itemStats.getMax_price() + " per " + item.getQuantityUnit());
            priceAverage.setText("Price Average :\n" + currency + " " + itemStats.getAvg_price() + " per " + item.getQuantityUnit());
            shopCount.setText("Available in " + itemStats.getShopCount() + " Shops");
        }

        itemRating.setText(String.format("%.2f",item.getRt_rating_avg()));
        ratingCount.setText("( " + String.valueOf(item.getRt_rating_count()) + " Ratings )");




        if(item.getRt_rating_count()==0)
        {
            itemRating.setText(" New ");
            itemRating.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.phonographyBlue));
            ratingCount.setVisibility(View.GONE);
        }
        else
        {

            ratingCount.setText("( " + String.valueOf(item.getRt_rating_count()) + " Ratings )");
            itemRating.setText(String.format(" %.2f ",item.getRt_rating_avg()));
            itemRating.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.gplus_color_2));
            ratingCount.setVisibility(View.VISIBLE);
        }



        String imagePath = PrefGeneral.getServiceURL(getActivity())
                + "/api/v1/Item/Image/five_hundred_" + item.getItemImageURL() + ".jpg";


        Drawable drawable = VectorDrawableCompat
                .create(getActivity().getResources(),
                        R.drawable.ic_nature_people_white_48px, getActivity().getTheme());

        Picasso.with(getActivity()).load(imagePath)
                .placeholder(drawable)
                .into(categoryImage);

    }









    @Override
    public void showLoginScreen() {

        Intent intent = new Intent(getActivity(), Login.class);
        startActivityForResult(intent,123);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==123 && resultCode == RESULT_OK)
        {
            // login success
            adapter.getCartStats();
        }
    }



}

