package org.nearbyshops.enduser.Shops;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.wunderlist.slidinglayer.SlidingLayer;

import org.nearbyshops.enduser.Model.Shop;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.Shops.Interfaces.GetDataset;
import org.nearbyshops.enduser.Shops.Interfaces.NotifyDatasetChanged;
import org.nearbyshops.enduser.Shops.ListFragment.FragmentShopTwo;
import org.nearbyshops.enduser.Shops.MapsFragment.ShopMapFragment;
import org.nearbyshops.enduser.ShopsByCategory.Interfaces.NotifySort;
import org.nearbyshops.enduser.ShopsByCategory.Interfaces.NotifyTitleChanged;
import org.nearbyshops.enduser.ShopsByCategory.SlidingLayerSortShopsByCategory;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;

public class ShopsActivity extends AppCompatActivity implements NotifyTitleChanged,
        NotifySort , GetDataset{

    @State
    boolean isMapView = false;

    @State
    ArrayList<Shop> dataset = new ArrayList<>();

    @Bind(R.id.shop_count_indicator)
    TextView shopsCount;

    @Bind(R.id.slidingLayer)
    SlidingLayer slidingLayer;

//    FragmentShopTwo fragmentShopTwo;
//    private ShopMapFragment shopMapFragment;

    public static final String TAG_SHOP_FRAGMENT = "shop_fragment";
    public static final String TAG_MAP_FRAGMENT = "map_tag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops);
        ButterKnife.bind(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/



        if(getSupportFragmentManager().findFragmentByTag(TAG_SHOP_FRAGMENT)==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, FragmentShopTwo.newInstance(false),TAG_SHOP_FRAGMENT)
                    .commit();

        }




        if(savedInstanceState == null)
        {

            /*fragmentShopTwo = FragmentShopTwo.newInstance(false);
            // first run for the activity
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, fragmentShopTwo,"shop_fragment")
                    .commit();*/
        }
        else
        {
            onRestoreInstanceState(savedInstanceState);

            Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_SHOP_FRAGMENT);

            if(fragment instanceof NotifyDatasetChanged)
            {
                ((NotifyDatasetChanged)fragment).notifyDatasetChanged();
            }
        }



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupSlidingLayer();
    }



    void setupSlidingLayer()
    {

        ////slidingLayer.setShadowDrawable(R.drawable.sidebar_shadow);
        //slidingLayer.setShadowSizeRes(R.dimen.shadow_size);

        if(slidingLayer!=null) {
            slidingLayer.setChangeStateOnTap(true);
            slidingLayer.setSlidingEnabled(true);
            slidingLayer.setPreviewOffsetDistance(15);
            slidingLayer.setOffsetDistance(10);
            slidingLayer.setStickTo(SlidingLayer.STICK_TO_RIGHT);

//            DisplayMetrics metrics = new DisplayMetrics();
//            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(250, ViewGroup.LayoutParams.MATCH_PARENT);

            //slidingContents.setLayoutParams(layoutParams);

            //slidingContents.setMinimumWidth(metrics.widthPixels-50);


            if (getSupportFragmentManager().findFragmentByTag("sliding_layer")==null)
            {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.slidinglayerfragment,new SlidingLayerSortShopsByCategory(),"sliding_layer")
                        .commit();
            }

        }

    }


    @OnClick({R.id.icon_sort,R.id.text_sort})
    void sortClick()
    {
        slidingLayer.openLayer(true);
    }


    @Bind(R.id.icon_list)
    ImageView mapIcon;


    @OnClick({R.id.icon_list,R.id.text_list})
    void listMapClick()
    {

        if(isMapView)
        {

            Drawable icon = VectorDrawableCompat
                    .create(getResources(),
                            R.drawable.ic_map_white_24px, getTheme());

            mapIcon.setImageDrawable(icon);


            /*if(fragmentShopTwo == null)
            {
                fragmentShopTwo = FragmentShopTwo.newInstance(true);
            }*/

//            fragmentShopTwo = FragmentShopTwo.newInstance();

            /*getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack("name")
                    .replace(R.id.fragment_container, new FragmentShopTwo().newInstance(true),"shop_fragment")
                    .commit();*/

//            getSupportFragmentManager().popBackStackImmediate();



            if(getSupportFragmentManager().findFragmentByTag(TAG_SHOP_FRAGMENT)==null)
            {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container, FragmentShopTwo.newInstance(false),TAG_SHOP_FRAGMENT)
                        .commit();

            }
            else
            {
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(getSupportFragmentManager().findFragmentByTag("map_tag"))
                        .commit();


                getSupportFragmentManager()
                        .beginTransaction()
                        .show(getSupportFragmentManager().findFragmentByTag("shop_fragment"))
                        .commit();
            }



            isMapView = false;
        }
        else
        {


            Drawable icon = VectorDrawableCompat
                    .create(getResources(),
                            R.drawable.ic_view_list_white_24px, getTheme());



            mapIcon.setImageDrawable(icon);



            /*if(shopMapFragment == null)
            {

            }*/

//            shopMapFragment = new ShopMapFragment();


            if(getSupportFragmentManager().findFragmentByTag("map_tag")==null)
            {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container, new ShopMapFragment(),"map_tag")
                        .commit();

            }
            else
            {

                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(getSupportFragmentManager().findFragmentByTag("shop_fragment"))
                        .commit();


                getSupportFragmentManager()
                        .beginTransaction()
                        .show(getSupportFragmentManager().findFragmentByTag("map_tag"))
                        .commit();

            }


            //.addToBackStack("map")

            isMapView = true;
        }

    }








    @Override
    public void notifySortChanged() {

//        Fragment fragment = (Fragment) mPagerAdapter.instantiateItem(mViewPager,mViewPager.getCurrentItem());

//        Fragment fragment = getSupportFragmentManager().findFragmentByTag("shop_fragment");


        Fragment fragment = getSupportFragmentManager().findFragmentByTag("shop_fragment");

        if(fragment instanceof NotifySort)
        {
            ((NotifySort)fragment).notifySortChanged();
        }

    }


    @Override
    public void NotifyTitleChanged(String title, int tabPosition) {
        shopsCount.setText(title);
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }



    /*@Override
    public void setDataset(ArrayList<Shop> dataset) {

        this.dataset = dataset;
    }*/


    @Override
    public ArrayList<Shop> getDataset() {

        return dataset;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this,outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Icepick.restoreInstanceState(this,savedInstanceState);
    }
}
