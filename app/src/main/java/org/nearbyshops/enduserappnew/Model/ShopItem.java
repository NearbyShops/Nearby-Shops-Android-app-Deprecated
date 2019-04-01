package org.nearbyshops.enduserappnew.Model;


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


	// Table Name
	public static final String TABLE_NAME = "SHOP_ITEM";

	// column Names
	public static final String SHOP_ID = "SHOP_ID";
	public static final String ITEM_ID = "ITEM_ID";
	public static final String AVAILABLE_ITEM_QUANTITY = "AVAILABLE_ITEM_QUANTITY";
	public static final String ITEM_PRICE = "ITEM_PRICE";

	//public static final String QUANTITY_UNIT = "QUANTITY_UNIT";
	//public static final String QUANTITY_MULTIPLE = "QUANTITY_MULTIPLE";

	public static final String MIN_QUANTITY_PER_ORDER = "MIN_QUANTITY_PER_ORDER";
	public static final String MAX_QUANTITY_PER_ORDER = "MAX_QUANTITY_PER_ORDER";

	public static final String DATE_TIME_ADDED = "DATE_TIME_ADDED";
	public static final String LAST_UPDATE_DATE_TIME = "LAST_UPDATE_DATE_TIME";
	public static final String EXTRA_DELIVERY_CHARGE = "EXTRA_DELIVERY_CHARGE";




	// create table statement

	public static final String createTableShopItemPostgres = "CREATE TABLE IF NOT EXISTS " + ShopItem.TABLE_NAME + "("
			+ " " + ShopItem.ITEM_ID + " INT,"
			+ " " + ShopItem.SHOP_ID + " INT,"
			+ " " + ShopItem.AVAILABLE_ITEM_QUANTITY + " INT,"
			+ " " + ShopItem.ITEM_PRICE + " FLOAT,"
			+ " " + ShopItem.LAST_UPDATE_DATE_TIME + " timestamp with time zone,"
			+ " " + ShopItem.EXTRA_DELIVERY_CHARGE + " FLOAT,"
			+ " " + ShopItem.DATE_TIME_ADDED + " timestamp with time zone NOT NULL DEFAULT now(),"
			+ " FOREIGN KEY(" + ShopItem.SHOP_ID +") REFERENCES SHOP(SHOP_ID),"
			+ " FOREIGN KEY(" + ShopItem.ITEM_ID +") REFERENCES ITEM(ITEM_ID),"
			+ " PRIMARY KEY (" + ShopItem.SHOP_ID + ", " + ShopItem.ITEM_ID + ")"
			+ ")";







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