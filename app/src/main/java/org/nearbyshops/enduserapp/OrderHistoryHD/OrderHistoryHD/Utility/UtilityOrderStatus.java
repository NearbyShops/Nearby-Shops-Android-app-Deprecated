package org.nearbyshops.enduserapp.OrderHistoryHD.OrderHistoryHD.Utility;


import org.nearbyshops.enduserapp.ModelStatusCodes.OrderStatusHomeDelivery;

/**
 * Created by sumeet on 23/12/16.
 */

public class UtilityOrderStatus {

    public static String getStatus(int orderStatusHomeDelivery, boolean deliveryReceived, boolean paymentReceived)
    {
        if(orderStatusHomeDelivery==1)
        {
            return "Placed";
        }
        else if(orderStatusHomeDelivery==2)
        {
            return "Confirmed";
        }
        else if(orderStatusHomeDelivery==3)
        {
            return "Packed";
        }
        else if(orderStatusHomeDelivery==4)
        {
            return "Pending Handover";
        }
        else if(orderStatusHomeDelivery==5)
        {
            return "Out for Delivery";
        }
        else if(orderStatusHomeDelivery==6 && (!deliveryReceived && !paymentReceived))
        {
            return "Pending Approvals";
        }
        else if(orderStatusHomeDelivery==6 && (!deliveryReceived && paymentReceived))
        {
            return "Pending Delivery Approval";
        }
        else if(orderStatusHomeDelivery==6 && (deliveryReceived && !paymentReceived))
        {
            return "Pending Payments";
        }
        else if(orderStatusHomeDelivery == 6 && (deliveryReceived && paymentReceived))
        {
            return "Complete";
        }
        else if(orderStatusHomeDelivery== OrderStatusHomeDelivery.CANCELLED_BY_SHOP)
        {
            return "Cancelled By Shop";
        }
        else if(orderStatusHomeDelivery == OrderStatusHomeDelivery.CANCELLED_BY_SHOP_RETURN_PENDING)
        {
            return "Pending Return - Cancelled By Shop";
        }
        else if(orderStatusHomeDelivery == OrderStatusHomeDelivery.CANCELLED_BY_USER_RETURN_PENDING)
        {
            return "Pending Return - Cancelled By User";
        }
        else if(orderStatusHomeDelivery == OrderStatusHomeDelivery.CANCELLED_BY_USER)
        {
            return "Cancelled By User";
        }
        else if(orderStatusHomeDelivery == OrderStatusHomeDelivery.RETURN_PENDING)
        {
            return "Return Pending";
        }
        else if(orderStatusHomeDelivery == OrderStatusHomeDelivery.RETURNED)
        {
            return "Returned";
        }


        return "";
    }


}
