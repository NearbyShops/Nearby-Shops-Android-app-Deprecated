package org.nearbyshops.enduser.Model;


import android.os.Parcel;
import android.os.Parcelable;

import org.nearbyshops.enduser.ModelStats.ItemStats;

import java.sql.Timestamp;

public class Item implements Parcelable{

	// Table Name
	public static final String TABLE_NAME = "ITEM";

	// column names
	public static final String ITEM_ID = "ITEM_ID";
	public static final String ITEM_NAME = "ITEM_NAME";
	public static final String ITEM_DESC = "ITEM_DESC";

	public static final String ITEM_IMAGE_URL = "ITEM_IMAGE_URL";
	public static final String BACKDROP_IMAGE_ID = "BACKDROP_IMAGE_ID";
	//public static final String ITEM_BRAND_NAME = "ITEM_BRAND_NAME";
	public static final String ITEM_CATEGORY_ID = "ITEM_CATEGORY_ID";

	// recently added
	public static final String QUANTITY_UNIT = "QUANTITY_UNIT";
	public static final String DATE_TIME_CREATED = "DATE_TIME_CREATED";
	public static final String ITEM_DESCRIPTION_LONG = "ITEM_DESCRIPTION_LONG";

	// To be added
	public static final String IS_ENABLED = "IS_ENABLED";
	public static final String IS_WAITLISTED = "IS_WAITLISTED";


	// Create Table Statement
	public static final String createTableItemPostgres = "CREATE TABLE IF NOT EXISTS "
			+ Item.TABLE_NAME + "("
			+ " " + Item.ITEM_ID + " SERIAL PRIMARY KEY,"
			+ " " + Item.ITEM_NAME + " text,"
			+ " " + Item.ITEM_DESC + " text,"
			+ " " + Item.ITEM_DESCRIPTION_LONG + " text,"
			+ " " + Item.ITEM_IMAGE_URL + " text,"
			+ " " + Item.QUANTITY_UNIT + " text,"
			+ " " + Item.ITEM_CATEGORY_ID + " INT,"
			+ " " + Item.IS_ENABLED + " boolean,"
			+ " " + Item.IS_WAITLISTED + " boolean,"
			+ " " + Item.DATE_TIME_CREATED + "  timestamp with time zone NOT NULL DEFAULT now(),"
			+ " FOREIGN KEY(" + Item.ITEM_CATEGORY_ID +") REFERENCES ITEM_CATEGORY(ID))";


	// Instance Variables

	private int itemID;


	private String itemName;


	private String itemDescription;
	private String itemImageURL;
	
	//technically it is the name of the manufacturer 
	// Typically its the name of the manufacturer
	
	// Only required for JDBC
	private int itemCategoryID;
	private ItemStats itemStats;

	// recently added
	private String quantityUnit;
	private Timestamp dateTimeCreated;
	private String itemDescriptionLong;
	private ItemCategory itemCategory;
	private Boolean isEnabled;
	private Boolean isWaitlisted;


	// Getter and Setter Statements

	//No-args constructor


	protected Item(Parcel in) {
		itemID = in.readInt();
		itemName = in.readString();
		itemDescription = in.readString();
		itemImageURL = in.readString();
		itemCategoryID = in.readInt();
		itemStats = in.readParcelable(ItemStats.class.getClassLoader());
		quantityUnit = in.readString();
		itemDescriptionLong = in.readString();
		itemCategory = in.readParcelable(ItemCategory.class.getClassLoader());

		dateTimeCreated = new Timestamp(in.readLong());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(itemID);
		dest.writeString(itemName);
		dest.writeString(itemDescription);
		dest.writeString(itemImageURL);
		dest.writeInt(itemCategoryID);
		dest.writeParcelable(itemStats, flags);
		dest.writeString(quantityUnit);
		dest.writeString(itemDescriptionLong);
		dest.writeParcelable(itemCategory, flags);

		if(dateTimeCreated!=null)
		{
			dest.writeLong(dateTimeCreated.getTime());
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

	public static final Creator<Item> CREATOR = new Creator<Item>() {
		@Override
		public Item createFromParcel(Parcel in) {
			return new Item(in);
		}

		@Override
		public Item[] newArray(int size) {
			return new Item[size];
		}
	};

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

	public Item() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getQuantityUnit() {
		return quantityUnit;
	}
	public void setQuantityUnit(String quantityUnit) {
		this.quantityUnit = quantityUnit;
	}
	public Timestamp getDateTimeCreated() {
		return dateTimeCreated;
	}
	public void setDateTimeCreated(Timestamp dateTimeCreated) {
		this.dateTimeCreated = dateTimeCreated;
	}
	public String getItemDescriptionLong() {
		return itemDescriptionLong;
	}
	public void setItemDescriptionLong(String itemDescriptionLong) {
		this.itemDescriptionLong = itemDescriptionLong;
	}
	public ItemStats getItemStats() {
		return itemStats;
	}
	public void setItemStats(ItemStats itemStats) {
		this.itemStats = itemStats;
	}
	public int getItemCategoryID() {
		return itemCategoryID;
	}
	public void setItemCategoryID(int itemCategoryID) {
		this.itemCategoryID = itemCategoryID;
	}
	public ItemCategory getItemCategory() {
		return itemCategory;
	}
	public void setItemCategory(ItemCategory itemCategory) {
		this.itemCategory = itemCategory;
	}
	public int getItemID() {
		return itemID;
	}
	public void setItemID(int itemID) {
		this.itemID = itemID;
	}
	public String getItemDescription() {
		return itemDescription;
	}
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	public String getItemImageURL() {
		return itemImageURL;
	}
	public void setItemImageURL(String itemImageURL) {
		this.itemImageURL = itemImageURL;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}



/*
	if(dateTimeCreated!=null)
	{
		dest.writeLong(dateTimeCreated.getTime());
	}
	else
	{
		dest.writeLong(0);
	}

	*/


	/*dateTimeCreated = new Timestamp(in.readLong());*/
}
