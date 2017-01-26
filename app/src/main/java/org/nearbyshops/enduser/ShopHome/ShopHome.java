package org.nearbyshops.enduser.ShopHome;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduser.Carts.CartItemListActivity;
import org.nearbyshops.enduser.ItemsInShop.ItemsInShop;
import org.nearbyshops.enduser.ItemsInShopByCat.ItemsInStockByCat;
import org.nearbyshops.enduser.Model.Shop;
import org.nearbyshops.enduser.MyApplication;
import org.nearbyshops.enduser.OrdersHomeDelivery.OrderHome;
import org.nearbyshops.enduser.OrdersHomePickFromShop.OrdersHomePickFromShop;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.ShopDetail.ShopDetail;
import org.nearbyshops.enduser.ShopReview.ShopReviews;
import org.nearbyshops.enduser.Utility.UtilityGeneral;
import org.nearbyshops.enduser.Utility.UtilityShopHome;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopHome extends AppCompatActivity {

    private Shop shop;

    @Bind(R.id.shop_name) TextView shopName;
    @Bind(R.id.shop_address) TextView shopAddress;
    @Bind(R.id.shop_logo) ImageView shopLogo;
    @Bind(R.id.delivery) TextView delivery;
    @Bind(R.id.distance) TextView distance;
    @Bind(R.id.rating) TextView rating;
    @Bind(R.id.rating_count) TextView rating_count;
    @Bind(R.id.description) TextView description;
    @Bind(R.id.option_items_by_category) ImageView optionItemsByCategory;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_home);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bindShop();
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




    @OnClick(R.id.option_items_by_category)
    void ItemsByCategoryClick()
    {

//        Intent intent = new Intent(this, ShopItemsInShopByCat.class);

        Intent intent = new Intent(this,ItemsInStockByCat.class);
        startActivity(intent);

    }


    @OnClick(R.id.option_cart)
    void shopCart()
    {
        Intent intent = new Intent(this,CartItemListActivity.class);
        intent.putExtra(CartItemListActivity.SHOP_INTENT_KEY,shop);
        startActivity(intent);
    }

    @OnClick(R.id.option_orders)
    void ordersClick()
    {
        Intent intent = new Intent(this,OrderHome.class);
        intent.putExtra(OrderHome.IS_FILTER_BY_SHOP,true);
        startActivity(intent);
    }


    @OnClick(R.id.option_orders_pfs)
    void ordersPFSClick()
    {
        Intent intent = new Intent(this,OrdersHomePickFromShop.class);
        intent.putExtra(OrdersHomePickFromShop.IS_FILTER_BY_SHOP,true);
        startActivity(intent);
    }



    @OnClick(R.id.option_items)
    void ItemsClick()
    {
        Intent intent = new Intent(this, ItemsInShop.class);
        startActivity(intent);
    }


    @OnClick(R.id.shop_card)
    void shopCardClick()
    {
            Intent intent = new Intent(this, ShopDetail.class);
            intent.putExtra(ShopDetail.SHOP_DETAIL_INTENT_KEY,shop);
            startActivity(intent);
    }



    @OnClick(R.id.option_shop_reviews)
    void shopReviews()
    {
        Intent intent = new Intent(this, ShopReviews.class);
        intent.putExtra(ShopReviews.SHOP_INTENT_KEY, shop);
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }
}
