package org.nearbyshops.enduser.zzStandardInterfacesGeneric;

import java.util.List;

/**
 * Created by sumeet on 19/5/16.
 */
public interface DataSubscriber<T>{

    public void createCallback(boolean isOffline, boolean isSuccessful, int httpStatusCode, T t);

    public void readCallback(boolean isOffline, boolean isSuccessful, int httpStatusCode, T t);

    public void readManyCallback(boolean isOffline, boolean isSuccessful, int httpStatusCode, List<T> t);

    public void updateCallback(boolean isOffline, boolean isSuccessful, int httpStatusCode);

    public void deleteShopCallback(boolean isOffline, boolean isSuccessful, int httpStatusCode);
}
