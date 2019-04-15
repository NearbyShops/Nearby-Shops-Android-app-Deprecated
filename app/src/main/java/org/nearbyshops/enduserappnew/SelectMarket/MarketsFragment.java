package org.nearbyshops.enduserappnew.SelectMarket;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.gson.Gson;

import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Home;
import org.nearbyshops.enduserappnew.Interfaces.LocationUpdated;
import org.nearbyshops.enduserappnew.Interfaces.NotifySearch;
import org.nearbyshops.enduserappnew.ItemsByCategoryTypeSimple.Utility.HeaderItemsList;
import org.nearbyshops.enduserappnew.ModelRoles.User;
import org.nearbyshops.enduserappnew.ModelServiceConfig.Endpoints.ServiceConfigurationEndPoint;
import org.nearbyshops.enduserappnew.ModelServiceConfig.ServiceConfigurationGlobal;
import org.nearbyshops.enduserappnew.ModelServiceConfig.ServiceConfigurationLocal;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefLocation;
import org.nearbyshops.enduserappnew.Preferences.PrefLoginGlobal;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.RetrofitRESTContractSDS.ServiceConfigService;
import org.nearbyshops.enduserappnew.ShopsByCategory.Interfaces.NotifySort;
import org.nearbyshops.enduserappnew.ShopsByCategory.Interfaces.NotifyTitleChanged;
import org.nearbyshops.enduserappnew.Utility.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MarketsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, NotifySort, NotifySearch,
        ViewHolderMarket.VHMarketNotifications, LocationUpdated {




//    @Inject
//    OrderServicePFS orderService;

    @Inject Gson gson;

//    @Inject ServiceConfigService serviceConfigService;

    RecyclerView recyclerView;
    AdapterMarkets adapter;

    public List<Object> dataset = new ArrayList<>();

    GridLayoutManager layoutManager;
    SwipeRefreshLayout swipeContainer;



    boolean show = true;

    final private int limit = 5;
    int offset = 0;
    int item_count = 0;
    boolean isDestroyed;






    public MarketsFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);

    }


    public static MarketsFragment newInstance() {
        MarketsFragment fragment = new MarketsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_services, container, false);


        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        swipeContainer = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeContainer);


        if(savedInstanceState==null)
        {
            makeRefreshNetworkCall();
        }


        setupRecyclerView();
        setupSwipeContainer();




        ButterKnife.bind(this,rootView);



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

        adapter = new AdapterMarkets(dataset,this);


        recyclerView.setAdapter(adapter);


        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST)
        );



        layoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

//        layoutManager.setSpanCount(metrics.widthPixels/400);





        int spanCount = (int) (metrics.widthPixels/(230 * metrics.density));

        if(spanCount==0){
            spanCount = 1;
        }

        layoutManager.setSpanCount(spanCount);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                if(dy > 20)
//                {
//
//                    boolean previous = show;
//
//                    show = false ;
//
//                    if(show!=previous)
//                    {
//                        // changed
//                        Log.d("scrolllog","show");
//
//                        if(getActivity() instanceof ToggleFab)
//                        {
//                            ((ToggleFab)getActivity()).hideFab();
//                        }
//                    }
//
//                }else if(dy < -20)
//                {
//
//                    boolean previous = show;
//
//                    show = true;
//
//                    if(show!=previous)
//                    {
//                        Log.d("scrolllog","hide");
//
//                        if(getActivity() instanceof ToggleFab)
//                        {
//                            ((ToggleFab)getActivity()).showFab();
//                        }
//                    }
//                }
//
//            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


                if(offset + limit > layoutManager.findLastVisibleItemPosition()+1-1)
                {
                    return;
                }


                if(layoutManager.findLastVisibleItemPosition()==dataset.size()-1+1)
                {
                    // trigger fetch next page

//                    if(layoutManager.findLastVisibleItemPosition() == previous_position)
//                    {
//                        return;
//                    }


                    if((offset+limit)<=item_count)
                    {
                        offset = offset + limit;
                        makeNetworkCall(false);

                        adapter.setLoadMore(true);
                    }
                    else
                    {
                        adapter.setLoadMore(false);
                    }

//                    previous_position = layoutManager.findLastVisibleItemPosition();

                }

            }
        });
    }



//    int previous_position = -1;



    @Override
    public void onRefresh() {

        offset = 0;
        makeNetworkCall(true);
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







    void makeNetworkCall(final boolean clearDataset)
    {

//            Shop currentShop = UtilityShopHome.getShop(getContext());
//
//        String current_sort = "";
//        current_sort = UtilitySortServices.getSort(getContext()) + " " + UtilitySortServices.getAscending(getContext());
//
////        showToastMessage(UtilityLogin.getAuthorizationHeaders(getActivity()));
//
//        Boolean filterOfficial = null;
//        Boolean filterVerified = null;
//
//        if(UtilitySortServices.getOfficial(getActivity()))
//        {
//            filterOfficial = true;
//        }
//
//        if(UtilitySortServices.getVerified(getActivity()))
//        {
//            filterVerified = true;
//        }
//
//        Integer serviceType = null;
//
//        if(UtilitySortServices.getServiceType(getActivity())!=-1)
//        {
//            serviceType = UtilitySortServices.getServiceType(getActivity());
//        }





        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(PrefServiceConfig.getServiceURL_SDS(MyApplication.getAppContext()))
                .client(new OkHttpClient().newBuilder().build())
                .build();





        Call<ServiceConfigurationEndPoint> call = retrofit.create(ServiceConfigService.class).getShopListSimple(
                PrefLocation.getLatitude(getActivity()),
                PrefLocation.getLongitude(getActivity()),
                    null,null,
                    searchQuery,
                    null,null,
                null,
                    null,limit,offset);


//        PrefLocation.getLatitude(getActivity()),
//                PrefLocation.getLongitude(getActivity()),

//        filterOfficial,filterVerified,
//                serviceType,

            call.enqueue(new Callback<ServiceConfigurationEndPoint>() {
                @Override
                public void onResponse(Call<ServiceConfigurationEndPoint> call, Response<ServiceConfigurationEndPoint> response) {

                    if(isDestroyed)
                    {
                        return;
                    }

                    if(response.body()!= null)
                    {
                        item_count = response.body().getItemCount();
//                        adapter.setTotalItemsCount(item_count);


                        if(clearDataset)
                        {
                            dataset.clear();


//                            dataset.add(PrefServiceConfig.getServiceConfigLocal(getActivity()));

                            ServiceConfigurationLocal configurationLocal = PrefServiceConfig.getServiceConfigLocal(getActivity());

                            if(configurationLocal!=null)
                            {
                                dataset.add(configurationLocal);
                            }


                            User user = PrefLoginGlobal.getUser(getActivity());

                            if(user!=null)
                            {
                                dataset.add(user);
                            }



                            if(item_count>0)
                            {
                                dataset.add(new HeaderItemsList());
                            }

                        }

                        if(response.body().getResults()!=null)
                        {
                            dataset.addAll(response.body().getResults());
                        }


                        adapter.notifyDataSetChanged();
                        notifyTitleChanged();

                    }





//                    showToastMessage("Item Count : " + String.valueOf(item_count));
                    swipeContainer.setRefreshing(false);

                }

                @Override
                public void onFailure(Call<ServiceConfigurationEndPoint> call, Throwable t) {
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
    public void onResume() {
        super.onResume();
        notifyTitleChanged();
        isDestroyed=false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed=true;
    }



    void showToastMessage(String message)
    {
        if(getActivity()!=null)
        {
            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        }

    }




    void notifyTitleChanged()
    {

        if(getActivity() instanceof NotifyTitleChanged)
        {
            ((NotifyTitleChanged)getActivity())
                    .NotifyTitleChanged(
                            "Complete (" + String.valueOf(dataset.size())
                                    + "/" + String.valueOf(item_count) + ")",1);


        }
    }







    // Refresh the Confirmed PlaceholderFragment

    private static String makeFragmentName(int viewId, int index) {
        return "android:switcher:" + viewId + ":" + index;
    }




    @Override
    public void notifySortChanged() {
        makeRefreshNetworkCall();
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





    @Override
    public void listItemClick(ServiceConfigurationGlobal configurationGlobal, int position) {
        showToastMessage("List item click !");
    }

    @Override
    public void selectMarketSuccessful(ServiceConfigurationGlobal configurationGlobal, int position) {

        if(getActivity() instanceof MarketSelected)
        {
            ((MarketSelected) getActivity()).marketSelected();
        }
    }

    @Override
    public void showMessage(String message) {
        showToastMessage(message);
    }





    @Override
    public void permissionGranted() {

    }



    @Override
    public void locationUpdated() {
        makeRefreshNetworkCall();
    }


    public interface MarketSelected
    {
        void marketSelected();
    }






    @OnClick(R.id.fab)
    void fabClick()
    {
        showDialogSubmitURL();
    }





    private void showDialogSubmitURL()
    {
        FragmentManager fm = getChildFragmentManager();
        SubmitURLDialog submitURLDialog = new SubmitURLDialog();
        submitURLDialog.show(fm,"serviceUrl");
    }


}
