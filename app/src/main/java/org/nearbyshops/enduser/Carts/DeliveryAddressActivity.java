package org.nearbyshops.enduser.Carts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.nearbyshops.enduser.zaDeprecatedItemCategories.DaggerComponentBuilder;
import org.nearbyshops.enduser.Model.EndUser;
import org.nearbyshops.enduser.ModelStats.DeliveryAddress;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.RetrofitRESTContract.DeliveryAddressService;
import org.nearbyshops.enduser.Utility.UtilityGeneral;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryAddressActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, DeliveryAddressAdapter.NotifyDeliveryAddress, Callback<List<DeliveryAddress>>, View.OnClickListener {

    @Inject
    DeliveryAddressService deliveryAddressService;

    RecyclerView recyclerView;

    DeliveryAddressAdapter adapter;

    GridLayoutManager layoutManager;

    SwipeRefreshLayout swipeContainer;

    List<DeliveryAddress> dataset = new ArrayList<>();

    EndUser endUser = null;

    public final static String SHOP_INTENT_KEY = "shop_cart_item";
    public final static String CART_STATS_INTENT_KEY = "cart_stats";


    TextView addNewAddress;


    public DeliveryAddressActivity() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_address);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // findView By id'// STOPSHIP: 11/6/16

        swipeContainer = (SwipeRefreshLayout)findViewById(R.id.swipeContainer);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        addNewAddress = (TextView) findViewById(R.id.addNewAddress);

        addNewAddress.setOnClickListener(this);


        setupSwipeContainer();
        setupRecyclerView();
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


        adapter = new DeliveryAddressAdapter(dataset,this,this);

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

        int spanCount = (metrics.widthPixels/350);

        if(spanCount > 0)
        {
            layoutManager.setSpanCount(spanCount);
        }

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

        Call<List<DeliveryAddress>> call = deliveryAddressService.getAddresses(
                UtilityGeneral.getEndUserID(this));

                call.enqueue(this);
    }



    @Override
    public void onResponse(Call<List<DeliveryAddress>> call, Response<List<DeliveryAddress>> response) {


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
    public void onFailure(Call<List<DeliveryAddress>> call, Throwable t) {

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





    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);

    }



    @Override
    public void notifyEdit(DeliveryAddress deliveryAddress) {


        Intent intent = new Intent(this,EditAddressActivity.class);
        intent.putExtra(EditAddressActivity.DELIVERY_ADDRESS_INTENT_KEY,deliveryAddress);
        startActivity(intent);

    }

    @Override
    public void notifyRemove(DeliveryAddress deliveryAddress) {

        showToastMessage("Remove");

    }

    @Override
    public void notifyListItemClick(DeliveryAddress deliveryAddress) {

        Intent output = new Intent();
        output.putExtra("output",deliveryAddress);
        setResult(2,output);
        finish();
    }




    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.addNewAddress:

                addNewAddressClick(v);

                break;

            default:
                break;
        }

    }




    void addNewAddressClick(View view)
    {

        Intent intent = new Intent(this,AddAddressActivity.class);
        startActivity(intent);

    }





    /*


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


     */

}
