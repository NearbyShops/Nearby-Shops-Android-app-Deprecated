package org.nearbyshops.enduserappnew.ShopDetailNew;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.nearbyshops.enduserappnew.Interfaces.NotifySearch;
import org.nearbyshops.enduserappnew.OrderHistoryHD.OrderHistoryHD.Interfaces.RefreshFragment;
import org.nearbyshops.enduserappnew.OrderHistoryHD.OrderHistoryHD.PagerAdapter;
import org.nearbyshops.enduserappnew.OrderHistoryNew.OrdersFragmentNew;
import org.nearbyshops.enduserappnew.R;

import butterknife.ButterKnife;


public class ShopDetail extends AppCompatActivity {


    public static final String SHOP_DETAIL_INTENT_KEY = "shop_detail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail_new);
        ButterKnife.bind(this);



        if (getSupportFragmentManager().findFragmentByTag("shop_detail_fragment") == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ShopDetailFragment(), "shop_detail_fragment")
                    .commit();
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
