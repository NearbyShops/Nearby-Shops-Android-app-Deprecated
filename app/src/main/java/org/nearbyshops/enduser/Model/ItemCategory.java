package org.nearbyshops.enduser.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemCategory implements Parcelable{

	
	int itemCategoryID;
	
	String categoryName;
	String categoryDescription;
	
	//AbstractCategory parentCategory;
	
	// items contained in this category
	//@OneToMany(mappedBy="itemCategory")
	
	//List<Item> items = new ArrayList<Item>();

	int parentCategoryID;
	boolean isLeafNode;

	String imagePath;

	// variables for utility functions

	ItemCategory parentCategory = null;






	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public int getParentCategoryID() {
		return parentCategoryID;
	}

	public void setParentCategoryID(int parentCategoryID) {
		this.parentCategoryID = parentCategoryID;
	}


	public boolean getIsLeafNode() {
		return isLeafNode;
	}

	public void setIsLeafNode(boolean isLeafNode) {
		this.isLeafNode = isLeafNode;
	}



	//no-args Constructor
	public ItemCategory() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	//Getters and Setters

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


	public ItemCategory getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(ItemCategory parentCategory) {
		this.parentCategory = parentCategory;
	}


	// parcelable Implementation

	protected ItemCategory(Parcel in) {
		itemCategoryID = in.readInt();
		categoryName = in.readString();
		categoryDescription = in.readString();
		parentCategoryID = in.readInt();
		isLeafNode = in.readByte() != 0;
		imagePath = in.readString();
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

}
