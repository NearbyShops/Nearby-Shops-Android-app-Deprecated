package org.nearbyshops.enduser.OrderDetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.nearbyshops.enduser.R;


public class OrderDetail extends AppCompatActivity {

//    public static final String ORDER_TAG = "ORDER_TAG";
    public static final String FRAGMENT_ORDER_DETAIL = "FRAGMENT_ORDER_DETAIL";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        if(getSupportFragmentManager().findFragmentByTag(FRAGMENT_ORDER_DETAIL)==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container,new FragmentOrderDetail(),FRAGMENT_ORDER_DETAIL)
                    .commit();
        }
    }


}
