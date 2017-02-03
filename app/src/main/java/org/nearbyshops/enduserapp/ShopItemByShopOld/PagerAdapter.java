package org.nearbyshops.enduserapp.ShopItemByShopOld;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.nearbyshops.enduserapp.ShopItemByShopOld.ItemCategories.ItemCategoriesFragmentShopHome;
import org.nearbyshops.enduserapp.ShopItemByShopOld.ShopItems.FragmentShopItemsByShop;

/**
 * Created by sumeet on 27/6/16.
 */

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {

//    DetachedTabs activity;

    public PagerAdapter(FragmentManager fm) {
        super(fm);

//        this.activity = activity;
    }


//    private ItemCategoriesFragmentSimple itemCategoriesFragment = new ItemCategoriesFragmentSimple();
//    private FragmentItemsEditor fragmentItemsEditor = FragmentItemsEditor.newInstance(FragmentItemsEditor.MODE_RECENTLY_ADDED);

//    private FragmentShopTwo fragmentShop = new FragmentShopTwo();

    private ItemCategoriesFragmentShopHome itemCategoriesFragment = new ItemCategoriesFragmentShopHome();
    private FragmentShopItemsByShop fragmentShop = new FragmentShopItemsByShop();

//    private ItemRemakeFragment itemRemakeFragment = new ItemRemakeFragment();;

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).



        if(position == 0)
        {
//            activity.setNotificationReceiver(itemCategoriesFragment);

            return itemCategoriesFragment;
        }
        else if (position == 1)
        {

            return fragmentShop;
        }


        return new ItemCategoriesFragmentShopHome();
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
                return titleCategories;
            case 1:
                return titleItems;
            case 2:
                return titleDetachedItemCategories;
            case 3:
                return titleDetachedItems;
        }
        return null;
    }



    private String titleCategories = "SubCategories(0/0)";
    private String titleItems = "ShopItems(0/0)";
    private String titleDetachedItemCategories = "Detached Item-Categories (0/0)";
    private String titleDetachedItems = "Detached Items (0/0)";


    public void setTitle(String title, int tabPosition)
    {
        if(tabPosition == 0){

            titleCategories = title;
        }
        else if (tabPosition == 1)
        {

            titleItems = title;
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