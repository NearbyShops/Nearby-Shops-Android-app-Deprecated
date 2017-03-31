package org.nearbyshops.enduserapp.ItemImageSlider;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.nearbyshops.enduserapp.R;

public class ItemImagesFullscreen extends AppCompatActivity {

    public static final String TAG_FRAGMENT = "fragment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_images_fullscreen);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        if(getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT)==null)
        {
            // add fragment to the activity
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container,new ItemImageFullscreenFragment(),TAG_FRAGMENT)
                    .commit();
        }


//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }




}
