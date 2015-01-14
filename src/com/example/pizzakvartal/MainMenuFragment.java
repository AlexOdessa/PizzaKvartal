package com.example.pizzakvartal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainMenuFragment extends ListFragment {

	ArrayList<Dish> mDishTypes;
	
	ListView lv;
	


	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
	}

	@Override
	public void onDetach() {
		super.onDetach();
		
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Dish dish = mDishTypes.get(position);
		//FragmentManager fm=getChildFragmentManager();
		//Fragment frag=new ExtendedMenuFragment();
		//fm.beginTransaction().replace(R.id.mainFragment, frag).addToBackStack(null).commit();
		
		final FragmentTransaction ft = getFragmentManager().beginTransaction(); 
		Fragment frag=ExtendedMenuFragment.newInstance(dish.getDishType());
		ft.replace(R.id.mainFragment, frag, "ExtendedMenuTag"); 
		ft.addToBackStack(null);
		ft.commit(); 
		
		
	}

	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.menulist, null);
		lv=(ListView) v.findViewById(android.R.id.list);
		
		
		
		
		
		return v;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

		mDishTypes = DishLab.getDishLab(getActivity()).getDishTypes();
		MyAdapter adapter = new MyAdapter(getActivity(), 0, mDishTypes);
		setListAdapter(adapter);

		
		
		
		
		
	}
	
	

	
	




	/*@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

			if (getActivity().getActionBar() != null)
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
				getActivity().getActionBar().setTitle(R.string.app_name);
		}
	}
*/








	private class MyAdapter extends ArrayAdapter<Dish> {

		public MyAdapter(Context context, int resource, List<Dish> objects) {
			super(context, resource, objects);
			// TODO Auto-generated constructor stub
		}

		
		private class ViewHolder{
			ImageView imageDish;
			TextView dishName;
			
		}
		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			ViewHolder holder;
			if (convertView == null){
				holder=new ViewHolder();
			
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.mainmenu, parent, false);

			holder.imageDish = (ImageView) convertView
					.findViewById(R.id.imageDish);
			holder.dishName = (TextView) convertView
					.findViewById(R.id.textDishName);
			
			convertView.setTag(holder);
			}
			else {
				holder=(ViewHolder) convertView.getTag();
				
			}

			holder.dishName.setText(mDishTypes.get(position).getDishType());
			
			holder.imageDish.setImageResource(getId(mDishTypes.get(position).getUriPhoto(), R.drawable.class));
			
			return convertView;
		}

	}

	
	private int getId(String resourceName, Class<?> c) {
	    try {
	        Field idField = c.getDeclaredField(resourceName);
	        return idField.getInt(idField);
	    } catch (Exception e) {
	    	return -1;
	    }
		
	}
	
	public static MainMenuFragment newInstance(int selButton) {

		Bundle arg = new Bundle();
		arg.putInt(SingleFragmentActivity.EXTRA_FRAGMENT, selButton);

		MainMenuFragment fragment = new MainMenuFragment();
		fragment.setArguments(arg);

		return fragment;
	}
}
