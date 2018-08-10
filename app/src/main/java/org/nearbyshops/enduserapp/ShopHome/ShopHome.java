package org.nearbyshops.enduserapp.ShopHome;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserapp.Carts.CartItem.CartItemListActivity;
import org.nearbyshops.enduserapp.ItemsInShop.ItemsInShop;
import org.nearbyshops.enduserapp.ItemsInShopByCat.ItemsInShopByCat;
import org.nearbyshops.enduserapp.Model.Shop;
import org.nearbyshops.enduserapp.OrdersHomeDelivery.OrderHome;
import org.nearbyshops.enduserapp.OrdersHomePickFromShop.OrdersHomePickFromShop;
import org.nearbyshops.enduserapp.R;
import org.nearbyshops.enduserapp.ShopDetail.ShopDetail;
import org.nearbyshops.enduserapp.ShopReview.ShopReviews;
import org.nearbyshops.enduserapp.Utility.PrefGeneral;
import org.nearbyshops.enduserapp.Utility.UtilityShopHome;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopHome extends AppCompatActivity {

    private Shop shop;

    @BindView(R.id.shop_name) TextView shopName;
    @BindView(R.id.shop_address) TextView shopAddress;
    @BindView(R.id.shop_logo) ImageView shopLogo;
    @BindView(R.id.delivery) TextView delivery;
    @BindView(R.id.distance) TextView distance;
    @BindView(R.id.rating) TextView rating;
    @BindView(R.id.rating_count) TextView rating_count;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.option_items_by_category) ImageView optionItemsByCategory;



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

            String imagePath = PrefGeneral.getServiceURL(this) + "/api/v1/Shop/Image/"
                    + "five_hundred_" + shop.getLogoImagePath() + ".jpg";

            Drawable placeholder = VectorDrawableCompat
                    .create(getResources(),
                            R.drawable.ic_nature_people_white_48px, getTheme());

            Picasso.with(this)
                    .load(imagePath)
                    .placeholder(placeholder)
                    .into(shopLogo);


            String currency = "";
            currency = PrefGeneral.getCurrencySymbol(this);

            delivery.setText("Delivery : " + currency + ". " + String.format( "%.2f", shop.getDeliveryCharges()) + " per order");
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

        Intent intent = new Intent(this,ItemsInShopByCat.class);
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


//    @OnClick(R.id.option_orders_pfs)
//    void ordersPFSClick()
//    {
//        Intent intent = new Intent(this,OrdersHomePickFromShop.class);
//        intent.putExtra(OrdersHomePickFromShop.IS_FILTER_BY_SHOP,true);
//        startActivity(intent);
//    }




//    @OnClick(R.id.option_items)
//    void ItemsClick()
//    {
//        Intent intent = new Intent(this, ItemsInShop.class);
//        startActivity(intent);
//    }


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


    @OnClick(R.id.option_shop_staff)
    void shopStaffClick()
    {
        showToastMessage("Feature coming soon !");
    }


    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }


}
