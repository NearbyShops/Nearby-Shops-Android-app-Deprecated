package org.nearbyshops.enduserappnew.OrderHistoryNew;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.wunderlist.slidinglayer.SlidingLayer;

import org.nearbyshops.enduserappnew.Interfaces.NotifySearch;
import org.nearbyshops.enduserappnew.LoginPlaceholder.FragmentSignInMessage;
import org.nearbyshops.enduserappnew.OrderHistoryHD.OrderHistoryHD.Interfaces.RefreshFragment;
import org.nearbyshops.enduserappnew.OrderHistoryHD.OrderHistoryHD.PagerAdapter;
import org.nearbyshops.enduserappnew.OrderHistoryNew.SlidingLayerSort.SlidingLayerSortOrders;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ShopsByCategory.Interfaces.NotifySort;
import org.nearbyshops.enduserappnew.ShopsByCategory.Interfaces.NotifyTitleChanged;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class OrderHistoryNew extends AppCompatActivity {


    private PagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

//    @BindView(R.id.slidingLayer)
//    SlidingLayer slidingLayer;

    public static final String TAG_SLIDING_LAYER = "sliding_layer";

    public static final String IS_FILTER_BY_SHOP = "IS_FILTER_BY_SHOP";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_new);
        ButterKnife.bind(this);


        if (getSupportFragmentManager().findFragmentByTag("order_history_fragment") == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new OrdersFragmentNew(), "order_history_fragment")
                    .commit();
        }

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_order_history_new, menu);


        // Associate searchable configuration with the SearchView
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));



        MenuItem item = menu.findItem(R.id.action_search);

        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {


                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);


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

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);



            if(fragment instanceof NotifySearch)
            {
                ((NotifySearch) fragment).search(query);
            }
        }
    }



//    @Override
    public void NotifyLogin() {


        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if(fragment instanceof RefreshFragment)
        {
            ((RefreshFragment) fragment).refreshFragment();
        }
    }


}
