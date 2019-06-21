package org.nearbyshops.enduserappnew.Checkout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import org.nearbyshops.enduserappnew.API.CartStatsService;
import org.nearbyshops.enduserappnew.API.OrderService;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.DeliveryAddress.DeliveryAddressActivity;
import org.nearbyshops.enduserappnew.Home;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.Model.ModelCartOrder.Order;
import org.nearbyshops.enduserappnew.Model.ModelStats.CartStats;
import org.nearbyshops.enduserappnew.Model.ModelStats.DeliveryAddress;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Preferences.UtilityFunctions;
import org.nearbyshops.enduserappnew.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import java.util.List;

public class PlaceOrderActivity extends AppCompatActivity implements View.OnClickListener{


    Order order = new Order();
//    OrderPFS orderPFS = new OrderPFS();

    @Inject
    CartStatsService cartStatsService;

    @Inject
    OrderService orderService;
//    @Inject OrderServicePFS orderServicePFS;


    CartStats cartStats;
    CartStats cartStatsFromNetworkCall;

    TextView addPickAddress;
    DeliveryAddress selectedAddress;




    // Total Fields
    TextView subTotal;
    TextView deliveryCharges;
    TextView total;

    @BindView(R.id.free_delivery_info) TextView freeDeliveryInfo;

    @BindView(R.id.radioPickFromShop) RadioButton pickFromShopCheck;
    @BindView(R.id.radioHomeDelivery) RadioButton homeDelieryCheck;
    @BindView(R.id.placeOrder) TextView placeOrder;


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


//        cartStats = getIntent().getParcelableExtra(CART_STATS_INTENT_KEY);

        String cartStatsJson = getIntent().getStringExtra(CART_STATS_INTENT_KEY);
        cartStats = UtilityFunctions.provideGson().fromJson(cartStatsJson, CartStats.class);


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

        Intent intent = new Intent(this, DeliveryAddressActivity.class);

        startActivityForResult(intent,1);



        //startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == 2 && data != null)
        {
            String jsonString = data.getStringExtra("output");
            selectedAddress = UtilityFunctions.provideGson().fromJson(jsonString,DeliveryAddress.class);

//            selectedAddress = data.getParcelableExtra("output");

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





//        @Override
//        protected void onSaveInstanceState(Bundle outState) {
//            super.onSaveInstanceState(outState);
//
//            if(selectedAddress!=null)
//            {
//                outState.putParcelable("selectedAddress",selectedAddress);
//            }
//        }
//
//        @Override
//        protected void onRestoreInstanceState(Bundle savedInstanceState) {
//            super.onRestoreInstanceState(savedInstanceState);
//
//            selectedAddress = savedInstanceState.getParcelable("selectedAddress");
//        }



    void makeNetworkCall() {

        if (cartStats == null){

            return;
        }


        Call<List<CartStats>> call = cartStatsService.getCart(
                PrefLogin.getUser(this).getUserID(),cartStats.getCartID(),
                null,true,null,null
        );



        call.enqueue(new Callback<List<CartStats>>() {
            @Override
            public void onResponse(Call<List<CartStats>> call, Response<List<CartStats>> response) {


                if(response!=null)
                {
                    cartStatsFromNetworkCall = response.body().get(0);
                    setTotal();
                    setRadioCheck();
                }
            }

            @Override
            public void onFailure(Call<List<CartStats>> call, Throwable t) {

                showToastMessage("Network connection failed. Check Internet connectivity !");
            }
        });
    }




    void setRadioCheck()
    {
        if(cartStatsFromNetworkCall!=null)
        {
            Shop shop = cartStatsFromNetworkCall.getShop();

            if(shop!=null)
            {
                homeDelieryCheck.setEnabled(shop.getHomeDeliveryAvailable());
                pickFromShopCheck.setEnabled(shop.getPickFromShopAvailable());
            }
        }
    }





    void setTotal()
    {
        if(cartStatsFromNetworkCall!=null)
        {

            freeDeliveryInfo.setText("Free delivery is offered above the order of " + String.valueOf(PrefGeneral.getCurrencySymbol(this)) + " " + String.valueOf(cartStatsFromNetworkCall.getShop().getBillAmountForFreeDelivery()));


            subTotal.setText("Subtotal: " + PrefGeneral.getCurrencySymbol(this) + " " + cartStats.getCart_Total());
            deliveryCharges.setText("Delivery Charges : N/A");

            //total.setText("Total : " + cartStats.getCart_Total()+ );

            if(pickFromShopCheck.isChecked())
            {
                total.setText("Total : " + PrefGeneral.getCurrencySymbol(this) + " " + String.format( "%.2f", cartStats.getCart_Total()));
                deliveryCharges.setText("Delivery Charges : "+ PrefGeneral.getCurrencySymbol(this) + " " + 0);
            }



            if(homeDelieryCheck.isChecked())
            {


                if(cartStatsFromNetworkCall.getCart_Total() < cartStatsFromNetworkCall.getShop().getBillAmountForFreeDelivery())
                {

                    total.setText("Total : " + PrefGeneral.getCurrencySymbol(this) + " " + String.format( "%.2f", cartStats.getCart_Total() + cartStats.getShop().getDeliveryCharges()));
                    deliveryCharges.setText("Delivery Charges : " + PrefGeneral.getCurrencySymbol(this) + " " + cartStats.getShop().getDeliveryCharges());
                }
                else
                {

                    deliveryCharges.setText("Delivery Charges : Zero " + "(Delivery is free above the order of : " + PrefGeneral.getCurrencySymbol(this) + " " + String.valueOf(cartStatsFromNetworkCall.getShop().getBillAmountForFreeDelivery()) + " )");
                    total.setText("Total : " + PrefGeneral.getCurrencySymbol(this) + " " + String.format( "%.2f", cartStats.getCart_Total()));
                }

            }
        }
    }







    @OnClick({R.id.radioPickFromShop, R.id.radioHomeDelivery})
    void radioCheckClicked()
    {
        setTotal();

        if(pickFromShopCheck.isChecked())
        {
            addPickAddress.setText("Select Address");
        }
        else
        {
            addPickAddress.setText("Select Delivery Address");
        }
    }






    @OnClick(R.id.placeOrder)
    void placeOrderClick()
    {

        if(pickFromShopCheck.isChecked()==false && homeDelieryCheck.isChecked()== false)
        {
            showToastMessage("Please select delivery type !");
            return;
        }


        if(selectedAddress==null)
        {
            showToastMessage("Please add/select Delivery Address !");
            return;
        }



        if(cartStatsFromNetworkCall==null)
        {
            showToastMessage("Network problem. Try again !");
            return;
        }



        order.setEndUserID(PrefLogin.getUser(this).getUserID());

        order.setDeliveryAddressID(selectedAddress.getId());
//        orderPFS.setDeliveryAddressID(selectedAddress.getId());

        if(pickFromShopCheck.isChecked())
        {
            order.setPickFromShop(true);
//            placeOrderPFS();
        }
        else if(homeDelieryCheck.isChecked())
        {
            order.setPickFromShop(false);
        }

        placeOrderHD();


//        order.setOrderStatus(1);

    }

//    void placeOrderPFS()
//    {
//        Call<ResponseBody> call = orderServicePFS.postOrder(orderPFS,cartStatsFromNetworkCall.getCartID());
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//
//                if(response!=null)
//                {
//                    if(response.code() == 201)
//                    {
//                        showToastMessage("Successful !");
//
//
//                        Intent i = new Intent(PlaceOrderActivity.this,HomeNew.class);
//
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//                        startActivity(i);
//
//                    }else
//                    {
//                        showToastMessage("failed Code : !" + String.valueOf(response.code()));
//                    }
//
//                }else
//                {
//                    showToastMessage("failed !");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                showToastMessage("Network connection Failed !");
//            }
//        });
//    }




    void placeOrderHD()
    {
        Call<ResponseBody> call = orderService.postOrder(order,cartStatsFromNetworkCall.getCartID());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response!=null)
                {
                    if(response.code() == 201)
                    {
                        showToastMessage("Successful !");


                        Intent i = new Intent(PlaceOrderActivity.this, Home.class);

                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(i);

                    }else
                    {
                        showToastMessage("failed Code : !" + String.valueOf(response.code()));
                    }

                }else
                {
                    showToastMessage("failed !");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Network connection Failed !");

            }
        });

    }








    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }



//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
////        ButterKnife.unBindView(this);
//    }
}
