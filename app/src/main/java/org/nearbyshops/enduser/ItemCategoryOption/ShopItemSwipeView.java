package org.nearbyshops.enduser.ItemCategoryOption;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.RelativeLayout;

import com.wunderlist.slidinglayer.SlidingLayer;

import org.nearbyshops.enduser.ItemCategoryOption.Interfaces.NotifyBackPressed;
import org.nearbyshops.enduser.ItemCategoryOption.Interfaces.NotifyCategoryChanged;
import org.nearbyshops.enduser.ItemCategoryOption.Interfaces.NotifyTitleChanged;
import org.nearbyshops.enduser.Model.ItemCategory;
import org.nearbyshops.enduser.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

public class ShopItemSwipeView extends AppCompatActivity implements NotifyCategoryChanged, NotifyTitleChanged{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    public static final String ITEM_CATEGORY_INTENT_KEY = "itemCategoryIntentKey";
    ItemCategory itemCategory;


    public ItemCategory notifiedCurrentCategory;

    private PagerAdapter mSectionsPagerAdapter;

    @Bind(R.id.tabLayoutCat) TabLayout tabLayoutCat;

    @Bind(R.id.tablayout)
    TabLayout tabLayout;

    //@Bind(R.id.filter)
    //ImageView filter;

    SlidingLayer slidingLayer;

    RelativeLayout slidingContents;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    // Implemented Interfaces

    NotifyBackPressed notifyBackPressed;

    // Implemented Interfaces Ends

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_item_swipe_view);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        itemCategory = getIntent().getParcelableExtra(ITEM_CATEGORY_INTENT_KEY);
        mSectionsPagerAdapter = new PagerAdapter(getSupportFragmentManager(),itemCategory);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        tabLayout.setupWithViewPager(mViewPager);

        slidingLayer = (SlidingLayer)findViewById(R.id.slidingLayer);
        slidingContents = (RelativeLayout) findViewById(R.id.slidingContents);


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

        }

    }



    //@OnClick({R.id.filter,R.id.filterText})
    void filterIconClick(View view)
    {

        if(slidingLayer.isOpened())
        {

            slidingLayer.closeLayer(true);

        }else
        {

            slidingLayer.openLayer(true);
        }

    }


    void fabClick(View view)
    {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        if(slidingLayer.isClosed())
        {
            slidingLayer.openLayer(true);

        }
        else
        {
            slidingLayer.closeLayer(true);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shop_item_swipe_view, menu);

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

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }


    public NotifyBackPressed getNotifyBackPressed() {
        return notifyBackPressed;
    }

    public void setNotifyBackPressed(NotifyBackPressed notifyBackPressed) {
        this.notifyBackPressed = notifyBackPressed;
    }


    @Override
    public void onBackPressed() {


//        super.onBackPressed();

        if(notifyBackPressed!=null)
        {
            removeLastTab();

            if(notifyBackPressed.backPressed())
            {
                super.onBackPressed();

                Log.d("applog","Back pressed Not Null : True");

            }else
            {
                mViewPager.setCurrentItem(0,true);

                Log.d("applog","Back pressed Not Null : False");
            }


        }
        else
        {
            Log.d("applog","Back pressed Null !");
            super.onBackPressed();

        }
    }




    void insertTab(String categoryName)
    {

        if(categoryName==null)
        {
            return;
        }

        if(tabLayoutCat.getVisibility()==View.GONE)
        {
            tabLayoutCat.setVisibility(View.VISIBLE);
        }

        tabLayoutCat.addTab(tabLayoutCat.newTab().setText("" + categoryName + " : : "));
        tabLayoutCat.setScrollPosition(tabLayoutCat.getTabCount()-1,0,true);

    }

    void removeLastTab()
    {
        if(tabLayoutCat.getTabCount()>0)
        {
            tabLayoutCat.removeTabAt(tabLayoutCat.getTabCount()-1);
            tabLayoutCat.setScrollPosition(tabLayoutCat.getTabCount()-1,0,true);
        }


        if(tabLayoutCat.getTabCount()==0)
        {
//            tabLayoutCat.setVisibility(View.GONE);
        }
    }


//    List<NotifyCategoryChanged> notifyCategoryChangedList = new ArrayList<>();

    NotifyCategoryChanged notifyCategoryChangedItemFragemnt;
    NotifyCategoryChanged notifyCategoryChangedShopFragment;

    @Override
    public void categoryChanged(ItemCategory currentCategory, boolean isBackPressed) {

        // update category title tab strip

        if (!isBackPressed) {

            insertTab(currentCategory.getCategoryName());

        }


        ((NotifyCategoryChanged)mSectionsPagerAdapter).categoryChanged(currentCategory,isBackPressed);


        // forward update to the other fragments

        this.notifiedCurrentCategory = currentCategory;

        if(notifyCategoryChangedItemFragemnt!=null)
        {
            notifyCategoryChangedItemFragemnt.categoryChanged(currentCategory,isBackPressed);
        }

        if(notifyCategoryChangedShopFragment!=null)
        {
            notifyCategoryChangedShopFragment.categoryChanged(currentCategory,isBackPressed);
        }
    }

    @Override
    public void notifySwipeToRight() {

        mViewPager.setCurrentItem(1);
    }


    @Override
    public void titleChanged(int tabPosition, int currentItemCount, int totalItemCount) {

        ((NotifyTitleChanged)mSectionsPagerAdapter).titleChanged(tabPosition,currentItemCount,totalItemCount);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Icepick.restoreInstanceState(this,savedInstanceState);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this,outState);
    }
}