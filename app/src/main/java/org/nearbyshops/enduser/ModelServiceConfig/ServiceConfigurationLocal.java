package org.nearbyshops.enduser.ModelServiceConfig;

import java.sql.Timestamp;

/**
 * Created by sumeet on 19/6/16.
 */
public class ServiceConfigurationLocal {


    // Table Name
    public static final String TABLE_NAME = "SERVICE_CONFIGURATION";

    // column Names
    public static final String SERVICE_CONFIGURATION_ID = "SERVICE_CONFIGURATION_ID";

//    public static final String IMAGE_PATH = "IMAGE_PATH";
    public static final String LOGO_IMAGE_PATH = "LOGO_IMAGE_PATH";
    public static final String BACKDROP_IMAGE_PATH = "BACKDROP_IMAGE_PATH";

    public static final String SERVICE_NAME = "SERVICE_NAME";
    public static final String HELPLINE_NUMBER = "HELPLINE_NUMBER";


    public static final String DESCRIPTION_SHORT = "DESCRIPTION_SHORT";
    public static final String DESCRIPTION_LONG = "DESCRIPTION_LONG";

    public static final String ADDRESS = "ADDRESS";
    public static final String CITY = "CITY";
    public static final String PINCODE = "PINCODE";
    public static final String LANDMARK = "LANDMARK";
    public static final String STATE = "STATE";
    public static final String COUNTRY = "COUNTRY";

    public static final String ISO_COUNTRY_CODE = "ISO_COUNTRY_CODE";
    public static final String ISO_LANGUAGE_CODE = "ISO_LANGUAGE_CODE";
    public static final String ISO_CURRENCY_CODE = "ISO_CURRENCY_CODE";

    public static final String SERVICE_TYPE = "SERVICE_TYPE";
    public static final String SERVICE_LEVEL = "SERVICE_LEVEL";

    public static final String LAT_CENTER = "LAT_CENTER";
    public static final String LON_CENTER = "LON_CENTER";

    public static final String SERVICE_RANGE = "SERVICE_RANGE";

    public static final String CREATED = "CREATED";
    public static final String UPDATED = "UPDATED";


    // Create Table Statement
    public static final String createTableServiceConfigurationPostgres
            = "CREATE TABLE IF NOT EXISTS " + ServiceConfigurationLocal.TABLE_NAME + "("
            + " " + ServiceConfigurationLocal.SERVICE_CONFIGURATION_ID + " SERIAL PRIMARY KEY,"

//            + " " + ServiceConfigurationLocal.IMAGE_PATH + " text,"
            + " " + ServiceConfigurationLocal.LOGO_IMAGE_PATH + " text,"
            + " " + ServiceConfigurationLocal.BACKDROP_IMAGE_PATH + " text,"

            + " " + ServiceConfigurationLocal.SERVICE_NAME + " text,"
            + " " + ServiceConfigurationLocal.HELPLINE_NUMBER + " text,"

            + " " + ServiceConfigurationGlobal.DESCRIPTION_SHORT + " text,"
            + " " + ServiceConfigurationGlobal.DESCRIPTION_LONG + " text,"

            + " " + ServiceConfigurationLocal.ADDRESS + " text,"
            + " " + ServiceConfigurationLocal.CITY + " text,"
            + " " + ServiceConfigurationLocal.PINCODE + " BIGINT,"
            + " " + ServiceConfigurationLocal.LANDMARK + " text,"
            + " " + ServiceConfigurationLocal.STATE + " text,"
            + " " + ServiceConfigurationLocal.COUNTRY + " text,"

            + " " + ServiceConfigurationLocal.ISO_COUNTRY_CODE + " text,"
            + " " + ServiceConfigurationLocal.ISO_LANGUAGE_CODE + " text,"
            + " " + ServiceConfigurationLocal.ISO_CURRENCY_CODE + " text,"

            + " " + ServiceConfigurationLocal.SERVICE_TYPE + " INT,"
            + " " + ServiceConfigurationLocal.SERVICE_LEVEL + " INT,"

            + " " + ServiceConfigurationLocal.LAT_CENTER + " FLOAT,"
            + " " + ServiceConfigurationLocal.LON_CENTER + " FLOAT,"
            + " " + ServiceConfigurationLocal.SERVICE_RANGE + " FLOAT,"

            + " " + ServiceConfigurationLocal.UPDATED + " timestamp with time zone,"
            + " " + ServiceConfigurationLocal.CREATED + " timestamp with time zone NOT NULL DEFAULT now()"
            + ")";






    // Instance Variables
    private int serviceID;

//    private String imagePath;
    private String logoImagePath;
    private String backdropImagePath;

    private String serviceName;
    private String helplineNumber;

    private String descriptionShort;
    private String descriptionLong;

    private String address;
    private String city;
    private Long pincode;

    private String landmark;
    private String state;
    private String country;

    private String ISOCountryCode;
    private String ISOLanguageCode;
    private String ISOCurrencyCode;

    private Integer serviceType;
    private Integer serviceLevel;

    private double latCenter;
    private double lonCenter;

    private Integer serviceRange;
//    private Integer shopDeliveryRangeMax;

    private Timestamp created;
    private Timestamp updated;

    // real time variables : the values of these variables are generated in real time.
    private Double rt_distance;


    public String getDescriptionShort() {
        return descriptionShort;
    }

    public void setDescriptionShort(String descriptionShort) {
        this.descriptionShort = descriptionShort;
    }

    public String getDescriptionLong() {
        return descriptionLong;
    }

    public void setDescriptionLong(String descriptionLong) {
        this.descriptionLong = descriptionLong;
    }

    public String getISOCurrencyCode() {
        return ISOCurrencyCode;
    }

    public void setISOCurrencyCode(String ISOCurrencyCode) {
        this.ISOCurrencyCode = ISOCurrencyCode;
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

    public Double getRt_distance() {
        return rt_distance;
    }

    public void setRt_distance(Double rt_distance) {
        this.rt_distance = rt_distance;
    }

    public String getISOCountryCode() {
        return ISOCountryCode;
    }

    public void setISOCountryCode(String ISOCountryCode) {
        this.ISOCountryCode = ISOCountryCode;
    }

    public String getISOLanguageCode() {
        return ISOLanguageCode;
    }

    public void setISOLanguageCode(String ISOLanguageCode) {
        this.ISOLanguageCode = ISOLanguageCode;
    }

    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }


    public String getLogoImagePath() {
        return logoImagePath;
    }

    public void setLogoImagePath(String logoImagePath) {
        this.logoImagePath = logoImagePath;
    }

    public String getBackdropImagePath() {
        return backdropImagePath;
    }

    public void setBackdropImagePath(String backdropImagePath) {
        this.backdropImagePath = backdropImagePath;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getHelplineNumber() {
        return helplineNumber;
    }

    public void setHelplineNumber(String helplineNumber) {
        this.helplineNumber = helplineNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getPincode() {
        return pincode;
    }

    public void setPincode(Long pincode) {
        this.pincode = pincode;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getServiceLevel() {
        return serviceLevel;
    }

    public void setServiceLevel(Integer serviceLevel) {
        this.serviceLevel = serviceLevel;
    }

    public Double getLatCenter() {
        return latCenter;
    }

    public void setLatCenter(Double latCenter) {
        this.latCenter = latCenter;
    }

    public Double getLonCenter() {
        return lonCenter;
    }

    public void setLonCenter(Double lonCenter) {
        this.lonCenter = lonCenter;
    }

    public Integer getServiceRange() {
        return serviceRange;
    }

    public void setServiceRange(Integer serviceRange) {
        this.serviceRange = serviceRange;
    }

//    public Double getLatMax() {
//        return latMax;
//    }
//
//    public void setLatMax(Double latMax) {
//        this.latMax = latMax;
//    }
//
//    public Double getLonMax() {
//        return lonMax;
//    }
//
//    public void setLonMax(Double lonMax) {
//        this.lonMax = lonMax;
//    }
//
//    public Double getLatMin() {
//        return latMin;
//    }
//
//    public void setLatMin(Double latMin) {
//        this.latMin = latMin;
//    }
//
//    public Double getLonMin() {
//        return lonMin;
//    }
//
//    public void setLonMin(Double lonMin) {
//        this.lonMin = lonMin;
//    }
}
