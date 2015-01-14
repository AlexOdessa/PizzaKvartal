package com.example.pizzakvartal;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class DeliveryFragment extends Fragment {
	@TargetApi(11)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
			getActivity().getActionBar().setTitle(R.string.delivery);
		}
	}

	
	@TargetApi(11)
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
			getActivity().getActionBar().setTitle(R.string.delivery);
		}
	}



	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.deliverylayout, container, false);
		
		
		
		return v;
	}

	public static DeliveryFragment newInstance(int selButton) {

		Bundle arg = new Bundle();
		arg.putInt(SingleFragmentActivity.EXTRA_FRAGMENT, selButton);

		DeliveryFragment fragment = new DeliveryFragment();
		fragment.setArguments(arg);

		return fragment;
	}
	

}
