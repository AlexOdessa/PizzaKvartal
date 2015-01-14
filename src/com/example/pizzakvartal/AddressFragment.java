package com.example.pizzakvartal;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class AddressFragment extends Fragment {

	public static final String DIALOG_ADDRESS_PICKER = "com.example.pizzakvartal.address";

	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.addressfragment, null);
		
		
		TextView tvArray[] = { (TextView) v.findViewById(R.id.textAction),
				(TextView) v.findViewById(R.id.textView3),
				(TextView) v.findViewById(R.id.textView4),
				(TextView) v.findViewById(R.id.TextView5),
				(TextView) v.findViewById(R.id.textView6),
				(TextView) v.findViewById(R.id.TextView7),
				(TextView) v.findViewById(R.id.textView8),
				(TextView) v.findViewById(R.id.TextView9),
				(TextView) v.findViewById(R.id.TextView10),

		};

		ImageButton ibArray[] = {
				(ImageButton) v.findViewById(R.id.imageButton2),
				(ImageButton) v.findViewById(R.id.imageButton3),
				(ImageButton) v.findViewById(R.id.imageButton4),
				(ImageButton) v.findViewById(R.id.imageButton5),
				(ImageButton) v.findViewById(R.id.imageButton6),
				(ImageButton) v.findViewById(R.id.imageButton7),
				(ImageButton) v.findViewById(R.id.imageButton8),
				(ImageButton) v.findViewById(R.id.imageButton9),
				(ImageButton) v.findViewById(R.id.imageButton10),

		};

		for (int i = 0; i < tvArray.length; i++) {
			tvArray[i].setOnClickListener(new clickImageLogin(i));
			ibArray[i].setOnClickListener(new clickImageLogin(i));
		}

		return v;
	}

	private class clickImageLogin implements OnClickListener {

		int butPressed;

		clickImageLogin(int butPressed) {
			this.butPressed = butPressed;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			FragmentManager fm = getActivity().getSupportFragmentManager();
			AddressDialogFragment dialog = AddressDialogFragment
					.newInstance(butPressed);
			dialog.show(fm, DIALOG_ADDRESS_PICKER);
		}

	}

	
	@TargetApi(11)
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

			// getSupportFragmentManager().popBackStack(null,
			// FragmentManager.POP_BACK_STACK_INCLUSIVE);
			getActivity().getActionBar().setTitle(R.string.addressAndContacts);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		//SingleFragmentActivity.addressButton.setVisibility(View.VISIBLE);

	}

	public static AddressFragment newInstance(int selButton) {

		Bundle arg = new Bundle();
		arg.putInt(SingleFragmentActivity.EXTRA_FRAGMENT, selButton);

		AddressFragment fragment = new AddressFragment();
		fragment.setArguments(arg);

		return fragment;
	}

}
