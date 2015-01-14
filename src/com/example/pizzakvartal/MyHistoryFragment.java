package com.example.pizzakvartal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyHistoryFragment extends ListFragment {
	ArrayList<Dish> mHistoryArray;
	ListView lv;
	PointF oldCoord;
	TextView noOrdersTextView;
	
	@TargetApi(11)
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActivity().getActionBar().setTitle(R.string.myOrders);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.menulist, container, false);
		lv = (ListView) v.findViewById(android.R.id.list);
		noOrdersTextView=(TextView) v.findViewById(R.id.noOrdersText);
		if (mHistoryArray.isEmpty()) noOrdersTextView.setVisibility(View.VISIBLE);
		
		
		return v;
	}

	@TargetApi(11)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		mHistoryArray = DishLab.getDishLab(getActivity()).getDateHistory(
				getActivity());

		HistoryAdapter adapter = new HistoryAdapter(getActivity(), 0,
				mHistoryArray);
		setListAdapter(adapter);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActivity().getActionBar().setTitle(R.string.myOrders);
		}

	}

	private class HistoryAdapter extends ArrayAdapter<Dish> {

		public HistoryAdapter(Context context, int resource, List<Dish> objects) {
			super(context, resource, objects);
			// TODO Auto-generated constructor stub
		}

		private class ViewHolder {

			TextView orderDate;
			TextView deleteButton;
			RelativeLayout relLayoutF;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.historymenu, parent, false);
				// holder.imageDish = (ImageView)
				// convertView.findViewById(R.id.imageDish);

				holder.relLayoutF = (RelativeLayout) convertView
						.findViewById(R.id.relLayoutF);

				holder.deleteButton = (TextView) convertView
						.findViewById(R.id.deleteOrderBtn);

				holder.orderDate = (TextView) convertView
						.findViewById(R.id.textDishName);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();

			}

			long dateOrder = mHistoryArray.get(position).getDate();

			SimpleDateFormat sdf = new SimpleDateFormat("d-MM-yyyy HH:mm");
			holder.orderDate.setText(sdf.format(dateOrder));
			
			holder.deleteButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
					//Dish dish = mHistoryArray.get(position);
					DishLab.getDishLab(getActivity()).deleteOrder(getActivity(), mHistoryArray.get(position).getDate());
					
					mHistoryArray = DishLab.getDishLab(getActivity()).getDateHistory(
							getActivity());
					holder.deleteButton.setVisibility(View.INVISIBLE);
					((HistoryAdapter)lv.getAdapter()).notifyDataSetChanged();
					if (mHistoryArray.isEmpty()) noOrdersTextView.setVisibility(View.VISIBLE);
				}
			});
			
			
			holder.relLayoutF.setOnTouchListener(new View.OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub

					switch (arg1.getAction()) {
					case MotionEvent.ACTION_DOWN:
						// Log.i("XXX", " ACTION_DOWN");
						oldCoord = new PointF(arg1.getX(), arg1.getY());
						break;
					case MotionEvent.ACTION_MOVE:
						// Log.i("XXX", " ACTION_MOVE");
						break;
					case MotionEvent.ACTION_UP:
						// Log.i("XXX", " ACTION_UP");
						PointF newCoord = new PointF(arg1.getX(), arg1.getY());
						// Log.d("XXX",
						// (newCoord.x-oldCoord.x)+" "+(newCoord.y-oldCoord.y));
						int absMoveX = (int) ((newCoord.x - oldCoord.x) > 0 ? newCoord.x
								- oldCoord.x
								: -(newCoord.x - oldCoord.x));
						int absMoveY = (int) ((newCoord.y - oldCoord.y) > 0 ? newCoord.y
								- oldCoord.y
								: -(newCoord.y - oldCoord.y));
						
						if (absMoveX > 2 || absMoveY>2) {
							if (holder.deleteButton.getVisibility()==View.INVISIBLE)
								holder.deleteButton.setVisibility(View.VISIBLE);
							else 
								holder.deleteButton.setVisibility(View.INVISIBLE);
						} else
						{
							holder.deleteButton.setVisibility(View.INVISIBLE);
							
							//-
							Dish dish = mHistoryArray.get(position);

							final FragmentTransaction ft = getFragmentManager().beginTransaction();
							Fragment frag = MyHistoryOrderFragment.newInstance(dish.getDate());
							ft.replace(R.id.mainFragment, frag, "FRAGMENT_MORE");
							ft.addToBackStack(null);
							ft.commit();
							
							//-
							//Log.d("XXX", lv.getCheckedItemPosition()+"");
						}break;

					}

					return true;
				}
			});

			return convertView;
		}

	}

	/*public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Dish dish = mHistoryArray.get(position);

		final FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment frag = MyHistoryOrderFragment.newInstance(dish.getDate());
		ft.replace(R.id.mainFragment, frag, "FRAGMENT_MORE");
		ft.addToBackStack(null);
		ft.commit();

	}*/

	public static MyHistoryFragment newInstance(int selButton) {

		Bundle arg = new Bundle();
		arg.putInt(SingleFragmentActivity.EXTRA_FRAGMENT, selButton);

		MyHistoryFragment fragment = new MyHistoryFragment();
		fragment.setArguments(arg);

		return fragment;
	}
}
