package org.nearbyshops.enduserappnew.ModelPickFromShop;


import org.nearbyshops.enduserappnew.Model.Shop;
import org.nearbyshops.enduserappnew.ModelRoles.User;
import org.nearbyshops.enduserappnew.ModelStats.DeliveryAddress;

import java.sql.Timestamp;

/**
 * Created by sumeet on 29/5/16.
 */
public class OrderPFS {


    // Table Name for Distributor
    public static final String TABLE_NAME = "ORDER_PICK_FROM_SHOP";

    // Column names for Distributor
    public static final String ORDER_ID_PFS = "ORDER_ID_PFS";






    // Instance Variables

    private Integer orderID;
    private Integer endUserID;
    private Integer shopID;
    private Shop shop;
    //int orderStatus;

    private int orderTotal;
    private int itemCount;
    private int appServiceCharge;

    private Integer deliveryAddressID;

    private Integer statusPickFromShop;

    private Boolean deliveryReceived;
    private Boolean paymentReceived;

    private String reasonCancelledByShop;
    private String reasonCancelledByUser;

    private Timestamp timestampPlaced;
    private Timestamp timestampConfirmed;
    private Timestamp timestampPacked;
    private Timestamp timestampReadyToPickup;
    private Timestamp timestampDelivered;


    private DeliveryAddress deliveryAddress;
    private OrderStatsPFS orderStats;


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

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public int getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(int orderTotal) {
        this.orderTotal = orderTotal;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getAppServiceCharge() {
        return appServiceCharge;
    }

    public void setAppServiceCharge(int appServiceCharge) {
        this.appServiceCharge = appServiceCharge;
    }

    public Integer getDeliveryAddressID() {
        return deliveryAddressID;
    }

    public void setDeliveryAddressID(Integer deliveryAddressID) {
        this.deliveryAddressID = deliveryAddressID;
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

    public String getReasonCancelledByShop() {
        return reasonCancelledByShop;
    }

    public void setReasonCancelledByShop(String reasonCancelledByShop) {
        this.reasonCancelledByShop = reasonCancelledByShop;
    }

    public String getReasonCancelledByUser() {
        return reasonCancelledByUser;
    }

    public void setReasonCancelledByUser(String reasonCancelledByUser) {
        this.reasonCancelledByUser = reasonCancelledByUser;
    }

    public Timestamp getTimestampPlaced() {
        return timestampPlaced;
    }

    public void setTimestampPlaced(Timestamp timestampPlaced) {
        this.timestampPlaced = timestampPlaced;
    }

    public Timestamp getTimestampConfirmed() {
        return timestampConfirmed;
    }

    public void setTimestampConfirmed(Timestamp timestampConfirmed) {
        this.timestampConfirmed = timestampConfirmed;
    }

    public Timestamp getTimestampPacked() {
        return timestampPacked;
    }

    public void setTimestampPacked(Timestamp timestampPacked) {
        this.timestampPacked = timestampPacked;
    }

    public Timestamp getTimestampReadyToPickup() {
        return timestampReadyToPickup;
    }

    public void setTimestampReadyToPickup(Timestamp timestampReadyToPickup) {
        this.timestampReadyToPickup = timestampReadyToPickup;
    }

    public Timestamp getTimestampDelivered() {
        return timestampDelivered;
    }

    public void setTimestampDelivered(Timestamp timestampDelivered) {
        this.timestampDelivered = timestampDelivered;
    }

    public DeliveryAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }


    public OrderStatsPFS getOrderStats() {
        return orderStats;
    }

    public void setOrderStats(OrderStatsPFS orderStats) {
        this.orderStats = orderStats;
    }
}
