package org.nearbyshops.enduserappnew.ModelStatusCodes;

/**
 * Created by sumeet on 12/6/16.
 */





// :: staff functions
// confirmOrder()
// setOrderPacked()
// handoverToDelivery()
// acceptReturn()
// unpackOrder()
// paymentReceived()


// delivery guy functions
// AcceptPackage() | DeclinePackage()
// Return() | Deliver()



public class OrderStatusPickFromShop {

    public static final int ORDER_PLACED = 1; // Confirm (Staff)
    public static final int ORDER_CONFIRMED = 2; // Pack (Staff)
    public static final int ORDER_PACKED = 3; // payment-received(staff)

    public static final int DELIVERED = 4;// Payment Received-Complete



    public static final int CANCELLED_BY_SHOP = 19;
    public static final int CANCELLED_BY_USER = 20;



    public static String getStatusString(int orderStatus)
    {
        String statusString = "";

        if(orderStatus==ORDER_PLACED)
        {
            statusString = "Order Placed - Pick from Shop";
        }
        else if(orderStatus==ORDER_CONFIRMED)
        {
            statusString = "Order Confirmed - Pick from Shop";
        }
        else if(orderStatus ==ORDER_PACKED)
        {
            statusString = "Order Packed - Pick from Shop";
        }
        else if(orderStatus==DELIVERED)
        {
            statusString = "Delivered - Pick from Shop";
        }
        else if(orderStatus==CANCELLED_BY_SHOP)
        {
            statusString  = "Cancelled By Shop - Pick from Shop";
        }
        else if(orderStatus==CANCELLED_BY_USER)
        {
            statusString = "Cancelled By User - Pick from Shop";
        }


        return statusString;
    }


}
