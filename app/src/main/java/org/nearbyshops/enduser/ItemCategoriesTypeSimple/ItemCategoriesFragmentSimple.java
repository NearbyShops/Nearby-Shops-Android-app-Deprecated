package org.nearbyshops.enduser.ItemCategoriesTypeSimple;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.nearbyshops.enduser.DaggerComponentBuilder;
import org.nearbyshops.enduser.ItemCategoriesTypeSimple.Interfaces.NotifyBackPressed;
import org.nearbyshops.enduser.Model.Item;
import org.nearbyshops.enduser.Model.ItemCategory;
import org.nearbyshops.enduser.ModelEndPoints.ItemCategoryEndPoint;
import org.nearbyshops.enduser.ModelEndPoints.ItemEndPoint;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.RetrofitRESTContract.ItemCategoryService;
import org.nearbyshops.enduser.RetrofitRESTContract.ItemService;
import org.nearbyshops.enduser.ShopsByCategory.Interfaces.NotifyGeneral;
import org.nearbyshops.enduser.Utility.UtilityGeneral;
import org.nearbyshops.enduser.UtilitySort.UtilitySortItemsByCategory;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.State;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 2/12/16.
 */

public class ItemCategoriesFragmentSimple extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterSimple.NotificationsFromAdapter , NotifyBackPressed {

    boolean isDestroyed = false;

    private int limit_item = 10;
    int offset_item = 0;
    int item_count_item_category = 0;
    int item_count_item;

    @Bind(R.id.swipe_container) SwipeRefreshLayout swipeContainer;
    @Bind(R.id.recycler_view) RecyclerView itemCategoriesList;

    ArrayList<Object> dataset = new ArrayList<>();
    GridLayoutManager layoutManager;

    AdapterSimple listAdapter;


    @Inject
    ItemCategoryService itemCategoryService;


    @Inject
    ItemService itemService;


    ItemCategory currentCategory = null;


    public ItemCategoriesFragmentSimple() {

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
        View rootView = inflater.inflate(R.layout.fragment_item_categories_simple, container, false);

        ButterKnife.bind(this,rootView);


        if(savedInstanceState ==null)
        {
            swipeContainer.post(new Runnable() {
                @Override
                public void run() {

                    swipeContainer.setRefreshing(true);

                    onRefresh();
                }
            });
        }


        setupRecyclerView();
        setupSwipeContainer();
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

        listAdapter = new AdapterSimple(dataset,getActivity(),this);
        itemCategoriesList.setAdapter(listAdapter);

        layoutManager = new GridLayoutManager(getActivity(),4, LinearLayoutManager.VERTICAL,false);
        itemCategoriesList.setLayoutManager(layoutManager);



        // Code for Staggered Grid Layout
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {


            @Override
            public int getSpanSize(int position) {

                if(dataset.get(position) instanceof ItemCategory)
                {
                    return 2;
                }
                else if(dataset.get(position) instanceof Item)
                {
                    return 4;
                }

                return 2;
            }
        });


        final DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

//        layoutManager.setSpanCount(metrics.widthPixels/350);


//        int spanCount = (int) (metrics.widthPixels/(150 * metrics.density));
//
//        if(spanCount==0){
//            spanCount = 1;
//        }

//        layoutManager.setSpanCount(spanCount);


        itemCategoriesList.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(layoutManager.findLastVisibleItemPosition()==dataset.size()-1)
                {
                    // trigger fetch next page

                    if((offset_item + limit_item)<=item_count_item)
                    {
                        offset_item = offset_item + limit_item;
//                        makeRequestRetrofit(false,false,false);

                        makeRequestItem(false,false);

                    }

                }
            }

        });

    }


    @Override
    public void onRefresh() {
        makeRequestItemCategory(true);
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
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        }
    }



    boolean isFirst = true;


    void makeRequestItemCategory(final boolean clearDataset)
    {


        Call<ItemCategoryEndPoint> endPointCall = itemCategoryService.getItemCategoriesEndPoint(
                null,
                currentCategory.getItemCategoryID(),
                null,
                (double) UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LAT_CENTER_KEY, 0),
                (double) UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LON_CENTER_KEY, 0),
                null,null,null,
                "id",null,null,false);


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


                    if(isFirst)
                    {
                        isFirst = false;


                        if(clearDataset)
                        {
                            dataset.clear();
                        }

                    }
                    else
                    {
                        isFirst = true;// reset the flag
                    }



                    dataset.addAll(endPoint.getResults());
                    listAdapter.notifyDataSetChanged();

//                    notifyTitleChanged();


                }


                makeRequestItem(true,true);

                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<ItemCategoryEndPoint> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }

                showToastMessage("Network request failed. Please check your connection !");
                if(swipeContainer!=null)
                {
                    swipeContainer.setRefreshing(false);
                }

                makeRequestItem(true,true);

            }
        });
    }




    void makeRequestItem(final boolean clearDataset, boolean resetOffset)
    {

        if(resetOffset)
        {
            offset_item = 0;
        }


        String current_sort = "";

        current_sort = UtilitySortItemsByCategory.getSort(getContext()) + " " + UtilitySortItemsByCategory.getAscending(getContext());

        Call<ItemEndPoint> endPointCall = itemService.getItemsEndpoint(currentCategory.getItemCategoryID(),
                null,
                (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LAT_CENTER_KEY),
                (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LON_CENTER_KEY),
                null,null, null, null,
                current_sort, limit_item,offset_item,null);


        endPointCall.enqueue(new Callback<ItemEndPoint>() {
            @Override
            public void onResponse(Call<ItemEndPoint> call, Response<ItemEndPoint> response) {


                if(isDestroyed)
                {
                    return;
                }



                if(response.body()!=null)
                {

                    if(isFirst)
                    {
                        isFirst = false;
                        if(clearDataset)
                        {
                            dataset.clear();
                        }
                    }
                    else
                    {
                        isFirst = true;// reset the flag
                    }

                    dataset.addAll(response.body().getResults());
                    listAdapter.notifyDataSetChanged();

                    if(response.body().getItemCount()!=null)
                    {
                        item_count_item = response.body().getItemCount();
                    }

//                    notifyTitleChanged();
                }


                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<ItemEndPoint> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }

                swipeContainer.setRefreshing(false);

                showToastMessage("Network request failed. Please check your connection !");

            }
        });

    }





    @Override
    public void notifyRequestSubCategory(ItemCategory itemCategory) {

        ItemCategory temp = currentCategory;
        currentCategory = itemCategory;
        currentCategory.setParentCategory(temp);


        swipeContainer.post(new Runnable() {
            @Override
            public void run() {

                swipeContainer.setRefreshing(true);
                onRefresh();
            }
        });
    }




    @Override
    public boolean backPressed() {

        int currentCategoryID = 1; // the ID of root category is always supposed to be 1

        if(currentCategory!=null) {


            if (currentCategory.getParentCategory() != null) {

                currentCategory = currentCategory.getParentCategory();
                currentCategoryID = currentCategory.getItemCategoryID();

            } else {
                currentCategoryID = currentCategory.getParentCategoryID();
            }


            if (currentCategoryID != -1) {

                swipeContainer.post(new Runnable() {
                    @Override
                    public void run() {

                        swipeContainer.setRefreshing(true);
                        onRefresh();
                    }
                });
            }
        }

        return currentCategoryID == -1;
    }

}
