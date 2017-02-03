package org.nearbyshops.enduserapp.ItemsByCategoryHorizontal.ItemCategories;


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

import org.nearbyshops.enduserapp.DaggerComponentBuilder;
import org.nearbyshops.enduserapp.Model.ItemCategory;
import org.nearbyshops.enduserapp.ModelEndPoints.ItemCategoryEndPoint;
import org.nearbyshops.enduserapp.R;
import org.nearbyshops.enduserapp.RetrofitRESTContract.ItemCategoryService;
import org.nearbyshops.enduserapp.ShopsByCategoryOld.Interfaces.NotifyBackPressed;
import org.nearbyshops.enduserapp.ShopsByCategoryOld.Interfaces.NotifyCategoryChanged;
import org.nearbyshops.enduserapp.ShopsByCategoryOld.Interfaces.NotifyGeneral;
import org.nearbyshops.enduserapp.ShopsByCategoryOld.Interfaces.NotifyTitleChanged;
import org.nearbyshops.enduserapp.ShopsByCategoryOld.Interfaces.ToggleFab;
import org.nearbyshops.enduserapp.Utility.UtilityGeneral;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import icepick.State;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemCategoriesHorizontal extends Fragment
        implements AdapterHorizontal.ReceiveNotificationsFromAdapter,
        SwipeRefreshLayout.OnRefreshListener,
        NotifyBackPressed {



    @State
    ArrayList<ItemCategory> dataset = new ArrayList<>();

    RecyclerView itemCategoriesList;
    AdapterHorizontal listAdapter;
    GridLayoutManager layoutManager;

    @State boolean show = true;

    @Inject
    ItemCategoryService itemCategoryService;

    @State
    ItemCategory currentCategory = null;



    private int limit = 30;
    @State int offset = 0;
    @State int item_count = 0;


//    @Bind(R.id.swipeContainer)
//    SwipeRefreshLayout swipeContainer;


    boolean isDestroyed;





    public ItemCategoriesHorizontal() {
        super();

        // Inject the dependencies using Dependency Injection
        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

        currentCategory = new ItemCategory();
        currentCategory.setItemCategoryID(1);
        currentCategory.setCategoryName("");
        currentCategory.setParentCategoryID(-1);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);

        View rootView = inflater.inflate(R.layout.fragment_item_categories_horizontal, container, false);

        ButterKnife.bind(this,rootView);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        itemCategoriesList = (RecyclerView)rootView.findViewById(R.id.recyclerViewItemCategories);


        if(savedInstanceState==null)
        {
            // make request to the network only for the first time and not the second time or when the context is changed.

            // reset the offset before making request


           /* swipeContainer.post(new Runnable() {
                @Override
                public void run() {
                    swipeContainer.setRefreshing(true);

                    try {


                        // make a network call


                    } catch (IllegalArgumentException ex)
                    {
                        ex.printStackTrace();

                    }
                }
            });*/



            offset = 0;
            dataset.clear();
            makeRequestRetrofit(false,false);

        }
//        else
//        {
//            onViewStateRestored(savedInstanceState);
//        }



        setupRecyclerView();
        setupSwipeContainer();


        return  rootView;

    }


    void setupRecyclerView()
    {

        listAdapter = new AdapterHorizontal(dataset,getActivity(),this,this);
        itemCategoriesList.setAdapter(listAdapter);
        layoutManager = new GridLayoutManager(getActivity(),1, LinearLayoutManager.HORIZONTAL,false);


        itemCategoriesList.setLayoutManager(layoutManager);





        // Code for Staggered Grid Layout
        /*layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position % 3 == 0 ? 2 : 1);
            }
        });
        */


        final DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

//        layoutManager.setSpanCount(metrics.widthPixels/350);


        int spanCount = (int) (metrics.widthPixels/(230 * metrics.density));

        if(spanCount==0){
            spanCount = 1;
        }

//        layoutManager.setSpanCount(spanCount);
        layoutManager.setSpanCount(1);



        itemCategoriesList.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(layoutManager.findLastVisibleItemPosition()==dataset.size()-1)
                {
                    // trigger fetch next page

                    if((offset+limit)<=item_count)
                    {
                        offset = offset + limit;
                        makeRequestRetrofit(false,false);
                    }

                }
            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                if(dy > 20)
                {

                    boolean previous = show;

                    show = false ;

                    if(show!=previous)
                    {
                        // changed
                        Log.d("scrolllog","show");

                        if(getActivity() instanceof ToggleFab)
                        {
                            ((ToggleFab)getActivity()).hideFab();
                        }
                    }

                }else if(dy < -20)
                {

                    boolean previous = show;

                    show = true;

                    if(show!=previous)
                    {
                        Log.d("scrolllog","hide");

                        if(getActivity() instanceof ToggleFab)
                        {
                            ((ToggleFab)getActivity()).showFab();
                        }
                    }
                }


            }

        });

    }



    void setupSwipeContainer()
    {
//
//        if(swipeContainer!=null) {
//
//            swipeContainer.setOnRefreshListener(this);
//            swipeContainer.setColorSchemeResources(
//                    android.R.color.holo_blue_bright,
//                    android.R.color.holo_green_light,
//                    android.R.color.holo_orange_light,
//                    android.R.color.holo_red_light);
//        }

    }



    public void makeRequestRetrofit(final boolean notifyItemCategoryChanged, final boolean backPressed)
    {

//        Shop currentShop = ApplicationState.getInstance().getCurrentShop();


        Call<ItemCategoryEndPoint> endPointCall = itemCategoryService.getItemCategoriesEndPoint(
                null,
                currentCategory.getItemCategoryID(),
                null,
                (double) UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LAT_CENTER_KEY, 0),
                (double) UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LON_CENTER_KEY, 0),
                (double) UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MAX_KEY, 0),
                (double) UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MIN_KEY, 0),
                (double) UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.PROXIMITY_KEY, 0),true,
                "id",limit,offset,false);

        Log.d("applog","DetachedTabs: Network call made !");


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
                    item_count = endPoint.getItemCount();
                    dataset.addAll(endPoint.getResults());
                    listAdapter.notifyDataSetChanged();

                    /*if(notifyItemCategoryChanged)
                    {
                        if(currentCategory!=null)
                        {

                            ((NotifyCategoryChanged)getActivity())
                                    .itemCategoryChanged(currentCategory,backPressed);
                        }
                    }*/

                    notifyTitleChanged();


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

//                if(swipeContainer!=null)
//                {
//                    swipeContainer.setRefreshing(false);
//                }
            }
        });

    }




    private void showToastMessage(String message)
    {
        if(getActivity()!=null)
        {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void notifyItemCategorySelected() {

        if(getActivity() instanceof ToggleFab)
        {
            ((ToggleFab)getActivity()).showFab();
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }


    @Override
    public void onRefresh() {

        // reset the offset and make a network call
        offset = 0;
        dataset.clear();
        makeRequestRetrofit(false,false);
    }



    @Override
    public void notifyRequestSubCategory(ItemCategory itemCategory) {

        ItemCategory temp = currentCategory;
        currentCategory = itemCategory;
        currentCategory.setParentCategory(temp);

        notifyCategoryChanged(false);

        if (getActivity() instanceof NotifyGeneral) {
            ((NotifyGeneral) getActivity()).insertTab(currentCategory.getCategoryName());
        }

        dataset.clear();
        offset = 0; // reset the offset
        makeRequestRetrofit(true, false);
    }


    void notifyCategoryChanged(boolean backPressed)
    {
        // notify the change in category
        if (currentCategory != null) {

            ((NotifyCategoryChanged) getActivity())
                    .itemCategoryChanged(currentCategory, false);
        }
    }




    @Override
    public boolean backPressed() {

        int currentCategoryID = 1; // the ID of root category is always supposed to be 1

        // clear the selected items when back button is pressed
        listAdapter.selectedItems.clear();

        if(currentCategory!=null) {


            if(getActivity() instanceof NotifyGeneral)
            {
                ((NotifyGeneral)getActivity()).removeLastTab();
            }


            if (currentCategory.getParentCategory() != null) {

                currentCategory = currentCategory.getParentCategory();
                currentCategoryID = currentCategory.getItemCategoryID();

            } else {
                currentCategoryID = currentCategory.getParentCategoryID();
            }


            // notify change in category
            notifyCategoryChanged(true);

            if (currentCategoryID != -1) {

                dataset.clear();
                offset =0; // reset the offset
                makeRequestRetrofit(true,true);
            }

        }

        if(currentCategoryID == -1)
        {
//            super.onBackPressed();

            return  true;
        }else
        {
            return  false;
        }

    }


//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        Icepick.saveInstanceState(this, outState);
//    }


//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//
//        Icepick.restoreInstanceState(this, savedInstanceState);
//        notifyTitleChanged();
//    }




    void notifyTitleChanged()
    {
        if(getActivity() instanceof NotifyTitleChanged)
        {
            //currentCategory.getCategoryName(

            ((NotifyTitleChanged) getActivity())
                    .NotifyTitleChanged("Subcategories ("
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
    public void onResume() {
        super.onResume();

        isDestroyed = false;
    }
}