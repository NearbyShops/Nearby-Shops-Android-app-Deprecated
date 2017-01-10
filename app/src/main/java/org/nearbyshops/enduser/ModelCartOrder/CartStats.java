package org.nearbyshops.enduser.ModelCartOrder;

import org.nearbyshops.enduser.Model.Shop;

/**
 * Created by sumeet on 1/6/16.
 */
public class CartStats {

    int cartID;

    int itemsInCart;

    double cart_Total;

    int shopID;


    Shop shop;


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
