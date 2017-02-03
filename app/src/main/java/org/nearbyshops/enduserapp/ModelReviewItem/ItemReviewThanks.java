package org.nearbyshops.enduserapp.ModelReviewItem;

import org.nearbyshops.enduserapp.ModelRoles.EndUser;

/**
 * Created by sumeet on 21/10/16.
 */

public class ItemReviewThanks {

    // Table Name
    public static final String TABLE_NAME = "ITEM_REVIEW_THANKS";

    // column Names
    public static final String END_USER_ID = "END_USER_ID"; // foreign Key
    public static final String ITEM_REVIEW_ID = "ITEM_REVIEW_ID"; // foreign Key


    // Create Table Statement
    public static final String createTableItemReviewThanksPostgres = "CREATE TABLE IF NOT EXISTS "
            + ItemReviewThanks.TABLE_NAME + "("

            + " " + ItemReviewThanks.END_USER_ID + " INT,"
            + " " + ItemReviewThanks.ITEM_REVIEW_ID + " INT,"

            + " FOREIGN KEY(" + ItemReviewThanks.END_USER_ID +") REFERENCES " + EndUser.TABLE_NAME + "(" + EndUser.END_USER_ID + "),"
            + " FOREIGN KEY(" + ItemReviewThanks.ITEM_REVIEW_ID +") REFERENCES " + ItemReview.TABLE_NAME + "(" + ItemReview.ITEM_REVIEW_ID + "),"
            + " PRIMARY KEY (" + ItemReviewThanks.END_USER_ID + ", " + ItemReviewThanks.ITEM_REVIEW_ID + ")"
            + ")";


    // instance Variables

    private Integer endUserID;
    private Integer itemReviewID;


    public Integer getEndUserID() {
        return endUserID;
    }

    public void setEndUserID(Integer endUserID) {
        this.endUserID = endUserID;
    }

    public Integer getItemReviewID() {
        return itemReviewID;
    }

    public void setItemReviewID(Integer itemReviewID) {
        this.itemReviewID = itemReviewID;
    }
}
