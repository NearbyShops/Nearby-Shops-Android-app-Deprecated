package org.nearbyshops.enduserappnew.ShopItemByItem.FilledCarts;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
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

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.ItemDetail.ItemDetail;
//import org.nearbyshops.enduser.ItemsByCategoryTypeSimple.AdapterSimple;
import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.Model.ShopItem;
import org.nearbyshops.enduserappnew.ModelEndPoints.ShopItemEndPoint;
import org.nearbyshops.enduserappnew.ModelRoles.User;
import org.nearbyshops.enduserappnew.ModelStats.ItemStats;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.ShopItemService;
import org.nearbyshops.enduserappnew.ShopHome.ShopHome;
import org.nearbyshops.enduserappnew.ShopItemByItem.Interfaces.NotifyFillCartsChanged;
import org.nearbyshops.enduserappnew.ShopItemByItem.Interfaces.NotifyNewCartsChanged;
import org.nearbyshops.enduserappnew.ShopItemByItem.Interfaces.NotifySwipeToRight;
import org.nearbyshops.enduserappnew.Shops.UtilityLocation;
import org.nearbyshops.enduserappnew.ShopsByCategoryOld.Interfaces.NotifySort;
import org.nearbyshops.enduserappnew.ShopsByCategoryOld.Interfaces.NotifyTitleChanged;
import org.nearbyshops.enduserappnew.Utility.PrefGeneral;
import org.nearbyshops.enduserappnew.Utility.PrefLogin;
import org.nearbyshops.enduserappnew.ShopItemByItem.SlidingLayerSort.UtilitySortShopItems;
import org.nearbyshops.enduserappnew.Utility.PrefShopHome;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilledCartsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        AdapterFilledCarts.NotifyFilledCart, NotifyNewCartsChanged, NotifySort
{

    Item item;

    @Inject
    ShopItemService shopItemService;


    ArrayList<ShopItem> dataset = new ArrayList<>();

//    @Bind(R.id.items_list_item)
//    CardView itemsListItem;


    RecyclerView recyclerView;
    AdapterFilledCarts adapter;
    GridLayoutManager layoutManager;
    SwipeRefreshLayout swipeContainer;

    boolean isDestroyed;


//    TextView itemDescription;
//    TextView itemName;
//    ImageView itemImage;



//    TextView priceRange;
//    TextView shopCount;
//    @Bind(R.id.item_rating) TextView itemRating;
//    @Bind(R.id.rating_count) TextView ratingCount;


//    NotifyFillCartsChanged notifyPagerAdapter;


//    NotifySwipeToRight notifyTabsActivity;


    public FilledCartsFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }

    public static FilledCartsFragment newInstance(Item item) {

        FilledCartsFragment fragment = new FilledCartsFragment();
        Bundle args = new Bundle();
        args.putParcelable("item",item);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        View rootView = inflater.inflate(R.layout.fragment_filled_carts, container, false);

        ButterKnife.bind(this,rootView);


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




        if(savedInstanceState==null)
        {
            setupRecyclerView();
            swipeRefresh();
        }
        else
        {
            onViewStateRestored(savedInstanceState);
            setupRecyclerView();
            adapter.makeNetworkCall();
        }




        // BindViewings for Item
//        itemDescription = (TextView) rootView.findViewById(R.id.itemDescription);
//        itemName = (TextView) rootView.findViewById(R.id.itemName);
//        itemImage = (ImageView) rootView.findViewById(R.id.itemImage);
//        priceRange = (TextView) rootView.findViewById(R.id.price_range);
//        shopCount = (TextView) rootView.findViewById(R.id.shop_count);


        bindItem();


        return rootView;
    }


    void setupRecyclerView()
    {
        adapter = new AdapterFilledCarts(dataset,getActivity(),item,this);

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


        int spanCount = (int) (metrics.widthPixels/(230 * metrics.density));

        if(spanCount==0){
            spanCount = 1;
        }

        layoutManager.setSpanCount(spanCount);


    }



    void swipeRefresh()
    {

        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);

//                    dataset.clear();
                    makeNetworkCall(false);
            }
        });
    }


    @Override
    public void onRefresh() {
        makeNetworkCall(false);
    }



    void makeNetworkCall(final boolean notifyChange)
    {

            User endUser = PrefLogin.getUser(getActivity());

            if(endUser == null)
            {
//                showLoginDialog();


                swipeContainer.setRefreshing(false);

                if(getActivity() instanceof NotifySwipeToRight)
                {
                    ((NotifySwipeToRight)getActivity()).notifySwipeRight();

                    return;
                }
            }



        String current_sort = "";

        current_sort = UtilitySortShopItems.getSort(getContext()) + " " + UtilitySortShopItems.getAscending(getContext());



        Call<ShopItemEndPoint> callEndpoint = shopItemService.getShopItemEndpoint(
                    null,null,item.getItemID(),
                    UtilityLocation.getLatitude(getActivity()),
                    UtilityLocation.getLongitude(getActivity()),
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

                    if (response.body() != null)
                    {

                        dataset.clear();
                        dataset.addAll(response.body().getResults());

                        if(response.body().getItemCount()==0)
                        {
                            if(getActivity() instanceof NotifySwipeToRight)
                            {
                                ((NotifySwipeToRight)getActivity()).notifySwipeRight();
                            }
                        }




                        if(notifyChange)
                        {
                            filledCartsChanged();
                        }


                        adapter.makeNetworkCall();
                        adapter.notifyDataSetChanged();
                        notifyTitleChanged();
                        swipeContainer.setRefreshing(false);
                    }
                    else
                    {

                        if(getActivity() instanceof NotifySwipeToRight)
                        {
                            ((NotifySwipeToRight)getActivity()).notifySwipeRight();
                        }
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




    void filledCartsChanged()
    {
        if(getActivity() instanceof NotifyFillCartsChanged)
        {
            ((NotifyFillCartsChanged)getActivity()).notifyFilledCartsChanged();
        }
    }



    @Override
    public void notifyCartDataChanged() {


        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);

                makeNetworkCall(true);
            }
        });


    }

    @Override
    public void notifyShopLogoClick(Shop shop) {

//        Intent intent = new Intent(getActivity(), ShopDetail.class);
//        intent.putExtra(ShopDetail.SHOP_DETAIL_INTENT_KEY,shop);
//        startActivity(intent);


        Intent shopHomeIntent = new Intent(getActivity(), ShopHome.class);
        PrefShopHome.saveShop(shop,getActivity());
        startActivity(shopHomeIntent);


    }


//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//        Icepick.restoreInstanceState(this,savedInstanceState);
//        notifyTitleChanged();
//    }


//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        Icepick.saveInstanceState(this,outState);
//    }



    void notifyTitleChanged()
    {
        if(getActivity() instanceof NotifyTitleChanged)
        {
            ((NotifyTitleChanged) getActivity())
                    .NotifyTitleChanged(
                            " Filled Carts ("
                            + String.valueOf(dataset.size()) + ")",0
                    );
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isDestroyed = true;
    }




    @Override
    public void notifyNewCartsChanged() {
//        swipeRefresh();
        makeNetworkCall(false);
    }


    @Override
    public void notifySortChanged() {
        onRefresh();
    }



    @OnClick(R.id.include)
    void listItemClick()
    {

        Intent intent = new Intent(getActivity(), ItemDetail.class);
        intent.putExtra(ItemDetail.ITEM_DETAIL_INTENT_KEY,item);
        getActivity().startActivity(intent);


    }







    @BindView(R.id.itemName) TextView categoryName;
//        TextView categoryDescription;

//    @BindView(R.id.items_list_item) CardView itemCategoryListItem;
    @BindView(R.id.itemImage) ImageView categoryImage;
    @BindView(R.id.price_range) TextView priceRange;
    @BindView(R.id.price_average) TextView priceAverage;
    @BindView(R.id.shop_count) TextView shopCount;
    @BindView(R.id.item_rating) TextView itemRating;
    @BindView(R.id.rating_count) TextView ratingCount;


    void bindItem()
    {

        categoryName.setText(item.getItemName());

        ItemStats itemStats = item.getItemStats();

        if(itemStats!=null)
        {
            String currency = "";
            currency = PrefGeneral.getCurrencySymbol(getActivity());

            priceRange.setText("Price Range :\n" + currency + ". " + itemStats.getMin_price() + " - " + itemStats.getMax_price() + " per " + item.getQuantityUnit());
            priceAverage.setText("Price Average :\n" + currency + ". " + itemStats.getAvg_price() + " per " + item.getQuantityUnit());
            shopCount.setText("Available in " + itemStats.getShopCount() + " Shops");
        }

        itemRating.setText(String.format("%.2f",item.getRt_rating_avg()));
        ratingCount.setText("( " + String.valueOf(item.getRt_rating_count()) + " Ratings )");


        String imagePath = PrefGeneral.getServiceURL(getActivity())
                + "/api/v1/Item/Image/five_hundred_" + item.getItemImageURL() + ".jpg";


        Drawable drawable = VectorDrawableCompat
                .create(getActivity().getResources(),
                        R.drawable.ic_nature_people_white_48px, getActivity().getTheme());

        Picasso.with(getActivity()).load(imagePath)
                .placeholder(drawable)
                .into(categoryImage);

    }




}

