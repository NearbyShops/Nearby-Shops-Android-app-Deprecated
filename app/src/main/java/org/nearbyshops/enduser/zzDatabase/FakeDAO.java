package org.nearbyshops.enduser.zzDatabase;

import android.util.Log;

import org.nearbyshops.enduser.Model.ItemCategory;
import org.nearbyshops.enduser.zzStandardInterfaces.DataProviderItemCategory;
import org.nearbyshops.enduser.zzStandardInterfacesGeneric.DataSubscriber;

import java.util.List;

/**
 * Created by sumeet on 19/5/16.
 */
public class FakeDAO implements DataProviderItemCategory,DataSubscriber<ItemCategory>

{

    static FakeDAO instance;


    public static FakeDAO getInstance()
    {

        if(instance == null)
        {
            instance = new FakeDAO();

        }

        return instance;
    }



    @Override
    public void createCallback(boolean isOffline,
                               boolean isSuccessful,
                               int httpStatusCode,
                               ItemCategory itemCategory) {

        Log.d("datalog","saving data into DB");

    }

    @Override
    public void readCallback(boolean isOffline,
                             boolean isSuccessful,
                             int httpStatusCode,
                             ItemCategory itemCategory) {

        Log.d("datalog","saving data into DB");
    }

    @Override
    public void readManyCallback(boolean isOffline, boolean isSuccessful, int httpStatusCode, List<ItemCategory> t) {

        Log.d("datalog","ReadMany: saving data into DB");
    }

    @Override
    public void updateCallback(boolean isOffline, boolean isSuccessful, int httpStatusCode) {

        Log.d("datalog","saving data into DB");
    }


    @Override
    public void deleteShopCallback(boolean isOffline, boolean isSuccessful, int httpStatusCode) {

        Log.d("datalog","saving data into DB");
    }




    public void readMany(int parentID, int shopID, DataSubscriber<ItemCategory> Subscriber) {

        Log.d("datalog","read data from DB");

        Subscriber.readManyCallback(true,false,-1,null);

    }

    @Override
    public void read(int ID, DataSubscriber<ItemCategory> Subscriber) {

        Log.d("datalog","read data from DB");
    }

    @Override
    public void delete(int ID, DataSubscriber Subscriber) {

    }

    @Override
    public void insert(ItemCategory itemCategory, DataSubscriber<ItemCategory> Subscriber) {

    }

    @Override
    public void update(ItemCategory itemCategory, DataSubscriber Subscriber) {

    }

    @Override
    public void subscribe(DataSubscriber<ItemCategory> Subscriber) {

    }

    @Override
    public void readMany(int parentID,
                         int shopID,
                         double latCenter,
                         double lonCenter,
                         double deliveryRangeMax,
                         double deliveryRangeMin,
                         double proximity, DataSubscriber<ItemCategory> Subscriber) {

        Subscriber.readManyCallback(true,false,-1,null);

    }
}
