package org.nearbyshops.enduser.Model;


import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;

public class ShopItem implements Parcelable{
	
	public static final String UNIT_KG = "Kg.";
	public static final String UNIT_GRAMS = "Grams.";
	
	//int shopID;

	public ShopItem() {
		super();
		// TODO Auto-generated constructor stub
	}





	// holding shop and item reference for parsing JSON
	private Shop shop;
	//int itemID;
	private Item item;


	private int shopID;
	private int itemID;
	private int availableItemQuantity;
	private double itemPrice;

	
	// in certain cases the shop might take extra delivery charge for the particular item 
	// in most of the cases this charge would be zero, unless in some cases that item is so big that 
	// it requires special delivery. For example if you are buying some furniture. In that case the furniture
	
	
	// would require some special arrangement for delivery which might involve extra delivery cost
	//int extraDeliveryCharge = 0;
	
	// the minimum quantity that a end user - customer can buy 
	//int minQuantity;
	
	// the maximum quantity of this item that an end user can buy
	//int maxQuantity;




	// recently added variables
	private int extraDeliveryCharge;
	private Timestamp dateTimeAdded;
	private Timestamp lastUpdateDateTime;


	protected ShopItem(Parcel in) {
		shop = in.readParcelable(Shop.class.getClassLoader());
		item = in.readParcelable(Item.class.getClassLoader());
		shopID = in.readInt();
		itemID = in.readInt();
		availableItemQuantity = in.readInt();
		itemPrice = in.readDouble();
		extraDeliveryCharge = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(shop, flags);
		dest.writeParcelable(item, flags);
		dest.writeInt(shopID);
		dest.writeInt(itemID);
		dest.writeInt(availableItemQuantity);
		dest.writeDouble(itemPrice);
		dest.writeInt(extraDeliveryCharge);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<ShopItem> CREATOR = new Creator<ShopItem>() {
		@Override
		public ShopItem createFromParcel(Parcel in) {
			return new ShopItem(in);
		}

		@Override
		public ShopItem[] newArray(int size) {
			return new ShopItem[size];
		}
	};

	public int getExtraDeliveryCharge() {
		return extraDeliveryCharge;
	}

	public void setExtraDeliveryCharge(int extraDeliveryCharge) {
		this.extraDeliveryCharge = extraDeliveryCharge;
	}

	public Timestamp getDateTimeAdded() {
		return dateTimeAdded;
	}

	public void setDateTimeAdded(Timestamp dateTimeAdded) {
		this.dateTimeAdded = dateTimeAdded;
	}

	public Timestamp getLastUpdateDateTime() {
		return lastUpdateDateTime;
	}

	public void setLastUpdateDateTime(Timestamp lastUpdateDateTime) {
		this.lastUpdateDateTime = lastUpdateDateTime;
	}

	public double getItemPrice() {
		return itemPrice;
	}


	public void setItemPrice(double itemPrice) {
		this.itemPrice = itemPrice;
	}


	public int getShopID() {
		return shopID;
	}


	public void setShopID(int shopID) {
		this.shopID = shopID;
	}


	public int getItemID() {
		return itemID;
	}


	public void setItemID(int itemID) {
		this.itemID = itemID;
	}


	public int getAvailableItemQuantity() {
		return availableItemQuantity;
	}


	public void setAvailableItemQuantity(int availableItemQuantity) {
		this.availableItemQuantity = availableItemQuantity;
	}


	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
}