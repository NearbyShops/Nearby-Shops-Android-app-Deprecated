package org.nearbyshops.enduserappnew.ModelPickFromShop;


import org.nearbyshops.enduserappnew.Model.Item;

/**
 * Created by sumeet on 29/5/16.
 */
public class OrderItemPFS {



    // Table Name for Distributor
    public static final String TABLE_NAME = "ORDER_ITEM_PICK_FROM_SHOP";

    // Column names for Distributor

    public static final String ITEM_ID = "ITEM_ID";     // FOREIGN KEY
    public static final String ORDER_ID = "ORDER_ID";   // Foreign KEY
    public static final String ITEM_QUANTITY = "ITEM_QUANTITY";
    public static final String ITEM_PRICE_AT_ORDER = "ITEM_PRICE_AT_ORDER";


    // Create table OrderItemPFS in Postgres
    public static final String createtableOrderItemPostgres = "CREATE TABLE IF NOT EXISTS " + OrderItemPFS.TABLE_NAME + "("
            + " " + OrderItemPFS.ITEM_ID + " INT,"
            + " " + OrderItemPFS.ORDER_ID + " INT,"
            + " " + OrderItemPFS.ITEM_PRICE_AT_ORDER + " FLOAT,"
            + " " + OrderItemPFS.ITEM_QUANTITY + " INT,"
            + " FOREIGN KEY(" + OrderItemPFS.ITEM_ID +") REFERENCES " + Item.TABLE_NAME + "(" + Item.ITEM_ID + "),"
            + " FOREIGN KEY(" + OrderItemPFS.ORDER_ID +") REFERENCES " + OrderPFS.TABLE_NAME + "(" + OrderPFS.ORDER_ID_PFS + "),"
            + " PRIMARY KEY (" + OrderItemPFS.ITEM_ID + ", " + OrderItemPFS.ORDER_ID + "),"
            + " UNIQUE (" + OrderItemPFS.ITEM_ID + "," + OrderItemPFS.ORDER_ID  + ")"
            + ")";




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
