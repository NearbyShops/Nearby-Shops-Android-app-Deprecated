package org.nearbyshops.enduser.Model;


public class ShopItem{
	
	public static final String UNIT_KG = "Kg.";
	public static final String UNIT_GRAMS = "Grams.";
	
	//int shopID;

	public ShopItem() {
		super();
		// TODO Auto-generated constructor stub
	}





	// holding shop and item reference for parsing JSON
	Shop shop;

	//int itemID;
	Item item;


	// Foreign Key only for JDBC
	int shopID;
	
	// foreign Key for JDBC
	int itemID;
	
	
	int availableItemQuantity;
	
	double itemPrice;
	
		
	// put this into item
	// the units of quantity for item. For Example if you are buying vegetables 
	String quantityUnit;

	// consider that if you want to buy in the multiples of 500 grams. You would buy 500grams,1000grams, 1500grams, 2000grams
	int quantityMultiple;

	
	// in certain cases the shop might take extra delivery charge for the particular item 
	// in most of the cases this charge would be zero, unless in some cases that item is so big that 
	// it requires special delivery. For example if you are buying some furniture. In that case the furniture
	
	
	// would require some special arrangement for delivery which might involve extra delivery cost
	//int extraDeliveryCharge = 0;
	
	// the minimum quantity that a end user - customer can buy 
	//int minQuantity;
	
	// the maximum quantity of this item that an end user can buy
	//int maxQuantity;


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

	public String getQuantityUnit() {
		return quantityUnit;
	}


	public void setQuantityUnit(String quantityUnit) {
		this.quantityUnit = quantityUnit;
	}


	public int getQuantityMultiple() {
		return quantityMultiple;
	}


	public void setQuantityMultiple(int quantityMultiple) {
		this.quantityMultiple = quantityMultiple;
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