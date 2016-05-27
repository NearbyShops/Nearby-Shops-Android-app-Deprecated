package org.nearbyshops.enduser.ModelStats;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sumeet on 26/5/16.
 */
public class ItemStats implements Parcelable {

    int itemID;
    double min_price;
    double max_price;
    int shopCount;

    /*
        Getter and Setter methods


     */

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public double getMin_price() {
        return min_price;
    }

    public void setMin_price(double min_price) {
        this.min_price = min_price;
    }

    public double getMax_price() {
        return max_price;
    }

    public void setMax_price(double max_price) {
        this.max_price = max_price;
    }

    public int getShopCount() {
        return shopCount;
    }

    public void setShopCount(int shopCount) {
        this.shopCount = shopCount;
    }



    // Parcelable Implementation


    protected ItemStats(Parcel in) {
        itemID = in.readInt();
        min_price = in.readDouble();
        max_price = in.readDouble();
        shopCount = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(itemID);
        dest.writeDouble(min_price);
        dest.writeDouble(max_price);
        dest.writeInt(shopCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ItemStats> CREATOR = new Creator<ItemStats>() {
        @Override
        public ItemStats createFromParcel(Parcel in) {
            return new ItemStats(in);
        }

        @Override
        public ItemStats[] newArray(int size) {
            return new ItemStats[size];
        }
    };




}
