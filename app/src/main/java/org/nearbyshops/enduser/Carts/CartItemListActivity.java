package org.nearbyshops.enduser.Carts;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.widget.Toast;

import org.nearbyshops.enduser.DaggerComponentBuilder;
import org.nearbyshops.enduser.Model.CartItem;
import org.nearbyshops.enduser.Model.Shop;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.RetrofitRESTContract.CartItemService;
import org.nearbyshops.enduser.Utility.UtilityGeneral;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartItemListActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener, Callback<List<CartItem>>,CartItemAdapter.NotifyCartItem {


    @Inject
    CartItemService cartItemService;

    RecyclerView recyclerView;

    CartItemAdapter adapter;

    GridLayoutManager layoutManager;

    SwipeRefreshLayout swipeContainer;

    List<CartItem> dataset = new ArrayList<>();

    Shop shop = null;

    public final static String SHOP_INTENT_KEY = "shop_cart_item";


    public CartItemListActivity() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_item_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // findViewByID's
        swipeContainer = (SwipeRefreshLayout)findViewById(R.id.swipeContainer);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        setupSwipeContainer();
        setupRecyclerView();


        // get shop from intent

        shop = getIntent().getParcelableExtra(SHOP_INTENT_KEY);

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


        adapter = new CartItemAdapter(dataset,this,this);

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

        if(UtilityGeneral.isNetworkAvailable(this))
        {
            if(shop!=null)
            {
                Call<List<CartItem>> call = cartItemService.getCartItem(0,0,
                        UtilityGeneral.getEndUserID(this),shop.getShopID());

                call.enqueue(this);
            }

        }
        else
        {
            showToastMessage("No network. Application is Offline !");
            swipeContainer.setRefreshing(false);
        }

    }


    @Override
    public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {

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
    public void onFailure(Call<List<CartItem>> call, Throwable t) {

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
    public void notifyUpdate(CartItem cartItem) {

        Call<ResponseBody> call = cartItemService.updateCartItem(cartItem,0,0);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code() == 200)
                {
                    showToastMessage("Item Updated !");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Update failed. Try again !");
            }
        });

    }

    @Override
    public void notifyRemove(CartItem cartItem) {

        Call<ResponseBody> call = cartItemService.deleteCartItem(cartItem.getCartID(),cartItem.getItemID(),0,0);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code() == 200)
                {
                    showToastMessage("Item Deleted");

                    // refresh the list
                    makeNetworkCall();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }



}
