package org.nearbyshops.enduserappnew.ImageSlider;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;


import org.nearbyshops.enduserappnew.Model.ItemImage;
import org.nearbyshops.enduserappnew.Preferences.UtilityFunctions;

import java.util.List;



/**
 * Created by sumeet on 23/4/17.
 */

public class PagerAdapter extends FragmentPagerAdapter {



    private List<Object> imagesList;


    public PagerAdapter(FragmentManager fm, List<Object> imagesList) {
        super(fm);

        this.imagesList = imagesList;
    }

    @Override
    public Fragment getItem(int position) {

        FragmentImage fragment = new FragmentImage();

        Bundle bundle = new Bundle();
        ItemImage taxiData = (ItemImage) imagesList.get(position);
        String jsonString = UtilityFunctions.provideGson()
                .toJson(taxiData);


        bundle.putString("taxi_image",jsonString);
        fragment.setArguments(bundle);



        showLogMessage("List Size : " + String.valueOf(imagesList.size()));

        return fragment;
    }



    void showLogMessage(String string)
    {
        Log.d("image_slider",string);
    }




    @Override
    public int getCount() {
        return imagesList.size();
    }




}
