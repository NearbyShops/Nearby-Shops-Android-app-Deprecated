package org.nearbyshops.enduser.ModelReview;


import org.nearbyshops.enduser.ModelRoles.EndUser;

/**
 * Created by sumeet on 21/10/16.
 */

public class ShopReviewThanks {

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


    // instance Variables

    private Integer endUserID;
    private Integer shopReviewID;


    public Integer getEndUserID() {
        return endUserID;
    }

    public void setEndUserID(Integer endUserID) {
        this.endUserID = endUserID;
    }

    public Integer getShopReviewID() {
        return shopReviewID;
    }

    public void setShopReviewID(Integer shopReviewID) {
        this.shopReviewID = shopReviewID;
    }
}
