package org.nearbyshops.enduserapp.SelectService;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.nearbyshops.enduserapp.R;


public class SelectService extends AppCompatActivity {

    public static final String TAG_FRAGMENT = "TAG_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_custom);

        if(getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT)==null)
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container,new FragmentSelectService(),TAG_FRAGMENT)
                    .commit();
        }


    }



}
