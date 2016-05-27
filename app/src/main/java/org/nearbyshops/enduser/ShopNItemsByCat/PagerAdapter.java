package org.nearbyshops.enduser.ShopNItemsByCat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.nearbyshops.enduser.Model.ItemCategory;

/**
 * Created by sumeet on 25/5/16.
 */
public class PagerAdapter extends FragmentPagerAdapter{

    ItemCategory itemCategory = null;

    public PagerAdapter(FragmentManager fm, ItemCategory itemCategory) {
        super(fm);
        this.itemCategory = itemCategory;
    }

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if(position==0)
        {

            return ItemFragment.newInstance(itemCategory);

        }else if(position==1)
        {

            return ShopFragment.newInstance(position + 1,itemCategory);

        }else if(position == 2)
        {
            return ShopFragment.newInstance(position + 1,itemCategory);
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

        String categoryName = itemCategory.getCategoryName();

        switch (position) {

            case 0:

                return categoryName + " Items" ;
            case 1:
                return categoryName + " Shops";

            case 2:
                return " Map";

        }
        return null;
    }

}
