package org.nearbyshops.enduser.Carts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.nearbyshops.enduser.DaggerComponentBuilder;
import org.nearbyshops.enduser.Home;
import org.nearbyshops.enduser.Model.Order;
import org.nearbyshops.enduser.ModelStats.CartStats;
import org.nearbyshops.enduser.ModelStats.DeliveryAddress;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.RetrofitRESTContract.CartStatsService;
import org.nearbyshops.enduser.RetrofitRESTContract.OrderService;
import org.nearbyshops.enduser.Utility.UtilityGeneral;
import org.nearbyshops.enduser.Utility.UtilityLogin;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceOrderActivity extends AppCompatActivity implements View.OnClickListener, Callback<List<CartStats>> {


    Order order = new Order();

    @Inject
    CartStatsService cartStatsService;

    @Inject
    OrderService orderService;


    CartStats cartStats;

    CartStats cartStatsFromNetworkCall;

    TextView addPickAddress;

    DeliveryAddress selectedAddress;


    // Total Fields
    TextView subTotal;
    TextView deliveryCharges;
    TextView total;

    @Bind(R.id.radioPickFromShop)
    RadioButton pickFromShopCheck;

    @Bind(R.id.radioHomeDelivery)
    RadioButton homeDelieryCheck;

    @Bind(R.id.placeOrder)
    TextView placeOrder;


    // address views
    TextView name;
    TextView deliveryAddressView;
    TextView city;
    TextView pincode;
    TextView landmark;
    TextView phoneNumber;
    RelativeLayout addressContainer;

    // address views ends

    public final static String CART_STATS_INTENT_KEY = "cart_stats_intent_key";


    public PlaceOrderActivity() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // findViewByID'// STOPSHIP: 11/6/16

        addPickAddress = (TextView) findViewById(R.id.pickFromSavedAddresses);
        addPickAddress.setOnClickListener(this);


        name = (TextView) findViewById(R.id.name);
        deliveryAddressView = (TextView)findViewById(R.id.deliveryAddress);
        city = (TextView)findViewById(R.id.city);
        pincode = (TextView)findViewById(R.id.pincode);
        landmark = (TextView)findViewById(R.id.landmark);
        phoneNumber = (TextView)findViewById(R.id.phoneNumber);
        addressContainer = (RelativeLayout) findViewById(R.id.selectedDeliveryAddress);

        // Total Fields


        subTotal = (TextView) findViewById(R.id.subTotal);
        deliveryCharges = (TextView) findViewById(R.id.deliveryCharges);
        total = (TextView) findViewById(R.id.total);

        pickFromShopCheck = (RadioButton) findViewById(R.id.radioPickFromShop);
        homeDelieryCheck = (RadioButton) findViewById(R.id.radioHomeDelivery);



        // Bind View Ends


        cartStats = getIntent().getParcelableExtra(CART_STATS_INTENT_KEY);

        if(savedInstanceState!=null)
        {
            selectedAddress = savedInstanceState.getParcelable("selectedAddress");
        }


        if(selectedAddress!=null)
        {
            addressContainer.setVisibility(View.VISIBLE);
            bindDataToViews(selectedAddress);
        }else
        {
            addressContainer.setVisibility(View.GONE);

        }

        if(cartStatsFromNetworkCall==null)
        {
            makeNetworkCall();
        }

    }


    @Override
    public void onClick(View v) {

        Intent intent = new Intent(this,DeliveryAddressActivity.class);

        startActivityForResult(intent,1);



        //startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == 2 && data != null)
        {
            selectedAddress = data.getParcelableExtra("output");

            if(selectedAddress!=null)
            {
                addressContainer.setVisibility(View.VISIBLE);

                bindDataToViews(selectedAddress);

            }

        }

    }


    void bindDataToViews(DeliveryAddress deliveryAddress)
    {
        if(deliveryAddress != null)
        {
            name.setText(deliveryAddress.getName());
            deliveryAddressView.setText(deliveryAddress.getDeliveryAddress());
            city.setText(deliveryAddress.getCity());
            pincode.setText(String.valueOf(deliveryAddress.getPincode()));
            landmark.setText(deliveryAddress.getLandmark());
            phoneNumber.setText(String.valueOf(deliveryAddress.getPhoneNumber()));
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(selectedAddress!=null)
        {
            outState.putParcelable("selectedAddress",selectedAddress);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        selectedAddress = savedInstanceState.getParcelable("selectedAddress");
    }



    void makeNetworkCall() {

        if (cartStats == null){

            return;
        }

        Call<List<CartStats>> call = cartStatsService.getCart(UtilityLogin.getEndUser(this).getEndUserID(),cartStats.getCartID(),0,0);

        call.enqueue(this);

    }


    @Override
    public void onResponse(Call<List<CartStats>> call, Response<List<CartStats>> response) {

        if(response!=null)
        {
            cartStatsFromNetworkCall = response.body().get(0);
            setTotal();
        }
    }

    @Override
    public void onFailure(Call<List<CartStats>> call, Throwable t) {


        showToastMessage("Network connection failed. Check Internet connectivity !");
    }



    void setTotal()
    {
        if(cartStatsFromNetworkCall!=null)
        {
            subTotal.setText("Subtotal: " + cartStats.getCart_Total());
            deliveryCharges.setText("Delivery Charges : N/A");

            //total.setText("Total : " + cartStats.getCart_Total()+ );

            if(pickFromShopCheck.isChecked())
            {
                total.setText("Total : " + String.format( "%.2f", cartStats.getCart_Total()));
                deliveryCharges.setText("Delivery Charges : " + 0);
            }

            if(homeDelieryCheck.isChecked())
            {
                total.setText("Total : " + String.format( "%.2f", cartStats.getCart_Total() + cartStats.getShop().getDeliveryCharges()));
                deliveryCharges.setText("Delivery Charges : " + cartStats.getShop().getDeliveryCharges());
            }
        }
    }


    @OnClick({R.id.radioPickFromShop,R.id.radioHomeDelivery})
    void radioCheckClicked()
    {
        setTotal();
    }


    @OnClick(R.id.placeOrder)
    void placeOrderClick()
    {

        if(selectedAddress==null)
        {
            showToastMessage("Please add/select Delivery Address !");
            return;
        }

        if(pickFromShopCheck.isChecked()==false && homeDelieryCheck.isChecked()== false)
        {
            showToastMessage("Please select delivery type !");
            return;
        }



        order.setDeliveryAddressID(selectedAddress.getId());

        if(pickFromShopCheck.isChecked())
        {
            order.setPickFromShop(true);
        }
        else if(homeDelieryCheck.isChecked())
        {
            order.setPickFromShop(false);
        }

        order.setOrderStatus(1);

        if(cartStatsFromNetworkCall==null)
        {
            showToastMessage("Network problem. Try again !");
            return;
        }

        Call<Order> call = orderService.postOrder(order,cartStatsFromNetworkCall.getCartID());

        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {

                if(response!=null)
                {
                    if(response.code() == 201)
                    {
                        showToastMessage("Successful !");


                        Intent i = new Intent(PlaceOrderActivity.this,Home.class);

                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(i);

                    }else
                    {

                        showToastMessage("failed !");
                    }

                }else
                {
                    showToastMessage("failed !");
                }

            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {

                showToastMessage("Network connection Failed !");

            }
        });


    }




    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }
}
