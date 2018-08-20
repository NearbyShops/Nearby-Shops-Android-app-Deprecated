package org.nearbyshops.enduserappnew.ModelStats;

import android.os.Parcel;
import android.os.Parcelable;

import org.nearbyshops.enduserappnew.Model.Shop;

/**
 * Created by sumeet on 1/6/16.
 */
public class CartStats implements Parcelable{


    int cartID;

    int itemsInCart;

    double cart_Total;

    int shopID;

    Shop shop;






    // Parcelable Implementation

    protected CartStats(Parcel in) {
        cartID = in.readInt();
        itemsInCart = in.readInt();
        cart_Total = in.readDouble();
        shopID = in.readInt();
        shop = in.readParcelable(Shop.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cartID);
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



    // Getter and Setter Methods


    public int getCartID() {
        return cartID;
    }

    public void setCartID(int cartID) {
        this.cartID = cartID;
    }

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



}
