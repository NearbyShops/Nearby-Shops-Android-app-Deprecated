package org.nearbyshops.enduserappnew.Model;

import java.sql.Timestamp;

/**
 * Created by sumeet on 28/2/17.
 */
public class ShopImage {

    
    // Table Name
    public static final String TABLE_NAME = "SHOP_IMAGES";

    // column names
    public static final String SHOP_IMAGE_ID = "SHOP_IMAGE_ID";
    public static final String SHOP_ID = "SHOP_ID";
    public static final String IMAGE_FILENAME = "IMAGE_FILENAME";

    public static final String TIMESTAMP_CREATED = "TIMESTAMP_CREATED";
    public static final String TIMESTAMP_UPDATED = "TIMESTAMP_UPDATED";

    public static final String CAPTION_TITLE = "CAPTION_TITLE";
    public static final String CAPTION = "CAPTION";
    public static final String COPYRIGHTS = "COPYRIGHTS";
    public static final String IMAGE_ORDER = "IMAGE_ORDER";




    // create table statement
    public static final String createTablePostgres = "CREATE TABLE IF NOT EXISTS "

            + ShopImage.TABLE_NAME + "("

            + " " + ShopImage.SHOP_IMAGE_ID + " SERIAL PRIMARY KEY,"
            + " " + ShopImage.SHOP_ID + " int,"
            + " " + ShopImage.IMAGE_FILENAME + " text,"

            + " " + ShopImage.TIMESTAMP_CREATED + " timestamp with time zone NOT NULL DEFAULT now(),"
            + " " + ShopImage.TIMESTAMP_UPDATED + " timestamp with time zone,"

            + " " + ShopImage.CAPTION_TITLE + " text,"
            + " " + ShopImage.CAPTION + " text,"
            + " " + ShopImage.COPYRIGHTS + " text,"
            + " " + ShopImage.IMAGE_ORDER + " int,"

            + " FOREIGN KEY(" + ShopImage.SHOP_ID +") REFERENCES " + Shop.TABLE_NAME + "(" + Shop.SHOP_ID + ") ON DELETE SET NULL "
            + ")";






    // instance variables

    private int shopImageID;
    private int shopID;
    private String imageFilename;

    private Timestamp timestampCreated;
    private Timestamp timestampUpdated;

    private String captionTitle;
    private String caption;
    private String copyrights;
    private int imageOrder;

    // getter and setters


    public int getShopImageID() {
        return shopImageID;
    }

    public void setShopImageID(int shopImageID) {
        this.shopImageID = shopImageID;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public String getImageFilename() {
        return imageFilename;
    }

    public void setImageFilename(String imageFilename) {
        this.imageFilename = imageFilename;
    }

    public Timestamp getTimestampCreated() {
        return timestampCreated;
    }

    public void setTimestampCreated(Timestamp timestampCreated) {
        this.timestampCreated = timestampCreated;
    }

    public Timestamp getTimestampUpdated() {
        return timestampUpdated;
    }

    public void setTimestampUpdated(Timestamp timestampUpdated) {
        this.timestampUpdated = timestampUpdated;
    }

    public String getCaptionTitle() {
        return captionTitle;
    }

    public void setCaptionTitle(String captionTitle) {
        this.captionTitle = captionTitle;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCopyrights() {
        return copyrights;
    }

    public void setCopyrights(String copyrights) {
        this.copyrights = copyrights;
    }

    public int getImageOrder() {
        return imageOrder;
    }

    public void setImageOrder(int imageOrder) {
        this.imageOrder = imageOrder;
    }
}
