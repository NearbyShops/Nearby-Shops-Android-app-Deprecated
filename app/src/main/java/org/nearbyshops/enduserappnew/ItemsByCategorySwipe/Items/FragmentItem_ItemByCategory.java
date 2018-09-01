package org.nearbyshops.enduserappnew.ItemsByCategorySwipe.Items;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Model.Item;
import org.nearbyshops.enduserappnew.Model.ItemCategory;
import org.nearbyshops.enduserappnew.ModelEndPoints.ItemEndPoint;
import org.nearbyshops.enduserappnew.Preferences.PrefLocation;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.ItemService;
import org.nearbyshops.enduserappnew.ShopsByCategory.Interfaces.NotifyCategoryChanged;
import org.nearbyshops.enduserappnew.ShopsByCategory.Interfaces.NotifyGeneral;
import org.nearbyshops.enduserappnew.ShopsByCategory.Interfaces.NotifySort;
import org.nearbyshops.enduserappnew.ShopsByCategory.Interfaces.NotifyTitleChanged;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Items.SlidingLayerSort.UtilitySortItemsByCategory;

import java.util.ArrayList;

import javax.inject.Inject;

//import icepick.Icepick;
//import icepick.State;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 25/5/16.
 */
public class FragmentItem_ItemByCategory extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, NotifyCategoryChanged , NotifySort{



//        ItemCategory itemCategory;

    ItemCategory notifiedCurrentCategory;

    boolean isSaved = false;

    @Inject
    ItemService itemService;

    RecyclerView recyclerView;
    AdapterItem adapter;

    ArrayList<Item> dataset = new ArrayList<>();

    GridLayoutManager layoutManager;
    SwipeRefreshLayout swipeContainer;

    boolean isDestroyed;


    private static final String ARG_SECTION_NUMBER = "section_number";



    boolean isbackPressed = false;


    private int limit = 10;
    int offset = 0;

    int item_count = 0;


    // Interface References

//    NotifyTitleChanged notifyTitleChanged;

    // Interface References Ends


    public FragmentItem_ItemByCategory() {
        // inject dependencies through dagger
        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

        Log.d("applog","Item Fragment Constructor");
    }

    /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static FragmentItem_ItemByCategory newInstance(ItemCategory itemCategory) {

            FragmentItem_ItemByCategory fragment = new FragmentItem_ItemByCategory();
            Bundle args = new Bundle();
            args.putParcelable("itemCat",itemCategory);
            fragment.setArguments(args);
            return fragment;
        }




        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            setRetainInstance(true);
            View rootView = inflater.inflate(R.layout.fragment_items_item_by_category, container, false);

//            itemCategory = getArguments().getParcelable("itemCat");

            recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
            swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);

            if(savedInstanceState==null)
            {

                makeRefreshNetworkCall();
                isSaved = true;
            }
            else
            {
                onViewStateRestored(savedInstanceState);
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

        adapter = new AdapterItem(dataset,getActivity(),this);

        recyclerView.setAdapter(adapter);

        layoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);

        /*recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST)
        );

        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(),DividerItemDecoration.HORIZONTAL_LIST)
        );*/


        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

//        layoutManager.setSpanCount(metrics.widthPixels/350);


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
                        makeNetworkCall(false);
                    }

                    previous_position = dataset.size();

                }
            }
        });
    }


    int previous_position = -1;



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




    void makeNetworkCall(final boolean clearDataset)
    {




        if(notifiedCurrentCategory==null)
        {
            swipeContainer.setRefreshing(false);

            return;
        }




        String current_sort = "";

        current_sort = UtilitySortItemsByCategory.getSort(getContext()) + " " + UtilitySortItemsByCategory.getAscending(getContext());



        Call<ItemEndPoint> endPointCall = itemService.getItemsEndpoint(notifiedCurrentCategory.getItemCategoryID(),
                null,
                PrefLocation.getLatitude(getActivity()),
                PrefLocation.getLongitude(getActivity()),
                null, null,
                null,null,
                current_sort, limit,offset,null);



        endPointCall.enqueue(new Callback<ItemEndPoint>() {
            @Override
            public void onResponse(Call<ItemEndPoint> call, Response<ItemEndPoint> response) {

                if(isDestroyed)
                {
                    return;
                }



                if(response.body()!=null)
                {
                    if(clearDataset)
                    {
                        dataset.clear();
                    }
                    dataset.addAll(response.body().getResults());
                    adapter.notifyDataSetChanged();

                    if(response.body().getItemCount()!=null)
                    {
                        item_count = response.body().getItemCount();
                    }


                    if(!notifiedCurrentCategory.getisAbstractNode() && item_count>0 && !isbackPressed)
                    {
                        if(getActivity() instanceof NotifyGeneral)
                        {
                            ((NotifyGeneral)getActivity()).notifySwipeToright();
                        }

                        // reset the flag
                        isbackPressed = false;
                    }


                    notifyTitleChanged();
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

    public int getItemCount() {
        return item_count;
    }


    public static class EndPoint implements Callback<ItemEndPoint>{

        @Override
        public void onResponse(Call<ItemEndPoint> call, Response<ItemEndPoint> response) {

        }

        @Override
        public void onFailure(Call<ItemEndPoint> call, Throwable t) {

        }
    }




    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {

//        dataset.clear();
        offset = 0; // reset the offset
        makeNetworkCall(true);
    }



    // apply ice pack


//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
////        Icepick.saveInstanceState(this, outState);
////        outState.putParcelableArrayList("dataset",dataset);
//
//    }



    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);


//        Icepick.restoreInstanceState(this, savedInstanceState);
        notifyTitleChanged();
/*
        if (savedInstanceState != null) {

            ArrayList<Item> tempList = savedInstanceState.getParcelableArrayList("dataset");

            dataset.clear();
            dataset.addAll(tempList);



            adapter.notifyDataSetChanged();
        }*/

    }




    @Override
    public void itemCategoryChanged(ItemCategory currentCategory, Boolean isBackPressed) {


        notifiedCurrentCategory = currentCategory;
//        dataset.clear();
//        offset = 0 ; // reset the offset
//        onRefresh();
        makeRefreshNetworkCall();

        this.isbackPressed = isBackPressed;
    }



    void notifyTitleChanged()
    {
        String name = "";

        if(notifiedCurrentCategory!=null)
        {
            name = notifiedCurrentCategory.getCategoryName();
        }


        if(getActivity() instanceof NotifyTitleChanged)
        {
            ((NotifyTitleChanged)getActivity())
                    .NotifyTitleChanged( name +
                            " Items (" + String.valueOf(dataset.size())
                            + "/" + String.valueOf(item_count) + ")",1);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        isDestroyed = true;
    }



    @Override
    public void notifySortChanged() {
        makeRefreshNetworkCall();
    }


}
