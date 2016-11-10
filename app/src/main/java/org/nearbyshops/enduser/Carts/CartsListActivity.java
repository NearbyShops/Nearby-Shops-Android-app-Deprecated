package org.nearbyshops.enduser.Carts;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import org.nearbyshops.enduser.DaggerComponentBuilder;
import org.nearbyshops.enduser.Login.LoginDialog;
import org.nearbyshops.enduser.ModelRoles.EndUser;
import org.nearbyshops.enduser.ModelStats.CartStats;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.RetrofitRESTContract.CartStatsService;
import org.nearbyshops.enduser.Utility.UtilityGeneral;
import org.nearbyshops.enduser.Utility.UtilityLogin;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartsListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, Callback<List<CartStats>> {


    @Inject
    CartStatsService cartStatsService;

    RecyclerView recyclerView;
    CartsListAdapter adapter;
    GridLayoutManager layoutManager;
    SwipeRefreshLayout swipeContainer;

    List<CartStats> dataset = new ArrayList<>();


    public CartsListActivity() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carts_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // findViewByID's
        swipeContainer = (SwipeRefreshLayout)findViewById(R.id.swipeContainer);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        setupSwipeContainer();
        setupRecyclerView();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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


        adapter = new CartsListAdapter(dataset,this);

        recyclerView.setAdapter(adapter);

        layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);

        //recyclerView.addItemDecoration(
        //        new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST)
        //);

        //recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL_LIST));

        //itemCategoriesList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        layoutManager.setSpanCount(metrics.widthPixels/350);

    }


    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {

        makeNetworkCall();
    }



    void makeNetworkCall()
    {


        EndUser endUser = UtilityLogin.getEndUser(this);
        if(endUser==null)
        {
            showLoginDialog();

            swipeContainer.setRefreshing(false);
            return;
        }



        Call<List<CartStats>> call = cartStatsService.getCart(
                endUser.getEndUserID(),null,null,true,
                (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LAT_CENTER_KEY),
                (double)UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LON_CENTER_KEY)
        );

        /*

        UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LAT_CENTER_KEY),
                UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LON_CENTER_KEY)


        */


        call.enqueue(this);

/*

        Log.d("applog",String.valueOf(endUser.getEndUserID()) + " "
        + UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LAT_CENTER_KEY) + " "
        + UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.LON_CENTER_KEY));
*/


        /*
        if(UtilityGeneral.isNetworkAvailable(this))
        {


        }
        else
        {

            showToastMessage("No network. Application is Offline !");
            swipeContainer.setRefreshing(false);

        }

        */

    }


    @Override
    public void onResponse(Call<List<CartStats>> call, Response<List<CartStats>> response) {



        if(response.body()!=null)
        {
            dataset.clear();
            dataset.addAll(response.body());
            adapter.notifyDataSetChanged();
        }else
        {
            dataset.clear();
            adapter.notifyDataSetChanged();
        }

        swipeContainer.setRefreshing(false);

    }

    @Override
    public void onFailure(Call<List<CartStats>> call, Throwable t) {

        showToastMessage("Network Request failed !");
        swipeContainer.setRefreshing(false);

    }


    @Override
    protected void onResume() {
        super.onResume();


        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);

                try {

                    makeNetworkCall();

                } catch (IllegalArgumentException ex)
                {
                    ex.printStackTrace();

                }

                adapter.notifyDataSetChanged();
            }
        });

    }



    private void showLoginDialog()
    {
        FragmentManager fm = getSupportFragmentManager();
        LoginDialog loginDialog = new LoginDialog();
        loginDialog.show(fm,"serviceUrl");
    }


}
