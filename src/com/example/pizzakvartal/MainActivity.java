package com.example.pizzakvartal;

import android.os.Bundle;
import android.support.v4.app.Fragment;




public class MainActivity extends SingleFragmentActivity {
private final int fragmentInt=0;
	
	@Override
	protected Fragment getFragment() {
		// TODO Auto-generated method stub
		Bundle arg=new Bundle();
		arg.putInt(SingleFragmentActivity.EXTRA_FRAGMENT, fragmentInt);
		
		MainMenuFragment fragment=new MainMenuFragment();
		fragment.setArguments(arg);
		return fragment;
	}

	

}
