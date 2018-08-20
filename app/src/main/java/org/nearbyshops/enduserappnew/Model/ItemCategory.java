package org.nearbyshops.enduserappnew.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemCategory implements Parcelable{

	// contract class describing the Globals schema for the ItemCategory

	// Table Name
	public static final String TABLE_NAME = "ITEM_CATEGORY";

	// Column Names
	public static final String ITEM_CATEGORY_ID = "ID";
	public static final String ITEM_CATEGORY_NAME = "ITEM_CATEGORY_NAME";
	public static final String ITEM_CATEGORY_DESCRIPTION = "ITEM_CATEGORY_DESC";
	public static final String PARENT_CATEGORY_ID = "PARENT_CATEGORY_ID";
	public static final String IS_LEAF_NODE = "IS_LEAF";
	public static final String IMAGE_PATH = "IMAGE_PATH";
	public static final String CATEGORY_ORDER = "CATEGORY_ORDER";

	// to be implemented
	public static final String ITEM_CATEGORY_DESCRIPTION_SHORT = "ITEM_CATEGORY_DESCRIPTION_SHORT";
	public static final String IS_ABSTRACT = "IS_ABSTRACT";


	// to be Implemented
	public static final String IS_ENABLED = "IS_ENABLED";
	public static final String IS_WAITLISTED = "IS_WAITLISTED";




	// Create Table Statement


	public static final String createTableItemCategoryPostgres = "CREATE TABLE IF NOT EXISTS "
			+ ItemCategory.TABLE_NAME + "("
			+ " " + ItemCategory.ITEM_CATEGORY_ID + " SERIAL PRIMARY KEY,"
			+ " " + ItemCategory.ITEM_CATEGORY_NAME + " text,"
			+ " " + ItemCategory.ITEM_CATEGORY_DESCRIPTION + " text,"
			+ " " + ItemCategory.PARENT_CATEGORY_ID + " INT,"
			+ " " + ItemCategory.IS_LEAF_NODE + " boolean,"
			+ " " + ItemCategory.IMAGE_PATH + " text,"
			+ " " + ItemCategory.CATEGORY_ORDER + " INT,"

			+ " " + ItemCategory.ITEM_CATEGORY_DESCRIPTION_SHORT + " text,"
			+ " " + ItemCategory.IS_ABSTRACT + " boolean,"

			+ " " + ItemCategory.IS_ENABLED + " boolean,"
			+ " " + ItemCategory.IS_WAITLISTED + " boolean,"
			+ " FOREIGN KEY(" + ItemCategory.PARENT_CATEGORY_ID +") REFERENCES "
			+ ItemCategory.TABLE_NAME + "(" + ItemCategory.ITEM_CATEGORY_ID + ")"
			+ ")";



	// Instance Variables


	private int itemCategoryID;
	private String categoryName;
	private String categoryDescription;
	private int parentCategoryID;
	private boolean isLeafNode;
	private String imagePath;
	private int categoryOrder;
	// recently added
	private boolean isAbstractNode;
	private String descriptionShort;
	private boolean isEnabled;
	private boolean isWaitlisted;


	private String rt_gidb_service_url;
	ItemCategory parentCategory = null;


	protected ItemCategory(Parcel in) {
		itemCategoryID = in.readInt();
		categoryName = in.readString();
		categoryDescription = in.readString();
		parentCategoryID = in.readInt();
		isLeafNode = in.readByte() != 0;
		imagePath = in.readString();
		categoryOrder = in.readInt();
		isAbstractNode = in.readByte() != 0;
		descriptionShort = in.readString();
		isEnabled = in.readByte() != 0;
		isWaitlisted = in.readByte() != 0;
		rt_gidb_service_url = in.readString();
		parentCategory = in.readParcelable(ItemCategory.class.getClassLoader());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(itemCategoryID);
		dest.writeString(categoryName);
		dest.writeString(categoryDescription);
		dest.writeInt(parentCategoryID);
		dest.writeByte((byte) (isLeafNode ? 1 : 0));
		dest.writeString(imagePath);
		dest.writeInt(categoryOrder);
		dest.writeByte((byte) (isAbstractNode ? 1 : 0));
		dest.writeString(descriptionShort);
		dest.writeByte((byte) (isEnabled ? 1 : 0));
		dest.writeByte((byte) (isWaitlisted ? 1 : 0));
		dest.writeString(rt_gidb_service_url);
		dest.writeParcelable(parentCategory, flags);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<ItemCategory> CREATOR = new Creator<ItemCategory>() {
		@Override
		public ItemCategory createFromParcel(Parcel in) {
			return new ItemCategory(in);
		}

		@Override
		public ItemCategory[] newArray(int size) {
			return new ItemCategory[size];
		}
	};

	public String getRt_gidb_service_url() {
		return rt_gidb_service_url;
	}

	public void setRt_gidb_service_url(String rt_gidb_service_url) {
		this.rt_gidb_service_url = rt_gidb_service_url;
	}

	//no-args Constructor
	public ItemCategory() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ItemCategory getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(ItemCategory parentCategory) {
		this.parentCategory = parentCategory;
	}


	//Getters and Setters


	public Integer getCategoryOrder() {
		return categoryOrder;
	}

	public void setCategoryOrder(Integer categoryOrder) {
		this.categoryOrder = categoryOrder;
	}

	public Boolean getisAbstractNode() {
		return isAbstractNode;
	}

	public void setisAbstractNode(Boolean abstractNode) {
		isAbstractNode = abstractNode;
	}

	public String getDescriptionShort() {
		return descriptionShort;
	}

	public void setDescriptionShort(String descriptionShort) {
		this.descriptionShort = descriptionShort;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryDescription() {
		return categoryDescription;
	}

	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}

	public int getItemCategoryID() {
		return itemCategoryID;
	}

	public void setItemCategoryID(int itemCategoryID) {
		this.itemCategoryID = itemCategoryID;
	}


	public Integer getParentCategoryID() {
		return parentCategoryID;
	}

	public void setParentCategoryID(Integer parentCategoryID) {
		this.parentCategoryID = parentCategoryID;
	}

	public boolean getIsLeafNode() {
		return isLeafNode;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean enabled) {
		isEnabled = enabled;
	}

	public boolean isWaitlisted() {
		return isWaitlisted;
	}

	public void setWaitlisted(boolean waitlisted) {
		isWaitlisted = waitlisted;
	}

	public void setIsLeafNode(boolean isLeafNode) {
		this.isLeafNode = isLeafNode;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
}
