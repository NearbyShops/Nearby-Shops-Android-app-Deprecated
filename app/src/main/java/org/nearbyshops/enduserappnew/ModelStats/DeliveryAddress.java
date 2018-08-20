package org.nearbyshops.enduserappnew.ModelStats;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sumeet on 10/6/16.
 */
public class DeliveryAddress implements Parcelable{


    // Table Name
    public static final String TABLE_NAME = "DELIVERY_ADDRESS";

    // column Names
    public static final String ID = "ID";
    public static final String NAME = "DISTRIBUTOR_NAME";
    public static final String PHONE_NUMBER = "PHONE_NUMBER";

    public static final String DELIVERY_ADDRESS = "DELIVERY_ADDRESS";
    public static final String CITY = "CITY";

    public static final String PINCODE = "PINCODE";
    public static final String LANDMARK = "LANDMARK";
    public static final String END_USER_ID = "END_USER_ID"; // Primary Key

    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";


    public DeliveryAddress() {
    }

    // instance variables
    private int id;
    private String name;
    private long phoneNumber;
    private String deliveryAddress;
    private String city;
    private long pincode;
    private String landmark;
    private double latitude;
    private double longitude;
    private int endUserID;


    protected DeliveryAddress(Parcel in) {
        id = in.readInt();
        name = in.readString();
        phoneNumber = in.readLong();
        deliveryAddress = in.readString();
        city = in.readString();
        pincode = in.readLong();
        landmark = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        endUserID = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeLong(phoneNumber);
        dest.writeString(deliveryAddress);
        dest.writeString(city);
        dest.writeLong(pincode);
        dest.writeString(landmark);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeInt(endUserID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DeliveryAddress> CREATOR = new Creator<DeliveryAddress>() {
        @Override
        public DeliveryAddress createFromParcel(Parcel in) {
            return new DeliveryAddress(in);
        }

        @Override
        public DeliveryAddress[] newArray(int size) {
            return new DeliveryAddress[size];
        }
    };

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getEndUserID() {
        return endUserID;
    }

    public void setEndUserID(int endUserID) {
        this.endUserID = endUserID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public long getPincode() {
        return pincode;
    }

    public void setPincode(long pincode) {
        this.pincode = pincode;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }
}
