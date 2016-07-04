package org.nearbyshops.enduser.zzStandardInterfaces;

import org.nearbyshops.enduser.Model.Item;
import org.nearbyshops.enduser.Model.ShopItem;
import org.nearbyshops.enduser.zzStandardInterfacesGeneric.DataProvider;
import org.nearbyshops.enduser.zzStandardInterfacesGeneric.DataSubscriber;

/**
 * Created by sumeet on 27/5/16.
 */
public interface DataProviderShopItem extends DataProvider<ShopItem> {


    public void readMany(
            int ShopID, int itemID,
            int itemCategoryID,
            double latCenter,double lonCenter,
            double deliveryRangeMax,
            double deliveryRangeMin,
            double proximity,
            DataSubscriber<Item> subscriber);

}
