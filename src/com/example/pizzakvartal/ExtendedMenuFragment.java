package com.example.pizzakvartal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ExtendedMenuFragment extends ListFragment {

	public final static String EXTRA_DISH_TYPE = "com.example.pizzakvartal.extradishtype";
	ArrayList<Dish> mDishInfo;
	ArrayList<Dish> mDishArray;

	ListView lv;
	ProgressBar pb;
	public boolean mCheckThreadRunning;
	AsyncTask<Void, Void, Void> thread;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.menulist, null);
		lv = (ListView) v.findViewById(android.R.id.list);
		pb = (ProgressBar) v.findViewById(R.id.progressBar1);

		// -------
		
		lv.setVisibility(View.INVISIBLE);
		pb.setVisibility(View.VISIBLE);
		if (!mCheckThreadRunning){
			thread = new CheckThread().execute();
		}

		return v;
	}

	@TargetApi(11)
	public void readDB() {
		
		if (getActivity() == null) {
			return;
		}
		
		String mDishType = getArguments().getString(EXTRA_DISH_TYPE);
		
		
		if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
			getActivity().getActionBar().setTitle(mDishType);
		}
		
		mDishInfo.clear();
		for (Dish d : mDishArray) {
			if (d.getDishType().equals(mDishType)) {
				mDishInfo.add(d);

			}

		}
		

		lv.setVisibility(View.VISIBLE);
		pb.setVisibility(View.INVISIBLE);
		((InfoAdapter) getListAdapter()).notifyDataSetChanged();

	}

	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

		mDishInfo = new ArrayList<Dish>();
		mDishArray = DishLab.getDishLab(getActivity()).getDishes();
		// --------

		// mDishInfo = DishLab.getDishLab(getActivity()).getDishTypes();
		InfoAdapter adapter = new InfoAdapter(getActivity(), 0, mDishInfo);
		setListAdapter(adapter);

	}

	private class CheckThread extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			mCheckThreadRunning = true;
			// TODO Auto-generated method stub
			while (DishLab.sThreadRunning) {

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			readDB();
			
			
			
			
			mCheckThreadRunning = false;
		}

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (mCheckThreadRunning)
			thread.cancel(true);
	}

	private class InfoAdapter extends ArrayAdapter<Dish> {

		public InfoAdapter(Context context, int resource, List<Dish> objects) {
			super(context, resource, objects);
			// TODO Auto-generated constructor stub
		}

		private class ViewHolder {
			ImageView imageDish;
			TextView dishName;
			TextView dishPrice;
			TextView dishNumber;
			ImageButton imagePlus;
			ImageButton imageMinus;
			RelativeLayout rl;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub

			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();

				
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.extendedmenu, parent, false);

				holder.rl=(RelativeLayout) convertView.findViewById(R.id.relLayoutF);
				
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

			if (mDishInfo.get(position).getDishQuantity() > 0) {
				holder.dishNumber.setVisibility(View.VISIBLE);
				holder.imageMinus.setVisibility(View.VISIBLE);
				holder.dishNumber.setText(String.valueOf(mDishInfo
						.get(position).getDishQuantity()));
			} else {

				holder.dishNumber.setVisibility(View.INVISIBLE);
				holder.imageMinus.setVisibility(View.INVISIBLE);

			}

			holder.dishName.setText(mDishInfo.get(position).getDishName());

			if (mDishInfo.get(position).getUriPhoto() == null
					| mDishInfo.get(position).getUriPhoto().equals(""))

				holder.imageDish.setImageResource(R.drawable.no_image2);

			else{
				String sdPath=Environment.getExternalStorageDirectory()
				.getAbsolutePath().concat(Utils.workFolder);
				holder.imageDish.setImageBitmap(NewThreadGetMenuInfo.decodeSampledBitmapFromUri(sdPath.concat(mDishInfo.get(position).getUriPhoto()), 120,120));
				//holder.imageDish.setImageResource(getId(mDishInfo.get(position).getUriPhoto(), R.drawable.class));
			}
			holder.dishPrice.setText(mDishInfo.get(position).getDishPrice1());

			holder.imagePlus.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					int quantSelDish = mDishInfo.get(position)
							.getDishQuantity();

					mDishInfo.get(position).setDishQuantity(++quantSelDish);

					setQuantity(position, quantSelDish);

					// TODO Auto-generated method stub
					holder.imageMinus.setVisibility(View.VISIBLE);

					holder.dishNumber.setVisibility(View.VISIBLE);
					holder.dishNumber.setText(String.valueOf(quantSelDish));

				}
			});

			holder.imageMinus.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int quantSelDish = mDishInfo.get(position)
							.getDishQuantity();

					mDishInfo.get(position).setDishQuantity(--quantSelDish);

					setQuantity(position, quantSelDish);

					holder.dishNumber.setText(String.valueOf(quantSelDish));
					// TODO Auto-generated method stub
					if (quantSelDish == 0) {
						holder.imageMinus.setVisibility(View.INVISIBLE);

						holder.dishNumber.setVisibility(View.INVISIBLE);
					}
				}
			});

			holder.rl.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startExtendedDishFragment(mDishInfo.get(position).getId());
					
				}
			});
			
			return convertView;
		}

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((InfoAdapter) getListAdapter()).notifyDataSetChanged();
	}

	public static ExtendedMenuFragment newInstance(String mDishType) {

		Bundle arg = new Bundle();
		arg.putString(EXTRA_DISH_TYPE, mDishType);
		ExtendedMenuFragment fragment = new ExtendedMenuFragment();
		fragment.setArguments(arg);

		return fragment;
	}

	private void setQuantity(int position, int quantSelDish) {

		DataBaseHelper myDbHelper = new DataBaseHelper(getActivity());
		try {

			myDbHelper.openDataBase();

		} catch (SQLException sqle) {

			throw sqle;

		}
		
		for (Dish d : mDishArray) {
			if (d.getDishId() == mDishInfo.get(position).getDishId()){
				d.setDishQuantity(quantSelDish);
				myDbHelper.savedDish2BD(d);
				break;
			}
		}
		myDbHelper.close();
		
		int sum = DishLab.getDishLab(getActivity()).getQuantityDish();
		SingleFragmentActivity.quantitySum.setText(String.valueOf(sum));
		if (sum == 0)
			SingleFragmentActivity.quantitySum.setVisibility(View.INVISIBLE);

		else
			SingleFragmentActivity.quantitySum.setVisibility(View.VISIBLE);

	}

	private int getId(String resourceName, Class<?> c) {
		try {
			Field idField = c.getDeclaredField(resourceName);
			return idField.getInt(idField);
		} catch (Exception e) {
			return -1;
		}

	}


	public void startExtendedDishFragment(UUID uuid) {
		// TODO Auto-generated method stub
		
		
		
		
		
		final FragmentTransaction ft = getFragmentManager().beginTransaction(); 
		Fragment frag=ExtendedDishFragment.newInstance(uuid);
		
		ft.replace(R.id.mainFragment, frag, "ExtendedDishTag"); 
		ft.addToBackStack(null);
		ft.commit();
		
		
	}





}
