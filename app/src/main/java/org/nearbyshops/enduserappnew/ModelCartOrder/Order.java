package org.nearbyshops.enduserappnew.ModelCartOrder;

import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.ModelStats.DeliveryAddress;

import java.sql.Timestamp;

/**
 * Created by sumeet on 29/5/16.
 */
public class Order {


    // Table Name for Distributor
    public static final String TABLE_NAME = "CUSTOMER_ORDER";

    // Column names for Distributor
    public static final String ORDER_ID = "ORDER_ID";
    public static final String END_USER_ID = "END_USER_ID"; // foreign Key
    public static final String SHOP_ID = "SHOP_ID"; // foreign Key
    public static final String DELIVERY_CHARGES = "DELIVERY_CHARGES";
    public static final String DELIVERY_ADDRESS_ID = "DELIVERY_ADDRESS_ID";
    public static final String PICK_FROM_SHOP = "PICK_FROM_SHOP";


    public static final String STATUS_HOME_DELIVERY = "STATUS_HOME_DELIVERY";
    public static final String STATUS_PICK_FROM_SHOP = "STATUS_PICK_FROM_SHOP";
    public static final String DELIVERY_RECEIVED = "DELIVERY_RECEIVED";
    public static final String PAYMENT_RECEIVED = "PAYMENT_RECEIVED";

    public static final String DELIVERY_GUY_SELF_ID = "DELIVERY_GUY_SELF_ID";

    public static final String ORDER_TOTAL = "ORDER_TOTAL";
//    public static final String TAX_AMOUNT = "TAX_AMOUNT";
    public static final String ITEM_COUNT = "ITEM_COUNT";
    public static final String APP_SERVICE_CHARGE = "APP_SERVICE_CHARGE";

    public static final String REASON_FOR_CANCELLED_BY_USER = "REASON_FOR_CANCELLED_BY_USER";
    public static final String REASON_FOR_CANCELLED_BY_SHOP = "REASON_FOR_CANCELLED_BY_SHOP";
    public static final String REASON_FOR_ORDER_RETURNED = "REASON_FOR_ORDER_RETURNED";


    public static final String DATE_TIME_PLACED = "DATE_TIME_PLACED";

    public static final String TIMESTAMP_HD_CONFIRMED = "TIMESTAMP_HD_CONFIRMED";
    public static final String TIMESTAMP_HD_PACKED = "TIMESTAMP_HD_PACKED";
    public static final String TIMESTAMP_HD_OUT_FOR_DELIVERY = "TIMESTAMP_HD_OUT_FOR_DELIVERY";
    public static final String TIMESTAMP_HD_DELIVERED = "TIMESTAMP_HD_DELIVERED";




    // Create Table Order In postgres
//
//    public static final String createTableOrderPostgres = "CREATE TABLE IF NOT EXISTS " + Order.TABLE_NAME + "("
//            + " " + Order.ORDER_ID + " SERIAL PRIMARY KEY,"
//            + " " + Order.END_USER_ID + " INT,"
//            + " " + Order.SHOP_ID + " INT,"
//
//            + " " + Order.ORDER_TOTAL + " INT,"
//            + " " + Order.ITEM_COUNT + " INT,"
//            + " " + Order.APP_SERVICE_CHARGE + " INT,"
//
//            + " " + Order.REASON_FOR_CANCELLED_BY_SHOP + " text,"
//            + " " + Order.REASON_FOR_CANCELLED_BY_USER + " text,"
//            + " " + Order.REASON_FOR_ORDER_RETURNED + " text,"
//
//            + " " + Order.TIMESTAMP_HD_CONFIRMED + " timestamp with time zone,"
//            + " " + Order.TIMESTAMP_HD_PACKED + " timestamp with time zone,"
//            + " " + Order.TIMESTAMP_HD_OUT_FOR_DELIVERY + " timestamp with time zone,"
//            + " " + Order.TIMESTAMP_HD_DELIVERED + " timestamp with time zone,"
//
//            + " " + Order.STATUS_HOME_DELIVERY + " INT,"
//            + " " + Order.STATUS_PICK_FROM_SHOP + " INT,"
//            + " " + Order.DELIVERY_RECEIVED + " boolean,"
//            + " " + Order.PAYMENT_RECEIVED + " boolean,"
//            + " " + Order.DELIVERY_CHARGES + " INT,"
//            + " " + Order.DELIVERY_ADDRESS_ID + " INT,"
//            + " " + Order.PICK_FROM_SHOP + " boolean,"
//            + " " + Order.DELIVERY_GUY_SELF_ID + " INT,"
//            + " " + Order.DATE_TIME_PLACED + " timestamp with time zone NOT NULL DEFAULT now(),"
//            + " FOREIGN KEY(" + Order.END_USER_ID +") REFERENCES " + EndUser.TABLE_NAME + "(" + EndUser.END_USER_ID + "),"
//            + " FOREIGN KEY(" + Order.SHOP_ID +") REFERENCES " + Shop.TABLE_NAME + "(" + Shop.SHOP_ID + "),"
//            + " FOREIGN KEY(" + Order.DELIVERY_ADDRESS_ID +") REFERENCES " + DeliveryAddress.TABLE_NAME + "(" + DeliveryAddress.ID + "),"
//            + " FOREIGN KEY(" + Order.DELIVERY_GUY_SELF_ID +") REFERENCES " + DeliveryGuySelf.TABLE_NAME + "(" + DeliveryGuySelf.DELIVERY_GUY_SELF_ID + ")"
//            + ")";




    // Instance Variables

    private Integer orderID;
    private Integer endUserID;
    private Integer shopID;
    private Shop shop;
    //int orderStatus;

    private Integer statusHomeDelivery;
    private Integer statusPickFromShop;
    private Boolean deliveryReceived;
    private Boolean paymentReceived;

    private Integer deliveryCharges;
    private Integer deliveryAddressID;
    private Boolean pickFromShop;

    private Integer deliveryGuySelfID;
    private Timestamp dateTimePlaced;

    private DeliveryAddress deliveryAddress;
    private OrderStats orderStats;


    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Integer getOrderID() {
        return orderID;
    }

    public void setOrderID(Integer orderID) {
        this.orderID = orderID;
    }

    public Integer getEndUserID() {
        return endUserID;
    }

    public void setEndUserID(Integer endUserID) {
        this.endUserID = endUserID;
    }

    public Integer getShopID() {
        return shopID;
    }

    public void setShopID(Integer shopID) {
        this.shopID = shopID;
    }

    public Integer getStatusHomeDelivery() {
        return statusHomeDelivery;
    }

    public void setStatusHomeDelivery(Integer statusHomeDelivery) {
        this.statusHomeDelivery = statusHomeDelivery;
    }

    public Integer getStatusPickFromShop() {
        return statusPickFromShop;
    }

    public void setStatusPickFromShop(Integer statusPickFromShop) {
        this.statusPickFromShop = statusPickFromShop;
    }

    public Boolean getDeliveryReceived() {
        return deliveryReceived;
    }

    public void setDeliveryReceived(Boolean deliveryReceived) {
        this.deliveryReceived = deliveryReceived;
    }

    public Boolean getPaymentReceived() {
        return paymentReceived;
    }

    public void setPaymentReceived(Boolean paymentReceived) {
        this.paymentReceived = paymentReceived;
    }

    public Integer getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(Integer deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

    public Integer getDeliveryAddressID() {
        return deliveryAddressID;
    }

    public void setDeliveryAddressID(Integer deliveryAddressID) {
        this.deliveryAddressID = deliveryAddressID;
    }

    public Boolean getPickFromShop() {
        return pickFromShop;
    }

    public void setPickFromShop(Boolean pickFromShop) {
        this.pickFromShop = pickFromShop;
    }

    public Integer getDeliveryGuySelfID() {
        return deliveryGuySelfID;
    }

    public void setDeliveryGuySelfID(Integer deliveryGuySelfID) {
        this.deliveryGuySelfID = deliveryGuySelfID;
    }

    public Timestamp getDateTimePlaced() {
        return dateTimePlaced;
    }

    public void setDateTimePlaced(Timestamp dateTimePlaced) {
        this.dateTimePlaced = dateTimePlaced;
    }

    public DeliveryAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public OrderStats getOrderStats() {
        return orderStats;
    }

    public void setOrderStats(OrderStats orderStats) {
        this.orderStats = orderStats;
    }
}
