package org.nearbyshops.enduserappnew.ItemsInShopByCat;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.ItemDetail.ItemDetail;
import org.nearbyshops.enduserappnew.ItemsByCategoryTypeSimple.Interfaces.NotifyBackPressed;
import org.nearbyshops.enduserappnew.ItemsByCategoryTypeSimple.Utility.HeaderItemsList;
import org.nearbyshops.enduserappnew.ItemsInShopByCat.Interfaces.NotifyIndicatorChanged;
import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.ItemCategory;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.Model.ShopItem;
import org.nearbyshops.enduserappnew.ModelEndPoints.ItemCategoryEndPoint;
import org.nearbyshops.enduserappnew.ModelEndPoints.ShopItemEndPoint;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.ItemCategoryService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.ShopItemService;
import org.nearbyshops.enduserappnew.ItemsInShopByCat.SlidingLayerSort.UtilitySortItemsInShop;
import org.nearbyshops.enduserappnew.Interfaces.NotifySearch;
import org.nearbyshops.enduserappnew.ShopsByCategoryOld.Interfaces.NotifySort;
import org.nearbyshops.enduserappnew.Utility.PrefShopHome;

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

public class ItemsInShopByCatFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterItemsInShop.NotificationsFromAdapter , NotifyBackPressed, NotifySort, NotifySearch {


//    Map<Integer,ShopItem> shopItemMapTemp = new HashMap<>();

    boolean isDestroyed = false;
    boolean show = true;

    int item_count_item_category = 0;

    private int limit_item = 10;
    int offset_item = 0;
    int item_count_item;
    int fetched_items_count = 0;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.recycler_view)
    RecyclerView itemCategoriesList;

    ArrayList<Object> dataset = new ArrayList<>();
    ArrayList<ItemCategory> datasetCategory = new ArrayList<>();
    ArrayList<ShopItem> datasetShopItems = new ArrayList<>();



    @BindView(R.id.itemsInCart) public TextView itemsInCart;
    @BindView(R.id.cartTotal) public TextView cartTotal;


    GridLayoutManager layoutManager;

    AdapterItemsInShop listAdapter;


    @Inject
    ItemCategoryService itemCategoryService;

    @Inject
    ShopItemService shopItemService;

//    @Inject
//    ItemService itemService;


    ItemCategory currentCategory = null;


    public ItemsInShopByCatFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_items_in_stock_by_cat, container, false);

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


        listAdapter = new AdapterItemsInShop(dataset,getActivity(),this,this);
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

        /*Call<ItemCategoryEndPoint> endPointCall = itemCategoryService.getItemCategoriesEndPoint(
                null,
                currentCategory.getItemCategoryID(),
                null,
                null,
                null,
                null,null,null,
                "ITEM_CATEGORY_NAME",null,null,false);*/

        Call<ItemCategoryEndPoint> endPointCall = null;

        Shop currentShop = PrefShopHome.getShop(getContext());

        if(searchQuery == null)
        {
           endPointCall = itemCategoryService.getItemCategoriesEndPoint(
                    currentShop.getShopID(),currentCategory.getItemCategoryID(),
                    null,
                    null,null,
                    null,null,
                    null,true,
                    ItemCategory.CATEGORY_ORDER,null,null,false);


        }
        else
        {


            endPointCall = itemCategoryService.getItemCategoriesEndPoint(
                    currentShop.getShopID(),currentCategory.getItemCategoryID(),
                    null,
                    null,null,
                    null,null,
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
            headerItemCategory.setHeading(currentCategory.getCategoryName() + " Subcategories");
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
            headerItem.setHeading(currentCategory.getCategoryName() + " Items In Shop");
        }
        else
        {
            headerItem.setHeading("Search Results (Items In Shop)");
        }


        dataset.add(headerItem);
        dataset.addAll(datasetShopItems);
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

        current_sort = UtilitySortItemsInShop.getSort(getContext()) + " " + UtilitySortItemsInShop.getAscending(getContext());


        Call<ShopItemEndPoint> endPointCall = null;

        Shop currentShop = PrefShopHome.getShop(getContext());


        if(searchQuery==null)
        {



            endPointCall = shopItemService.getShopItemEndpoint(
                    currentCategory.getItemCategoryID(),
                    currentShop.getShopID(),
                    null,null,null,null,null,null,null,
                    null,null,
                    null,null,null,
                    null,true,current_sort,
                    limit_item,offset_item,false,
                    true);

        }
        else
        {


            endPointCall = shopItemService.getShopItemEndpoint(
                    null,
                    currentShop.getShopID(),
                    null,null,null,null,null,null,null,null,null,
                    null,null,null,
                    searchQuery,
                    true,current_sort,
                    limit_item,offset_item,false,
                    true);
        }


        endPointCall.enqueue(new Callback<ShopItemEndPoint>() {
            @Override
            public void onResponse(Call<ShopItemEndPoint> call, Response<ShopItemEndPoint> response) {


                if(isDestroyed)
                {
                    return;
                }


                if(clearDataset)
                {

                    if(response.body()!=null)
                    {

                        datasetShopItems.clear();
                        datasetShopItems.addAll(response.body().getResults());
                        item_count_item = response.body().getItemCount();
                        fetched_items_count = datasetShopItems.size();

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
            public void onFailure(Call<ShopItemEndPoint> call, Throwable t) {

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
            ((NotifyIndicatorChanged) getActivity()).notifyItemIndicatorChanged(String.valueOf(fetched_items_count) + " out of " + String.valueOf(item_count_item) + " " + currentCategory.getCategoryName() + " Items in Shop");
        }
    }


    @Override
    public void notifySortChanged() {

        System.out.println("Notify Sort Clicked !");
        makeRefreshNetworkCall();
    }



    // display shop Item Status




    @Override
    public void notifyItemImageClick(Item item) {

        Intent intent = new Intent(getActivity(), ItemDetail.class);
        intent.putExtra(ItemDetail.ITEM_DETAIL_INTENT_KEY,item);
        getActivity().startActivity(intent);
    }

}
