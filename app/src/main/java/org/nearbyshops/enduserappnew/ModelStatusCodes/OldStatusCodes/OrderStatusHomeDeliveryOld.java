package org.nearbyshops.enduserappnew.ModelStatusCodes.OldStatusCodes;

/**
 * Created by sumeet on 12/6/16.
 */
public class OrderStatusHomeDeliveryOld {

    public static final int ORDER_PLACED = 1;
    public static final int ORDER_CONFIRMED = 2;
    public static final int ORDER_PACKED = 3;

    public static final int PENDING_HANDOVER = 4;
    public static final int HANDOVER_ACCEPTED = 5;
//    public static final int PENDING_DELIVERY = 6;
    public static final int PENDING_DELIVERY = 6;// PENDING DELIVERY | PENDING_DELIVERY_ACCEPT_PENDING_PAYMENT
//    public static final int DELIVERY_ACCEPTED_PAYMENT_PENDING = 7;
//    public static final int PAYMENT_ACCEPTED_DELIVERY_PENDING = 8;
    public static final int DELIVERY_COMPLETE = 7;


    public static final int CANCELLED_BY_SHOP_RETURN_PENDING = 15;
    public static final int CANCELLED_BY_SHOP = 16;

    public static final int CANCELLED_BY_USER_RETURN_PENDING = 17;
    public static final int CANCELLED_BY_USER = 18;

    public static final int RETURN_PENDING = 19;
    public static final int RETURNED = 20;


    // timestamp_placed
    // timestamp_confirmed
    // timestamp_packed
    // timestamp_out_for_delivery
    // timestamp_delivered

    // timestamp_cancelled_by_user
    // timestamp_cancelled_by_shop

}
