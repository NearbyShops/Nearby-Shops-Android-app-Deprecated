package org.nearbyshops.enduser.zzStandardInterfaces;

import org.nearbyshops.enduser.Model.Shop;
import org.nearbyshops.enduser.zzStandardInterfacesGeneric.DataProvider;
import org.nearbyshops.enduser.zzStandardInterfacesGeneric.DataSubscriber;

/**
 * Created by sumeet on 25/5/16.
 */
public interface DataProviderShop extends DataProvider<Shop> {

    public void readMany(
            Integer distributorID,
            Integer itemCategoryID,
            Double latCenter,
            Double lonCenter,
            Double deliveryRangeMax,
            Double deliveryRangeMin,
            Double proximity,
            DataSubscriber<Shop> Subscriber
    );

}
