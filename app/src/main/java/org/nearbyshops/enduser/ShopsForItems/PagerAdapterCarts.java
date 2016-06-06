package org.nearbyshops.enduser.ShopsForItems;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.nearbyshops.enduser.Model.Item;
import org.nearbyshops.enduser.Model.ItemCategory;
import org.nearbyshops.enduser.ShopItemsByItemCategory.ItemFragment;
import org.nearbyshops.enduser.ShopItemsByItemCategory.ShopFragment;

/**
 * Created by sumeet on 25/5/16.
 */
public class PagerAdapterCarts extends FragmentPagerAdapter implements AdapterNewCarts.NotifyCallbacks{

    Item item = null;

    FilledCartsFragment filledCartsFragment;
    NewCartsFragment newCartsFragment;


    public PagerAdapterCarts(FragmentManager fm, Item item) {
        super(fm);
        this.item = item;
    }

    public PagerAdapterCarts(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if(position==0)
        {

            filledCartsFragment = FilledCartsFragment.newInstance(item);



            return filledCartsFragment;

        }else if(position==1)
        {

            newCartsFragment = NewCartsFragment.newInstance(item);

            newCartsFragment.setNotifyCallbacks(this);


            return newCartsFragment;


        }else if(position == 2)
        {

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

                int filledCartsTotal = 0 ;

                if(filledCartsFragment!=null)
                {
                    if(filledCartsFragment.dataset!=null)
                    {
                        filledCartsTotal = filledCartsFragment.dataset.size();
                    }
                }


                return ("Filled Carts - " + String.valueOf(filledCartsTotal));


            case 1:

                int newCartsTotal = 0 ;

                if(newCartsFragment!=null)
                {
                    if(newCartsFragment.dataset!=null)
                    {
                        newCartsTotal = newCartsFragment.dataset.size();
                    }

                }

                return ("New Carts - " + String.valueOf(newCartsTotal));

            case 2:
                return " Map";

        }
        return null;
    }


    @Override
    public void notifyAddToCart() {

        filledCartsFragment.notifyCartDataChanged();

        this.notifyDataSetChanged();
    }
}
