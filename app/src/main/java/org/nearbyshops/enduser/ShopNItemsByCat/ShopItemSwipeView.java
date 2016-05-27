package org.nearbyshops.enduser.ShopNItemsByCat;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;

import com.wunderlist.slidinglayer.SlidingLayer;

import org.nearbyshops.enduser.Model.ItemCategory;
import org.nearbyshops.enduser.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopItemSwipeView extends AppCompatActivity {

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

    private PagerAdapter mSectionsPagerAdapter;

    @Bind(R.id.tablayout)
    TabLayout tabLayout;

    @Bind(R.id.filter)
    ImageView filter;

    SlidingLayer slidingLayer;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

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


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        tabLayout.setupWithViewPager(mViewPager);

        slidingLayer = (SlidingLayer)findViewById(R.id.slidingLayer);


        ////slidingLayer.setShadowDrawable(R.drawable.sidebar_shadow);
        //slidingLayer.setShadowSizeRes(R.dimen.shadow_size);

        slidingLayer.setChangeStateOnTap(false);
        slidingLayer.setSlidingEnabled(true);
        slidingLayer.setPreviewOffsetDistance(15);
        slidingLayer.setOffsetDistance(10);
        slidingLayer.setStickTo(SlidingLayer.STICK_TO_RIGHT);





    }




    @OnClick({R.id.filter,R.id.filterText})
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


    @OnClick(R.id.fab)
    void fabClick(View view)
    {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        if(slidingLayer.isClosed())
        {
            slidingLayer.openLayer(true);
        }else
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
}
