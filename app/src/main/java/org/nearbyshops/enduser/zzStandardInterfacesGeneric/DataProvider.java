package org.nearbyshops.enduser.zzStandardInterfacesGeneric;

/**
 * Created by sumeet on 17/5/16.
 */
public interface DataProvider<T> {

    // IRUD operations - Insert, Read, Update, Delete


    public void read(
            int ID,
            DataSubscriber<T> Subscriber
    );


    /*
    public void readMany(
            Map<String, String> stringParams,
            Map<String, Integer> intParams,
            Map<String, Boolean> booleanParams,
            DataSubscriber<T> Subscriber
    );
    */

    public void delete(
            int ID,
            DataSubscriber Subscriber
    );

    public void insert(
            T t,
            DataSubscriber<T> Subscriber
    );

    public void update(
            T t,
            DataSubscriber Subscriber
    );

    public void subscribe(
            DataSubscriber<T> Subscriber
    );

}
