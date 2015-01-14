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
import android.widget.ImageView;
import android.widget.TextView;

public class ActionsFragment extends Fragment {

	public static final String DIALOG_ACTIONS_PICKER = "com.example.pizzakvartal.actions";

	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.actionsfragment, container, false);
		
		//TextView textAction=(TextView) v.findViewById(R.id.textAction);
		ImageView imageAction1=(ImageView)  v.findViewById(R.id.actionImage);
		imageAction1.setImageResource(R.drawable.action3);
		
		ImageView imageAction2=(ImageView)  v.findViewById(R.id.actionImage2);
		imageAction2.setImageResource(R.drawable.action4);
		
		
		
		
		
		return v;
	}

	
@TargetApi(11)
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

			
			getActivity().getActionBar().setTitle(R.string.actionS);
		}
	}



	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

		
	}

	public static ActionsFragment newInstance(int selButton) {

		Bundle arg = new Bundle();
		arg.putInt(SingleFragmentActivity.EXTRA_FRAGMENT, selButton);

		ActionsFragment fragment = new ActionsFragment();
		fragment.setArguments(arg);

		return fragment;
	}
	

}
