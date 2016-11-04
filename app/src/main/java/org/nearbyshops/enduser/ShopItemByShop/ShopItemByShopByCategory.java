package org.nearbyshops.enduser.ShopItemByShop;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.wunderlist.slidinglayer.SlidingLayer;

import org.nearbyshops.enduser.ItemsByCategory.SlidingLayerSortItems;
import org.nearbyshops.enduser.Model.ItemCategory;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.ShopsByCategory.Interfaces.NotifyBackPressed;
import org.nearbyshops.enduser.ShopsByCategory.Interfaces.NotifyCategoryChanged;
import org.nearbyshops.enduser.ShopsByCategory.Interfaces.NotifyGeneral;
import org.nearbyshops.enduser.ShopsByCategory.Interfaces.NotifySort;
import org.nearbyshops.enduser.ShopsByCategory.Interfaces.NotifyTitleChanged;
import org.nearbyshops.enduser.ShopsByCategory.SlidingLayerSortShops_;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopItemByShopByCategory extends AppCompatActivity implements NotifyGeneral,
        NotifyTitleChanged, ViewPager.OnPageChangeListener, NotifyCategoryChanged, NotifySort {


    @Bind(R.id.slidinglayerfragment)
    FrameLayout slidingFragmentContainer;


    private PagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

//    public NotifyBackPressed notifyBackPressed;
//    public NotifyCategoryChanged notifyCategoryChanged;


    @Bind(R.id.appbar)
    AppBarLayout appBar;

    @Bind(R.id.tablayout)
    TabLayout tabLayout;

    @Bind(R.id.tablayoutPager)
    TabLayout tabLayoutPager;


    @Bind(R.id.slidingLayer)
    SlidingLayer slidingLayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_item_by_shop_by_category);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);
        tabLayoutPager.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(this);

        setupSlidingLayer();

        insertTab("Root");
    }



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



            if(getSupportFragmentManager().findFragmentByTag("sliding_layer")==null)
            {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.slidinglayerfragment,new SlidingLayerSortShopItemsByShop(),"sliding_layer")
                        .commit();
            }
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_item_categories_tabs, menu);
        return false;
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

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void NotifyTitleChanged(String title, int tabPosition) {

        mPagerAdapter.setTitle(title,tabPosition);
    }






    @Override
    public void onBackPressed() {

        Fragment fragment = (Fragment) mPagerAdapter.instantiateItem(mViewPager,0);

        if(fragment instanceof NotifyBackPressed)
        {

            if(((NotifyBackPressed)fragment).backPressed())
            {
                super.onBackPressed();
            }else
            {
//                mViewPager.setCurrentItem(0,true);
                mViewPager.setCurrentItem(0);
            }

        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }


    @Override
    public void itemCategoryChanged(ItemCategory currentCategory, Boolean isBackPressed) {

        Log.d("applog","Item Category Changed : " + currentCategory.getCategoryName() + " : " + String.valueOf(currentCategory.getItemCategoryID()));

        Fragment fragment = (Fragment) mPagerAdapter.instantiateItem(mViewPager,1);

        if(fragment instanceof NotifyCategoryChanged)
        {
            ((NotifyCategoryChanged)fragment).itemCategoryChanged(currentCategory,isBackPressed);
        }

    }

    @Override
    public void insertTab(String categoryName) {

        if(tabLayout.getVisibility()==View.GONE)
        {
            tabLayout.setVisibility(View.VISIBLE);
        }

        tabLayout.addTab(tabLayout.newTab().setText("" + categoryName + " : : "));
        tabLayout.setScrollPosition(tabLayout.getTabCount()-1,0,true);
    }

    @Override
    public void removeLastTab() {

        if(tabLayout.getTabCount()<=1)
        {
            return;
        }

        tabLayout.removeTabAt(tabLayout.getTabCount()-1);
        tabLayout.setScrollPosition(tabLayout.getTabCount()-1,0,true);
    }

    @Override
    public void notifySwipeToright() {

//        mViewPager.setCurrentItem(1,true);
        mViewPager.setCurrentItem(1);
    }




    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


    }

    @Override
    public void onPageSelected(int position) {

        if(position==0)
        {

        }else if(position == 1)
        {


        }
    }


    @OnClick({R.id.icon_sort,R.id.text_sort})
    void sortClick()
    {
        slidingLayer.openLayer(true);
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }



    @Override
    public void notifySortChanged() {

        if(mViewPager.getCurrentItem()==1)
        {
            Fragment fragment = (Fragment) mPagerAdapter.instantiateItem(mViewPager,mViewPager.getCurrentItem());

            if(fragment instanceof NotifySort)
            {
                ((NotifySort)fragment).notifySortChanged();
            }
        }
    }

}
