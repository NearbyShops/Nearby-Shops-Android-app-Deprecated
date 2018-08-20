package org.nearbyshops.enduserappnew.OrdersHomeDelivery;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.CancelledOrders.CancelledOrdersHomeDelivery;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.OrderHistoryHD.OrderHistoryHD.OrderHistoryHD;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ShopDetail.ShopDetail;
import org.nearbyshops.enduserappnew.Utility.PrefGeneral;
import org.nearbyshops.enduserappnew.Utility.PrefShopHome;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderHome extends AppCompatActivity {

    private Shop shop;


    @BindView(R.id.shop_name) TextView shopName;
    @BindView(R.id.shop_address) TextView shopAddress;
    @BindView(R.id.shop_logo) ImageView shopLogo;
    @BindView(R.id.delivery) TextView delivery;
    @BindView(R.id.distance) TextView distance;
    @BindView(R.id.rating) TextView rating;
    @BindView(R.id.rating_count) TextView rating_count;
    @BindView(R.id.description) TextView description;


    public static final String IS_FILTER_BY_SHOP = "IS_FILTER_BY_SHOP";


    boolean filterByShop = false;
    @BindView(R.id.shop_card)
    ConstraintLayout shopCard;

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




    @OnClick(R.id.shop_card)
    void shopCardClick()
    {
        shop = PrefShopHome.getShop(this);
        Intent intent = new Intent(this, ShopDetail.class);
        intent.putExtra(ShopDetail.SHOP_DETAIL_INTENT_KEY,shop);
        startActivity(intent);
    }



    void bindShop()
    {
        shop = PrefShopHome.getShop(this);

        if(shop!=null)
        {
            shopName.setText(shop.getShopName());

            if(shop.getShopAddress()!=null)
            {
                shopAddress.setText(shop.getShopAddress() + "\n" + String.valueOf(shop.getPincode()));
            }

//            String imagePath = UtilityGeneral.getImageEndpointURL(MyApplication.getAppContext())
//                    + shop.getLogoImagePath();

            String imagePath = PrefGeneral.getServiceURL(this) + "/api/v1/Shop/Image/"
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
