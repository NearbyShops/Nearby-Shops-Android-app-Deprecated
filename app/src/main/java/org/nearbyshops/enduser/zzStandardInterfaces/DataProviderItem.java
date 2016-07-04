package org.nearbyshops.enduser.zzStandardInterfaces;

import org.nearbyshops.enduser.Model.Item;
import org.nearbyshops.enduser.zzStandardInterfacesGeneric.DataProvider;
import org.nearbyshops.enduser.zzStandardInterfacesGeneric.DataSubscriber;

/**
 * Created by sumeet on 26/5/16.
 */
public interface DataProviderItem extends DataProvider<Item> {

    public void readMany(
            int itemCategoryID,
            int shopID,
            double latCenter,
            double lonCenter,
            double deliveryRangeMax,
            double deliveryRangeMin,
            double proximity,
            DataSubscriber<Item> subscriber);

}
