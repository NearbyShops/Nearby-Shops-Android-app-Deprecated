package org.nearbyshops.enduserappnew.ItemsByCategoryHorizontal;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.wunderlist.slidinglayer.SlidingLayer;
import org.nearbyshops.enduserappnew.Items.SlidingLayerSort.SlidingLayerSortItems;
import org.nearbyshops.enduserappnew.ItemsByCategoryHorizontal.ItemCategories.ItemCategoriesHorizontal;
import org.nearbyshops.enduserappnew.ItemsByCategoryHorizontal.Items.FragmentItemScreenHorizontal;
import org.nearbyshops.enduserappnew.Model.ItemCategory;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ShopsByCategoryOld.Interfaces.NotifyBackPressed;
import org.nearbyshops.enduserappnew.ShopsByCategoryOld.Interfaces.NotifyCategoryChanged;
import org.nearbyshops.enduserappnew.ShopsByCategoryOld.Interfaces.NotifyGeneral;
import org.nearbyshops.enduserappnew.ShopsByCategoryOld.Interfaces.NotifySort;
import org.nearbyshops.enduserappnew.ShopsByCategoryOld.Interfaces.NotifyTitleChanged;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ItemsByCatS2 extends AppCompatActivity implements NotifyCategoryChanged , NotifySort, NotifyGeneral, NotifyTitleChanged{



    @BindView(R.id.tablayout)
    TabLayout tabLayout;

    LinearLayoutManager linearLayoutManager;

    @BindView(R.id.slidingLayer)
    SlidingLayer slidingLayer;


    public final String TAG_ITEM_CAT_FRAGMENT = "item_category_fragment";
    public final String TAG_ITEM_FRAGMENT = "item_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_by_cat_s2);
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


        if(getSupportFragmentManager().findFragmentByTag(TAG_ITEM_CAT_FRAGMENT) == null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.item_categories_container,new ItemCategoriesHorizontal(),TAG_ITEM_CAT_FRAGMENT)
                    .commit();
        }


        if(getSupportFragmentManager().findFragmentByTag(TAG_ITEM_FRAGMENT)==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.item_fragment_container,new FragmentItemScreenHorizontal(),TAG_ITEM_FRAGMENT)
                    .commit();
        }



        insertTab("Root");


        setupSlidingLayer();
    }


    void setupSlidingLayer()
    {

        ////slidingLayer.setShadowDrawable(R.drawable.sidebar_shadow);
        //slidingLayer.setShadowSizeRes(R.dimen.shadow_size);

        if(slidingLayer!=null)
        {
            slidingLayer.setChangeStateOnTap(true);
            slidingLayer.setSlidingEnabled(true);
//            slidingLayer.setPreviewOffsetDistance(15);
            slidingLayer.setOffsetDistance(30);
            slidingLayer.setStickTo(SlidingLayer.STICK_TO_RIGHT);

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(250, ViewGroup.LayoutParams.MATCH_PARENT);

            //slidingContents.setLayoutParams(layoutParams);

            //slidingContents.setMinimumWidth(metrics.widthPixels-50);


            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.slidinglayerfragment,new SlidingLayerSortItems())
                    .commit();

        }

    }




    @Override
    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_ITEM_CAT_FRAGMENT);

        if(fragment instanceof NotifyBackPressed)
        {

            if(((NotifyBackPressed)fragment).backPressed())
            {
                super.onBackPressed();
            }

        }

    }





    @Override
    public void itemCategoryChanged(ItemCategory currentCategory, Boolean isBackPressed) {

//        Log.d("applog","Item Category Changed : " + currentCategory.getCategoryName() + " : " + String.valueOf(currentCategory.getItemCategoryID()));

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_ITEM_FRAGMENT);

        if(fragment instanceof NotifyCategoryChanged)
        {
            ((NotifyCategoryChanged)fragment).itemCategoryChanged(currentCategory,isBackPressed);
        }

    }






    @OnClick({R.id.icon_sort,R.id.text_sort})
    void sortClick()
    {
        slidingLayer.openLayer(true);
    }



    @BindView(R.id.item_categories_container)
    FrameLayout itemCategoriesContainer;

    @BindView(R.id.icon_sub)
    ImageView expandIcon;

    @OnClick({R.id.icon_sub,R.id.text_sub})
    void subClick()
    {


        Drawable remove = VectorDrawableCompat
                .create(getResources(),
                        R.drawable.ic_remove_white_24px, getTheme());


        Drawable add = VectorDrawableCompat
                .create(getResources(),
                        R.drawable.ic_add_white_24px, getTheme());


        if(itemCategoriesContainer.getVisibility() == View.GONE)
        {
            itemCategoriesContainer.setVisibility(View.VISIBLE);
            expandIcon.setImageDrawable(remove);

        }
        else if(itemCategoriesContainer.getVisibility() == View.VISIBLE)
        {
            itemCategoriesContainer.setVisibility(View.GONE);
            expandIcon.setImageDrawable(add);
        }

    }


    @Override
    public void notifySortChanged() {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_ITEM_FRAGMENT);

        if(fragment instanceof NotifySort)
        {
            ((NotifySort)fragment).notifySortChanged();
        }
    }



    @Override
    public void removeLastTab() {

        if(tabLayout.getTabCount()<=1)
        {
            return;
        }

        tabLayout.removeTabAt(tabLayout.getTabCount()-1);
        tabLayout.setScrollPosition(tabLayout.getTabCount()-1,0,true);

        if(tabLayout.getTabCount()==0)
        {
//            tabLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void notifySwipeToright() {

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






    @BindView(R.id.text_sub)
    TextView textSub;

    @BindView(R.id.shop_count_indicator)
    TextView shopCountIndicator;


    @Override
    public void NotifyTitleChanged(String title, int tabPosition) {

        if(tabPosition==0)
        {
            // title for item Categories
            textSub.setText(title);

        }
        else if(tabPosition==1)
        {
            // title for items
            shopCountIndicator.setText(title);
        }

    }

    @Override
    public void titleChanged(int i, int size, int item_count) {

    }
}
