package org.nearbyshops.enduserappnew.ItemsInShop;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.wunderlist.slidinglayer.SlidingLayer;

import org.nearbyshops.enduserappnew.ItemsInShop.ShopItems.FragmentItemsInShop;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ItemsInShopByCat.SlidingLayerSort.SlidingLayerSortItemsInShop;
import org.nearbyshops.enduserappnew.ShopsByCategoryOld.Interfaces.NotifySort;
import org.nearbyshops.enduserappnew.ShopsByCategoryOld.Interfaces.NotifyTitleChanged;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ItemsInShop extends AppCompatActivity implements NotifyTitleChanged, NotifySort{


    public static final String TAG_SHOP_ITEM_FRAGMENT = "shop_item_fragment";
    public static final String TAG_SLIDING_LAYER = "sliding_layer";

    @BindView(R.id.slidingLayer)
    SlidingLayer slidingLayer;

    @BindView(R.id.shop_count_indicator)
    TextView shopsCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_in_shop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/



        if(getSupportFragmentManager().findFragmentByTag(TAG_SHOP_ITEM_FRAGMENT)==null)
        {
            // add fragment to the activity
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, FragmentItemsInShop.newInstance(),TAG_SHOP_ITEM_FRAGMENT)
                    .commit();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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



            if(getSupportFragmentManager().findFragmentByTag(TAG_SLIDING_LAYER)==null)
            {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.slidinglayerfragment,new SlidingLayerSortItemsInShop(),TAG_SLIDING_LAYER)
                        .commit();
            }
        }

    }



    @OnClick({R.id.icon_sort,R.id.text_sort})
    void sortClick()
    {
        slidingLayer.openLayer(true);
    }


    @Override
    public void NotifyTitleChanged(String title, int tabPosition) {
        shopsCount.setText(title);
    }

    @Override
    public void titleChanged(int i, int size, int item_count) {

    }


    @Override
    public void notifySortChanged() {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_SHOP_ITEM_FRAGMENT);

        if(fragment instanceof NotifySort)
        {
            ((NotifySort)fragment).notifySortChanged();
        }
    }



}
