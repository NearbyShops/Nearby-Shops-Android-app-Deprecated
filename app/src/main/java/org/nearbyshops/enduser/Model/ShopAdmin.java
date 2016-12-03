package org.nearbyshops.enduser.Model;



/**
 * Created by sumeet on 14/6/16.
 */
public class ShopAdmin {

    // Note : ShopAdmin has one to one relationship with Shop therefore the columns of ShopAdmin has been
    // merged inside the Shop table for preserving data and relationship consistency.
    // The entity remains distinct and not the same as Shop which in simple terms imply that although both
    // entity columns are merged in one table the entity remains distinct.

    // Table Name : Table does not exist for ShopAdmin because the columns are merged in Shop Table

    // column Names
    public static final String NAME = "NAME";

    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";

    public static final String PROFILE_IMAGE_URL = "PROFILE_IMAGE_URL";
    public static final String PHONE_NUMBER = "PHONE_NUMBER";

    // to be Implemented
    public static final String ADMIN_ENABLED = "ADMIN_ENABLED";
    public static final String ADMIN_WAITLISTED = "ADMIN_WAITLISTED";



    // instance Variables

    private int shopID;

    private String name;

    private String username;
    private String password;

    private String profileImageURL;
    private String phoneNumber;

    private Boolean adminEnabled;
    private Boolean adminWaitlisted;



    // getter and setters


    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getAdminEnabled() {
        return adminEnabled;
    }

    public void setAdminEnabled(Boolean adminEnabled) {
        this.adminEnabled = adminEnabled;
    }

    public Boolean getAdminWaitlisted() {
        return adminWaitlisted;
    }

    public void setAdminWaitlisted(Boolean adminWaitlisted) {
        this.adminWaitlisted = adminWaitlisted;
    }
}
