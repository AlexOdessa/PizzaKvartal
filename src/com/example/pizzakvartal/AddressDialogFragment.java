package com.example.pizzakvartal;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.R.drawable;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AddressDialogFragment extends DialogFragment {
	public static final String EXTRA_ADDRESS = "com.example.pizzakvartal.extraaddress";
	ArrayList<Restaurants> mRestaurantsArray;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		Restaurants mRest;
		mRestaurantsArray = new ArrayList<Restaurants>();
		// -----1-------
		mRest = new Restaurants();
		mRest.setAddress("ул. ћаршала √оворова, 18");
		mRest.setMapResourse("map1");
		mRest.setType("1"); // ---- 1-итал квартал, 2- каса, 3 - белла, 4-тасти
		mRest.setPhoneNumber1("(063) 19 000 54");
		mRest.setPhoneNumber2("(048) 759 76 16");

		mRestaurantsArray.add(mRest);
		// -----2------
		mRest = new Restaurants();
		mRest.setAddress("ул. «аболотного, 52");
		mRest.setMapResourse("map2");
		mRest.setType("1"); // ---- 1-итал квартал, 2- каса, 3 - белла, 4-тасти
		mRest.setPhoneNumber1("(063) 190-01-12");
		mRest.setPhoneNumber2("(048) 784 67 98");

		mRestaurantsArray.add(mRest);
		// -----3------
		mRest = new Restaurants();
		mRest.setAddress("ул. ѕреображенска€, 62");
		mRest.setMapResourse("map3");
		mRest.setType("1"); // ---- 1-итал квартал, 2- каса, 3 - белла, 4-тасти
		mRest.setPhoneNumber1("(048) 722 03 70");

		mRestaurantsArray.add(mRest);
		// -----4------
		mRest = new Restaurants();
		mRest.setAddress("ул. ƒобровольского, 129");
		mRest.setMapResourse("map4");
		mRest.setType("1"); // ---- 1-итал квартал, 2- каса, 3 - белла, 4-тасти
		mRest.setPhoneNumber1("(063) 190 00 53");
		mRest.setPhoneNumber2("(048) 770 00 10");

		mRestaurantsArray.add(mRest);
		// -----5------
		mRest = new Restaurants();
		mRest.setAddress("ул. ∆уковского, 31");
		mRest.setMapResourse("map5");
		mRest.setType("1"); // ---- 1-итал квартал, 2- каса, 3 - белла, 4-тасти
		mRest.setPhoneNumber1("(048) 785 20 73");

		mRestaurantsArray.add(mRest);
		// -----6------
		mRest = new Restaurants();
		mRest.setAddress("г. »льичевск, ул. Ћенина 20б");
		mRest.setMapResourse("map6");
		mRest.setType("1"); // ---- 1-итал квартал, 2- каса, 3 - белла, 4-тасти
		mRest.setPhoneNumber1("(048 68) 7 99 91");
		mRest.setPhoneNumber2("(093) 620 66 55");

		mRestaurantsArray.add(mRest);
		// -----7------
		mRest = new Restaurants();
		mRest.setAddress(" ул. ≈катериненска€ 8/10 ");
		mRest.setMapResourse("map7");
		mRest.setType("2"); // ---- 1-итал квартал, 2- каса, 3 - белла, 4-тасти
		mRest.setPhoneNumber1("(048) 726 96 26");
		mRest.setPhoneNumber2("(093) 879 57 39");

		mRestaurantsArray.add(mRest);
		// -----8------
		mRest = new Restaurants();
		mRest.setAddress("ул. ѕреображенска€ 55 ");
		mRest.setMapResourse("map8");
		mRest.setType("3"); // ---- 1-итал квартал, 2- каса, 3 - белла, 4-тасти
		mRest.setPhoneNumber1(" (048) 785 01 02");
		mRest.setPhoneNumber2("(063) 190 01 22");

		mRestaurantsArray.add(mRest);

		// -----9------
		mRest = new Restaurants();
		mRest.setAddress("ул. ƒерибасовска€, 26");
		mRest.setMapResourse("map9");
		mRest.setType("4"); // ---- 1-итал квартал, 2- каса, 3 - белла, 4-тасти
		mRest.setPhoneNumber1("(048) 33 9 223");

		mRestaurantsArray.add(mRest);

	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (getActivity() == null)
			return null;
		else {

			int selItem = getArguments().getInt(EXTRA_ADDRESS);

			final Restaurants rest = mRestaurantsArray.get(selItem);

			final Dialog dialog = new Dialog(getActivity());
			// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setTitle(rest.mAddress);
			dialog.setContentView(R.layout.addresslayout);

			ImageButton image1 = (ImageButton) dialog
					.findViewById(R.id.imageButton6);

			image1.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Uri number = Uri.parse("tel:" + rest.getPhoneNumber1());
					Intent i = new Intent(Intent.ACTION_DIAL, number);

					startActivity(i);

				}
			});

			TextView tv1 = (TextView) dialog.findViewById(R.id.numberOfDish);
			tv1.setText(rest.getPhoneNumber1());

			ImageButton image2;
			TextView tv2;

			LinearLayout ll = (LinearLayout) dialog
					.findViewById(R.id.layoutPhone2);

			if (rest.getPhoneNumber2() == null) {
				ll.setVisibility(View.GONE);

			} else {

				image2 = (ImageButton) dialog.findViewById(R.id.imageMinus);
				tv2 = (TextView) dialog.findViewById(R.id.TextView01);
				tv2.setText(rest.getPhoneNumber2());

				image2.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Uri uri = Uri.parse("tel:" + rest.mPhoneNumber2);
						Intent i = new Intent(Intent.ACTION_DIAL, uri);
						startActivity(i);
					}
				});

			}

			TouchImageView touchView = (TouchImageView) dialog
					.findViewById(R.id.imgDisplay);
			touchView.setImageResource(getId(rest.getMapResourse(),
					R.drawable.class));

			return dialog;
		}

	}

	public static AddressDialogFragment newInstance(int item) {

		Bundle arg = new Bundle();
		arg.putInt(EXTRA_ADDRESS, item);
		AddressDialogFragment dialog = new AddressDialogFragment();
		dialog.setArguments(arg);

		return dialog;

	}

	private int getId(String resourceName, Class<?> c) {
		try {
			Field idField = c.getDeclaredField(resourceName);
			return idField.getInt(idField);
		} catch (Exception e) {
			return -1;
		}

	}

}
