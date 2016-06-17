package org.nearbyshops.enduser.Model;


import android.os.Parcel;
import android.os.Parcelable;

import org.nearbyshops.enduser.ModelStats.ItemStats;

import java.sql.Timestamp;

public class Item implements Parcelable{


	ItemCategory itemCategory;
	ItemStats itemStats;

	int itemID;
	
	String itemName;
	String itemDescription;
	String itemImageURL;
	
	//technically it is the name of the manufacturer

	int itemCategoryID;



	// recently added
	String quantityUnit;
	Timestamp dateTimeCreated;
	String itemDescriptionLong;









	protected Item(Parcel in) {
		itemCategory = in.readParcelable(ItemCategory.class.getClassLoader());
		itemStats = in.readParcelable(ItemStats.class.getClassLoader());
		itemID = in.readInt();
		itemName = in.readString();
		itemDescription = in.readString();
		itemImageURL = in.readString();
		itemCategoryID = in.readInt();
		quantityUnit = in.readString();
		itemDescriptionLong = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(itemCategory, flags);
		dest.writeParcelable(itemStats, flags);
		dest.writeInt(itemID);
		dest.writeString(itemName);
		dest.writeString(itemDescription);
		dest.writeString(itemImageURL);
		dest.writeInt(itemCategoryID);
		dest.writeString(quantityUnit);
		dest.writeString(itemDescriptionLong);
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




	/*

	// Getter and Setter Methods

	 */


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



	//No-args constructor

	public Item() {
		super();
		// TODO Auto-generated constructor stub
	}



	// getters and Setter methods

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



	/*
	*	Parcelable Implementation
	*
	*
	*
	*/


}
