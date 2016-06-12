package org.nearbyshops.enduser.ModelStats;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sumeet on 10/6/16.
 */
public class DeliveryAddress implements Parcelable {

    int id;
    String name;
    long phoneNumber;
    String deliveryAddress;
    String city;
    long pincode;
    String landmark;
    int endUserID;

    public DeliveryAddress() {
    }

    // getter and setter methods

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


    // Parcelable Implementation


    protected DeliveryAddress(Parcel in) {
        id = in.readInt();
        name = in.readString();
        phoneNumber = in.readLong();
        deliveryAddress = in.readString();
        city = in.readString();
        pincode = in.readLong();
        landmark = in.readString();
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
}
