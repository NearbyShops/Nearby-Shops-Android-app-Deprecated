package org.nearbyshops.enduserapp.ShopItemByItem;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.nearbyshops.enduserapp.Model.Item;
import org.nearbyshops.enduserapp.ShopItemByItem.FilledCarts.FilledCartsFragment;
import org.nearbyshops.enduserapp.ShopItemByItem.NewCarts.NewCartsFragment;

/**
 * Created by sumeet on 25/5/16.
 */
public class PagerAdapterShopItem extends FragmentStatePagerAdapter{

    private Item item = null;

    private FilledCartsFragment filledCartsFragment;
    private NewCartsFragment newCartsFragment;


    public PagerAdapterShopItem(FragmentManager fm, Item item) {
        super(fm);
        this.item = item;
    }

    public PagerAdapterShopItem(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if(position==0)
        {

            filledCartsFragment = FilledCartsFragment.newInstance(item);

//            filledCartsFragment.setNotifyPagerAdapter(this);

            return filledCartsFragment;

        }else if(position==1)
        {

            newCartsFragment = NewCartsFragment.newInstance(item);

//            newCartsFragment.setNotifyPagerAdapter(this);


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

/*
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


                return ("Filled Carts (" + String.valueOf(filledCartsTotal) + ")");


            case 1:

                int newCartsTotal = 0 ;

                if(newCartsFragment!=null)
                {
                    if(newCartsFragment.dataset!=null)
                    {
                        newCartsTotal = newCartsFragment.dataset.size();
                    }

                }

                return ("New Carts (" + String.valueOf(newCartsTotal) + ")");

            case 2:
                return " Map";

        }
        return null;
    }
*/



   // public void notifyAddToCart() {

//        filledCartsFragment.notifyCartDataChanged();

  //      this.notifyDataSetChanged();
//    /}


    //boolean isNewCartCallbackFired = false;
    //boolean isFilledCartCallbackFired = false;

/*
    boolean flag = true;

    @Override
    public void notifyFilledCartsChanged() {



        if (flag) {

            flag = false;

            newCartsFragment.onResume();

        } else
        {
            flag = true;
        }



        notifyDataSetChanged();



        //if(isNewCartCallbackFired == false) {



        //    isNewCartCallbackFired = false;
        //}

        //isFilledCartCallbackFired = true;

    }
*/


/*
    @Override
    public void notifyNewCartsChanged() {

        if(flag)
        {

            flag = false;

            filledCartsFragment.onResume();

        }else
        {
            flag = true;
        }


        notifyDataSetChanged();



        //if(isFilledCartCallbackFired){



        //    isFilledCartCallbackFired = false;
        //}


        //isNewCartCallbackFired = true;
    }
*/



    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {

            case 0:
                return titleFilledCarts;
            case 1:
                return titleNewCarts;
            case 2:
                return titleDetachedItemCategories;
            case 3:
                return titleDetachedItems;
        }
        return null;
    }



    private String titleFilledCarts = "Filled Carts (0/0)";
    private String titleNewCarts = "New Carts (0/0)";
    private String titleDetachedItemCategories = "Detached Item-Categories (0/0)";
    private String titleDetachedItems = "Detached Items (0/0)";


    public void setTitle(String title, int tabPosition)
    {
        if(tabPosition == 0){

            titleFilledCarts = title;
        }
        else if (tabPosition == 1)
        {

            titleNewCarts = title;
        }else if(tabPosition == 2)
        {
            titleDetachedItemCategories = title;

        }else if(tabPosition == 3)
        {
            titleDetachedItems = title;
        }


        notifyDataSetChanged();
    }



}
