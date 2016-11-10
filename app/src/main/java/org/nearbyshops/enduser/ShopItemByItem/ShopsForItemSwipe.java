package org.nearbyshops.enduser.ShopItemByItem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.wunderlist.slidinglayer.SlidingLayer;

import org.nearbyshops.enduser.Carts.CartsListActivity;
import org.nearbyshops.enduser.Login.NotifyAboutLogin;
import org.nearbyshops.enduser.Model.Item;
import org.nearbyshops.enduser.ModelRoles.EndUser;
import org.nearbyshops.enduser.ModelStats.ItemStats;
import org.nearbyshops.enduser.MyApplication;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.ShopItemByItem.Interfaces.NotifyFillCartsChanged;
import org.nearbyshops.enduser.ShopItemByItem.Interfaces.NotifyNewCartsChanged;
import org.nearbyshops.enduser.ShopItemByItem.Interfaces.NotifySwipeToRight;
import org.nearbyshops.enduser.ShopReview.SlidingLayerSortReview;
import org.nearbyshops.enduser.ShopsByCategory.Interfaces.NotifySort;
import org.nearbyshops.enduser.ShopsByCategory.Interfaces.NotifyTitleChanged;
import org.nearbyshops.enduser.Utility.UtilityGeneral;
import org.nearbyshops.enduser.Utility.UtilityLogin;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 31/5/16.
 */
public class ShopsForItemSwipe extends AppCompatActivity implements Target, NotifySort,
        NotifySwipeToRight,NotifyFillCartsChanged, NotifyTitleChanged,NotifyNewCartsChanged, NotifyAboutLogin{


    private PagerAdapterShopItem pagerAdapter;

    private ViewPager mViewPager;

    @Bind(R.id.tablayout)
    TabLayout tabLayout;


    Item item;

    ImageView actionBarImage;

    Toolbar toolbar;

    public static final String ITEM_INTENT_KEY = "item";

    @Bind(R.id.appbar)
    AppBarLayout appBarLayout;

//    @Bind(R.id.itemStats)
//    TextView itemStatsText;


    public static final String TAG_SLIDING_LAYER_FRAGMENT = "sliding_layer";




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_shops_for_item);
        ButterKnife.bind(this);

        item = getIntent().getParcelableExtra(ITEM_INTENT_KEY);

//        actionBarImage = (ImageView) findViewById(R.id.actionBarImage);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(item!=null)
        {
            String imagePath = UtilityGeneral.getImageEndpointURL(MyApplication.getAppContext())
                    + item.getItemImageURL();

//            Picasso.with(this).load(imagePath).placeholder(R.drawable.nature_people).into(actionBarImage);
//            Picasso.with(this).load(imagePath).placeholder(R.drawable.nature_people).into(this);

            //toolbar.setTitle("Shops For : \n" + item.getItemName());
            //toolbar.setSubtitle("Available in Shops");
            getSupportActionBar().setTitle(item.getItemName());

            ItemStats itemStats = item.getItemStats();

//            itemStatsText.setText("Available in " + itemStats.getShopCount() + " Shops" + "\n"
//                                    + "Rs " + itemStats.getMin_price() + " - " + itemStats.getMax_price());

        }


        // setup pager adapter
        pagerAdapter = new PagerAdapterShopItem(getSupportFragmentManager(),item);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);

        setupSlidingLayer();
    }


    @Bind(R.id.slidingLayer)
    SlidingLayer slidingLayer;




    void setupSlidingLayer()
    {

        ////slidingLayer.setShadowDrawable(R.drawable.sidebar_shadow);
        //slidingLayer.setShadowSizeRes(R.dimen.shadow_size);

        if(slidingLayer!=null)
        {
            slidingLayer.setChangeStateOnTap(true);
            slidingLayer.setSlidingEnabled(true);
            slidingLayer.setPreviewOffsetDistance(15);
            slidingLayer.setOffsetDistance(10);
            slidingLayer.setStickTo(SlidingLayer.STICK_TO_RIGHT);

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);



            //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(250, ViewGroup.LayoutParams.MATCH_PARENT);

            //slidingContents.setLayoutParams(layoutParams);

            //slidingContents.setMinimumWidth(metrics.widthPixels-50);


            if(getSupportFragmentManager().findFragmentByTag(TAG_SLIDING_LAYER_FRAGMENT)==null)
            {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.slidinglayerfragment,new SlidingLayerSortShopItem(),TAG_SLIDING_LAYER_FRAGMENT)
                        .commit();
            }


        }

    }




    @OnClick({R.id.icon_sort,R.id.text_sort})
    void sortClick()
    {
        slidingLayer.openLayer(true);
    }


    @OnClick({R.id.icon_checkout,R.id.text_checkout})
    void checkoutClick()
    {

        EndUser endUser = UtilityLogin.getEndUser(this);

        if(endUser == null)
        {
            showToastMessage("Please Login to continue ...");

            return;
        }

        cartsButtonClick();
    }


    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_shop_item_by_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {


            return true;
        }
        else if(id == R.id.action_cart)
        {
            cartsButtonClick();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }



    void cartsButtonClick()
    {
        Intent intent = new Intent(this, CartsListActivity.class);
        startActivity(intent);
    }



    void fabClick(View view)
    {
        Snackbar.make(view, "SnackBar message !", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

        Palette palette = Palette.from(bitmap).generate();

        int color = 323235;
        int vibrant = palette.getVibrantColor(color);
        int vibrantLight = palette.getLightVibrantColor(color);
        int vibrantDark = palette.getDarkVibrantColor(color);
        int muted = palette.getMutedColor(color);
        int mutedLight = palette.getLightMutedColor(color);
        int mutedDark = palette.getDarkMutedColor(color);

        Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();

        //if(vibrantSwatch!=null) {
        //  originalTitle.setTextColor(vibrantSwatch.getTitleTextColor());
        //}




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //getWindow().setStatusBarColor(muted);
        }


      //fab.setBackgroundColor(vibrantDark);

        //originalTitle.setBackgroundColor(vibrantDark);

        if(appBarLayout!=null)
        {
            //appBarLayout.setBackgroundColor(vibrantDark);
        }
        if(toolbar!=null)
        {
            //toolbar.setBackgroundColor(mutedDark);
        }

    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }

    @Override
    public void notifySwipeRight() {

        mViewPager.setCurrentItem(1,false);
    }

    @Override
    public void notifyFilledCartsChanged() {

        Fragment fragment = (Fragment)pagerAdapter.instantiateItem(mViewPager,1);

        if(fragment instanceof NotifyFillCartsChanged)
        {
            ((NotifyFillCartsChanged)fragment).notifyFilledCartsChanged();
        }
    }

    @Override
    public void NotifyTitleChanged(String title, int tabPosition) {
        pagerAdapter.setTitle(title,tabPosition);
    }



    @Override
    public void notifyNewCartsChanged() {


        Fragment fragment = (Fragment)pagerAdapter.instantiateItem(mViewPager,0);

            if(fragment instanceof NotifyNewCartsChanged)
            {
                ((NotifyNewCartsChanged)fragment).notifyNewCartsChanged();
            }
    }


    @Override
    public void NotifyLogin() {

        notifyNewCartsChanged();
        notifyFilledCartsChanged();
    }




    @Override
    public void notifySortChanged() {

        Fragment fragment = (Fragment)pagerAdapter.instantiateItem(mViewPager,mViewPager.getCurrentItem());

        if(fragment instanceof NotifySort)
        {
            ((NotifySort)fragment).notifySortChanged();
        }

    }


}
