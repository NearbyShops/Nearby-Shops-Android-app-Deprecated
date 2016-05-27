package org.nearbyshops.enduser.StandardInterfaces;

import org.nearbyshops.enduser.Model.Item;
import org.nearbyshops.enduser.StandardInterfacesGeneric.DataProvider;
import org.nearbyshops.enduser.StandardInterfacesGeneric.DataSubscriber;

import retrofit2.http.Query;

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
