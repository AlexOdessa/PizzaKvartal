package com.example.pizzakvartal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

	public DBHelper(Context context) {
		super(context, "dbPizzaKvartal", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table full_menu (" 
		          + "id integer primary key autoincrement,"
		          + "dish_info_full text,"
		          + "dish_type text,"
		          + "dish_name text,"
		          + "dish_price1 text,"
		          + "dish_price2 text,"
		          + "quantity integer,"
		          + "photo_uri text,"
		          + "connection_time text,"
		          + "field1 text,"
		          + "field2 text"
		          
		          + ");");
		
		db.execSQL("create table full_menu_tmp (" 
		          + "id integer primary key autoincrement,"
		          + "dish_info_full text,"
		          + "dish_type text,"
		          + "dish_name text,"
		          + "dish_price1 text,"
		          + "dish_price2 text,"
		          + "quantity text,"
		          + "photo_uri text,"
		          + "connection_time text,"
		          + "field1 text,"
		          + "field2 text"
		          + ");");	
		
		db.execSQL("create table order_arch (" 
		          + "id integer primary key autoincrement,"
		          + "order_id integer,"
		          + "dish_type text,"
		          + "dish_name text,"
		          + "dish_price1 text,"
		          + "quantity text"
		         
	      
		          + ");");	
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
