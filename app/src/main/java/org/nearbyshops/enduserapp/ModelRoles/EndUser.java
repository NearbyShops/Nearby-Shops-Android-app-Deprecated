package org.nearbyshops.enduserapp.ModelRoles;

import java.sql.Timestamp;

/**
 * Created by sumeet on 29/5/16.
 */
public class EndUser {


    // Table Name for EndUser
    public static final String TABLE_NAME = "END_USER";

    // Column names for EndUser

    public static final String END_USER_ID = "ID";
    public static final String END_USER_NAME = "END_USER_NAME";
    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";

    public static final String ABOUT = "ABOUT";
    public static final String PROFILE_IMAGE_URL = "PROFILE_IMAGE_URL";


    // to be Implemented
    public static final String IS_ENABLED = "IS_ENABLED";
    public static final String IS_WAITLISTED = "IS_WAITLISTED";


    // to be implemented
    public static final String CREATED = "CREATED";
    public static final String UPDATED = "UPDATED";

	/*
	Enable or Disable indicates whether the account is enabled or not.
	When the staff is approving the new accounts they might put some or few accounts on hold because they might
	want to do some extra enquiry before they give them an approval. In this situation they might transfer these accounts
	on to waitlisted in order to distinctly identify them and they do not get mixed with other new accounts.
	*/



    // Create Table EndUser

    public static final String createTableEndUserPostgres
            = "CREATE TABLE IF NOT EXISTS " + EndUser.TABLE_NAME + "("
            + " " + EndUser.END_USER_ID + " SERIAL PRIMARY KEY,"
            + " " + EndUser.END_USER_NAME + " text,"
            + " " + EndUser.USERNAME + " text UNIQUE,"
            + " " + EndUser.PASSWORD + " text,"

            + " " + EndUser.ABOUT + " text,"
            + " " + EndUser.PROFILE_IMAGE_URL + " text,"

            + " " + EndUser.IS_ENABLED + " boolean,"
            + " " + EndUser.IS_WAITLISTED + " boolean,"

            + " " + EndUser.CREATED + " timestamp with time zone NOT NULL DEFAULT now(),"
            + " " + EndUser.UPDATED + " timestamp with time zone "
            + ")";




    // Instance Variables
    private int endUserID;
    private String name;
    private String username;
    private String password;

    private String about;
    private String profileImageURL;

    private Boolean isEnabled;
    private Boolean isWaitlisted;
    private Timestamp created;
    private Timestamp updated;



    // Getter and Setters


    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public Boolean getWaitlisted() {
        return isWaitlisted;
    }

    public void setWaitlisted(Boolean waitlisted) {
        isWaitlisted = waitlisted;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public int getEndUserID() {
        return endUserID;
    }
    public void setEndUserID(int endUserID) {
        this.endUserID = endUserID;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }
}
