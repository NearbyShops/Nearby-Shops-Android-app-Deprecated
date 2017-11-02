package org.nearbyshops.enduserapp.Shops.ListFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
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
import org.nearbyshops.enduserapp.Model.Shop;
import org.nearbyshops.enduserapp.ModelEndPoints.ShopEndPoint;
import org.nearbyshops.enduserapp.Notifications.NonStopService.LocationUpdateService;
import org.nearbyshops.enduserapp.R;
import org.nearbyshops.enduserapp.RetrofitRESTContract.ShopService;
import org.nearbyshops.enduserapp.Shops.Interfaces.GetDataset;
import org.nearbyshops.enduserapp.Shops.Interfaces.NotifyDatasetChanged;
import org.nearbyshops.enduserapp.Interfaces.NotifySearch;
import org.nearbyshops.enduserapp.Shops.ShopsActivity;
import org.nearbyshops.enduserapp.Shops.UtilityLocation;
import org.nearbyshops.enduserapp.ShopsByCategoryOld.Interfaces.NotifySort;
import org.nearbyshops.enduserapp.ShopsByCategoryOld.Interfaces.NotifyTitleChanged;
import org.nearbyshops.enduserapp.Shops.SlidingLayerSort.UtilitySortShopsByCategory;

import java.util.ArrayList;

import javax.inject.Inject;

import icepick.State;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by sumeet on 25/5/16.
 */
public class FragmentShopTwo extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener, NotifySort, NotifyDatasetChanged,NotifySearch{

        ArrayList<Shop> dataset;

        @State boolean isSaved;
        @Inject ShopService shopService;

        RecyclerView recyclerView;
        AdapterShopTwo adapter;
        GridLayoutManager layoutManager;

        SwipeRefreshLayout swipeContainer;

        final private int limit = 10;
        @State int offset = 0;
        @State int item_count = 0;

        boolean switchMade = false;
        boolean isDestroyed;

        public FragmentShopTwo() {
            // inject dependencies through dagger
            DaggerComponentBuilder.getInstance()
                    .getNetComponent().Inject(this);

            Log.d("applog","Shop Fragment Constructor");

        }

    /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static FragmentShopTwo newInstance(boolean switchMade) {

            FragmentShopTwo fragment = new FragmentShopTwo();
            Bundle args = new Bundle();
//            args.putParcelable("itemCat",itemCategory);
            args.putBoolean("switch",switchMade);
            fragment.setArguments(args);

            return fragment;
        }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);


    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }



    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_shops_two, container, false);

            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);

            recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
            swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
            switchMade = getArguments().getBoolean("switch");

                if(savedInstanceState==null && !switchMade)
                {
                    // ensure that there is no swipe to right on first fetch
//                    isbackPressed = true;
                    makeRefreshNetworkCall();
                    isSaved = true;
                }

                else if(savedInstanceState == null && switchMade)
                {

                }
                else
                {
//                    Log.d("shopsbycategory","saved State");
//                    onViewStateRestored(savedInstanceState);

                }


            setupRecyclerView();
            setupSwipeContainer();
//            notifyDataset();
            notifyTitleChanged();





//                getActivity().startService(new Intent(getActivity(), LocationUpdateService.class));

//                IntentFilter filter = new IntentFilter(LocationUpdateService.ACTION);
//                LocalBroadcastManager.getInstance(getActivity()).registerReceiver(testReceiver, filter);



                return rootView;
        }





        @Override
        public void notifyDatasetChanged()
        {
            if(dataset == null)
            {
                if(getActivity() instanceof GetDataset)
                {
                    dataset = ((GetDataset)getActivity()).getDataset();
                }
            }


            setupRecyclerView();

        }








       /* void notifyDataset()
        {
            if(getActivity() instanceof NotifyDataset)
            {
                ((NotifyDataset)getActivity()).setDataset(dataset);
            }
        }*/




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
            if(recyclerView == null)
            {
                return;
            }


            if(dataset == null)
            {
                if(getActivity() instanceof GetDataset)
                {
                    dataset = ((GetDataset)getActivity()).getDataset();
                }
            }


            adapter = new AdapterShopTwo(dataset,getActivity(),this);

            recyclerView.setAdapter(adapter);

            layoutManager = new GridLayoutManager(getActivity(),1);
            recyclerView.setLayoutManager(layoutManager);

//            recyclerView.addItemDecoration(new EqualSpaceItemDecoration(5));

            /*recyclerView.addItemDecoration(
                    new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST)
            );

            recyclerView.addItemDecoration(
                    new DividerItemDecoration(getActivity(),DividerItemDecoration.HORIZONTAL_LIST)
            );*/

            //itemCategoriesList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);


            int spanCount = (int) (metrics.widthPixels/(230 * metrics.density));

            if(spanCount==0){
                spanCount = 1;
            }

            layoutManager.setSpanCount(spanCount);

//            layoutManager.setSpanCount(metrics.widthPixels/350);

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if(layoutManager.findLastVisibleItemPosition()==dataset.size())
                    {
                        // trigger fetch next page

//                        if(dataset.size()== previous_position)
//                        {
//                            return;
//                        }


                        if(offset + limit > layoutManager.findLastVisibleItemPosition())
                        {
                            return;
                        }


                        if((offset+limit)<=item_count)
                        {
                            offset = offset + limit;
                            makeNetworkCall(false,false);
                        }

//                        previous_position = dataset.size();

                    }
                }
            });
        }




//    int previous_position = -1;


    public int getItemCount()
    {
        return item_count;
    }





        private void makeRefreshNetworkCall() {

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



        @Override
        public void onRefresh() {


            if(dataset == null)
            {
                if(getActivity() instanceof GetDataset)
                {
                    dataset = ((GetDataset)getActivity()).getDataset();
                }
            }

//            offset = 0; // reset the offset
//            dataset.clear();
//            adapter.notifyDataSetChanged();
            makeNetworkCall(true,true);
        }







        private void makeNetworkCall(final boolean clearDataset, boolean resetOffset)
        {

            if(resetOffset)
            {
                offset = 0;
            }

            if(dataset == null)
            {
                if(getActivity() instanceof GetDataset)
                {
                    dataset = ((GetDataset)getActivity()).getDataset();
                }
            }


//            (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MAX_KEY)
//            (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MIN_KEY),
//                    (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.PROXIMITY_KEY),


            String current_sort = "";
            current_sort = UtilitySortShopsByCategory.getSort(getContext()) + " " + UtilitySortShopsByCategory.getAscending(getContext());

            Call<ShopEndPoint> callEndpoint = shopService.getShops(
                    null,
                    null,
                    UtilityLocation.getLatitude(getActivity()),
                    UtilityLocation.getLongitude(getActivity()),
                    null, null, null,
                    searchQuery,current_sort,limit,offset,false
            );



            System.out.println("Lat : " + UtilityLocation.getLatitude(getActivity())
                                + "\nLon : " + UtilityLocation.getLongitude(getActivity()));



            callEndpoint.enqueue(new Callback<ShopEndPoint>() {
                @Override
                public void onResponse(Call<ShopEndPoint> call, Response<ShopEndPoint> response) {

                    if(isDestroyed)
                    {
                        return;
                    }

    //                dataset.clear();

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

                    }


                    notifyTitleChanged();
                    notifyMapDataChanged();
                    swipeContainer.setRefreshing(false);

                }

                @Override
                public void onFailure(Call<ShopEndPoint> call, Throwable t) {

                    if(isDestroyed)
                    {
                        return;
                    }

                    showToastMessage(getString(R.string.network_request_failed));
                    swipeContainer.setRefreshing(false);
                }
            });

        }



        void showToastMessage(String message)
        {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        }





    // apply ice pack


//        @Override
//        public void onSaveInstanceState(Bundle outState) {
//            super.onSaveInstanceState(outState);
//            Icepick.saveInstanceState(this, outState);
//        }
//
//

//        @Override
//        public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//            super.onViewStateRestored(savedInstanceState);
//
//            Icepick.restoreInstanceState(this, savedInstanceState);
//            notifyTitleChanged();
//        }


        /*@Override
        public void itemCategoryChanged(ItemCategory currentCategory, Boolean isBackPressed) {


            notifiedCurrentCategory = currentCategory;
            dataset.clear();
            offset = 0 ; // reset the offset
            makeNetworkCall();

            this.isbackPressed = isBackPressed;
        }*/



        void notifyTitleChanged()
        {

            if(dataset == null)
            {
                if(getActivity() instanceof GetDataset)
                {
                    dataset = ((GetDataset)getActivity()).getDataset();
                }
            }



            if(getActivity() instanceof NotifyTitleChanged)
            {
                ((NotifyTitleChanged)getActivity())
                        .NotifyTitleChanged(
                                "Displaying " + String.valueOf(dataset.size())
                                + " out of " + String.valueOf(item_count) + " Shops",1);


                //" Shops (" + String.valueOf(dataset.size())
                //+ "/" + String.valueOf(item_count) + ")"
            }
        }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
    }



    @Override
    public void notifySortChanged() {
        makeRefreshNetworkCall();
    }



    void notifyMapDataChanged()
    {
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("map_tag");

        if(fragment instanceof NotifyDatasetChanged)
        {
            ((NotifyDatasetChanged)fragment).notifyDatasetChanged();
        }
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



    // location code



    @Override
    public void onResume() {
        super.onResume();

        isDestroyed = false;
    }


    @Override
    public void onPause() {
        super.onPause();

        // Unregister the listener when the application is paused

//        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(testReceiver);

    }


    @Override
    public void onStop() {
        super.onStop();

//        getActivity().stopService(new Intent(getActivity(), LocationUpdateService.class));

    }


//
//    private BroadcastReceiver testReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            int resultCode = intent.getIntExtra("resultCode", RESULT_CANCELED);
//
//            if (resultCode == RESULT_OK) {
//
////                String resultValue = intent.getStringExtra("resultValue");
////                Toast.makeText(getActivity(), resultValue, Toast.LENGTH_SHORT).show();
//
//                showToastMessage("Broadcast Received !");
//            }
//        }
//    };



}
