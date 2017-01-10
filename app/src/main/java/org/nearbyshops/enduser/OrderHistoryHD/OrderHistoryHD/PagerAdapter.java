package org.nearbyshops.enduser.OrderHistoryHD.OrderHistoryHD;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.nearbyshops.enduser.OrderHistoryHD.OrderHistoryHD.Complete.CompleteOrdersFragment;
import org.nearbyshops.enduser.OrderHistoryHD.OrderHistoryHD.Pending.PendingOrdersFragment;


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
            return PendingOrdersFragment.newInstance();
        }
        else if(position==1)
        {
            return CompleteOrdersFragment.newInstance();
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
                return titlePlaced;
            case 1:
                return titleConfirmed;
            case 2:
                return "SECTION 3";
        }
        return null;
    }


    private String titlePlaced = "Pending(0/0)";
    private String titleConfirmed = "Complete(0/0)";


    public void setTitle(String title, int tabPosition)
    {
        if(tabPosition == 0){

            titlePlaced = title;
        }
        else if (tabPosition == 1)
        {

            titleConfirmed = title;
        }

        notifyDataSetChanged();
    }



}
