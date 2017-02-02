package org.nearbyshops.enduser.OrdersHomeDelivery;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduser.CancelledOrders.CancelledOrdersHomeDelivery;
import org.nearbyshops.enduser.Model.Shop;
import org.nearbyshops.enduser.OrderHistoryHD.OrderHistoryHD.OrderHistoryHD;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.ShopDetail.ShopDetail;
import org.nearbyshops.enduser.Utility.UtilityGeneral;
import org.nearbyshops.enduser.Utility.UtilityShopHome;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderHome extends AppCompatActivity {

    private Shop shop;


    @Bind(R.id.shop_name) TextView shopName;
    @Bind(R.id.shop_address) TextView shopAddress;
    @Bind(R.id.shop_logo) ImageView shopLogo;
    @Bind(R.id.delivery) TextView delivery;
    @Bind(R.id.distance) TextView distance;
    @Bind(R.id.rating) TextView rating;
    @Bind(R.id.rating_count) TextView rating_count;
    @Bind(R.id.description) TextView description;


    public static final String IS_FILTER_BY_SHOP = "IS_FILTER_BY_SHOP";


    boolean filterByShop = false;
    @Bind(R.id.shop_card) CardView shopCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_home);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        filterByShop = getIntent().getBooleanExtra(IS_FILTER_BY_SHOP,false);
        if(filterByShop)
        {
            shopCard.setVisibility(View.VISIBLE);
        }
        else
        {
            shopCard.setVisibility(View.GONE);
        }

        bindShop();
    }



    @OnClick(R.id.order_history)
    void orderHistoryClick()
    {
        Intent intent = new Intent(this, OrderHistoryHD.class);
        intent.putExtra(OrderHistoryHD.IS_FILTER_BY_SHOP,getIntent().getBooleanExtra(IS_FILTER_BY_SHOP,false));
        startActivity(intent);
    }


    @OnClick(R.id.cancelled_hd)
    void cancelledOrders()
    {
        Intent intent = new Intent(this, CancelledOrdersHomeDelivery.class);
        intent.putExtra(CancelledOrdersHomeDelivery.IS_FILTER_BY_SHOP,getIntent().getBooleanExtra(IS_FILTER_BY_SHOP,false));
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.shop_card)
    void shopCardClick()
    {
        shop = UtilityShopHome.getShop(this);
        Intent intent = new Intent(this, ShopDetail.class);
        intent.putExtra(ShopDetail.SHOP_DETAIL_INTENT_KEY,shop);
        startActivity(intent);
    }



    void bindShop()
    {
        shop = UtilityShopHome.getShop(this);

        if(shop!=null)
        {
            shopName.setText(shop.getShopName());

            if(shop.getShopAddress()!=null)
            {
                shopAddress.setText(shop.getShopAddress() + "\n" + String.valueOf(shop.getPincode()));
            }

//            String imagePath = UtilityGeneral.getImageEndpointURL(MyApplication.getAppContext())
//                    + shop.getLogoImagePath();

            String imagePath = UtilityGeneral.getServiceURL(this) + "/api/v1/Shop/Image/"
                    + "five_hundred_" + shop.getLogoImagePath() + ".jpg";

            Drawable placeholder = VectorDrawableCompat
                    .create(getResources(),
                            R.drawable.ic_nature_people_white_48px, getTheme());

            Picasso.with(this)
                    .load(imagePath)
                    .placeholder(placeholder)
                    .into(shopLogo);

            delivery.setText("Delivery : Rs " + String.format( "%.2f", shop.getDeliveryCharges()) + " per order");
            distance.setText("Distance : " + String.format( "%.2f", shop.getRt_distance()) + " Km");


            if(shop.getRt_rating_count()==0)
            {
                rating.setText("N/A");
                rating_count.setText("( Not Yet Rated )");

            }
            else
            {
                rating.setText(String.valueOf(shop.getRt_rating_avg()));
                rating_count.setText("( " + String.format( "%.0f", shop.getRt_rating_count()) + " Ratings )");
            }


            if(shop.getShortDescription()!=null)
            {
                description.setText(shop.getShortDescription());
            }

        }
    }


}
