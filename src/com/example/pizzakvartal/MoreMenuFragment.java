package com.example.pizzakvartal;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MoreMenuFragment extends Fragment {
	@TargetApi(11)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
			getActivity().getActionBar().setTitle(R.string.app_name);
		}
	}

	@TargetApi(11)
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
			getActivity().getActionBar().setTitle(R.string.app_name);
		}
	}


	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.morelayout, container, false);
		LinearLayout llDelivery = (LinearLayout) v
				.findViewById(R.id.llDelivery);
		LinearLayout llFeeadback = (LinearLayout) v
				.findViewById(R.id.llFeeadback);
		LinearLayout llHistory = (LinearLayout) v
				.findViewById(R.id.llHistory);
		
		llHistory.setOnClickListener(new ButtonsListener(0));
		llDelivery.setOnClickListener(new ButtonsListener(1));
		llFeeadback.setOnClickListener(new ButtonsListener(2));
		return v;
	}

	class ButtonsListener implements OnClickListener {

		int selButton;

		public ButtonsListener(int selButton) {
			// TODO Auto-generated constructor stub
			this.selButton = selButton;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			final FragmentTransaction ft = getFragmentManager()
					.beginTransaction();
			Fragment frag=null;

			switch (selButton) {
			case (1): {
				frag = DeliveryFragment.newInstance(4);
				break;
			}
			case (2): {
				frag = FeedbackFragment.newInstance(4);
				break;
			}
			case (0): {
				frag = MyHistoryFragment.newInstance(4);
				break;
			}
			}

			ft.replace(R.id.mainFragment, frag, "FRAGMENT_MORE");
			ft.addToBackStack(null);
			ft.commit();
		}

	}

	public static MoreMenuFragment newInstance(int selButton) {

		Bundle arg = new Bundle();
		arg.putInt(SingleFragmentActivity.EXTRA_FRAGMENT, selButton);

		MoreMenuFragment fragment = new MoreMenuFragment();
		fragment.setArguments(arg);

		return fragment;
	}

}
