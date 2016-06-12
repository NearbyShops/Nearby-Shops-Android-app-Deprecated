package org.nearbyshops.enduser.Model;

import java.sql.Timestamp;

/**
 * Created by sumeet on 29/5/16.
 */
public class Order {

    int orderID;
    int endUserID;
    int shopID;
    int orderStatus;

    int deliveryCharges;
    int deliveryAddressID;
    boolean pickFromShop;

    Timestamp dateTimePlaced;






    // Getter and Setter Methods
    public int getDeliveryAddressID() {
        return deliveryAddressID;
    }

    public void setDeliveryAddressID(int deliveryAddressID) {
        this.deliveryAddressID = deliveryAddressID;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getEndUserID() {
        return endUserID;
    }

    public void setEndUserID(int endUserID) {
        this.endUserID = endUserID;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(int deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

    public boolean getPickFromShop() {
        return pickFromShop;
    }

    public void setPickFromShop(boolean pickFromShop) {
        this.pickFromShop = pickFromShop;
    }


    public Timestamp getDateTimePlaced() {
        return dateTimePlaced;
    }

    public void setDateTimePlaced(Timestamp dateTimePlaced) {
        this.dateTimePlaced = dateTimePlaced;
    }
}
