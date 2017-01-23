package org.nearbyshops.enduser.Settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.nearbyshops.enduser.R;


public class SettingsCustom extends AppCompatActivity {

    public static final String TAG_FRAGMENT = "TAG_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_custom);

        if(getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT)==null)
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container,new FragmentSettingsCustom(),TAG_FRAGMENT)
                    .commit();
        }
    }



}
