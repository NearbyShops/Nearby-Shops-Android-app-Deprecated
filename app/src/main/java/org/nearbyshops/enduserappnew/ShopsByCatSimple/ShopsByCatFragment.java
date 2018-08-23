package org.nearbyshops.enduserappnew.ShopsByCatSimple;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.ItemsByCategoryTypeSimple.Interfaces.NotifyBackPressed;
import org.nearbyshops.enduserappnew.ItemsByCategoryTypeSimple.Utility.HeaderItemsList;
import org.nearbyshops.enduserappnew.ItemsInShopByCat.Interfaces.NotifyIndicatorChanged;
import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.ItemCategory;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.ModelEndPoints.ItemCategoryEndPoint;
import org.nearbyshops.enduserappnew.ModelEndPoints.ShopEndPoint;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.ItemCategoryService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.ShopService;
import org.nearbyshops.enduserappnew.Interfaces.NotifySearch;
import org.nearbyshops.enduserappnew.Preferences.PrefLocation;
import org.nearbyshops.enduserappnew.ShopsByCategory.Interfaces.NotifySort;
import org.nearbyshops.enduserappnew.Shops.SlidingLayerSort.UtilitySortShopsByCategory;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 2/12/16.
 */

public class ShopsByCatFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterShopsByCat.NotificationsFromAdapter , NotifyBackPressed, NotifySort, NotifySearch {


//    Map<Integer,ShopItem> shopItemMapTemp = new HashMap<>();

    boolean isDestroyed = false;
    boolean show = true;

    int item_count_item_category = 0;

    private int limit_item = 10;
    int offset_item = 0;
    int item_count_item;
    int fetched_items_count = 0;

    @BindView(R.id.swipe_container) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.recycler_view) RecyclerView itemCategoriesList;

    ArrayList<Object> dataset = new ArrayList<>();
    ArrayList<ItemCategory> datasetCategory = new ArrayList<>();
    ArrayList<Shop> datasetShops = new ArrayList<>();


    GridLayoutManager layoutManager;

    AdapterShopsByCat listAdapter;


    @Inject
    ItemCategoryService itemCategoryService;

    @Inject
    ShopService shopService;

    ItemCategory currentCategory = null;


    public ShopsByCatFragment() {

        super();

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

        currentCategory = new ItemCategory();
        currentCategory.setItemCategoryID(1);
        currentCategory.setCategoryName("");
        currentCategory.setParentCategoryID(-1);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_shops_by_cat, container, false);

        ButterKnife.bind(this,rootView);


        setupRecyclerView();
        setupSwipeContainer();


        if(savedInstanceState ==null)
        {
            makeRefreshNetworkCall();
        }
        else
        {
            // add this at every rotation
//            listAdapter.shopItemMap.putAll(shopItemMapTemp);
        }


        notifyItemIndicatorChanged();

        return rootView;
    }



    void setupSwipeContainer()
    {

        if(swipeContainer!=null) {

            swipeContainer.setOnRefreshListener(this);
            swipeContainer.setColorSchemeResources(
                    android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }

    }



    void setupRecyclerView()
    {


        listAdapter = new AdapterShopsByCat(dataset,getActivity(),this,this);
        itemCategoriesList.setAdapter(listAdapter);

        layoutManager = new GridLayoutManager(getActivity(),6, LinearLayoutManager.VERTICAL,false);
        itemCategoriesList.setLayoutManager(layoutManager);



        // Code for Staggered Grid Layout
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {


            @Override
            public int getSpanSize(int position) {

                if(position == dataset.size())
                {
                    return 6;
                }
                else if(dataset.get(position) instanceof ItemCategory)
                {
                       final DisplayMetrics metrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

                    int spanCount = (int) (metrics.widthPixels/(180 * metrics.density));

                    if(spanCount==0){
                        spanCount = 1;
                    }

                    return (6/spanCount);

                }
                else if(dataset.get(position) instanceof Item)
                {

                    return 6;
                }
                else if(dataset.get(position) instanceof HeaderItemsList)
                {
                    return 6;
                }

                return 6;
            }
        });


        final DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);


        itemCategoriesList.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(offset_item + limit_item > layoutManager.findLastVisibleItemPosition())
                {
                    return;
                }

                if(layoutManager.findLastVisibleItemPosition()==dataset.size())
                {

                    // trigger fetch next page

                    if((offset_item+limit_item)<=item_count_item)
                    {
                        offset_item = offset_item + limit_item;
                        makeRequestShopItem(false,false);
                    }

                }
            }

        });

    }


//    @State int previous_position = -1;



    @Override
    public void onRefresh() {

        makeRequestItemCategory();
        makeRequestShopItem(true,true);
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
    public void onDestroyView() {
        super.onDestroyView();
        isDestroyed = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        isDestroyed=false;
    }



    private void showToastMessage(String message)
    {
        if(getActivity()!=null)
        {
            Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
        }
    }



    boolean isFirst = true;


    void makeRequestItemCategory()
    {

        Call<ItemCategoryEndPoint> endPointCall = null;

        if(searchQuery == null)
        {
//            /endPointCall = itemCategoryService.getItemCategoriesQuerySimple(
//                    currentCategory.getItemCategoryID(),
//                    null,
//                    null,
//                    "id",null,null
//            );

//            endPointCall = itemCategoryService.getItemCategories(
//                    currentShop.getShopID(),
//                    currentCategory.getItemCategoryID(),
//                    null,null,null,null,null,null,
//                    "id", null,null,
//                    false
//            );


            endPointCall = itemCategoryService.getItemCategoriesEndPoint(
                    null,currentCategory.getItemCategoryID(),
                    null,
                    PrefLocation.getLatitude(getActivity()),
                    PrefLocation.getLongitude(getActivity()),
                    null,
                    null,
                    null,true,
                    ItemCategory.CATEGORY_ORDER,null,null,false);

        }
        else
        {

//            endPointCall = itemCategoryService.getItemCategoriesQuerySimple(
//                    null,
//                    null,
//                    searchQuery,
//                    "id",null,null
//            );


//            endPointCall = itemCategoryService.getItemCategories(
//                    currentShop.getShopID(),
//                    null,
//                    null,null,null,null,null,null,
//                    "id", null,null,
//                    false
//            );

            endPointCall = itemCategoryService.getItemCategoriesEndPoint(
                    null,null,
                    null,
                    PrefLocation.getLatitude(getActivity()),
                    PrefLocation.getLongitude(getActivity()),
                    null,
                    null,
                    null,true,
                    ItemCategory.CATEGORY_ORDER,null,null,false);
        }



        endPointCall.enqueue(new Callback<ItemCategoryEndPoint>() {
            @Override
            public void onResponse(Call<ItemCategoryEndPoint> call, Response<ItemCategoryEndPoint> response) {

                if(isDestroyed)
                {
                    return;
                }

                if(response.body()!=null)
                {

                    ItemCategoryEndPoint endPoint = response.body();
                    item_count_item_category = endPoint.getItemCount();

                    datasetCategory.clear();
                    datasetCategory.addAll(endPoint.getResults());
                }


                if(isFirst)
                {
                    isFirst = false;
                }
                else
                {
                    // is last
                    refreshAdapter();
                    isFirst = true;// reset the flag
                }


//                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ItemCategoryEndPoint> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }

                showToastMessage("Network request failed. Please check your connection !");


                if(isFirst)
                {
                    isFirst = false;
                }
                else
                {
                    // is last
                    refreshAdapter();
                    isFirst = true;// reset the flag
                }



//                if(swipeContainer!=null)
//                {
//                    swipeContainer.setRefreshing(false);
//                }

            }
        });
    }



    void refreshAdapter()
    {
        dataset.clear();

        HeaderItemsList headerItemCategory = new HeaderItemsList();

        if(searchQuery==null)
        {
            if(currentCategory.getItemCategoryID()==1)
            {
                headerItemCategory.setHeading(" Categories");
            }
            else
            {
                headerItemCategory.setHeading(currentCategory.getCategoryName() + " Subcategories");
            }
        }
        else
        {
            headerItemCategory.setHeading( "Search Results (Subcategories)");
        }

        dataset.add(headerItemCategory);
        dataset.addAll(datasetCategory);
        HeaderItemsList headerItem = new HeaderItemsList();

        if(searchQuery==null)
        {
            headerItem.setHeading(currentCategory.getCategoryName() + " Shops");
        }
        else
        {
            headerItem.setHeading("Search Results (Shops)");
        }


        dataset.add(headerItem);
        dataset.addAll(datasetShops);
        listAdapter.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
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




    void makeRequestShopItem(final boolean clearDataset, boolean resetOffset)
    {

        if(resetOffset)
        {
            offset_item = 0;
        }

        String current_sort = "";

        current_sort = UtilitySortShopsByCategory.getSort(getContext()) + " " + UtilitySortShopsByCategory.getAscending(getContext());

        Call<ShopEndPoint> endPointCall = null;

//        Shop currentShop = UtilityShopHome.getShop(getContext());


        if(currentCategory.getItemCategoryID()==1)
        {
            datasetShops.clear();

            if(isFirst)
            {
                isFirst = false;
            }
            else
            {
                // is last
                refreshAdapter();
                isFirst = true;// reset the flag
            }

            return;
        }


        if(searchQuery==null)
        {

            endPointCall = shopService.filterShopsByItemCategory(
                    currentCategory.getItemCategoryID(),
                    null,
                    PrefLocation.getLatitude(getActivity()),
                    PrefLocation.getLongitude(getActivity()),
                    null,
                    null,
                    null,
                    current_sort,limit_item,offset_item,false
            );


        }
        else
        {

            endPointCall = shopService.filterShopsByItemCategory(
                    currentCategory.getItemCategoryID(),
                    null,
                    PrefLocation.getLatitude(getActivity()),
                    PrefLocation.getLongitude(getActivity()),
                    null,
                    null,
                    null,
                    current_sort,limit_item,offset_item,false
            );
        }


        endPointCall.enqueue(new Callback<ShopEndPoint>() {
            @Override
            public void onResponse(Call<ShopEndPoint> call, Response<ShopEndPoint> response) {


                if(isDestroyed)
                {
                    return;
                }


                if(clearDataset)
                {

                    if(response.body()!=null)
                    {

                        datasetShops.clear();
                        datasetShops.addAll(response.body().getResults());
                        item_count_item = response.body().getItemCount();
                        fetched_items_count = datasetShops.size();

//                        if(response.body().getItemCount()!=null)
//                        {
//
//                        }
                    }


                    if(isFirst)
                    {
                        isFirst = false;
                    }
                    else
                    {
                        // is last
                        refreshAdapter();
                        isFirst = true;// reset the flag
                    }

                }
                else
                {
                    if(response.body()!=null)
                    {

                        dataset.addAll(response.body().getResults());
                        fetched_items_count = fetched_items_count + response.body().getResults().size();
                        item_count_item = response.body().getItemCount();
                        listAdapter.notifyDataSetChanged();
                    }

                    swipeContainer.setRefreshing(false);
                }


                notifyItemIndicatorChanged();


            }

            @Override
            public void onFailure(Call<ShopEndPoint> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }


                if(clearDataset)
                {

                    if(isFirst)
                    {
                        isFirst = false;
                    }
                    else
                    {
                        // is last
                        refreshAdapter();
                        isFirst = true;// reset the flag
                    }
                }
                else
                {
                    swipeContainer.setRefreshing(false);
                }


                showToastMessage("Items: Network request failed. Please check your connection !");


            }
        });

    }






    @Override
    public void notifyRequestSubCategory(ItemCategory itemCategory) {

        ItemCategory temp = currentCategory;
        currentCategory = itemCategory;
        currentCategory.setParentCategory(temp);

        makeRefreshNetworkCall();

        // End Search Mode
        searchQuery = null;

        // reset previous flag

    }











    @Override
    public boolean backPressed() {

        // reset previous flag

        int currentCategoryID = 1; // the ID of root category is always supposed to be 1

        // clear selected items
//        listAdapter.selectedItems.clear();

        if(currentCategory!=null) {


            if (currentCategory.getParentCategory() != null) {

                currentCategory = currentCategory.getParentCategory();
                currentCategoryID = currentCategory.getItemCategoryID();

            } else {
                currentCategoryID = currentCategory.getParentCategoryID();
            }


            if (currentCategoryID != -1) {
                makeRefreshNetworkCall();
            }
        }

        return currentCategoryID == -1;
    }


    void notifyItemIndicatorChanged()
    {
        if(getActivity() instanceof NotifyIndicatorChanged)
        {
            ((NotifyIndicatorChanged) getActivity()).notifyItemIndicatorChanged(String.valueOf(fetched_items_count)
                    + " out of " + String.valueOf(item_count_item) + " " + currentCategory.getCategoryName() + " Shops");
        }
    }


    @Override
    public void notifySortChanged() {

        System.out.println("Notify Sort Clicked !");
        makeRefreshNetworkCall();
    }


    // display shop Item Status

}
