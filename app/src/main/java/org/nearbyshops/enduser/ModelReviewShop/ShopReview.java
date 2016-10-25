package org.nearbyshops.enduser.ModelReviewShop;

import android.os.Parcel;
import android.os.Parcelable;

import org.nearbyshops.enduser.Model.Shop;
import org.nearbyshops.enduser.ModelRoles.EndUser;

import java.sql.Timestamp;

/**
 * Created by sumeet on 8/8/16.
 */
public class ShopReview implements Parcelable{


    // Table Name
    public static final String TABLE_NAME = "SHOP_REVIEW";

    // column Names
    public static final String SHOP_REVIEW_ID = "SHOP_REVIEW_ID";
    public static final String SHOP_ID = "SHOP_ID";
    public static final String END_USER_ID = "END_USER_ID";
    public static final String RATING = "RATING";
    public static final String REVIEW_TEXT = "REVIEW_TEXT";
    public static final String REVIEW_DATE = "REVIEW_DATE";
    public static final String REVIEW_TITLE = "REVIEW_TITLE";

    // review_date, title


    // create Table statement
    public static final String createTableShopReviewPostgres =

            "CREATE TABLE IF NOT EXISTS " + ShopReview.TABLE_NAME + "("

            + " " + ShopReview.SHOP_REVIEW_ID + " SERIAL PRIMARY KEY,"
            + " " + ShopReview.SHOP_ID + " INT,"
            + " " + ShopReview.END_USER_ID + " INT,"
            + " " + ShopReview.RATING + " INT,"
            + " " + ShopReview.REVIEW_TEXT + " text,"
            + " " + ShopReview.REVIEW_TITLE + " text,"
            + " " + ShopReview.REVIEW_DATE + "  timestamp with time zone NOT NULL DEFAULT now(),"

            + " FOREIGN KEY(" + ShopReview.SHOP_ID +") REFERENCES " + Shop.TABLE_NAME + "(" + Shop.SHOP_ID + "),"
            + " FOREIGN KEY(" + ShopReview.END_USER_ID +") REFERENCES " + EndUser.TABLE_NAME + "(" + EndUser.END_USER_ID + "),"
            + " UNIQUE (" + ShopReview.SHOP_ID + "," + ShopReview.END_USER_ID + ")"
            + ")";

    public ShopReview() {
    }


    // Instance Variables

    private int shopReviewID;
    private int shopID;
    private int endUserID;
    private int rating;
    private String reviewText;
    private String reviewTitle;
    private Timestamp reviewDate;

    private EndUser rt_end_user_profile;
    private int rt_thanks_count;

    public int getRt_thanks_count() {
        return rt_thanks_count;
    }

    public void setRt_thanks_count(int rt_thanks_count) {
        this.rt_thanks_count = rt_thanks_count;
    }


    // getter and Setter Methods


    protected ShopReview(Parcel in) {
        shopReviewID = in.readInt();
        shopID = in.readInt();
        endUserID = in.readInt();
        rating = in.readInt();
        reviewText = in.readString();
        reviewTitle = in.readString();
        rt_thanks_count = in.readInt();

        reviewDate = new Timestamp(in.readLong());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(shopReviewID);
        dest.writeInt(shopID);
        dest.writeInt(endUserID);
        dest.writeInt(rating);
        dest.writeString(reviewText);
        dest.writeString(reviewTitle);
        dest.writeInt(rt_thanks_count);


        if(reviewDate!=null)
        {
            dest.writeLong(reviewDate.getTime());
        }
        else
        {
            dest.writeLong(0);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShopReview> CREATOR = new Creator<ShopReview>() {
        @Override
        public ShopReview createFromParcel(Parcel in) {
            return new ShopReview(in);
        }

        @Override
        public ShopReview[] newArray(int size) {
            return new ShopReview[size];
        }
    };

    public Integer getShopReviewID() {
        return shopReviewID;
    }

    public void setShopReviewID(Integer shopReviewID) {
        this.shopReviewID = shopReviewID;
    }

    public Integer getShopID() {
        return shopID;
    }

    public void setShopID(Integer shopID) {
        this.shopID = shopID;
    }

    public Integer getEndUserID() {
        return endUserID;
    }

    public void setEndUserID(Integer endUserID) {
        this.endUserID = endUserID;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }


    public String getReviewTitle() {
        return reviewTitle;
    }

    public void setReviewTitle(String reviewTitle) {
        this.reviewTitle = reviewTitle;
    }


    public Timestamp getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Timestamp reviewDate) {
        this.reviewDate = reviewDate;
    }


    public EndUser getRt_end_user_profile() {
        return rt_end_user_profile;
    }

    public void setRt_end_user_profile(EndUser rt_end_user_profile) {
        this.rt_end_user_profile = rt_end_user_profile;
    }
}
