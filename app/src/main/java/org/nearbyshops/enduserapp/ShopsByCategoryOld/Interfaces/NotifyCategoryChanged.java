package org.nearbyshops.enduserapp.ShopsByCategoryOld.Interfaces;


import org.nearbyshops.enduserapp.Model.ItemCategory;

/**
 * Created by sumeet on 22/9/16.
 */

public interface NotifyCategoryChanged {

    void itemCategoryChanged(ItemCategory currentCategory, Boolean isBackPressed);
}
