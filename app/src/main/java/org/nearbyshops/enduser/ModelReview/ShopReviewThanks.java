package org.nearbyshops.enduser.ModelReview;


import android.os.Parcel;
import android.os.Parcelable;

import org.nearbyshops.enduser.ModelRoles.EndUser;

/**
 * Created by sumeet on 21/10/16.
 */

public class ShopReviewThanks implements Parcelable{

    // Table Name
    public static final String TABLE_NAME = "SHOP_REVIEW_THANKS";

    // column Names
    public static final String END_USER_ID = "END_USER_ID"; // foreign Key
    public static final String SHOP_REVIEW_ID = "SHOP_REVIEW_ID"; // foreign Key


    // Create Table Statement
    public static final String createTableShopReviewThanksPostgres = "CREATE TABLE IF NOT EXISTS "
            + ShopReviewThanks.TABLE_NAME + "("

            + " " + ShopReviewThanks.END_USER_ID + " INT,"
            + " " + ShopReviewThanks.SHOP_REVIEW_ID + " INT,"

            + " FOREIGN KEY(" + ShopReviewThanks.END_USER_ID +") REFERENCES " + EndUser.TABLE_NAME + "(" + EndUser.END_USER_ID + "),"
            + " FOREIGN KEY(" + ShopReviewThanks.SHOP_REVIEW_ID +") REFERENCES " + ShopReview.TABLE_NAME + "(" + ShopReview.SHOP_REVIEW_ID + "),"
            + " PRIMARY KEY (" + ShopReviewThanks.END_USER_ID + ", " + ShopReviewThanks.SHOP_REVIEW_ID + ")"
            + ")";

    public ShopReviewThanks() {
    }


    // instance Variables

    private int endUserID;
    private int shopReviewID;

    protected ShopReviewThanks(Parcel in) {
        endUserID = in.readInt();
        shopReviewID = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(endUserID);
        dest.writeInt(shopReviewID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShopReviewThanks> CREATOR = new Creator<ShopReviewThanks>() {
        @Override
        public ShopReviewThanks createFromParcel(Parcel in) {
            return new ShopReviewThanks(in);
        }

        @Override
        public ShopReviewThanks[] newArray(int size) {
            return new ShopReviewThanks[size];
        }
    };

    public int getEndUserID() {
        return endUserID;
    }

    public void setEndUserID(int endUserID) {
        this.endUserID = endUserID;
    }

    public int getShopReviewID() {
        return shopReviewID;
    }

    public void setShopReviewID(int shopReviewID) {
        this.shopReviewID = shopReviewID;
    }
}
