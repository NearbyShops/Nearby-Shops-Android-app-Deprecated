package org.nearbyshops.enduserapp.Carts.CartItem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.nearbyshops.enduserapp.Carts.PlaceOrderActivity;
import org.nearbyshops.enduserapp.DaggerComponentBuilder;
import org.nearbyshops.enduserapp.LoginNew.Login;
import org.nearbyshops.enduserapp.ModelCartOrder.CartItem;
import org.nearbyshops.enduserapp.Model.Shop;
import org.nearbyshops.enduserapp.ModelRoles.User;
import org.nearbyshops.enduserapp.ModelStats.CartStats;
import org.nearbyshops.enduserapp.R;
import org.nearbyshops.enduserapp.RetrofitRESTContract.CartItemService;
import org.nearbyshops.enduserapp.RetrofitRESTContract.CartStatsService;
import org.nearbyshops.enduserapp.Utility.PrefLogin;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartItemListActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener, CartItemAdapter.NotifyCartItem {


    TextView confirmItems;

    @Inject
    CartItemService cartItemService;

    @Inject
    CartStatsService cartStatsService;

    RecyclerView recyclerView;

    CartItemAdapter adapter;

    GridLayoutManager layoutManager;

    SwipeRefreshLayout swipeContainer;

    List<CartItem> dataset = new ArrayList<>();

    Shop shop = null;

    CartStats cartStats = null;

    public final static String SHOP_INTENT_KEY = "shop_cart_item";
    public final static String CART_STATS_INTENT_KEY = "cart_stats";

    TextView totalValue;
    TextView estimatedTotal;

    double cartTotal = 0;


    // header views
    ImageView shopImage;
    TextView shopName;
    TextView rating;
    TextView distance;
    TextView deliveryCharge;
    TextView itemsInCart;
    TextView cartTotalHeader;
    LinearLayout cartsListItem;


    public CartItemListActivity() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_item_list);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // findViewByID's
        swipeContainer = (SwipeRefreshLayout)findViewById(R.id.swipeContainer);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        totalValue = (TextView) findViewById(R.id.totalValue);
        estimatedTotal = (TextView) findViewById(R.id.estimatedTotal);
        confirmItems = (TextView) findViewById(R.id.confirm);


//        shopImage = (ImageView) findViewById(R.id.shopImage);
//        shopName = (TextView) findViewById(R.id.shopName);
//        rating = (TextView) findViewById(R.id.rating);
//        distance = (TextView) findViewById(R.id.distance);
//        deliveryCharge = (TextView) findViewById(R.id.deliveryCharge);
//        itemsInCart = (TextView) findViewById(R.id.itemsInCart);
//        cartTotalHeader = (TextView) findViewById(R.id.cartTotal);
//        cartsListItem = (LinearLayout)findViewById(R.id.carts_list_item);




        // get shop from intent

        shop = getIntent().getParcelableExtra(SHOP_INTENT_KEY);
        cartStats = getIntent().getParcelableExtra(CART_STATS_INTENT_KEY);

        if(cartStats==null)
        {
            fetchCartStats();
        }



//        setupHeader();
        setupSwipeContainer();
        setupRecyclerView();

        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        if(savedInstanceState==null)
        {
            swipeRefresh();
        }


        displayCartStats();
    }



    void fetchCartStats()
    {

        User endUser = PrefLogin.getUser(this);

        if(endUser==null || shop==null)
        {
//            showLoginDialog();
            return;
        }


        Call<List<CartStats>> call = cartStatsService.getCart(endUser.getUserID(),null,shop.getShopID(),false,null,null);

        call.enqueue(new Callback<List<CartStats>>() {

            @Override
            public void onResponse(Call<List<CartStats>> call, Response<List<CartStats>> response) {

                if(response.code()==200 && response.body()!=null)
                {
                    if(response.body().size()>0)
                    {
                        cartStats = response.body().get(0);
                        cartStats.setShop(shop);
                        displayCartStats();
                    }
                }


            }

            @Override
            public void onFailure(Call<List<CartStats>> call, Throwable t) {

            }
        });

    }




    void displayCartStats()
    {

        if(cartStats!=null)
        {
            cartTotal = cartStats.getCart_Total();
            totalValue.setText(" : Rs " + String.format("%.2f", cartTotal));

            adapter.setCartStats(cartStats);
        }
    }



    @OnClick(R.id.confirm)
    void confirmItemsClick(View view)
    {
        Intent intent = new Intent(this,PlaceOrderActivity.class);
        intent.putExtra(PlaceOrderActivity.CART_STATS_INTENT_KEY,cartStats);

        startActivity(intent);
    }


    /*void setupHeader()
    {

        if(cartStats!=null)
        {
            itemsInCart.setText(cartStats.getItemsInCart() + " Items in Cart");
            cartTotalHeader.setText("Cart Total : Rs " + cartStats.getCart_Total());
        }

        if(shop!=null)
        {
            deliveryCharge.setText("Delivery\nRs " + shop.getDeliveryCharges() + "\nPer Order");
            distance.setText(String.format( "%.2f", shop.getDistance())
                    + " Km");

            shopName.setText(shop.getShopName());


            String imagePath = UtilityGeneral.getImageEndpointURL(MyApplication.getAppContext())
                    + shop.getImagePath();

            Picasso.with(this)
                    .load(imagePath)
                    .placeholder(R.drawable.nature_people)
                    .into(shopImage);
        }

    }
*/

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


        int spanCount = (int) (metrics.widthPixels/(230 * metrics.density));

        if(spanCount==0){
            spanCount = 1;
        }

        layoutManager.setSpanCount(spanCount);



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

        User endUser = PrefLogin.getUser(this);

        if(endUser==null)
        {
            showLoginDialog();
            swipeContainer.setRefreshing(false);
            return;
        }

        if(shop==null)
        {
            swipeContainer.setRefreshing(false);
            showToastMessage("Shop null !");
            return;
        }

        Call<List<CartItem>> call = cartItemService.getCartItem(null,null,
                endUser.getUserID(),shop.getShopID(),true);

        call.enqueue(new Callback<List<CartItem>>() {
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
        });




//        if(UtilityGeneral.isNetworkAvailable(this))
//        {
//
//
//
//        }
//        else
//        {
//            showToastMessage("No network. Application is Offline !");
//            swipeContainer.setRefreshing(false);
//        }

    }




    @Override
    protected void onResume() {
        super.onResume();
    }


    void swipeRefresh()
    {

        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);
                onRefresh();
            }
        });
    }



    @Override
    public void notifyUpdate(CartItem cartItem) {

//        Call<ResponseBody> call = cartItemService.updateCartItem(cartItem,0,0);
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                if(response.code() == 200)
//                {
//                    showToastMessage("Item Updated !");
//
//                    totalValue.setText(" : Rs " + String.format("%.2f", cartTotal));
//                    cartStats.setCart_Total(cartTotal);
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                showToastMessage("Update failed. Try again !");
//            }
//        });



        showToastMessage("Item Updated !");
        totalValue.setText(" : Rs " + String.format("%.2f", cartTotal));
        cartStats.setCart_Total(cartTotal);


    }

    @Override
    public void notifyRemove(CartItem cartItem) {

//        Call<ResponseBody> call = cartItemService.deleteCartItem(cartItem.getCartID(),cartItem.getItemID(),0,0);
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                if(response.code() == 200)
//                {
//                    showToastMessage("Item Deleted");
//
//                    // refresh the list
//                    makeNetworkCall();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                showToastMessage("Remove failed. Try again !");
//
//            }
//        });
    }






    @Override
    public void notifyTotal(double total) {

        cartTotal = total;
        estimatedTotal.setText("Estimated Total (Before Update) : Rs " + String.format("%.2f", cartTotal));
    }





    private void showLoginDialog()
    {
//        FragmentManager fm = getSupportFragmentManager();
//        LoginDialog loginDialog = new LoginDialog();
//        loginDialog.show(fm,"serviceUrl");


        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
    }

}
