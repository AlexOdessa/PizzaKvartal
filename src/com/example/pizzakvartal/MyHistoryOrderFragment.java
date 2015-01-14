package com.example.pizzakvartal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyHistoryOrderFragment extends Fragment {
	

	
	String dateOrderString;

	ArrayList<Dish> mHistoryArray;
	ArrayList<Dish> mDishArray;
	public final static String EXTRA_ORDER_DATE="com.example.pizzakvartal.extraorderdate";
	
	int cartSum;
	
	@TargetApi(11)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

		long dateOrder=getArguments().getLong(EXTRA_ORDER_DATE);
		
		SimpleDateFormat sdf = new SimpleDateFormat("d-MM-yyyy HH:mm");
		dateOrderString=sdf.format(dateOrder);

		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActivity().getActionBar().setTitle(dateOrderString);
		}
		
		cartSum = 0;

		mDishArray = DishLab.getDishLab(getActivity()).getDishes();

		mHistoryArray = DishLab.getDishLab(getActivity()).getOrderByDate(getActivity(), dateOrder);
		
		for (Dish d : mHistoryArray) {
			if (d.getDishQuantity() > 0) {
				
				cartSum += d.getDishQuantity() * getPrice(d.getDishPrice1());
			}

		}

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

			name.setText(mHistoryArray.get(position).getDishType().concat(" ")
					.concat(mHistoryArray.get(position).getDishName()));
			String dishPrice = mHistoryArray.get(position).getDishPrice1();
			int dishQuantity = mHistoryArray.get(position).getDishQuantity();

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
		View v = inflater.inflate(R.layout.historylayout, container, false);
		
		TextView delivText = (TextView) v.findViewById(R.id.delivText);
		TextView totalSum = (TextView) v.findViewById(R.id.totalSum);
		Button copy2cart=(Button) v.findViewById(R.id.copy2cart);
		
		
		
		delivText.setText("Заказ от ".concat(dateOrderString));
		totalSum.setText("Всего:".concat(String.valueOf(cartSum)));
		
		
		copy2cart.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				copyOrder2Cart();
				
				
			}
		});

		
		ListView listView1 = (ListView) v.findViewById(R.id.shedList);

		

		CartAdapter adapter = new CartAdapter(getActivity(), 0, mHistoryArray);
		listView1.setAdapter(adapter);
		getTotalHeightofListView(listView1);

		return v;
	}

	private void copyOrder2Cart() {

		DataBaseHelper myDbHelper = new DataBaseHelper(getActivity());
		try {

			myDbHelper.openDataBase();

		} catch (SQLException sqle) {

			throw sqle;

		}
		
		for (Dish d:mHistoryArray){
		
			Dish dish=DishLab.getDishLab(getActivity()).getDish(d.getDishType(), d.getDishName());
			
			dish.setDishQuantity(dish.getDishQuantity()+d.getDishQuantity());
		myDbHelper.savedDish2BD(dish);
		int sum = DishLab.getDishLab(getActivity()).getQuantityDish();
		SingleFragmentActivity.quantitySum.setText(String.valueOf(sum));
		if (sum == 0)
			SingleFragmentActivity.quantitySum.setVisibility(View.INVISIBLE);

		else
			SingleFragmentActivity.quantitySum.setVisibility(View.VISIBLE);
		
		//mNumber.setText(String.valueOf(mDish.getDishQuantity()));
		
		
		}
		Toast toast = Toast.makeText(getActivity(),
				"Заказ скопирован в корзину", Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		myDbHelper.close();
		
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
			getActivity().getActionBar().setTitle(dateOrderString);
		}

	}

	public static MyHistoryOrderFragment newInstance(long orderId) {
		Bundle arg = new Bundle();
		arg.putInt(SingleFragmentActivity.EXTRA_FRAGMENT, 4);
		arg.putLong(EXTRA_ORDER_DATE, orderId);
		
		MyHistoryOrderFragment fragment = new MyHistoryOrderFragment();
		fragment.setArguments(arg);

		return fragment;
	}

	

}
