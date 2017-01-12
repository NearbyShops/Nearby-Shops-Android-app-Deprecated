package org.nearbyshops.enduser.OrdersHomeDelivery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.nearbyshops.enduser.CancelledOrders.CancelledOrdersHomeDelivery;
import org.nearbyshops.enduser.OrderHistoryHD.OrderHistoryHD.OrderHistoryHD;
import org.nearbyshops.enduser.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_home);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    @OnClick(R.id.home_delivery)
    void orderHistoryClick()
    {
        startActivity(new Intent(this, OrderHistoryHD.class));
    }


    @OnClick(R.id.cancelled_hd)
    void cancelledOrders()
    {
        startActivity(new Intent(this, CancelledOrdersHomeDelivery.class));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
