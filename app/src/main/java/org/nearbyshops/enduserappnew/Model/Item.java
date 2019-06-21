package org.nearbyshops.enduserappnew.Model;

import org.nearbyshops.enduserappnew.Model.ModelStats.ItemStats;
import java.sql.Timestamp;

public class Item{

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


	private float rt_rating_avg;
	private float rt_rating_count;



	// getter and setter methods


	public int getItemID() {
		return itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
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

	public int getItemCategoryID() {
		return itemCategoryID;
	}

	public void setItemCategoryID(int itemCategoryID) {
		this.itemCategoryID = itemCategoryID;
	}

	public ItemStats getItemStats() {
		return itemStats;
	}

	public void setItemStats(ItemStats itemStats) {
		this.itemStats = itemStats;
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

	public ItemCategory getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(ItemCategory itemCategory) {
		this.itemCategory = itemCategory;
	}

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
