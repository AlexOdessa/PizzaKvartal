package com.example.pizzakvartal;

import java.util.UUID;

public class Dish {
	private String mDishType;
	private String mDishName;
	private String mDishPrice1;
	private String mDishPrice2;
	private String mDishInfoFull;
	private String mUriPhoto;
	private int mDishQuantity;
	private long mDate;
	private String mDishId;
	private UUID mId;

	Dish() {
		mId = UUID.randomUUID();
		mDishQuantity=0;
	}

	
	
	
	



	public long getDate() {
		return mDate;
	}








	public void setDate(long mDate) {
		this.mDate = mDate;
	}








	public String getDishId() {
		return mDishId;
	}








	public void setDishId(String mDishId) {
		this.mDishId = mDishId;
	}








	public int getDishQuantity() {
		return mDishQuantity;
	}




	public void setDishQuantity(int mDishQuantity) {
		this.mDishQuantity = mDishQuantity;
	}




	public String getUriPhoto() {
		return mUriPhoto;
	}




	public void setUriPhoto(String mUriPhoto) {
		this.mUriPhoto = mUriPhoto;
	}




	public String getDishName() {
		return mDishName;
	}




	public void setDishName(String mDishName) {
		this.mDishName = mDishName;
	}




	public String getDishType() {
		return mDishType;
	}

	public void setDishType(String mDishType) {
		this.mDishType = mDishType;
	}

	public String getDishPrice1() {
		return mDishPrice1;
	}

	public void setDishPrice1(String mDishPrice1) {
		this.mDishPrice1 = mDishPrice1;
	}

	public String getDishPrice2() {
		return mDishPrice2;
	}

	public void setDishPrice2(String mDishPrice2) {
		this.mDishPrice2 = mDishPrice2;
	}

	public String getDishInfoFull() {
		return mDishInfoFull;
	}

	public void setDishInfoFull(String mDishInfoFull) {
		this.mDishInfoFull = mDishInfoFull;
	}

	public UUID getId() {
		return mId;
	}

}
