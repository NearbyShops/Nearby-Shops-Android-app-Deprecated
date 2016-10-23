package org.nearbyshops.enduser.ShopReview;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.widget.Toast;


import com.wunderlist.slidinglayer.SlidingLayer;

import org.apache.commons.collections.ArrayStack;
import org.nearbyshops.enduser.DaggerComponentBuilder;
import org.nearbyshops.enduser.Login.LoginDialog;
import org.nearbyshops.enduser.Login.NotifyAboutLogin;
import org.nearbyshops.enduser.Model.Shop;
import org.nearbyshops.enduser.ModelEndPoints.ShopReviewEndPoint;
import org.nearbyshops.enduser.ModelEndPoints.ShopReviewThanksEndpoint;
import org.nearbyshops.enduser.ModelReview.ShopReview;
import org.nearbyshops.enduser.ModelReview.ShopReviewThanks;
import org.nearbyshops.enduser.ModelRoles.EndUser;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.RetrofitRESTContract.ShopReviewService;
import org.nearbyshops.enduser.RetrofitRESTContract.ShopReviewThanksService;
import org.nearbyshops.enduser.ShopReview.Interfaces.NotifyLoginByAdapter;
import org.nearbyshops.enduser.ShopsByCategory.Interfaces.NotifySort;
import org.nearbyshops.enduser.ShopsByCategory.SlidingLayerSortShops;
import org.nearbyshops.enduser.Utility.DividerItemDecoration;
import org.nearbyshops.enduser.Utility.UtilityLogin;
import org.nearbyshops.enduser.UtilitySort.UtilitySortShopItems;
import org.nearbyshops.enduser.UtilitySort.UtilitySortShopReview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopReviews extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener , NotifySort,
        NotifyLoginByAdapter, NotifyAboutLogin{


    @State
    ArrayList<ShopReview> dataset = new ArrayList<>();

    @State
    ArrayList<ShopReviewThanks> datasetThanks = new ArrayList<>();

    Map<Integer,ShopReviewThanks> thanksMap = new HashMap<>();


    @Bind(R.id.recyclerView)
    RecyclerView reviewsList;

    ShopReviewAdapter adapter;

    GridLayoutManager layoutManager;

    @Inject
    ShopReviewService shopReviewService;

    @Inject
    ShopReviewThanksService thanksService;


    @State
    Shop shop;

    boolean activityStopped = false;



    public static final String SHOP_INTENT_KEY = "shop_intent_key";


    // scroll variables
    private int limit = 30;
    @State int offset = 0;
    @State int item_count = 0;

    @Bind(R.id.slidingLayer)
    SlidingLayer slidingLayer;



//    HorizontalBarChart chart;





//    Unbinder unbinder;

    public ShopReviews() {

        // Inject the dependencies using Dependency Injection
        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_reviews);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//        chart = (HorizontalBarChart) findViewById(R.id.chart);


        shop = getIntent().getParcelableExtra(SHOP_INTENT_KEY);

        if(shop !=null)
        {
            getSupportActionBar().setTitle(shop.getShopName());
        }




        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if(savedInstanceState==null)
        {

            swipeContainer.post(new Runnable() {
                @Override
                public void run() {
                    swipeContainer.setRefreshing(true);

                    dataset.clear();
                    makeNetworkCall();
                }
            });


            makeNetworkCallThanks();
        }
        else
        {
            onRestoreInstanceState(savedInstanceState);
        }




        setupRecyclerView();
        setupSwipeContainer();
        setupSlidingLayer();


        // setup chart

        setupChart();

    }




    void setupChart()
    {



//        chart.setDescription("Shop Review Stats");


/*
        List<BarEntry> entries = new ArrayList<BarEntry>();

        entries.add(new BarEntry(1,909));
        entries.add(new BarEntry(2,50));
        entries.add(new BarEntry(3,342));
        entries.add(new BarEntry(4,130));
        entries.add(new BarEntry(5,250));


        BarDataSet dataSet = new BarDataSet(entries,"Ratings");

//        dataSet.setColors(new int[]{R.color.gplus_color_2,R.color.buttonColorDark,R.color.colorAccent,R.color.orangeDark,R.color.darkGreen},this);

        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData barData = new BarData(dataSet);
//        chart.setData(barData);


        XAxis axis = new XAxis();
        axis.setDrawGridLines(false);

*/

/*
        chart.setDrawGridBackground(false);
        chart.setDrawBorders(false);
        chart.setDrawValueAboveBar(true);


        chart.getXAxis().setDrawGridLines(false);
        XAxis x_axis = chart.getXAxis();
        x_axis.setDrawGridLines(false);
*/



/*
        chart.getXAxis().setDrawGridLines(false);
//        chart.getXAxis().mAxisRange = 1;

        chart.getXAxis().setGranularity(1f);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);


        chart.invalidate();
*/

    }





    void setupRecyclerView()
    {

        adapter = new ShopReviewAdapter(dataset,thanksMap,this);
        reviewsList.setAdapter(adapter);
        layoutManager = new GridLayoutManager(this,1);
        reviewsList.setLayoutManager(layoutManager);

        reviewsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(layoutManager.findLastVisibleItemPosition() == dataset.size()-1)
                {
                    // trigger fetch next page

                    if((offset+limit)<=item_count)
                    {
                        offset = offset + limit;
                        makeNetworkCall();
                    }

                }

            }
        });


        reviewsList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
//        reviewsList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL_LIST));

/*
        final DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        layoutManager.setSpanCount(metrics.widthPixels/350);
*/


    }



    void setupSlidingLayer()
    {

        ////slidingLayer.setShadowDrawable(R.drawable.sidebar_shadow);
        //slidingLayer.setShadowSizeRes(R.dimen.shadow_size);

        if(slidingLayer!=null)
        {
            slidingLayer.setChangeStateOnTap(true);
            slidingLayer.setSlidingEnabled(true);
            slidingLayer.setPreviewOffsetDistance(15);
            slidingLayer.setOffsetDistance(10);
            slidingLayer.setStickTo(SlidingLayer.STICK_TO_RIGHT);

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(250, ViewGroup.LayoutParams.MATCH_PARENT);

            //slidingContents.setLayoutParams(layoutParams);

            //slidingContents.setMinimumWidth(metrics.widthPixels-50);


            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.slidinglayerfragment,new SlidingLayerSortReview())
                    .commit();

        }

    }



    @OnClick({R.id.icon_sort,R.id.text_sort})
    void sortClick()
    {
        slidingLayer.openLayer(true);
    }






    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

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






    private void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();


        ButterKnife.unbind(this);
        /*if(unbinder!=null)
        {
            unbinder.unbind();
        }*/

    }

    @Override
    public void onRefresh() {

        dataset.clear();
        offset = 0 ; // reset the offset
        makeNetworkCall();

        makeNetworkCallThanks();

//        Log.d("applog","refreshed");
    }



    void onRefreshSwipeIndicator()
    {


        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);

                onRefresh();

            }
        });
    }

    private void makeNetworkCall() {



        String current_sort = "";

        current_sort = UtilitySortShopReview.getSort(this)
                + " " + UtilitySortShopReview.getAscending(this);




        Call<ShopReviewEndPoint> call = shopReviewService.getReviews(shop.getShopID(),null,
                true,current_sort,limit,offset,false);


        call.enqueue(new Callback<ShopReviewEndPoint>() {

            @Override
            public void onResponse(Call<ShopReviewEndPoint> call, Response<ShopReviewEndPoint> response) {

                if(activityStopped)
                {
                    return;
                }

                if(response.body()!=null && response.body().getResults()!=null)
                {
                    item_count = response.body().getItemCount();
                    dataset.addAll(response.body().getResults());
                    adapter.notifyDataSetChanged();

                }

                stopRefreshing();

            }

            @Override
            public void onFailure(Call<ShopReviewEndPoint> call, Throwable t) {

                if(activityStopped)
                {
                    return;
                }

                showToastMessage("Network Not available !");

                stopRefreshing();

            }
        });
    }



    private void makeNetworkCallThanks()
    {

        EndUser endUser = UtilityLogin.getEndUser(this);


        if(endUser==null)
        {
            // end user not logged in
            return;
        }

        Call<ShopReviewThanksEndpoint> endpointCall = thanksService.getShopReviewThanks(null,endUser.getEndUserID(),
                shop.getShopID(),null,100,0,null);




        endpointCall.enqueue(new Callback<ShopReviewThanksEndpoint>() {
            @Override
            public void onResponse(Call<ShopReviewThanksEndpoint> call, Response<ShopReviewThanksEndpoint> response) {


                if(activityStopped)
                {
                    return;
                }

                if(response.body()!=null)
                {
                    datasetThanks.clear();
                    datasetThanks.addAll(response.body().getResults());


                    for(ShopReviewThanks thanks: datasetThanks)
                    {
                        thanksMap.put(thanks.getShopReviewID(),thanks);
                    }

                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<ShopReviewThanksEndpoint> call, Throwable t) {


                if(activityStopped)
                {
                    return;
                }

            }
        });


    }





    @Override
    protected void onStop() {
        super.onStop();

        activityStopped = true;
    }

    void stopRefreshing()
    {
        if(swipeContainer!=null)
        {
            swipeContainer.setRefreshing(false);
        }
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Icepick.saveInstanceState(this,outState);
//        outState.putParcelableArrayList("dataset",dataset);
    }



    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Icepick.restoreInstanceState(this,savedInstanceState);

        thanksMap.clear();

        for(ShopReviewThanks thanks: datasetThanks)
        {
            thanksMap.put(thanks.getShopReviewID(),thanks);
        }

//        adapter.notifyDataSetChanged();



        /*if(savedInstanceState!=null)
        {
            ArrayList<ShopReview> tempCat = savedInstanceState.getParcelableArrayList("dataset");

            dataset.clear();
            dataset.addAll(tempCat);
            adapter.notifyDataSetChanged();
        }*/


    }




    private void showLoginDialog()
    {
        FragmentManager fm = getSupportFragmentManager();
        LoginDialog loginDialog = new LoginDialog();
        loginDialog.show(fm,"serviceUrl");
    }



    @Override
    public void notifySortChanged() {
        onRefreshSwipeIndicator();
    }



    @Override
    public void NotifyLogin() {
        onRefreshSwipeIndicator();
    }


    @Override
    public void NotifyLoginAdapter() {
        showLoginDialog();
    }
}
