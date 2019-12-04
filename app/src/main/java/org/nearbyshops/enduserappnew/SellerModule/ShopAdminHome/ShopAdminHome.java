package org.nearbyshops.enduserappnew.SellerModule.ShopAdminHome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.nearbyshops.enduserappnew.R;


public class ShopAdminHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_admin_home);




        if(savedInstanceState==null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,new ShopAdminHomeFragment())
                    .commitNow();
        }

    }



}
