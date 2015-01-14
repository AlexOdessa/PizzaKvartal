package com.example.pizzakvartal;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class OrderFragment extends Fragment {
	public static final String SHARED_PREFERENCES_NAME = "com.example.pizzakvartal_shared_preferences_name";
	public static final String SHARED_PREFERENCES_PHONE = "com.example.pizzakvartal_shared_preferences_phone";
	public static final String SHARED_PREFERENCES_ADDRESS = "com.example.pizzakvartal_shared_preferences_address";

	SharedPreferences sharedPreferences;
	String name = "1";
	String phone = "11";
	String address = "111";

	EditText nameEdit;
	EditText phoneEdit;
	EditText addressEdit;
	

	ArrayList<Dish> mInCartArray;
	ArrayList<Dish> mDishArray;

	int cartSum;
	boolean threadRunning;

	@TargetApi(11)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

		threadRunning = false;

		sharedPreferences = getActivity().getSharedPreferences(
				"com.example.pizzakvartal_shared_preferences",
				Context.MODE_PRIVATE);
		name = sharedPreferences.getString(SHARED_PREFERENCES_NAME, "");
		phone = sharedPreferences.getString(SHARED_PREFERENCES_PHONE, "");
		address = sharedPreferences.getString(SHARED_PREFERENCES_ADDRESS, "");
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActivity().getActionBar().setTitle(R.string.order);
		}
		cartSum = 0;

		mDishArray = DishLab.getDishLab(getActivity()).getDishes();

		mInCartArray = new ArrayList<Dish>();
		for (Dish d : mDishArray) {
			if (d.getDishQuantity() > 0) {
				mInCartArray.add(d);
				cartSum += d.getDishQuantity() * getPrice(d.getDishPrice1());
			}

		}

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
		// Editor editor=sharedPreferences.edit();

		preferencesEditor.putString(SHARED_PREFERENCES_NAME, nameEdit.getText()
				.toString().trim());
		preferencesEditor.putString(SHARED_PREFERENCES_PHONE, phoneEdit
				.getText().toString().trim());
		preferencesEditor.putString(SHARED_PREFERENCES_ADDRESS, addressEdit
				.getText().toString().trim());
		preferencesEditor.commit();
	}

	private class CartAdapter extends ArrayAdapter<Dish> {

		public CartAdapter(Context context, int resource, List<Dish> objects) {
			super(context, resource, objects);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.checklayout, parent, false);

			}
			TextView name = (TextView) convertView.findViewById(R.id.noOrdersText);
			TextView price = (TextView) convertView.findViewById(R.id.totalSum);
			TextView sum = (TextView) convertView.findViewById(R.id.textView3);

			name.setText(mInCartArray.get(position).getDishType().concat(" ")
					.concat(mInCartArray.get(position).getDishName()));
			String dishPrice = mInCartArray.get(position).getDishPrice1();
			int dishQuantity = mInCartArray.get(position).getDishQuantity();

			price.setText(String.valueOf(dishQuantity).concat(" X ")
					.concat(dishPrice));

			sum.setText((String.valueOf(getPrice(dishPrice) * dishQuantity))
					.concat(" грн."));

			return convertView;
		}

	}

	private int getPrice(String priceDish) {

		return Integer.parseInt(priceDish
				.substring(0, priceDish.indexOf("грн")).replaceAll("\\s+", ""));
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.orderlayout, container, false);
		nameEdit = (EditText) v.findViewById(R.id.editName);
		phoneEdit = (EditText) v.findViewById(R.id.editPhone);
		addressEdit = (EditText) v.findViewById(R.id.editAddress);

		TextView totalSum = (TextView) v.findViewById(R.id.totalSum);
		totalSum.setText("Всего:" + cartSum);
		
		// ScrollView sc=(ScrollView) v.findViewById(R.id.scrollView1);
		/*
		 * sc.setOnTouchListener(new View.OnTouchListener() {
		 * 
		 * @Override public boolean onTouch(View v, MotionEvent event) { // TODO
		 * Auto-generated method stub v.findViewById(R.id.shedList).getParent().
		 * requestDisallowInterceptTouchEvent(false); return false; } });
		 */

		Button buttonSend2Arch = (Button) v.findViewById(R.id.copy2cart);
		buttonSend2Arch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				DataBaseHelper myDbHelper = new DataBaseHelper(getActivity());
				try {

					myDbHelper.openDataBase();

				} catch (SQLException sqle) {

					throw sqle;

				}

				

				ContentValues cv = new ContentValues();
				
				
				
				long orderTime = System.currentTimeMillis();
				for (Dish d : mInCartArray) {
					cv.clear();
					
					cv.put("order_id", orderTime);
					cv.put("dish_type", d.getDishType());
					cv.put("dish_name", d.getDishName());
					cv.put("dish_price1", d.getDishPrice1());
					cv.put("quantity", d.getDishQuantity());
					
					
					myDbHelper.insertInTable("order_arch", cv);
					
					DishLab.clearDishQuantityFromBD(d, getActivity());
				}
				
				myDbHelper.close();
				//mInCartArray.clear();
				for (Dish d: mDishArray){
					d.setDishQuantity(0);
					
				}
				
				
				SingleFragmentActivity.quantitySum.setVisibility(View.INVISIBLE);
				
				
				
				FragmentManager fm = getActivity().getSupportFragmentManager();
				for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {    
				    fm.popBackStack();
				}
				
				
				//getActivity().finish();
				
				
			}
		});

		Button sendButton = (Button) v.findViewById(R.id.sendButton);
		sendButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (nameEdit.getText().toString().trim().length() == 0) {
					Toast toast = Toast.makeText(getActivity(),
							"Заполните поле ИМЯ", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				} else if (phoneEdit.getText().toString().trim().length() == 0) {
					Toast toast = Toast.makeText(getActivity(),
							"Заполните поле ТЕЛЕФОН", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				} else if (addressEdit.getText().toString().trim().length() == 0) {
					Toast toast = Toast.makeText(getActivity(),
							"Заполните поле АДРЕС", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				} else {
					if (!threadRunning) {

						// ---
						AlertDialog dialog;
						AlertDialog.Builder builder = new AlertDialog.Builder(
								getActivity());
						builder.setTitle("Пожалуйста подождите...");
						builder.setCancelable(false);
						ProgressBar pb = new ProgressBar(getActivity());
						builder.setView(pb);

						dialog = builder.create();
						dialog.show();
						// builder.show();
						new SendOrder().execute(dialog);
					}
					// ---

				}

			}
		});

		Button callButton = (Button) v.findViewById(R.id.buttonDelivCall);
		callButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Uri uri = Uri.parse("tel:" + "0487055705");
				Intent i = new Intent(Intent.ACTION_DIAL, uri);
				startActivity(i);

			}
		});

		ListView listView1 = (ListView) v.findViewById(R.id.shedList);

		nameEdit.setText(name);
		phoneEdit.setText(phone);
		addressEdit.setText(address);

		CartAdapter adapter = new CartAdapter(getActivity(), 0, mInCartArray);
		listView1.setAdapter(adapter);
		getTotalHeightofListView(listView1);

		return v;
	}

	public void getTotalHeightofListView(ListView listView) {

		ListAdapter mAdapter = listView.getAdapter();

		int totalHeight = 0;

		for (int i = 0; i < mAdapter.getCount(); i++) {
			View mView = mAdapter.getView(i, null, listView);

			mView.measure(
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),

					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

			totalHeight += mView.getMeasuredHeight();

		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (mAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();

	}

	@TargetApi(11)
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActivity().getActionBar().setTitle(R.string.order);
		}

	}

	public static OrderFragment newInstance() {
		Bundle arg = new Bundle();
		arg.putInt(SingleFragmentActivity.EXTRA_FRAGMENT, 2);

		OrderFragment fragment = new OrderFragment();
		fragment.setArguments(arg);

		return fragment;
	}

	class SendOrder extends AsyncTask<Object, Void, Void> {

		boolean status;
		AlertDialog dialog;

		@Override
		protected Void doInBackground(Object... params) {
			// TODO Auto-generated method stub
			dialog = (AlertDialog) params[0];
			threadRunning = true;
			status = false;

			String msg = nameEdit.getText().toString().concat("/")
					.concat(addressEdit.getText().toString()).concat("/")
					.concat(phoneEdit.getText().toString().concat("//"));
			msg = msg.concat(
					(String) DateFormat.format("yyyy-MM-dd hh:mm:ss",
							System.currentTimeMillis())).concat("<br>");

			for (Dish d : mInCartArray) {
				msg = msg.concat(d.getDishType()).concat(" ")
						.concat(d.getDishName()).concat("/")
						.concat(String.valueOf(d.getDishQuantity()))
						.concat(" шт.").concat("<br>");

			}

			if (msg.length() > 0) {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://italiankvartal.esy.es/pizzakvartal.php");
				try {
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							2);
					nameValuePairs.add(new BasicNameValuePair("id", "12345"));
					nameValuePairs.add(new BasicNameValuePair("message",
							URLEncoder.encode(msg, "UTF-8")));
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

					HttpResponse response = httpclient.execute(httppost);

					HttpEntity resEntity = response.getEntity();

					if (resEntity != null) {

						if (response.getStatusLine().getStatusCode() == 200) {
							status = true;
						}

					}

				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialog.dismiss();
			if (status) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle("Заказ отправлен");
				builder.setPositiveButton("ОК", null);
				builder.setIcon(R.drawable.positive);
				builder.setMessage(R.string.sendPositive);

				// builder.show();

				AlertDialog dialog = builder.show();
				TextView messageText = (TextView) dialog
						.findViewById(android.R.id.message);
				messageText.setGravity(Gravity.CENTER);
				dialog.show();
				// send2Arch.setVisibility(View.VISIBLE);

			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle("Заказ не отправлен");
				builder.setPositiveButton("ОК", null);
				builder.setIcon(R.drawable.negative);
				builder.setMessage(R.string.sendNegative);

				// builder.show();

				AlertDialog dialog = builder.show();
				TextView messageText = (TextView) dialog
						.findViewById(android.R.id.message);
				messageText.setGravity(Gravity.CENTER);
				dialog.show();

			}
			threadRunning = false;

		}

	}

}
