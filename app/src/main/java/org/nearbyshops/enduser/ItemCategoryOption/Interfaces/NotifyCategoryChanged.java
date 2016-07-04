package org.nearbyshops.enduser.ItemCategoryOption.Interfaces;

import org.nearbyshops.enduser.Model.ItemCategory;

/**
 * Created by sumeet on 4/7/16.
 */

public interface NotifyCategoryChanged {

    void categoryChanged(ItemCategory currentCategory, boolean isBackPressed);

    void notifySwipeToRight();
}
