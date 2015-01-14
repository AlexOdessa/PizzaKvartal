package com.example.pizzakvartal;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public abstract class SingleFragmentActivity extends FragmentActivity {

	abstract protected Fragment getFragment();

	public static String EXTRA_FRAGMENT = "com.example.pizzakvartal.extrafragment";

	ImageButton menuButton;
	ImageButton actionButton;
	ImageButton cartButton;
	ImageButton addressButton;
	ImageButton moreButton;
	static int selectButton;
	static TextView quantitySum;

	final String TAG_1 = "FRAGMENT_MAIN_MENU";
	final String TAG_2 = "FRAGMENT_ACTIONS";
	final String TAG_3 = "FRAGMENT_ADDRESS";
	final String TAG_4 = "FRAGMENT_CART";
	final String TAG_5 = "FRAGMENT_MORE";
	FragmentManager fm;

	private void startFragment(Fragment frag, String tag) {
		Fragment fragment = frag;
		String tagFrag = tag;
		// getSupportFragmentManager().popBackStack();
		if (tag.equals("FRAGMENT_MAIN_MENU")) {
			for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
				fm.popBackStack();
			}
			fm.beginTransaction().replace(R.id.mainFragment, fragment, tagFrag)
					.commit();
			selectButton = 0;
			// setUncheckedButtons();
			// menuButton.setImageResource(R.drawable.menu);
		} else
			fm.beginTransaction().replace(R.id.mainFragment, fragment, tagFrag)
					.addToBackStack(null).commit();

		setUncheckedButtons();
		selectButton = fragment.getArguments().getInt(EXTRA_FRAGMENT);

		setCheckedButton();
	}

	private void setCheckedButton() {

		switch (selectButton) {
		case 0: {
			menuButton.setImageResource(R.drawable.menu);
			break;
		}
		case 1: {
			actionButton.setImageResource(R.drawable.actions);
			break;
		}
		case 2: {
			cartButton.setImageResource(R.drawable.cart);
			break;
		}
		case 3: {
			addressButton.setImageResource(R.drawable.address);
			break;
		}
		case 4: {
			moreButton.setImageResource(R.drawable.more);
			break;
		}

		}

	}

	@TargetApi(11)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Fragment fragment;
		fm = getSupportFragmentManager();
		fragment = fm.findFragmentById(R.id.mainFragment);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(false);
		}

		getSupportFragmentManager().addOnBackStackChangedListener(
				new FragmentManager.OnBackStackChangedListener() {
					@Override
					public void onBackStackChanged() {
						int stackHeight = getSupportFragmentManager()
								.getBackStackEntryCount();

						if (stackHeight > 0) {
							// getActionBar().setHomeButtonEnabled(true);
							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
								getActionBar().setDisplayHomeAsUpEnabled(true);
							}
							Fragment fragment = fm
									.findFragmentById(R.id.mainFragment);
							String fragTag = fragment.getTag();
							if (fragTag.equals(TAG_4)) { // cart
								selectButton = 2;

							} else if (fragTag.equals(TAG_2))
								selectButton = 1; // actions
							else if (fragTag.equals(TAG_3))
								selectButton = 3; // address
							else if (fragTag.equals(TAG_5))
								selectButton = 4; //
							else
								selectButton = 0;

						} else {
							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
								getActionBar().setDisplayHomeAsUpEnabled(false);
								selectButton = 0;
								getActionBar().setTitle(R.string.app_name);
							}

							/*
							 * if (DishLab.getDishLab(getApplicationContext())
							 * .getQuantityDish() == 0) {
							 * 
							 * setUncheckedButtons();
							 * menuButton.setImageResource(R.drawable.menu);
							 * selectButton = 0; }
							 */

						}

						setUncheckedButtons();

						setCheckedButton();

					}

				});

		// -----------

		if (fragment == null && savedInstanceState == null) {
			fragment = getFragment();

			fm.beginTransaction().add(R.id.mainFragment, fragment).commit();
		}

		selectButton = fragment.getArguments().getInt(EXTRA_FRAGMENT);

		menuButton = (ImageButton) findViewById(R.id.menuButton);
		actionButton = (ImageButton) findViewById(R.id.actionButton);
		cartButton = (ImageButton) findViewById(R.id.cartButton);
		addressButton = (ImageButton) findViewById(R.id.addressButton);
		moreButton = (ImageButton) findViewById(R.id.moreButton);
		quantitySum = (TextView) findViewById(R.id.quantityDish);

		if (DishLab.getDishLab(this).getQuantityDish() > 0) {
			quantitySum.setVisibility(View.VISIBLE);
			quantitySum.setText(String.valueOf(DishLab.getDishLab(this)
					.getQuantityDish()));
		} else
			quantitySum.setVisibility(View.INVISIBLE);

		setUncheckedButtons();

		setCheckedButton();

		menuButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
					fm.popBackStack();
				}
				
				if (selectButton != 0) {

					startFragment(MainMenuFragment.newInstance(0), TAG_1);
					setUncheckedButtons();
					menuButton.setImageResource(R.drawable.menu);

				}
				
				else {
					startFragment(MainMenuFragment.newInstance(0), TAG_1);
					setUncheckedButtons();
					menuButton.setImageResource(R.drawable.menu);
					
				}
			}
			
		});

		actionButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (selectButton != 1) {

					for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
						fm.popBackStack();
					}
					ActionsFragment fragment = (ActionsFragment) fm
							.findFragmentByTag(TAG_2);
					if (fragment == null) {
						startFragment(ActionsFragment.newInstance(1), TAG_2);

						setUncheckedButtons();
						actionButton.setImageResource(R.drawable.actions);

					}

				}
			}
		});

		cartButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
					fm.popBackStack();
				}

				if (selectButton != 2) {

					// if
					// (DishLab.getDishLab(getApplicationContext()).getQuantityDish()
					// > 0) {

					CartFragment fragment = (CartFragment) fm
							.findFragmentByTag(TAG_4);
					if (fragment == null) {
						startFragment(CartFragment.newInstance(2), TAG_4);

						setUncheckedButtons();
						cartButton.setImageResource(R.drawable.cart);

					}

					// }
				} else {
					startFragment(CartFragment.newInstance(2), TAG_4);

					setUncheckedButtons();
					cartButton.setImageResource(R.drawable.cart);

				}
			}
		});

		addressButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (selectButton != 3) {
					for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
						fm.popBackStack();
					}
					AddressFragment fragment = (AddressFragment) fm
							.findFragmentByTag(TAG_3);

					if (fragment == null) {

						startFragment(AddressFragment.newInstance(3), TAG_3);

						setUncheckedButtons();
						addressButton.setImageResource(R.drawable.address);

					}
				}
			}
		});

		moreButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
					fm.popBackStack();
				}

				if (selectButton != 4) {

					MoreMenuFragment fragment = (MoreMenuFragment) fm
							.findFragmentByTag(TAG_5);

					if (fragment == null) {

						startFragment(MoreMenuFragment.newInstance(4), TAG_5);

						setUncheckedButtons();
						moreButton.setImageResource(R.drawable.more);

					}
				} else {
					startFragment(MoreMenuFragment.newInstance(4), TAG_5);

					setUncheckedButtons();
					moreButton.setImageResource(R.drawable.more);

				}
			}
		});
	}

	private void setUncheckedButtons() {
		menuButton.setImageResource(R.drawable.menublack);
		actionButton.setImageResource(R.drawable.actionsblack);
		cartButton.setImageResource(R.drawable.cartblack);
		addressButton.setImageResource(R.drawable.addressblack);
		moreButton.setImageResource(R.drawable.moreblack);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			// selectButton = 0;
			// setUncheckedButtons();
			// menuButton.setImageResource(R.drawable.menu);

			getSupportFragmentManager().popBackStack();
		}
		return true;

	}

	@TargetApi(11)
	@Override
	protected void onResume() {

		super.onResume();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

			if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

				getActionBar().setDisplayHomeAsUpEnabled(true);

			} else {
				getActionBar().setDisplayHomeAsUpEnabled(false);
				getActionBar().setTitle(R.string.app_name);
			}

		}
	}

}
