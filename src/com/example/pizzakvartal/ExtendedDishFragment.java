package com.example.pizzakvartal;

import java.util.ArrayList;
import java.util.UUID;

import android.annotation.TargetApi;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class ExtendedDishFragment extends Fragment {
	public final static String EXTRA_DISH = "com.example.pizzakvartal.extradish";

	//ArrayList<Dish> mDishInfo;
	ArrayList<Dish> mDishArray;
	Dish mDish;
	
	ImageButton mButtonMinus;
	ImageButton mButtonPlus;
	TextView mToCart;
	TextView mNumber;
	
	
	public static ExtendedDishFragment newInstance(UUID id) {

		Bundle arg = new Bundle();
		arg.putSerializable(EXTRA_DISH, id);
		ExtendedDishFragment fragment = new ExtendedDishFragment();
		fragment.setArguments(arg);

		return fragment;
	}

	
	
	@TargetApi(11)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setRetainInstance(true);
		mDishArray = DishLab.getDishLab(getActivity()).getDishes();
		UUID mDishId=(UUID) getArguments().getSerializable(EXTRA_DISH);
		mDish=DishLab.getDishLab(getActivity()).getDish(mDishId);
		
		

		
	}



	@TargetApi(11)
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
			getActivity().getActionBar().setTitle(mDish.getDishType());
		}
	}



	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.extendeddish, container, false);
		
		ImageView imageDish=(ImageView) view.findViewById(R.id.imageDish);
		
		
		TextView nameDish=(TextView) view.findViewById(R.id.textDishName);
		TextView dishInfo=(TextView) view.findViewById(R.id.textDishInfo);
		TextView dishPrice=(TextView) view.findViewById(R.id.textPrice);
		
		mButtonMinus=(ImageButton) view.findViewById(R.id.imageMinus);
		mButtonPlus=(ImageButton) view.findViewById(R.id.imagePlus);
		mToCart=(TextView) view.findViewById(R.id.toCart);
		mNumber=(TextView) view.findViewById(R.id.numberOfDish);
		
		nameDish.setText(mDish.getDishName());
		dishInfo.setText(mDish.getDishInfoFull());
		dishPrice.setText(mDish.getDishPrice1());
		
		if (mDish.getUriPhoto() == null
				| mDish.getUriPhoto().equals(""))

			imageDish.setImageResource(R.drawable.no_image2);

		else{
			String sdPath=Environment.getExternalStorageDirectory()
			.getAbsolutePath().concat(Utils.workFolder);
			imageDish.setImageBitmap(NewThreadGetMenuInfo.decodeSampledBitmapFromUri(sdPath.concat(mDish.getUriPhoto()), 120,120));
			
		}
		
		
		
		if (mDish.getDishQuantity()==0){
			mButtonMinus.setVisibility(View.INVISIBLE);
			mButtonPlus.setVisibility(View.INVISIBLE);
			mNumber.setVisibility(View.INVISIBLE);
			mToCart.setVisibility(View.VISIBLE);
		}
		else {
			mButtonMinus.setVisibility(View.VISIBLE);
			mButtonPlus.setVisibility(View.VISIBLE);
			mNumber.setVisibility(View.VISIBLE);
			mToCart.setVisibility(View.INVISIBLE);
			
			mNumber.setText(String.valueOf(mDish.getDishQuantity()));
		}
		
		mToCart.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mButtonMinus.setVisibility(View.VISIBLE);
				mButtonPlus.setVisibility(View.VISIBLE);
				mNumber.setVisibility(View.VISIBLE);
				mToCart.setVisibility(View.INVISIBLE);
				setQuantity(1);
				
			}
		});
		
		mButtonPlus.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				setQuantity(1);
				
			}
		});		
		
		mButtonMinus.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				setQuantity(-1);
				
				if (mDish.getDishQuantity()==0){
					
					mButtonMinus.setVisibility(View.INVISIBLE);
					mButtonPlus.setVisibility(View.INVISIBLE);
					mNumber.setVisibility(View.INVISIBLE);
					mToCart.setVisibility(View.VISIBLE);
				}
				
			}
		});	
		
		
		
		return view;
	}

	private void setQuantity(int quantSelDish) {

		DataBaseHelper myDbHelper = new DataBaseHelper(getActivity());
		try {

			myDbHelper.openDataBase();

		} catch (SQLException sqle) {

			throw sqle;

		}
		
		mDish.setDishQuantity(mDish.getDishQuantity()+quantSelDish);
		myDbHelper.savedDish2BD(mDish);
		int sum = DishLab.getDishLab(getActivity()).getQuantityDish();
		SingleFragmentActivity.quantitySum.setText(String.valueOf(sum));
		if (sum == 0)
			SingleFragmentActivity.quantitySum.setVisibility(View.INVISIBLE);

		else
			SingleFragmentActivity.quantitySum.setVisibility(View.VISIBLE);
		
		mNumber.setText(String.valueOf(mDish.getDishQuantity()));
		
		myDbHelper.close();
	}
	
}
