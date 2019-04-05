package org.nearbyshops.enduserappnew.ModelReviewShop;


import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.ModelRoles.User;

/**
 * Created by sumeet on 8/8/16.
 */
public class FavouriteShop {




    // instance Variables

    private Integer endUserID;
    private Integer shopID;

    // Getter and Setter


    public Integer getEndUserID() {
        return endUserID;
    }

    public void setEndUserID(Integer endUserID) {
        this.endUserID = endUserID;
    }

    public Integer getShopID() {
        return shopID;
    }

    public void setShopID(Integer shopID) {
        this.shopID = shopID;
    }
}
