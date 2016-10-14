package org.nearbyshops.enduser.ShopsByCategory.Interfaces;


import org.nearbyshops.enduser.Model.ItemCategory;

/**
 * Created by sumeet on 22/9/16.
 */

public interface NotifyCategoryChanged {

    void itemCategoryChanged(ItemCategory currentCategory, Boolean isBackPressed);
}
