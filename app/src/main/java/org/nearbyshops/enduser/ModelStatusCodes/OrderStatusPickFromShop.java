package org.nearbyshops.enduser.ModelStatusCodes;

/**
 * Created by sumeet on 13/6/16.
 */
public class OrderStatusPickFromShop {


    public static final int ORDER_PLACED = 1;
    public static final int ORDER_CONFIRMED = 2;
    public static final int ORDER_PACKED = 3;
    public static final int READY_FOR_PICKUP = 4;
    public static final int DELIVERY_COMPLETE = 5;

    public static final int STATUS_CANCELLED_BY_SHOP = 10;
    public static final int STATUS_CANCELLED_BY_END_USER = 11;

}
