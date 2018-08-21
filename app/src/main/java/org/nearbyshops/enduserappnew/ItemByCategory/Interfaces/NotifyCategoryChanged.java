package org.nearbyshops.enduserappnew.ItemByCategory.Interfaces;

import org.nearbyshops.enduserappnew.Model.ItemCategory;

/**
 * Created by sumeet on 4/7/16.
 */

public interface NotifyCategoryChanged {

    void categoryChanged(ItemCategory currentCategory, boolean isBackPressed);

    void notifySwipeToRight();
}
