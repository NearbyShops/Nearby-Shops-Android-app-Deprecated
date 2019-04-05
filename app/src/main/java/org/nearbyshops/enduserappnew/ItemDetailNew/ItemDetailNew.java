package org.nearbyshops.enduserappnew.ItemDetailNew;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import org.nearbyshops.enduserappnew.OrderHistoryHD.OrderHistoryHD.Interfaces.RefreshFragment;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.ShopDetailNew.ShopDetailFragment;

import butterknife.ButterKnife;


public class ItemDetailNew extends AppCompatActivity {


    public static final String SHOP_DETAIL_INTENT_KEY = "item_detail";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail_new);
        ButterKnife.bind(this);


        if (getSupportFragmentManager().findFragmentByTag("item_detail_fragment") == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ItemDetailFragment(), "item_detail_fragment")
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
