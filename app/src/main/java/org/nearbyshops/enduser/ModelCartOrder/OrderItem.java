package org.nearbyshops.enduser.ModelCartOrder;

import org.nearbyshops.enduser.Model.Item;

/**
 * Created by sumeet on 29/5/16.
 */
public class OrderItem {



    // Table Name for Distributor
    public static final String TABLE_NAME = "ORDER_ITEM";

    // Column names for Distributor

    public static final String ITEM_ID = "ITEM_ID";     // FOREIGN KEY
    public static final String ORDER_ID = "ORDER_ID";   // Foreign KEY
    public static final String ITEM_QUANTITY = "ITEM_QUANTITY";
    public static final String ITEM_PRICE_AT_ORDER = "ITEM_PRICE_AT_ORDER";


//    // Create table OrderItem in Postgres
//    public static final String createtableOrderItemPostgres = "CREATE TABLE IF NOT EXISTS " + OrderItem.TABLE_NAME + "("
//            + " " + OrderItem.ITEM_ID + " INT,"
//            + " " + OrderItem.ORDER_ID + " INT,"
//            + " " + OrderItem.ITEM_PRICE_AT_ORDER + " FLOAT,"
//            + " " + OrderItem.ITEM_QUANTITY + " INT,"
//            + " FOREIGN KEY(" + OrderItem.ITEM_ID +") REFERENCES " + Item.TABLE_NAME + "(" + Item.ITEM_ID + "),"
//            + " FOREIGN KEY(" + OrderItem.ORDER_ID +") REFERENCES " + Order.TABLE_NAME + "(" + Order.ORDER_ID + "),"
//            + " PRIMARY KEY (" + OrderItem.ITEM_ID + ", " + OrderItem.ORDER_ID + "),"
//            + " UNIQUE (" + OrderItem.ITEM_ID + "," + OrderItem.ORDER_ID  + ")"
//            + ")";
//



    // instance variables
    private Integer itemID;
    private Integer orderID;
    private Integer itemQuantity;
    private Integer itemPriceAtOrder;

    private Item item;


    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Integer getItemID() {
        return itemID;
    }

    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }

    public Integer getOrderID() {
        return orderID;
    }

    public void setOrderID(Integer orderID) {
        this.orderID = orderID;
    }

    public Integer getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public Integer getItemPriceAtOrder() {
        return itemPriceAtOrder;
    }

    public void setItemPriceAtOrder(Integer itemPriceAtOrder) {
        this.itemPriceAtOrder = itemPriceAtOrder;
    }
}
