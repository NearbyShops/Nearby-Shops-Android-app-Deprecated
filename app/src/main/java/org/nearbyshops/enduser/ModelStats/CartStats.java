package org.nearbyshops.enduser.ModelStats;

import android.os.Parcel;
import android.os.Parcelable;

import org.nearbyshops.enduser.Model.Shop;

/**
 * Created by sumeet on 1/6/16.
 */
public class CartStats implements Parcelable{

    int itemsInCart;

    double cart_Total;

    int shopID;

    Shop shop;



    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public int getItemsInCart() {
        return itemsInCart;
    }

    public void setItemsInCart(int itemsInCart) {
        this.itemsInCart = itemsInCart;
    }

    public double getCart_Total() {
        return cart_Total;
    }

    public void setCart_Total(double cart_Total) {
        this.cart_Total = cart_Total;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }




    protected CartStats(Parcel in) {
        itemsInCart = in.readInt();
        cart_Total = in.readDouble();
        shopID = in.readInt();
        shop = in.readParcelable(Shop.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(itemsInCart);
        dest.writeDouble(cart_Total);
        dest.writeInt(shopID);
        dest.writeParcelable(shop, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CartStats> CREATOR = new Creator<CartStats>() {
        @Override
        public CartStats createFromParcel(Parcel in) {
            return new CartStats(in);
        }

        @Override
        public CartStats[] newArray(int size) {
            return new CartStats[size];
        }
    };



}
