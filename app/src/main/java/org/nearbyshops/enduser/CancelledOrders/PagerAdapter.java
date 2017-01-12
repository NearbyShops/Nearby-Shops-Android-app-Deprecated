package org.nearbyshops.enduser.CancelledOrders;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.nearbyshops.enduser.CancelledOrders.CancelledByEndUser.FragmentCancelledByUser;
import org.nearbyshops.enduser.CancelledOrders.CancelledByShop.FragmentCancelledByShop;
import org.nearbyshops.enduser.CancelledOrders.ReturnedByDeliveryGuy.FragmentReturnedByDG;


/**
 * Created by sumeet on 15/11/16.
 */

public class PagerAdapter extends FragmentPagerAdapter {


    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a AccountsSelectionFragment (defined as a static inner class below).

        if(position==0)
        {
            return FragmentReturnedByDG.newInstance();

        }else if(position == 1)
        {
            return FragmentCancelledByShop.newInstance();
        }
        else if(position==2)
        {
            return FragmentCancelledByUser.newInstance();
        }

        return null;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {

            case 0:
                return titleReturnedByDG;
            case 1:
                return titleCancelledByShop;
            case 2:
                return titleCancelledByEndUser;
        }
        return null;
    }



    private String titleReturnedByDG = "Returned (0/0)";
    private String titleCancelledByShop = "Cancelled By Shop (0/0)";
    private String titleCancelledByEndUser = "Cancelled By User (0/0)";


    public void setTitle(String title, int tabPosition)
    {
        if(tabPosition == 0){

            titleReturnedByDG = title;
        }
        else if (tabPosition == 1)
        {
            titleCancelledByShop = title;

        }
        else if(tabPosition == 2)
        {
            titleCancelledByEndUser = title;
        }

        notifyDataSetChanged();
    }



}
