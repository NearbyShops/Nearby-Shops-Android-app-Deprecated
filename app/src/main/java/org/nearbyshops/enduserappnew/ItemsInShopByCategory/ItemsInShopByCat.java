package org.nearbyshops.enduserappnew.ItemsInShopByCategory;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.wunderlist.slidinglayer.SlidingLayer;
import org.nearbyshops.enduserappnew.Interfaces.NotifySearch;
import org.nearbyshops.enduserappnew.Interfaces.NotifySort;
import org.nearbyshops.enduserappnew.ItemsByCategory.Interfaces.NotifyBackPressed;
import org.nearbyshops.enduserappnew.ItemsInShopByCategory.Interfaces.NotifyIndicatorChanged;
import org.nearbyshops.enduserappnew.ItemsInShopByCategory.SlidingLayerSort.SlidingLayerSortItemsInShop;
import org.nearbyshops.enduserappnew.R;


public class ItemsInShopByCat extends AppCompatActivity{

    public static final String TAG_FRAGMENT = "items_in_stock_simple";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        ButterKnife.bind(this);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        if(getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT)==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container,new ItemsInShopByCatFragment(),TAG_FRAGMENT)
                    .commit();
        }
    }




    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }



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



}
