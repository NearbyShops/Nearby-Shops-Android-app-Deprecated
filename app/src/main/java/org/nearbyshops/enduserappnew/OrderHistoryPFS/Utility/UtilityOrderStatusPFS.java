package org.nearbyshops.enduserappnew.OrderHistoryPFS.Utility;

import org.nearbyshops.enduserappnew.ModelPickFromShop.OrderStatusPickFromShop;

/**
 * Created by sumeet on 23/12/16.
 */

public class UtilityOrderStatusPFS {



    public static String getStatusPFS(int orderStatusPickFromShop, boolean deliveryReceived, boolean paymentReceived)
    {
        if(orderStatusPickFromShop==1)
        {
            return "Placed";
        }
        else if(orderStatusPickFromShop==2)
        {
            return "Confirmed";
        }
        else if(orderStatusPickFromShop==3)
        {
            return "Packed";
        }
        else if(orderStatusPickFromShop==4 && (!deliveryReceived && !paymentReceived))
        {
            return "Ready for Pickup";
        }
        else if(orderStatusPickFromShop==4 && (!deliveryReceived && paymentReceived))
        {
            return "Pending Delivery Approval";
        }
        else if(orderStatusPickFromShop==4 && (deliveryReceived && !paymentReceived))
        {
            return "Pending Payments";
        }
        else if(orderStatusPickFromShop == 4 && (deliveryReceived && paymentReceived))
        {
            return "Complete";
        }
        else if(orderStatusPickFromShop== OrderStatusPickFromShop.CANCELLED_BY_SHOP)
        {
            return "Cancelled By Shop";
        }

        else if(orderStatusPickFromShop == OrderStatusPickFromShop.CANCELLED_BY_USER)
        {
            return "Cancelled By User";
        }

        return "";
    }

}
