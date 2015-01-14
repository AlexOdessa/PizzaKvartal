package com.example.pizzakvartal;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.view.Display;
import android.view.WindowManager;

public class Utils {

	public final static String workFolder = "/pizzakvartal/";

	Context ctx;

	Utils(Context ctx) {
		this.ctx = ctx;

	}

	@TargetApi(13)
	public int[] getScreenWidth() {
		int columnWidth;
		int columnHeight;

		WindowManager wm = (WindowManager) ctx
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		final Point point = new Point();

		if (Build.VERSION.SDK_INT >= 13) {
			display.getSize(point);

		} else {
			point.x = display.getWidth();
			point.y = display.getHeight();

		}

		columnWidth = point.x;
		columnHeight = point.y;
		return new int[] { columnWidth, columnHeight };

	}

	public String getTargetPath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath()+workFolder;

	}

	public boolean haveNetworkConnection() {
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}
		return haveConnectedWifi || haveConnectedMobile;
	}
	
}
