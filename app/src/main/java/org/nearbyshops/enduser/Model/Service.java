package org.nearbyshops.enduser.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;

/**
 * Created by sumeet on 19/6/16.
 */
public class Service implements Parcelable{

    int serviceID;
    String imagePath;
    String logoImagePath;

    String backdropImagePath;
    String serviceName;
    String helplineNumber;

    String address;
    String city;
    Long pincode;

    String landmark;
    String state;
    String country;

    String ISOCountryCode;
    String ISOLanguageCode;
    Integer serviceType;

    Integer serviceLevel;
    Double latCenter;
    Double lonCenter;

    Integer serviceRange;
    Boolean isEthicalServiceProvider;
    Boolean isVerified;

    Double latMax;
    Double lonMax;
    Double latMin;

    Double lonMin;
    String configurationNickname;
    String serviceURL;

    Timestamp created;
    Timestamp updated;


    // real time variables : the values of these variables are generated in real time.
    Double rt_distance;


    public Service() {
    }

    protected Service(Parcel in) {
        serviceID = in.readInt();
        imagePath = in.readString();
        logoImagePath = in.readString();
        backdropImagePath = in.readString();
        serviceName = in.readString();
        helplineNumber = in.readString();
        address = in.readString();
        city = in.readString();
        landmark = in.readString();
        state = in.readString();
        country = in.readString();
        ISOCountryCode = in.readString();
        ISOLanguageCode = in.readString();
        configurationNickname = in.readString();
        serviceURL = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(serviceID);
        dest.writeString(imagePath);
        dest.writeString(logoImagePath);
        dest.writeString(backdropImagePath);
        dest.writeString(serviceName);
        dest.writeString(helplineNumber);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(landmark);
        dest.writeString(state);
        dest.writeString(country);
        dest.writeString(ISOCountryCode);
        dest.writeString(ISOLanguageCode);
        dest.writeString(configurationNickname);
        dest.writeString(serviceURL);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Service> CREATOR = new Creator<Service>() {
        @Override
        public Service createFromParcel(Parcel in) {
            return new Service(in);
        }

        @Override
        public Service[] newArray(int size) {
            return new Service[size];
        }
    };

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

    public String getConfigurationNickname() {
        return configurationNickname;
    }

    public void setConfigurationNickname(String configurationNickname) {
        this.configurationNickname = configurationNickname;
    }

    public String getServiceURL() {
        return serviceURL;
    }

    public void setServiceURL(String serviceURL) {
        this.serviceURL = serviceURL;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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

    public Boolean getEthicalServiceProvider() {
        return isEthicalServiceProvider;
    }

    public void setEthicalServiceProvider(Boolean ethicalServiceProvider) {
        isEthicalServiceProvider = ethicalServiceProvider;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    public Double getLatMax() {
        return latMax;
    }

    public void setLatMax(Double latMax) {
        this.latMax = latMax;
    }

    public Double getLonMax() {
        return lonMax;
    }

    public void setLonMax(Double lonMax) {
        this.lonMax = lonMax;
    }

    public Double getLatMin() {
        return latMin;
    }

    public void setLatMin(Double latMin) {
        this.latMin = latMin;
    }

    public Double getLonMin() {
        return lonMin;
    }

    public void setLonMin(Double lonMin) {
        this.lonMin = lonMin;
    }
}
