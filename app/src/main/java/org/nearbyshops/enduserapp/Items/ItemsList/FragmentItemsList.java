package org.nearbyshops.enduserapp.Items.ItemsList;

import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
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

import org.nearbyshops.enduserapp.DaggerComponentBuilder;
import org.nearbyshops.enduserapp.Model.Item;
import org.nearbyshops.enduserapp.ModelEndPoints.ItemEndPoint;
import org.nearbyshops.enduserapp.R;
import org.nearbyshops.enduserapp.RetrofitRESTContract.ItemService;
import org.nearbyshops.enduserapp.Interfaces.NotifySearch;
import org.nearbyshops.enduserapp.Shops.UtilityLocation;
import org.nearbyshops.enduserapp.ShopsByCategoryOld.Interfaces.NotifySort;
import org.nearbyshops.enduserapp.ShopsByCategoryOld.Interfaces.NotifyTitleChanged;
import org.nearbyshops.enduserapp.Items.SlidingLayerSort.UtilitySortItemsByCategory;

import java.util.ArrayList;

import javax.inject.Inject;

import icepick.State;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 25/5/16.
 */
public class FragmentItemsList extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, NotifySort,NotifySearch{


    @State boolean isSaved = false;

    @Inject ItemService itemService;

    RecyclerView recyclerView;
    AdapterItem adapter;

    @State ArrayList<Item> dataset = new ArrayList<>();

    GridLayoutManager layoutManager;
    SwipeRefreshLayout swipeContainer;

    boolean isDestroyed;


    private static final String ARG_SECTION_NUMBER = "section_number";



    @State boolean isbackPressed = false;


    private int limit = 10;
    @State int offset = 0;

    @State int item_count = 0;


    // Interface References

//    NotifyTitleChanged notifyTitleChanged;

    // Interface References Ends


    public FragmentItemsList() {
        // inject dependencies through dagger
        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

        Log.d("applog","Item Fragment Constructor");
    }

    /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static FragmentItemsList newInstance() {

            FragmentItemsList fragment = new FragmentItemsList();
            Bundle args = new Bundle();
//            args.putParcelable("itemCat",itemCategory);
            fragment.setArguments(args);
            return fragment;
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            setRetainInstance(true);
            View rootView = inflater.inflate(R.layout.fragment_items_list, container, false);

//            itemCategory = getArguments().getParcelable("itemCat");

            recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
            swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);


            if(savedInstanceState==null)
            {

                makeRefreshNetworkCall();
                isSaved = true;
            }
//            else
//            {
//                onViewStateRestored(savedInstanceState);
//            }


            setupRecyclerView();
            setupSwipeContainer();
            notifyTitleChanged();

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
//                    if(dataset.size()== previous_position)
//                    {
//                        return;
//                    }


                    if(offset + limit > layoutManager.findLastVisibleItemPosition())
                    {
                        return;
                    }

                    // trigger fetch next page

                    if((offset+limit)<=item_count)
                    {
                        offset = offset + limit;
                        makeNetworkCall(false);
                    }

                }
            }
        });
    }


//    int previous_position = -1;




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




    void makeNetworkCall(final boolean clearDataset)
    {

        String current_sort = "";

        current_sort = UtilitySortItemsByCategory.getSort(getContext()) + " " + UtilitySortItemsByCategory.getAscending(getContext());

        Call<ItemEndPoint> endPointCall = itemService.getItemsEndpoint(null,
                null,
                UtilityLocation.getLatitude(getActivity()),
                UtilityLocation.getLongitude(getActivity()),
                null,
                null,
                null,
                searchQuery,current_sort,limit,offset,null);


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

                    if(response.body().getItemCount()!=null)
                    {
                        item_count = response.body().getItemCount();
                    }


                    notifyTitleChanged();
                }

                adapter.notifyDataSetChanged();
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



//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        Icepick.saveInstanceState(this, outState);
//    }



//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//        Icepick.restoreInstanceState(this, savedInstanceState);
//        notifyTitleChanged();
//
//    }



    void notifyTitleChanged()
    {
        if(getActivity() instanceof NotifyTitleChanged)
        {
            ((NotifyTitleChanged)getActivity())
                    .NotifyTitleChanged( "Displaying "+
                            String.valueOf(dataset.size())
                            + " out of " + String.valueOf(item_count) + " Items",1);
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

    @Override
    public void notifySortChanged() {
        makeRefreshNetworkCall();
    }


    public int getItemCount() {
        return item_count;
    }

}
