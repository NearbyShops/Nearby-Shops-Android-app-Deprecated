package org.nearbyshops.enduserappnew.ShopsByCategoryOld.Interfaces;


import org.nearbyshops.enduserappnew.Model.ItemCategory;

/**
 * Created by sumeet on 22/9/16.
 */

public interface NotifyCategoryChanged {

    void itemCategoryChanged(ItemCategory currentCategory, Boolean isBackPressed);
}
