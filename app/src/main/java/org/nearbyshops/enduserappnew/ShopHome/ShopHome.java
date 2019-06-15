package org.nearbyshops.enduserappnew.ShopHome;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.squareup.picasso.Picasso;
import org.nearbyshops.enduserappnew.CartItemList.CartItemListActivity;
import org.nearbyshops.enduserappnew.ItemsInShopByCategory.BackupDeprecated.ItemsInShopByCatDeprecated;
import org.nearbyshops.enduserappnew.Login.Login;
import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.OrderHistory.OrderHistory;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Preferences.PrefShopHome;
import org.nearbyshops.enduserappnew.Preferences.UtilityFunctions;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ShopDetail.ShopDetail;
import org.nearbyshops.enduserappnew.ShopDetail.ShopDetailFragment;
import org.nearbyshops.enduserappnew.ShopReview.ShopReviews;


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

    @BindView(R.id.indicator_pick_from_shop) TextView pickFromShopIndicator;
    @BindView(R.id.indicator_home_delivery) TextView homeDeliveryIndicator;





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
        shop = PrefShopHome.getShop(this);

        if(shop!=null)
        {
            shopName.setText(shop.getShopName());

            if(shop.getPickFromShopAvailable())
            {
                pickFromShopIndicator.setVisibility(View.VISIBLE);
            }
            else
            {
                pickFromShopIndicator.setVisibility(View.GONE);
            }



            if(shop.getHomeDeliveryAvailable())
            {
                homeDeliveryIndicator.setVisibility(View.VISIBLE);
            }
            else
            {
                homeDeliveryIndicator.setVisibility(View.GONE);
            }


            if(shop.getShopAddress()!=null)
            {
                shopAddress.setText(shop.getShopAddress() + ", " + shop.getCity()  + " - " + String.valueOf(shop.getPincode()));
            }

//            String imagePath = UtilityGeneral.getImageEndpointURL(MyApplication.getAppContext())
//                    + shop.getLogoImagePath();



            String imagePath = PrefGeneral.getServiceURL(this) + "/api/v1/Shop/Image/"
                    + "five_hundred_" + shop.getLogoImagePath() + ".jpg";

            Drawable placeholder = VectorDrawableCompat
                    .create(getResources(),
                            R.drawable.ic_nature_people_white_48px, getTheme());


            Picasso.get()
                    .load(imagePath)
                    .placeholder(placeholder)
                    .into(shopLogo);




            String currency = "";
            currency = PrefGeneral.getCurrencySymbol(this);

            delivery.setText("Delivery : " + currency + " " + String.format( "%.2f", shop.getDeliveryCharges()) + " per order");
            distance.setText("Distance : " + String.format( "%.2f", shop.getRt_distance()) + " Km");


            if(shop.getRt_rating_count()==0)
            {
                rating.setText("N/A");
                rating_count.setText("( Not Yet Rated )");

            }
            else
            {
                rating.setText(String.format("%.2f",shop.getRt_rating_avg()));
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

        Intent intent = new Intent(this, ItemsInShopByCatDeprecated.class);
        startActivity(intent);
    }






    @OnClick(R.id.option_cart)
    void shopCart()
    {

        if(PrefLogin.getUser(this)==null)
        {
            showLoginDialog();
            return;
        }



        Intent intent = new Intent(this, CartItemListActivity.class);
//        intent.putExtra(CartItemListActivity.SHOP_INTENT_KEY,shop);


        String jsonString = UtilityFunctions.provideGson().toJson(shop);
        intent.putExtra(CartItemListActivity.SHOP_INTENT_KEY,jsonString);
        startActivity(intent);
    }






    @OnClick(R.id.option_orders)
    void ordersClick()
    {

        if(PrefLogin.getUser(this)==null)
        {
            showLoginDialog();
            return;
        }


        Intent intent = new Intent(this, OrderHistory.class);
        intent.putExtra(OrderHistory.IS_FILTER_BY_SHOP,true);
//        intent.putExtra(IS_FILTER_BY_SHOP,getIntent().getBooleanExtra(IS_FILTER_BY_SHOP,false));
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
//            intent.putExtra(MarketDetail.SHOP_DETAIL_INTENT_KEY,shop);


            String jsonString = UtilityFunctions.provideGson().toJson(shop);
            intent.putExtra(ShopDetailFragment.TAG_JSON_STRING,jsonString);


            startActivity(intent);
    }






    @OnClick(R.id.option_shop_reviews)
    void shopReviews()
    {
        Intent intent = new Intent(this, ShopReviews.class);
//        intent.putExtra(ShopReviews.SHOP_INTENT_KEY, shop);

        String jsonString = UtilityFunctions.provideGson().toJson(shop);
        intent.putExtra(ShopReviews.SHOP_INTENT_KEY,jsonString);

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







    private void showLoginDialog()
    {

        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }


}
