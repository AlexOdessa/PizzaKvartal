package com.example.pizzakvartal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DishLab {
	public static final String SHARED_PREFERENCES_TIME = "com.example.pizzakvartal_shared_preferences_time";
	ArrayList<Dish> mDishArray;
	ArrayList<Dish> mDishTypes;
	ArrayList<Dish> mHistoryArray;
	// ArrayList<Dish> mCart;
	SharedPreferences sharedPreferences;
	public static DishLab mDishLab;
	public static boolean sThreadRunning = false;

	private DishLab(Context ctx) {
		// TODO Auto-generated constructor stub
		mHistoryArray = new ArrayList<Dish>();
		// -------Cart-------------
		// mCart = new ArrayList<Dish>();
		// ------------------------
		mDishArray = new ArrayList<Dish>();
		// ----------Dish Type----------------------
		mDishTypes = new ArrayList<Dish>();
		Dish dishType = new Dish();
		dishType.setDishType("Пицца большая 41 см");
		dishType.setUriPhoto("pizza_big");
		mDishTypes.add(dishType);

		dishType = new Dish();
		dishType.setDishType("Пицца маленькая 30 см");
		dishType.setUriPhoto("pizza_small");
		mDishTypes.add(dishType);

		dishType = new Dish();
		dishType.setDishType("Сэндвичи (до 17:00)");
		dishType.setUriPhoto("sendvich");
		mDishTypes.add(dishType);

		dishType = new Dish();
		dishType.setDishType("Завтраки (до 13:00)");
		dishType.setUriPhoto("breakfast");
		mDishTypes.add(dishType);

		dishType = new Dish();
		dishType.setDishType("Суши - Нигири");
		dishType.setUriPhoto("nigiri");
		mDishTypes.add(dishType);

		dishType = new Dish();
		dishType.setDishType("Суши - Гунканы");
		dishType.setUriPhoto("gunkan");
		mDishTypes.add(dishType);

		dishType = new Dish();
		dishType.setDishType("Роллы");
		dishType.setUriPhoto("rolls");
		mDishTypes.add(dishType);

		dishType = new Dish();
		dishType.setDishType("Горячие роллы");
		dishType.setUriPhoto("hot_rolls");
		mDishTypes.add(dishType);

		dishType = new Dish();
		dishType.setDishType("Ассорти роллы");
		dishType.setUriPhoto("madedish");
		mDishTypes.add(dishType);

		dishType = new Dish();
		dishType.setDishType("Салаты");
		dishType.setUriPhoto("salad");
		mDishTypes.add(dishType);

		dishType = new Dish();
		dishType.setDishType("Первые блюда");
		dishType.setUriPhoto("firstcourse");
		mDishTypes.add(dishType);

		dishType = new Dish();
		dishType.setDishType("Холодные закуски");
		dishType.setUriPhoto("snacks");
		mDishTypes.add(dishType);

		dishType = new Dish();
		dishType.setDishType("Теплые салаты");
		dishType.setUriPhoto("hotsalad");
		mDishTypes.add(dishType);

		dishType = new Dish();
		dishType.setDishType("Хлеб");
		dishType.setUriPhoto("bread");
		mDishTypes.add(dishType);

		dishType = new Dish();
		dishType.setDishType("Горячие закуски");
		dishType.setUriPhoto("hotsnacks");
		mDishTypes.add(dishType);

		dishType = new Dish();
		dishType.setDishType("Паста");
		dishType.setUriPhoto("pasta");
		mDishTypes.add(dishType);

		dishType = new Dish();
		dishType.setDishType("Блюда из мяса");
		dishType.setUriPhoto("meat");
		mDishTypes.add(dishType);

		dishType = new Dish();
		dishType.setDishType("Стейки и шашлык");
		dishType.setUriPhoto("steak");
		mDishTypes.add(dishType);

		dishType = new Dish();
		dishType.setDishType("Блюда из рыбы");
		dishType.setUriPhoto("fish");
		mDishTypes.add(dishType);

		dishType = new Dish();
		dishType.setDishType("Миксы");
		dishType.setUriPhoto("mix");
		mDishTypes.add(dishType);

		dishType = new Dish();
		dishType.setDishType("Гарниры");
		dishType.setUriPhoto("sidedish");
		mDishTypes.add(dishType);

		dishType = new Dish();
		dishType.setDishType("Соусы");
		dishType.setUriPhoto("sauce");
		mDishTypes.add(dishType);

		dishType = new Dish();
		dishType.setDishType("Десерты");
		dishType.setUriPhoto("dessert");
		mDishTypes.add(dishType);
		// ---------------------------------------------

		DataBaseHelper myDbHelper = new DataBaseHelper(ctx);

		try {

			myDbHelper.createDataBase();

		} catch (IOException ioe) {

			throw new Error("Unable to create database");

		}

		try {

			myDbHelper.openDataBase();

		} catch (SQLException sqle) {

			throw sqle;

		}

		// ----------------------------------------------
		// DBHelper dbh = new DBHelper(ctx);
		// SQLiteDatabase db = dbh.getReadableDatabase();

		Cursor curs = myDbHelper.getAllFields("full_menu");
		if (curs.moveToFirst()) {

			// Long mLastConnection =
			// Long.valueOf(curs.getString(curs.getColumnIndex("connection_time")));

			do {

				Dish dish = new Dish();

				// Log.d("XXX", curs.getColumnName(1));

				dish.setDishId(curs.getString(curs.getColumnIndex("id")));
				dish.setDishType(curs.getString(curs
						.getColumnIndex("dish_type")));
				dish.setDishName(curs.getString(curs
						.getColumnIndex("dish_name")));
				dish.setDishInfoFull(curs.getString(curs
						.getColumnIndex("dish_info_full")));
				dish.setDishPrice1(curs.getString(curs
						.getColumnIndex("dish_price1")));
				dish.setDishQuantity(curs.getInt(curs
						.getColumnIndex("quantity")));
				dish.setUriPhoto(curs.getString(curs
						.getColumnIndex("photo_uri")));

				mDishArray.add(dish);

			} while (curs.moveToNext()); 

			// if (curs!=null) curs.close();*/

			// ---------------
			sharedPreferences = ctx.getSharedPreferences(
					"com.example.pizzakvartal_shared_preferences",
					Context.MODE_PRIVATE);
			Long mLastConnection = sharedPreferences.getLong(
					SHARED_PREFERENCES_TIME, 0);
			// прошло более 24 часов с момента последнего подключения
			// 86400000
			if (System.currentTimeMillis() - mLastConnection > 86400000) {
				// Log.d("XXX", System.currentTimeMillis() -
				// mLastConnection+"");
				sThreadRunning = true;
				new NewThreadGetMenuInfo().execute(ctx);
			}

		} else {
			sThreadRunning = true;
			new NewThreadGetMenuInfo().execute(ctx);
		}

		if (myDbHelper != null)
			myDbHelper.close();
		if (curs != null)
			curs.close();

	}

	public static DishLab getDishLab(Context ctx) {
		if (mDishLab == null)
			mDishLab = new DishLab(ctx.getApplicationContext());

		return mDishLab;
	}

	public ArrayList<Dish> getDishes() {

		return mDishArray;
	}

	public ArrayList<Dish> getDishes(String mDishType) {
		ArrayList<Dish> mDishInfo = new ArrayList<Dish>();

		for (Dish d : mDishArray) {
			if (d.getDishType().equals(mDishType)) {
				mDishInfo.add(d);

			}

		}
		return mDishInfo;

	}

	public ArrayList<Dish> getDishTypes() {

		return mDishTypes;
	}

	public Dish getDish(UUID id) {

		for (int i = 0; i < mDishArray.size(); i++) {
			if (mDishArray.get(i).getId() == id)
				return mDishArray.get(i);

		}
		return null;

	}
	
	public Dish getDish(String dishType, String dishName) {

		for (int i = 0; i < mDishArray.size(); i++) {
			if (mDishArray.get(i).getDishType().equals(dishType) && mDishArray.get(i).getDishName().equals(dishName))
				return mDishArray.get(i);

		}
		return null;

	}
	

	public int getQuantityDish() {
		int sum = 0;
		for (Dish d : mDishArray) {
			if (d.getDishQuantity() > 0)
				sum++;

		}
		// Log.d("XXX",sum+"");
		return sum;
	}

	public static void clearDishQuantityFromBD(Dish dish, Context ctx) {

		DataBaseHelper myDbHelper = new DataBaseHelper(ctx);
		try {

			myDbHelper.openDataBase();

		} catch (SQLException sqle) {

			throw sqle;

		}
		myDbHelper.clearDishQuantityFromBD(dish);

		myDbHelper.close();

	}

	public ArrayList<Dish> getDateHistory(Context ctx) {

		DataBaseHelper myDbHelper = new DataBaseHelper(ctx);

		try {

			myDbHelper.createDataBase();

		} catch (IOException ioe) {

			throw new Error("Unable to create database");

		}

		try {

			myDbHelper.openDataBase();

		} catch (SQLException sqle) {

			throw sqle;

		}
		mHistoryArray.clear();
		Cursor curs = myDbHelper.getAllDateHistory();

		if (curs.moveToFirst()) {
			do {
				Dish dish = new Dish();

				dish.setDate(curs.getLong(curs.getColumnIndex("order_id")));

				mHistoryArray.add(dish);
			} while (curs.moveToNext());
		}
		if (curs != null)
			curs.close();
		myDbHelper.close();
		return mHistoryArray;

	}

	public void deleteOrder(Context ctx, long dateOrder){
		DataBaseHelper myDbHelper = new DataBaseHelper(ctx);
		try {

			myDbHelper.openDataBase();
			myDbHelper.deleteFromTable(dateOrder);
			
			
		} catch (SQLException sqle) {

			throw sqle;

		}
		if (myDbHelper!=null)
			myDbHelper.close();
		
	}
	
	
	public ArrayList<Dish> getOrderByDate(Context ctx, long dateOrder) {

		DataBaseHelper myDbHelper = new DataBaseHelper(ctx);

		/*try {

			myDbHelper.createDataBase();

		} catch (IOException ioe) {

			throw new Error("Unable to create database");

		}*/

		try {

			myDbHelper.openDataBase();

		} catch (SQLException sqle) {

			throw sqle;

		}
		ArrayList<Dish> mOrderArray = new ArrayList<Dish>();

		Cursor curs = myDbHelper.getOrderHistory(dateOrder);

		if (curs.moveToFirst()) {
			do {
				Dish dish = new Dish();

				dish.setDate(curs.getLong(curs.getColumnIndex("order_id")));
				dish.setDishType(curs.getString(curs
						.getColumnIndex("dish_type")));
				dish.setDishName(curs.getString(curs
						.getColumnIndex("dish_name")));
				dish.setDishPrice1(curs.getString(curs
						.getColumnIndex("dish_price1")));
				dish.setDishQuantity(curs.getInt(curs
						.getColumnIndex("quantity")));

				mOrderArray.add(dish);
			} while (curs.moveToNext());
		}
		if (curs != null)
			curs.close();
		myDbHelper.close();
		return mOrderArray;

	}

}
