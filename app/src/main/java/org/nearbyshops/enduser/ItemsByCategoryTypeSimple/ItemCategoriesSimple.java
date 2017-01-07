package org.nearbyshops.enduser.ItemsByCategoryTypeSimple;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.wunderlist.slidinglayer.SlidingLayer;

import org.nearbyshops.enduser.ItemsByCategoryTypeSimple.Interfaces.NotifyBackPressed;
import org.nearbyshops.enduser.ItemsByCategoryTypeSimple.Interfaces.NotifyHeaderChanged;
import org.nearbyshops.enduser.ItemsByCategorySwipe.SlidingLayerSortItems;
import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.ShopsByCategoryOld.Interfaces.NotifySort;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ItemCategoriesSimple extends AppCompatActivity implements NotifyHeaderChanged,NotifySort{

    public static final String TAG_FRAGMENT = "item_categories_simple";
    public static final String TAG_SLIDING = "sort_items_sliding";


    @Bind(R.id.text_sub) TextView itemHeader;
    @Bind(R.id.slidingLayer) SlidingLayer slidingLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_categories_simple);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

     /*   FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
     */   getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        if(getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT)==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container,new ItemCategoriesFragmentSimple(),TAG_FRAGMENT)
                    .commit();
        }


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


            if(getSupportFragmentManager().findFragmentByTag(TAG_SLIDING)==null)
            {
                System.out.println("Item Cat Simple : New Sliding Layer Loaded !");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.slidinglayerfragment,new SlidingLayerSortItems(),TAG_SLIDING)
                        .commit();
            }
        }

    }






//    Fragment fragment = null;


    @Override
    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag(TAG_FRAGMENT);

        //notifyBackPressed !=null

        if(fragment instanceof NotifyBackPressed)
        {
            if(((NotifyBackPressed) fragment).backPressed())
            {
                super.onBackPressed();
            }
        }
        else
        {
            super.onBackPressed();
        }
    }


    @Override
    public void notifyItemHeaderChanged(String header) {
        itemHeader.setText(header);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.icon_sort,R.id.text_sort})
    void sortClick()
    {
        slidingLayer.openLayer(true);
    }


    @Override
    public void notifySortChanged() {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);

        if(fragment instanceof NotifySort)
        {
            ((NotifySort)fragment).notifySortChanged();
        }
    }
}
