package org.nearbyshops.enduser.StandardInterfaces;

import org.nearbyshops.enduser.Model.ItemCategory;
import org.nearbyshops.enduser.Model.Shop;
import org.nearbyshops.enduser.StandardInterfacesGeneric.DataProvider;
import org.nearbyshops.enduser.StandardInterfacesGeneric.DataSubscriber;

import java.util.Map;

/**
 * Created by sumeet on 25/5/16.
 */
public interface DataProviderShop extends DataProvider<Shop> {

    public void readMany(
            int distributorID,
            int itemCategoryID,
            double latCenter,
            double lonCenter,
            double deliveryRangeMax,
            double deliveryRangeMin,
            double proximity,
            DataSubscriber<Shop> Subscriber
    );

}
