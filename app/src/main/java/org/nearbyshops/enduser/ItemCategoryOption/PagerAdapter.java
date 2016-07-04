package org.nearbyshops.enduser.ItemCategoryOption;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.nearbyshops.enduser.ItemCategoryOption.Interfaces.NotifyCategoryChanged;
import org.nearbyshops.enduser.ItemCategoryOption.Interfaces.NotifyTitleChanged;
import org.nearbyshops.enduser.Model.ItemCategory;

/**
 * Created by sumeet on 25/5/16.
 */
public class PagerAdapter extends FragmentPagerAdapter implements NotifyTitleChanged, NotifyCategoryChanged{

    ItemCategory itemCategory = null;


    FragmentItemCategories fragmentItemCategories = new FragmentItemCategories();

    FragmentItem fragmentItem = FragmentItem.newInstance(itemCategory);

    FragmentShop fragmentShop = FragmentShop.newInstance(2,itemCategory);



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


            return fragmentItemCategories;

        }else if(position==1)
        {

            return fragmentItem;



        }else if(position == 2)
        {

            return fragmentShop;
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

//
//        String categoryName = "";
//
//        if(itemCategory!=null)
//        {
//            categoryName = itemCategory.getCategoryName();
//        }

        switch (position) {

            case 0:

                return titleTabZero ;

            case 1:

                // categoryName + " Items"
                return titleTabOne ;

            case 2:

                // categoryName + " Shops"

                return titleTabTwo;
        }
        return null;
    }


    String titleTabZero = "";
    String titleTabOne = "";
    String titleTabTwo = "";

    String categoryName = "";



    @Override
    public void titleChanged(int tabPosition, int currentItemCount, int totalItemCount) {

        if(tabPosition==0)
        {
            titleTabZero = "Sub-Categories " + "(" + currentItemCount + ")";

        }else if(tabPosition == 1)
        {
            titleTabOne = categoryName + " Items " + "(" + currentItemCount + "/" + totalItemCount + ")";
        }
        else if (tabPosition == 2)
        {
            titleTabTwo = categoryName + " Shops" + "(" + currentItemCount + "/" + totalItemCount + ")";
        }


        notifyDataSetChanged();

    }


    @Override
    public void categoryChanged(ItemCategory currentCategory, boolean isBackPressed) {

        if(currentCategory.getCategoryName()==null)
        {
            categoryName = "";
        }
        else
        {
            categoryName = currentCategory.getCategoryName();
        }

    }

    @Override
    public void notifySwipeToRight() {

    }

}
