package com.example.pizzakvartal;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

	
	private static String DB_PATH;// =
									// "/data/data/com.example.pizzakvartal/databases/";

	private static String DB_NAME = "dbPizzaKvartal";

	private SQLiteDatabase myDataBase;

	private final Context myContext;


	public DataBaseHelper(Context context) {

		super(context, DB_NAME, null, 1);
		DB_PATH = context.getDatabasePath("dbPizzaKvartal").toString();

		this.myContext = context;
	}


	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (dbExist) {
			
		} else {


			this.getReadableDatabase();

			try {

				copyDataBase();

			} catch (IOException e) {

				throw new Error("Error copying database");

			}
		}

	}

	
	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {

			

		}

		if (checkDB != null) {

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

	
	private void copyDataBase() throws IOException {

		
		InputStream myInput = myContext.getAssets().open(DB_NAME);

		
		String outFileName = DB_PATH.concat(DB_NAME);

		
		OutputStream myOutput = new FileOutputStream(outFileName);

		
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public void openDataBase() throws SQLException {

		
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);

	}

	@Override
	public synchronized void close() {

		if (myDataBase != null)
			myDataBase.close();

		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public Cursor getAllFields(String table) {

		return myDataBase.query(table, null, null, null, null, null, null);
	}

	public void clearDBTable(String table) {

		myDataBase.delete(table, null, null);
	}

	
	public void insertInTable(String table, ContentValues cv) {

		myDataBase.insert(table, null, cv);
	}

	public void deleteFromTable(long orderDate){
		
		myDataBase.beginTransaction();
		myDataBase.delete("order_arch", "order_id = " + orderDate, null);
		myDataBase.setTransactionSuccessful();
		myDataBase.endTransaction();
	}
	
	
	public void savedDish2BD(Dish dish) {

		ContentValues cv = new ContentValues();

		

		cv.put("quantity", dish.getDishQuantity());

		myDataBase.update("full_menu", cv, "id=?",
				new String[] { dish.getDishId() });

	}

	public void clearDishQuantityFromBD(Dish dish) {
		ContentValues cv = new ContentValues();

		

		cv.put("quantity", 0);

		myDataBase.update("full_menu", cv, "id=?",
				new String[] { dish.getDishId() });

	}

	public boolean isFieldInDB(ContentValues cv) {

		String selection = "dish_type='"
				.concat(cv.getAsString("dish_type"))
				.concat("' AND dish_name ='")
				.concat(cv.getAsString("dish_name"))
				.concat("' AND dish_info_full='"
						.concat(cv.getAsString("dish_info_full"))
						.concat("' AND dish_price1='")
						.concat(cv.getAsString("dish_price1"))
						.concat("' AND photo_uri='")
						.concat(cv.getAsString("photo_uri")).concat("'"));

		Cursor c = myDataBase.query("full_menu", null, selection, null, null,
				null, null);
		if (!c.moveToFirst()) {
			return false;
		}

		if (c != null)
			c.close();

		return true;
	}

	public Cursor getAllDateHistory() {
		String queryString = "SELECT DISTINCT order_id FROM order_arch ORDER BY order_id DESC";
		return myDataBase.rawQuery(queryString, null);

	}
	
	public Cursor getOrderHistory(long orderDate) {
		String selection="order_id=?";
		String []selectionArg={String.valueOf(orderDate)};
		Cursor c = myDataBase.query("order_arch", null, selection, selectionArg, null,null, null);
				
				
		return c;
	}
}