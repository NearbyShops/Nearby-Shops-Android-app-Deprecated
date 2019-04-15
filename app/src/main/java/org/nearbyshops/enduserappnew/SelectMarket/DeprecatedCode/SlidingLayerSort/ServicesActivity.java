package org.nearbyshops.enduserappnew.SelectMarket.DeprecatedCode.SlidingLayerSort;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.wunderlist.slidinglayer.SlidingLayer;


import org.nearbyshops.enduserappnew.Interfaces.NotifySearch;
import org.nearbyshops.enduserappnew.Interfaces.ToggleFab;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.SelectMarket.MarketsFragment;
import org.nearbyshops.enduserappnew.SelectMarket.SubmitURLDialog;
import org.nearbyshops.enduserappnew.ShopsByCategory.Interfaces.NotifySort;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;






public class ServicesActivity extends AppCompatActivity implements NotifySort, ToggleFab {




    @BindView(R.id.slidingLayer) SlidingLayer slidingLayer;
    public static final String TAG_SLIDING_LAYER = "sliding_layer";

    public static final String TAG_FRAGMENT = "tag_fragment";

    @BindView(R.id.fab) FloatingActionButton fab;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//            }
//        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT) == null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,new MarketsFragment(),TAG_FRAGMENT)
                    .commit();
        }

        setupSlidingLayer();
    }


    @OnClick(R.id.fab)
    void fabClick()
    {
        showDialogSubmitURL();
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


            if (getSupportFragmentManager().findFragmentByTag(TAG_SLIDING_LAYER)==null)
            {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.slidinglayerfragment,new SlidingLayerSortServices(),TAG_SLIDING_LAYER)
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
    public void notifySortChanged() {

        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag(TAG_FRAGMENT);

        if(fragment instanceof NotifySort)
        {
            ((NotifySort)fragment).notifySortChanged();
        }

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_service_activity, menu);


        // Associate searchable configuration with the SearchView
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.action_search), new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {

//                Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);

                Fragment fragment = getSupportFragmentManager()
                        .findFragmentByTag(TAG_FRAGMENT);


                if(fragment instanceof NotifySearch)
                {
                    ((NotifySearch) fragment).endSearchMode();
                }

//                Toast.makeText(OrderHistoryHD.this, "onCollapsed Called ", Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        return true;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }


    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow

//            Toast.makeText(this,query,Toast.LENGTH_SHORT).show();

//            Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);

            Fragment fragment = getSupportFragmentManager()
                    .findFragmentByTag(TAG_FRAGMENT);

            if(fragment instanceof NotifySearch)
            {
                ((NotifySearch) fragment).search(query);
            }
        }
    }




    private void showDialogSubmitURL()
    {
        FragmentManager fm = getSupportFragmentManager();
        SubmitURLDialog submitURLDialog = new SubmitURLDialog();
        submitURLDialog.show(fm,"serviceUrl");
    }


    @Override
    public void showFab() {
        fab.animate().translationY(0);
    }

    @Override
    public void hideFab() {
        fab.animate().translationY(fab.getHeight()+50);
    }
}
