package com.example.pizzakvartal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CartFragment extends ListFragment {

	//public final static String EXTRA_DISH_TYPE = "com.example.pizzakvartal.extradishtype";
	//ArrayList<Dish> mDishInfo;
	ArrayList<Dish> mDishArray;
	ArrayList<Dish> mInCartArray;
	Button clearButton;
	Button confirmButton;
	final String TAG_1 = "EXTENDED_DISH_TAG";
	int cartSum;
	
	ListView lv;
	TextView sumText;
	//ProgressBar pb;
	//public boolean mCheckThreadRunning;
	//AsyncTask<Void, Void, Void> thread;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.cartlist, container,false);
		lv = (ListView) v.findViewById(android.R.id.list);
		clearButton=(Button) v.findViewById(R.id.clearButton);
		confirmButton=(Button) v.findViewById(R.id.sendButton);
		
		sumText=(TextView) v.findViewById(R.id.sumText);
		sumText.setText(String.valueOf(cartSum)+" грн.");
		
		
		TextView emptyCartText=(TextView) v.findViewById(R.id.emptyCartText);
		if (cartSum==0){
			emptyCartText.setVisibility(View.VISIBLE);
			lv.setVisibility(View.INVISIBLE);
			clearButton.setEnabled(false);
			confirmButton.setEnabled(false);
			
			
		}
		
		
		
		clearButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				for (Dish d:mInCartArray){
					DishLab.clearDishQuantityFromBD(d, getActivity());
					
					
				}
				mInCartArray.clear();
				for (Dish d: mDishArray){
					d.setDishQuantity(0);
				}
				
				SingleFragmentActivity.quantitySum.setVisibility(View.INVISIBLE);
				SingleFragmentActivity.selectButton=0;
				getFragmentManager().popBackStack();
				
			}
		});
		
		confirmButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentTransaction ft=getFragmentManager().beginTransaction();
				OrderFragment frag=OrderFragment.newInstance();

				ft.replace(R.id.mainFragment, frag, "FRAGMENT_CART"); 
				ft.addToBackStack(null);
				ft.commit();
				
			}
		});
		return v;
	}

	

	
	@TargetApi(11)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		cartSum=0;
		mInCartArray = new ArrayList<Dish>();
		mDishArray = DishLab.getDishLab(getActivity()).getDishes();
		// --------

		if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
			getActivity().getActionBar().setTitle(R.string.cart);
		}
		

		for (Dish d : mDishArray) {
			if (d.getDishQuantity()>0) {
				mInCartArray.add(d);
				cartSum+=d.getDishQuantity()*getPrice(d.getDishPrice1());
			}

		}
		
		
		
		
		CartAdapter adapter = new CartAdapter(getActivity(), 0, mInCartArray);
		setListAdapter(adapter);

	}

	private int getPrice(String priceDish){
		int price=0;
		
		try {
			price=Integer.parseInt(priceDish.substring(0,priceDish.indexOf("грн")).replaceAll("\\s+",""));
		} catch (StringIndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		return price;
	}



	private class CartAdapter extends ArrayAdapter<Dish> {

		public CartAdapter(Context context, int resource, List<Dish> objects) {
			super(context, resource, objects);
			// TODO Auto-generated constructor stub
		}

		private class ViewHolder {
			ImageView imageDish;
			TextView dishName;
			TextView dishInfo;
			TextView dishPrice;
			TextView dishNumber;
			ImageButton imagePlus;
			ImageButton imageMinus;
			
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub

			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();

				
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.extendeddish, parent, false);

				holder.dishInfo= (TextView) convertView
						.findViewById(R.id.textDishInfo);
				
				holder.imageDish = (ImageView) convertView
						.findViewById(R.id.imageDish);
				holder.dishName = (TextView) convertView
						.findViewById(R.id.textDishName);
				holder.dishPrice = (TextView) convertView
						.findViewById(R.id.textPrice);

				holder.dishNumber = (TextView) convertView
						.findViewById(R.id.numberOfDish);

				holder.imagePlus = (ImageButton) convertView
						.findViewById(R.id.imagePlus);
				holder.imageMinus = (ImageButton) convertView
						.findViewById(R.id.imageMinus);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();

			}

			((TextView)convertView.findViewById(R.id.toCart)).setVisibility(View.INVISIBLE);
			
			holder.dishNumber.setVisibility(View.VISIBLE);
			holder.imageMinus.setVisibility(View.VISIBLE);
			holder.dishNumber.setText(String.valueOf(mInCartArray
					.get(position).getDishQuantity()));
			
			if (mInCartArray.get(position).getUriPhoto() == null
					| mInCartArray.get(position).getUriPhoto().equals(""))

				holder.imageDish.setImageResource(R.drawable.no_image2);

			else{
				String sdPath=Environment.getExternalStorageDirectory()
				.getAbsolutePath().concat(Utils.workFolder);
				holder.imageDish.setImageBitmap(NewThreadGetMenuInfo.decodeSampledBitmapFromUri(sdPath.concat(mInCartArray.get(position).getUriPhoto()), 120,120));
				//holder.imageDish.setImageResource(getId(mDishInfo.get(position).getUriPhoto(), R.drawable.class));
			}

			holder.dishName.setText(mInCartArray.get(position).getDishName());


			holder.dishPrice.setText(mInCartArray.get(position).getDishPrice1());
			holder.dishInfo.setText(mInCartArray.get(position).getDishInfoFull());
			
			
			holder.imagePlus.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					int quantSelDish = mInCartArray.get(position)
							.getDishQuantity();
					cartSum+=getPrice(mInCartArray.get(position).getDishPrice1());
					sumText.setText(String.valueOf(cartSum)+" грн.");		
					
					mInCartArray.get(position).setDishQuantity(++quantSelDish);
					holder.dishNumber.setText(String.valueOf(quantSelDish));
					
					setQuantity(position, quantSelDish);

					// TODO Auto-generated method stub
					
					

				}
			});

			holder.imageMinus.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int quantSelDish = mInCartArray.get(position)
							.getDishQuantity();
					cartSum-=getPrice(mInCartArray.get(position).getDishPrice1());
					sumText.setText(String.valueOf(cartSum)+" грн.");
					
					mInCartArray.get(position).setDishQuantity(--quantSelDish);
					holder.dishNumber.setText(String.valueOf(quantSelDish));
					setQuantity(position, quantSelDish);

					
					// TODO Auto-generated method stub
					if (quantSelDish == 0) {
						mInCartArray.remove(position);
						((CartAdapter)getListAdapter()).notifyDataSetChanged();
						if (mInCartArray.size()==0) {
							SingleFragmentActivity.quantitySum.setVisibility(View.INVISIBLE);
							SingleFragmentActivity.selectButton=0;
							getFragmentManager().popBackStack(); 
							
							
						}
					}
				}
			});


			
			return convertView;
		}

	}
@TargetApi(11)
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
			getActivity().getActionBar().setTitle(R.string.cart);
		}
		
		((CartAdapter) getListAdapter()).notifyDataSetChanged();
		
	}


	private void setQuantity(int position, int quantSelDish) {

		DataBaseHelper myDbHelper = new DataBaseHelper(getActivity());
		try {

			myDbHelper.openDataBase();

		} catch (SQLException sqle) {

			throw sqle;

		}
		
		for (Dish d : mDishArray) {
			if (d.getDishId() == mInCartArray.get(position).getDishId()){
				d.setDishQuantity(quantSelDish);
				myDbHelper.savedDish2BD(d);
				break;
			}
		}
		
		int sum = DishLab.getDishLab(getActivity()).getQuantityDish();
		SingleFragmentActivity.quantitySum.setText(String.valueOf(sum));

		myDbHelper.close();
	}

	private int getId(String resourceName, Class<?> c) {
		try {
			Field idField = c.getDeclaredField(resourceName);
			return idField.getInt(idField);
		} catch (Exception e) {
			return -1;
		}

	}


	public static CartFragment newInstance(int selButton) {

		Bundle arg = new Bundle();
		arg.putInt(SingleFragmentActivity.EXTRA_FRAGMENT, selButton);

		CartFragment fragment = new CartFragment();
		fragment.setArguments(arg);

		return fragment;
	}





}
