package com.example.pizzakvartal;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class NewThreadGetMenuInfo extends AsyncTask<Object, Void, Boolean> {

	private final String mainUrl = "http://pizza-kvartal.com/menulist.php";
	private final String mainImageUrl = "http://pizza-kvartal.com";

	public final static String workFolder = "/pizzakvartal/";
	Context ctx;

	@Override
	protected Boolean doInBackground(Object... params) {
		// TODO Auto-generated method stub

		ctx = (Context) params[0];
		
		
		
		
		DataBaseHelper myDbHelper = new DataBaseHelper(ctx);
		try {

			myDbHelper.openDataBase();

		} catch (SQLException sqle) {

			throw sqle;

		}

		Utils utils=new Utils(ctx);
		if (!utils.haveNetworkConnection()) {
			if (myDbHelper != null)	myDbHelper.close();
			return false;
			}
		
		// DBHelper dbh = new DBHelper(ctx);
		// SQLiteDatabase db = dbh.getWritableDatabase();

		Elements title = null;
		Document doc = null;
		ContentValues cv = new ContentValues();

		try {
			doc = Jsoup.connect(mainUrl).get();
			myDbHelper.clearDBTable("full_menu_tmp");
			//Log.d("XXX", "1");
			// title = doc.getElementsByClass("col-650");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (myDbHelper != null)	myDbHelper.close();
			return false;
		}
		
		

		// String htmlPage=builder.toString();

		//Log.d("XXX", "13");
		String htmlPage = doc.toString().replace("<h5",
				"</div><div class=\"my_class\"><h5");
		doc = Jsoup.parse(htmlPage);
		title = doc.getElementsByClass("my_class");
		//Log.d("XXX", "2");

		String dishName = "";

		String dishFullInfo = "";
		String dishPrice1 = "";
		String dishPrice2 = "";
		String dishPhoto = "";

		for (Element titles : title) {

			if (titles.getElementsByTag("h5").size() > 0) {

				Elements title2 = titles.getElementsByClass("pagelist-item");
				if (title2.size() > 0) {

					cv.clear();

					String mDishType = titles.getElementsByTag("h5").text();

					for (Element titles2 : title2) {

						dishName = titles2.getElementsByTag("big").text();
						dishFullInfo = titles2.getElementsByTag("p").text();

						Elements titlePrice = titles2
								.getElementsByClass("price");

						for (int i = 0; i < titlePrice.size(); i++) {

							if (i == 0) {
								dishPrice1 = titlePrice.get(0).text();

							} else {
								dishPrice2 = titlePrice.get(1).text();

							}
						}
						if (titles2.getElementsByClass("price_s").size() > 0) {
							dishPrice1 = titles2.getElementsByClass("price_s")
									.text();
						}

						dishPhoto = titles2.getElementsByTag("img").attr("src");

						// если нет цены, то такое блюдо не записывать в базу
						if (titlePrice.size() == 0
								&& titles2.getElementsByClass("price_s").size() == 0)
							continue;

						if (!dishPrice1.substring(dishPrice1.length() - 4,
								dishPrice1.length()).equals("грн."))
							continue;

						if (mDishType.equals("Пицца")) { 

							if (!dishPrice2.equals("") && !dishPrice2.equals("-")) {
								cv.put("dish_type", "Пицца большая 41 см");
								cv.put("dish_name", dishName);
								cv.put("dish_info_full", dishFullInfo);
								cv.put("dish_price1", dishPrice2);
								// cv.put("dish_price2", dishPrice2);
								cv.put("photo_uri", dishPhoto);
								//cv.put("connection_time",										System.currentTimeMillis());
								myDbHelper.insertInTable("full_menu_tmp", cv);
								// db.setTransactionSuccessful();
								// db.endTransaction();

							}

							if (!dishPrice1.equals("")) {

								cv.put("dish_type", "Пицца маленькая 30 см");
								cv.put("dish_name", dishName);
								cv.put("dish_info_full", dishFullInfo);
								cv.put("dish_price1", dishPrice1);
								
								cv.put("photo_uri", dishPhoto);
								myDbHelper.insertInTable("full_menu_tmp", cv);
							}

						}

						else {

							if (mDishType.equals("Сендвичи (до 17:00)")
									|| mDishType.equals("Сэндвичи (до 17:00)"))
								mDishType = "Сэндвичи (до 17:00)";

							if (mDishType.equals("Нигири"))
								mDishType = "Суши - Нигири";

							if (mDishType.equals("Гунканы"))
								mDishType = "Суши - Гунканы";

							if (mDishType.equals("РОЛЛЫ"))
								mDishType = "Роллы";

							if (mDishType.equals("ГОРЯЧИЕ РОЛЛЫ"))
								mDishType = "Горячие роллы";

							if (mDishType.equals("АССОРТИ РОЛЛЫ"))
								mDishType = "Ассорти роллы";

							cv.put("dish_type", mDishType);
							cv.put("dish_name", dishName);
							cv.put("dish_info_full", dishFullInfo);
							cv.put("dish_price1", dishPrice1);

						
							cv.put("photo_uri", dishPhoto);
							myDbHelper.insertInTable("full_menu_tmp", cv);
						}

					}
				}
			}

		}

		//Log.d("XXX", "4");

		Cursor c_tmp = myDbHelper.getAllFields("full_menu_tmp");
		boolean identicalDB = true;

		if (c_tmp.moveToFirst()) {
			// myDbHelper.clearDBTable("full_menu");
			do {
				cv.clear();
				cv.put("dish_type",
						c_tmp.getString(c_tmp.getColumnIndex("dish_type")));
				cv.put("dish_name",
						c_tmp.getString(c_tmp.getColumnIndex("dish_name")));
				cv.put("dish_info_full",
						c_tmp.getString(c_tmp.getColumnIndex("dish_info_full")));
				cv.put("dish_price1",
						c_tmp.getString(c_tmp.getColumnIndex("dish_price1")));

				// -----------
				String filename;
				if (c_tmp.getString(c_tmp.getColumnIndex("photo_uri")).length() > 0) {
					String path = c_tmp.getString(c_tmp
							.getColumnIndex("photo_uri"));
					filename = path.substring(path.lastIndexOf('/') + 1);

					checkFile(path, filename);

					cv.put("photo_uri", filename);
				} else
					cv.put("photo_uri", "");
				// -----------

				

				identicalDB = myDbHelper.isFieldInDB(cv);

				// myDbHelper.insertInTable("full_menu", cv);

			} while (c_tmp.moveToNext() && identicalDB);

			
			if (!identicalDB) {
				Log.d("XXX", "updateDB");
				if (c_tmp.moveToFirst()) {
					myDbHelper.clearDBTable("full_menu");
					do {
						cv.clear();
						cv.put("dish_type", c_tmp.getString(c_tmp
								.getColumnIndex("dish_type")));
						cv.put("dish_name", c_tmp.getString(c_tmp
								.getColumnIndex("dish_name")));
						cv.put("dish_info_full", c_tmp.getString(c_tmp
								.getColumnIndex("dish_info_full")));
						cv.put("dish_price1", c_tmp.getString(c_tmp
								.getColumnIndex("dish_price1")));

						// -----------
						String filename;
						if (c_tmp.getString(c_tmp.getColumnIndex("photo_uri"))
								.length() > 0) {
							String path = c_tmp.getString(c_tmp
									.getColumnIndex("photo_uri"));
							filename = path
									.substring(path.lastIndexOf('/') + 1);

							checkFile(path, filename);

							cv.put("photo_uri", filename);
						} else
							cv.put("photo_uri", "");
						// -----------

						//cv.put("connection_time", c_tmp.getString(c_tmp.getColumnIndex("connection_time")));
						myDbHelper.insertInTable("full_menu", cv);
					} while (c_tmp.moveToNext());

				}
			}
			else { 
				Log.d("XXX", "not updateDB");
				
			}

		}

		
		if (c_tmp != null)
			c_tmp.close();
		if (myDbHelper != null)
			myDbHelper.close();
		
		// ------------
		DishLab.sThreadRunning = false;

		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (result){
		SharedPreferences sharedPreferences=ctx.getSharedPreferences(
				"com.example.pizzakvartal_shared_preferences",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
		// Editor editor=sharedPreferences.edit();

		preferencesEditor.putLong(DishLab.SHARED_PREFERENCES_TIME, System.currentTimeMillis()
				).commit();
		}else {
			DishLab.sThreadRunning = false;
			Toast.makeText(ctx, "Проблемы с интернет соединением", Toast.LENGTH_SHORT).show();
		}
		
	}

	public void checkFile(String path, String filename) {

		String sdPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + workFolder;

		File f1 = new File(sdPath);
		if (!f1.exists())
			f1.mkdir();

		File file = new File(sdPath.concat(filename));

		if (file.exists())
			return;
		//Log.d("XXX", "download");
		downloadFile(path, filename);

	}

	public boolean downloadFile(String path, String filename) {
		try {
			URL url = new URL(mainImageUrl.concat(path));

			URLConnection ucon = url.openConnection();

			ucon.setReadTimeout(5000);
			ucon.setConnectTimeout(10000);

			InputStream is = ucon.getInputStream();
			BufferedInputStream inStream = new BufferedInputStream(is, 1024 * 5);

			String sdPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath().concat(workFolder);

			File file = new File(sdPath.concat(filename));

			file.createNewFile();

			FileOutputStream outStream = new FileOutputStream(file);

			byte[] buff = new byte[5 * 1024];

			int len;
			while ((len = inStream.read(buff)) != -1) {
				outStream.write(buff, 0, len);
			}

			outStream.flush();
			outStream.close();
			inStream.close();

			// проверка
			Bitmap bitmap = decodeSampledBitmapFromUri(sdPath.concat(filename),
					50, 50);
			if (bitmap == null) {

				file.delete();

			}
			bitmap.recycle();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static Bitmap decodeSampledBitmapFromUri(String path, int reqWidth,
			int reqHeight) {
		Bitmap bm = null;

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeFile(path, options);

		return bm;

	}

	public static int calculateInSampleSize(

	BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}

		return inSampleSize;
	}

}
