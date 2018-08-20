package org.nearbyshops.enduserappnew.Application;

import android.util.Log;

import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.MyApplication;


/**
 * Created by sumeet on 15/3/16.
 */
public class ApplicationState {

    static ApplicationState instance = null;

    static double latCenterCurrent = 0;
    static double lonCenterCurrent = 0;
    static double deliveryRangeMin = 0;
    static double deliveryRangeMax = 0;
    static double proximityFilter = 0;


    Shop currentShop = null;

    MyApplication myApplication;


    private ApplicationState() {
    }


    public static ApplicationState getInstance()
    {

        if(instance == null)
        {
            instance = new ApplicationState();

            return instance;
        }

        return instance;
    }

    public static void setCurrentLocation(double latitude,double longitude)
    {
        latCenterCurrent = latitude;
        lonCenterCurrent = longitude;
    }

    public static double getCurrentLatitude()
    {
        return latCenterCurrent;
    }

    public static double getCurrentLongitude()
    {
        return lonCenterCurrent;
    }


    public static void setDeliveryRangeMin(double valueInKms)
    {
        deliveryRangeMin = valueInKms;
    }

    public static double getDeliveryRangeMin()
    {
        return deliveryRangeMin;
    }

    public static void setDeliveryRangeMax(double valueInKms)
    {
        deliveryRangeMax = valueInKms;
    }

    public static double getDeliveryRangeMax()
    {
        return deliveryRangeMax;
    }


    public static void setProximityFilter(double valueInKms)
    {
        proximityFilter = valueInKms;
    }

    public static double getProximityFilter()
    {
        return proximityFilter;
    }


    public Shop getCurrentShop() {

        Log.i("applog",String.valueOf(currentShop.getShopID()));

        return currentShop;
    }

    public void setCurrentShop(Shop currentShop) {

        Log.i("applog",String.valueOf(currentShop.getShopID()));

        this.currentShop = currentShop;
    }


    public MyApplication getMyApplication() {
        return myApplication;
    }

    public void setMyApplication(MyApplication myApplication) {
        this.myApplication = myApplication;
    }
}
