package org.nearbyshops.enduserappnew.ShopImages;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.ImageSliderShop.ImageSliderShop;
import org.nearbyshops.enduserappnew.Interfaces.OnTaxiFilterChanged;
import org.nearbyshops.enduserappnew.Model.ShopImage;
import org.nearbyshops.enduserappnew.ModelEndPoints.ShopImageEndPoint;
import org.nearbyshops.enduserappnew.ModelUtility.HeaderTitle;
import org.nearbyshops.enduserappnew.Preferences.UtilityFunctions;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.ShopImageService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.UserService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sumeet on 14/6/17.
 */

public class ShopImageListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        Adapter.NotificationsFromAdapter ,OnTaxiFilterChanged {

    boolean isDestroyed = false;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ConstraintLayout progressBar;




    @Inject
    UserService userService;

    @Inject
    ShopImageService service;

    GridLayoutManager layoutManager;
    Adapter listAdapter;

    List<Object> dataset = new ArrayList<>();
    List<ShopImage> shopListData;


    // flags
    boolean clearDataset = false;

//    boolean getRowCountVehicle = false;
//    boolean resetOffsetVehicle = false;


    private int limit = 30;
    int offset = 0;
    public int item_count = 0;




    @BindView(R.id.empty_screen) LinearLayout emptyScreen;



//    @BindView(R.id.drivers_count) TextView driversCount;
//    int i = 1;

    public ShopImageListFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }








    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_shop_images, container, false);
        ButterKnife.bind(this,rootView);


//        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(ContextCompat.getColor(getActivity(),R.color.white));
//        toolbar.setTitle("Trip History");
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);



        setupSwipeContainer();
        setupRecyclerView();

        if(savedInstanceState == null)
        {
            makeRefreshNetworkCall();
        }


//        driversCount.setText("Drivers COunt : " + String.valueOf(++i));

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

        listAdapter = new Adapter(dataset,getActivity(),this,this);
        recyclerView.setAdapter(listAdapter);

        layoutManager = new GridLayoutManager(getActivity(),1, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));


        final DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


                if(layoutManager.findLastVisibleItemPosition()==dataset.size())
                {

                    if(offset + limit > layoutManager.findLastVisibleItemPosition())
                    {
                        return;
                    }


                    // trigger fetch next page

                    if((offset + limit)<= item_count)
                    {
                        offset = offset + limit;

                        getShopImages();
                    }


                }
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        isDestroyed = false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
    }



    void makeRefreshNetworkCall()
    {
        progressBar.setVisibility(View.VISIBLE);

        swipeContainer.post(new Runnable() {
            @Override
            public void run() {

                swipeContainer.setRefreshing(true);
                onRefresh();
            }
        });
    }




    @Override
    public void onRefresh() {

        clearDataset = true;
//        getRowCountVehicle = true;
//        resetOffsetVehicle = true;

        getShopImages();
    }













    void getShopImages()
    {

//        if(resetOffsetVehicle)
//        {
//            offset = 0;
//            resetOffsetVehicle = false;
//        }


        if(clearDataset)
        {
            offset=0;
        }



        int shopID = getActivity().getIntent().getIntExtra("shop_id",0);



        Call<ShopImageEndPoint> call = service.getShopImages(
                shopID,
                ShopImage.IMAGE_ORDER,
                limit, offset,
                clearDataset,false
        );



        call.enqueue(new Callback<ShopImageEndPoint>() {
            @Override
            public void onResponse(Call<ShopImageEndPoint> call, Response<ShopImageEndPoint> response) {

                if(isDestroyed)
                {
                    return;
                }

                progressBar.setVisibility(View.INVISIBLE);

                if(response.code() == 200 && response.body()!=null) {

                    if (clearDataset) {
                        dataset.clear();
                        clearDataset = false;

                        item_count = response.body().getItemCount();

                        listAdapter.setItemCount(item_count);

                        if(item_count>0)
                        {
                            dataset.add(new HeaderTitle("Shop Images"));
                        }
                    }


                    if(item_count==0)
                    {
                        emptyScreen.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        emptyScreen.setVisibility(View.GONE);
                    }




                    if(response.body().getResults()!=null)
                    {
                        dataset.addAll(response.body().getResults());

                        shopListData = response.body().getResults();
                    }

                    listAdapter.notifyDataSetChanged();
                }

                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ShopImageEndPoint> call, Throwable t) {


                if(isDestroyed)
                {
                    return;
                }

                progressBar.setVisibility(View.INVISIBLE);

                showToastMessage("Network Connection Failed !");

                swipeContainer.setRefreshing(false);
            }
        });


    }




    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void taxiFiltersChanged() {
        makeRefreshNetworkCall();
    }













    @Override
    public void notifyListItemSelected() {


    }

    @Override
    public void listItemClick(ShopImage taxiImage, int position) {


        Intent intent = new Intent(getActivity(), ImageSliderShop.class);
        List<Object> list = new ArrayList<>();
        list.addAll(dataset);
        list.remove(0);

        String json = UtilityFunctions.provideGson().toJson(list);
        intent.putExtra("images_list",json);
        intent.putExtra("position",position-1);


        startActivity(intent);
    }





    @Override
    public boolean listItemLongClick(View view, ShopImage tripRequest, int position) {
        return true;
    }


}
