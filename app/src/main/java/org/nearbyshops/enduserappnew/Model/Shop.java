package org.nearbyshops.enduserappnew.Model;



import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;

public class Shop implements Parcelable{




	// Shop Table Name
	public static final String TABLE_NAME = "SHOP";

	// Shop columns

	public static final String SHOP_ID = "SHOP_ID";
	public static final String SHOP_NAME = "SHOP_NAME";
	public static final String DELIVERY_RANGE = "DELIVERY_RANGE";
	public static final String LAT_CENTER = "LAT_CENTER";
	public static final String LON_CENTER = "LON_CENTER";

	public static final String DELIVERY_CHARGES = "DELIVERY_CHARGES";
	public static final String BILL_AMOUNT_FOR_FREE_DELIVERY = "BILL_AMOUNT_FOR_FREE_DELIVERY";
	// to be added
	public static final String PICK_FROM_SHOP_AVAILABLE = "PICK_FROM_SHOP_AVAILABLE";
	public static final String HOME_DELIVERY_AVAILABLE = "HOME_DELIVERY_AVAILABLE";

	public static final String LOGO_IMAGE_PATH = "LOGO_IMAGE_PATH";

	// recently Added
	public static final String SHOP_ADDRESS = "SHOP_ADDRESS";
	public static final String CITY = "CITY";
	public static final String PINCODE = "PINCODE";
	public static final String LANDMARK = "LANDMARK";

	public static final String CUSTOMER_HELPLINE_NUMBER = "CUSTOMER_HELPLINE_NUMBER";
	public static final String DELIVERY_HELPLINE_NUMBER = "DELIVERY_HELPLINE_NUMBER";
	public static final String SHORT_DESCRIPTION = "SHORT_DESCRIPTION";
	public static final String LONG_DESCRIPTION = "LONG_DESCRIPTION";
	public static final String DATE_TIME_STARTED = "DATE_TIME_STARTED";
	public static final String IS_OPEN = "IS_SHOP_OPEN";

	// to be added
	public static final String SHOP_ENABLED = "SHOP_ENABLED";
	public static final String SHOP_WAITLISTED = "SHOP_WAITLISTED";

	// deprecated columns
//	public static final String LAT_MAX = "LAT_MAX";
//	public static final String LON_MAX = "LON_MAX";
//	public static final String LAT_MIN = "LAT_MIN";
//	public static final String LON_MIN = "LON_MIN";
//	public static final String DISTRIBUTOR_ID = "Distributor";


	// deleted columns

//	+ " " + Shop.DISTRIBUTOR_ID + " INT,"

//			+ " " + Shop.LON_MAX + " FLOAT,"
//			+ " " + Shop.LAT_MAX + " FLOAT,"
//			+ " " + Shop.LON_MIN + " FLOAT,"
//			+ " " + Shop.LAT_MIN + " FLOAT,"

	//	public static final String IMAGE_PATH = "IMAGE_PATH";
//	public static final String BACKDROP_IMAGE_PATH = "BACKDROP_IMAGE_PATH";




	// query postgres

	public static final String createTableShopPostgres =
			"CREATE TABLE IF NOT EXISTS " + Shop.TABLE_NAME + "("
			+ " " + Shop.SHOP_ID + " SERIAL PRIMARY KEY,"
			+ " " + Shop.SHOP_NAME + " text,"

			+ " " + Shop.DELIVERY_RANGE + " FLOAT,"
			+ " " + Shop.LON_CENTER + " FLOAT,"
			+ " " + Shop.LAT_CENTER + " FLOAT,"

			+ " " + Shop.DELIVERY_CHARGES + " FLOAT,"
			+ " " + Shop.BILL_AMOUNT_FOR_FREE_DELIVERY + " INT,"
			+ " " + Shop.PICK_FROM_SHOP_AVAILABLE + " boolean,"
			+ " " + Shop.HOME_DELIVERY_AVAILABLE + " boolean,"

			+ " " + Shop.SHOP_ENABLED + " boolean,"
			+ " " + Shop.SHOP_WAITLISTED + " boolean,"

			+ " " + Shop.LOGO_IMAGE_PATH + " text,"

			+ " " + Shop.SHOP_ADDRESS + " text,"
			+ " " + Shop.CITY + " text,"
			+ " " + Shop.PINCODE + " INT,"
			+ " " + Shop.LANDMARK + " text,"

			+ " " + Shop.CUSTOMER_HELPLINE_NUMBER + " text,"
			+ " " + Shop.DELIVERY_HELPLINE_NUMBER + " text,"

			+ " " + Shop.SHORT_DESCRIPTION + " text,"
			+ " " + Shop.LONG_DESCRIPTION + " text,"

			+ " " + Shop.DATE_TIME_STARTED + " timestamp with time zone NOT NULL DEFAULT now(),"
			+ " " + Shop.IS_OPEN + " boolean,"

			// shop admin fields / columns
			+ " " + ShopAdmin.NAME + " text,"
			+ " " + ShopAdmin.USERNAME + " text,"
			+ " " + ShopAdmin.PASSWORD + " text,"
			+ " " + ShopAdmin.PROFILE_IMAGE_URL + " text,"
			+ " " + ShopAdmin.PHONE_NUMBER + " text,"
			+ " " + ShopAdmin.ADMIN_ENABLED + " boolean,"
			+ " " + ShopAdmin.ADMIN_WAITLISTED + " boolean" + ")";


//			+ " FOREIGN KEY(" + Shop.DISTRIBUTOR_ID +") REFERENCES " + Distributor.TABLE_NAME + "(" + Distributor.DISTRIBUTOR_ID + ")"
//			+ ")"


	public Shop() {
		super();
	}



	// normal variables
	private int shopID;
	
	private String shopName;

	// the radius of the circle considering shop location as its center.
	//This is the distance upto which shop can deliver its items
	private double deliveryRange;

	// latitude and longitude for storing the location of the shop
	private double latCenter;
	private double lonCenter;

	// delivery charger per order
	private double deliveryCharges;
	private int billAmountForFreeDelivery;
	private boolean pickFromShopAvailable;
	private boolean homeDeliveryAvailable;

	private boolean shopEnabled;
	private boolean shopWaitlisted;


	
	private String logoImagePath;


	// added recently
	private String shopAddress;
	private String city;
	private long pincode;
	private String landmark;

	private String customerHelplineNumber;
	private String deliveryHelplineNumber;

	private String shortDescription;
	private String longDescription;

	private Timestamp dateTimeStarted;
	private boolean isOpen;


	// real time variables
	private double rt_distance;
	private float rt_rating_avg;
	private float rt_rating_count;



	// deleted columns
	// bounding coordinates for the shop generated using shop center coordinates and delivery range.
//	private double latMax;
//	private double lonMax;
//	private double latMin;
//	private double lonMin;


	protected Shop(Parcel in) {
		shopID = in.readInt();
		shopName = in.readString();
		deliveryRange = in.readDouble();
		latCenter = in.readDouble();
		lonCenter = in.readDouble();
		deliveryCharges = in.readDouble();
		billAmountForFreeDelivery = in.readInt();
		logoImagePath = in.readString();
		shopAddress = in.readString();
		city = in.readString();
		pincode = in.readLong();
		landmark = in.readString();
		customerHelplineNumber = in.readString();
		deliveryHelplineNumber = in.readString();
		shortDescription = in.readString();
		longDescription = in.readString();
		isOpen = in.readByte() != 0;
		rt_distance = in.readDouble();
		rt_rating_avg = in.readFloat();
		rt_rating_count = in.readFloat();

		dateTimeStarted = new Timestamp(in.readLong());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(shopID);
		dest.writeString(shopName);
		dest.writeDouble(deliveryRange);
		dest.writeDouble(latCenter);
		dest.writeDouble(lonCenter);
		dest.writeDouble(deliveryCharges);
		dest.writeInt(billAmountForFreeDelivery);
		dest.writeString(logoImagePath);
		dest.writeString(shopAddress);
		dest.writeString(city);
		dest.writeLong(pincode);
		dest.writeString(landmark);
		dest.writeString(customerHelplineNumber);
		dest.writeString(deliveryHelplineNumber);
		dest.writeString(shortDescription);
		dest.writeString(longDescription);
		dest.writeByte((byte) (isOpen ? 1 : 0));
		dest.writeDouble(rt_distance);
		dest.writeFloat(rt_rating_avg);
		dest.writeFloat(rt_rating_count);

		if(dateTimeStarted!=null)
		{
			dest.writeLong(dateTimeStarted.getTime());
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

	public static final Creator<Shop> CREATOR = new Creator<Shop>() {
		@Override
		public Shop createFromParcel(Parcel in) {
			return new Shop(in);
		}

		@Override
		public Shop[] newArray(int size) {
			return new Shop[size];
		}
	};

	public int getShopID() {
		return shopID;
	}

	public void setShopID(int shopID) {
		this.shopID = shopID;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public double getDeliveryRange() {
		return deliveryRange;
	}

	public void setDeliveryRange(double deliveryRange) {
		this.deliveryRange = deliveryRange;
	}

	public double getLatCenter() {
		return latCenter;
	}

	public void setLatCenter(double latCenter) {
		this.latCenter = latCenter;
	}

	public double getLonCenter() {
		return lonCenter;
	}

	public void setLonCenter(double lonCenter) {
		this.lonCenter = lonCenter;
	}

	public double getDeliveryCharges() {
		return deliveryCharges;
	}

	public void setDeliveryCharges(double deliveryCharges) {
		this.deliveryCharges = deliveryCharges;
	}

	public int getBillAmountForFreeDelivery() {
		return billAmountForFreeDelivery;
	}

	public void setBillAmountForFreeDelivery(int billAmountForFreeDelivery) {
		this.billAmountForFreeDelivery = billAmountForFreeDelivery;
	}

	public Boolean getPickFromShopAvailable() {
		return pickFromShopAvailable;
	}

	public void setPickFromShopAvailable(Boolean pickFromShopAvailable) {
		this.pickFromShopAvailable = pickFromShopAvailable;
	}

	public Boolean getHomeDeliveryAvailable() {
		return homeDeliveryAvailable;
	}

	public void setHomeDeliveryAvailable(Boolean homeDeliveryAvailable) {
		this.homeDeliveryAvailable = homeDeliveryAvailable;
	}

	public Boolean getShopEnabled() {
		return shopEnabled;
	}

	public void setShopEnabled(Boolean shopEnabled) {
		this.shopEnabled = shopEnabled;
	}

	public Boolean getShopWaitlisted() {
		return shopWaitlisted;
	}

	public void setShopWaitlisted(Boolean shopWaitlisted) {
		this.shopWaitlisted = shopWaitlisted;
	}

	public String getLogoImagePath() {
		return logoImagePath;
	}

	public void setLogoImagePath(String logoImagePath) {
		this.logoImagePath = logoImagePath;
	}

	public String getShopAddress() {
		return shopAddress;
	}

	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
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

	public String getCustomerHelplineNumber() {
		return customerHelplineNumber;
	}

	public void setCustomerHelplineNumber(String customerHelplineNumber) {
		this.customerHelplineNumber = customerHelplineNumber;
	}

	public String getDeliveryHelplineNumber() {
		return deliveryHelplineNumber;
	}

	public void setDeliveryHelplineNumber(String deliveryHelplineNumber) {
		this.deliveryHelplineNumber = deliveryHelplineNumber;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public Timestamp getDateTimeStarted() {
		return dateTimeStarted;
	}

	public void setDateTimeStarted(Timestamp dateTimeStarted) {
		this.dateTimeStarted = dateTimeStarted;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean open) {
		isOpen = open;
	}

	public double getRt_distance() {
		return rt_distance;
	}

	public void setRt_distance(double rt_distance) {
		this.rt_distance = rt_distance;
	}

	public float getRt_rating_avg() {
		return rt_rating_avg;
	}

	public void setRt_rating_avg(float rt_rating_avg) {
		this.rt_rating_avg = rt_rating_avg;
	}

	public float getRt_rating_count() {
		return rt_rating_count;
	}

	public void setRt_rating_count(float rt_rating_count) {
		this.rt_rating_count = rt_rating_count;
	}
}
