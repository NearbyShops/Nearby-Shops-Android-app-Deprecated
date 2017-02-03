package org.nearbyshops.enduserapp.OrdersCancelledPFS;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.nearbyshops.enduserapp.OrdersCancelledPFS.CancelledByShop.CancelledByShopFragmentPFS;
import org.nearbyshops.enduserapp.OrdersCancelledPFS.CancelledByUser.CancelledByUserFragmentPFS;


/**
 * Created by sumeet on 23/12/16.
 */


public class PagerAdapter extends FragmentPagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        if(position==0)
        {
            return CancelledByShopFragmentPFS.newInstance();
        }
        else if(position==1)
        {
            return CancelledByUserFragmentPFS.newInstance();
        }


        return null;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return titleCancelledByShop;
            case 1:
                return titleCancelledByUser;
            case 2:
                return "SECTION 3";
        }
        return null;
    }


    private String titleCancelledByShop = "Cancelled By Shop (0/0)";
    private String titleCancelledByUser = "Cancelled By User (0/0)";


    public void setTitle(String title, int tabPosition)
    {
        if(tabPosition == 0){

            titleCancelledByShop = title;
        }
        else if (tabPosition == 1)
        {

            titleCancelledByUser = title;
        }

        notifyDataSetChanged();
    }



}
