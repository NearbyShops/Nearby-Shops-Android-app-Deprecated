package org.nearbyshops.enduser.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sumeet on 30/5/16.
 */
public class CartItem implements Parcelable{

    int cartID;

    int itemID;

    Cart cart;

    Item item;

    int itemQuantity;


    int rt_availableItemQuantity;

    double rt_itemPrice;

    String rt_quantityUnit;

    public CartItem() {
    }

    public int getRt_availableItemQuantity() {
        return rt_availableItemQuantity;
    }

    public void setRt_availableItemQuantity(int rt_availableItemQuantity) {
        this.rt_availableItemQuantity = rt_availableItemQuantity;
    }

    public double getRt_itemPrice() {
        return rt_itemPrice;
    }

    public void setRt_itemPrice(double rt_itemPrice) {
        this.rt_itemPrice = rt_itemPrice;
    }

    public String getRt_quantityUnit() {
        return rt_quantityUnit;
    }

    public void setRt_quantityUnit(String rt_quantityUnit) {
        this.rt_quantityUnit = rt_quantityUnit;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getCartID() {
        return cartID;
    }

    public void setCartID(int cartID) {
        this.cartID = cartID;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }



    // parcelable implementation


    protected CartItem(Parcel in) {
        cartID = in.readInt();
        itemID = in.readInt();
        item = in.readParcelable(Item.class.getClassLoader());
        itemQuantity = in.readInt();
        rt_availableItemQuantity = in.readInt();
        rt_itemPrice = in.readDouble();
        rt_quantityUnit = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cartID);
        dest.writeInt(itemID);
        dest.writeParcelable(item, flags);
        dest.writeInt(itemQuantity);
        dest.writeInt(rt_availableItemQuantity);
        dest.writeDouble(rt_itemPrice);
        dest.writeString(rt_quantityUnit);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel in) {
            return new CartItem(in);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };


}
