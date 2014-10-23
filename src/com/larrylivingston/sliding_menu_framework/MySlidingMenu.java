package com.larrylivingston.sliding_menu_framework;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.larrylivingston.mews.R;
import com.larrylivingston.utilities_module.Utilities;

public class MySlidingMenu extends ListFragment {
	public static int [] heightWidth = new int[2];
	public static SharedPreferences sharedPreferences;
	private final static String PREFS_LOGGEDIN = Utilities.PREFS_LOGGEDIN;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.menu_list_layout, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//TODO Need to modify upon using
		sharedPreferences = getActivity().getSharedPreferences(Utilities.LOGIN_PREFS, Context.MODE_PRIVATE);
		heightWidth = Utilities.getHeightWidth(getActivity());
		SampleAdapter adapter = new SampleAdapter(getActivity());
		adapter.add(new MenuItem(null, R.drawable.menu_featured_image));
		adapter.add(new MenuItem(getResources().getString(R.string.menu_item_1), R.drawable.menu_icon_1));
		adapter.add(new MenuItem(getResources().getString(R.string.menu_item_2), R.drawable.menu_icon_2));
		adapter.add(new MenuItem(getResources().getString(R.string.menu_item_3), R.drawable.menu_icon_3));
		setListAdapter(adapter);
	}

	public class SampleAdapter extends ArrayAdapter<MenuItem> {
		public SampleAdapter(Context context) {
			super(context, 0);
		}
		//TODO Need to modify upon using
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(
						R.layout.menu_item_layout, null);
			}
			ImageView icon = (ImageView) convertView
					.findViewById(R.id.menu_imageview);
			icon.setImageResource(getItem(position).iconRes);
			if (position == 0) {
				LayoutParams para;
				para = icon.getLayoutParams();
				para.height = (int) heightWidth[0] * 1 / 3;
				para.width = (int) heightWidth[1] * 2 / 3;
				icon.setLayoutParams(para);
			}
			if (sharedPreferences.getBoolean(PREFS_LOGGEDIN, false) && position == 3) {
				convertView.setBackgroundColor(Color.RED);
				getItem(position).setTag("Logout");
			} else {
				convertView.setBackgroundColor(Color.YELLOW);
			}

			TextView title = (TextView)convertView.findViewById(R.id.menu_textview);
			title.setText(getItem(position).tag);
			LayoutParams para;
			para = title.getLayoutParams();
			para.height = (int) heightWidth[0] * 1 / 6;
			para.width = (int) heightWidth[1] * 2 / 3;
			title.setLayoutParams(para);
			return convertView;
		}
	}

	private class MenuItem {
		public String tag;
		public int iconRes;

		public MenuItem(String tag, int iconRes) {
			setTag(tag);
			this.iconRes = iconRes;
		}
		public void setTag(String tag){
			this.tag = tag;
		}
	}
	
	//TODO Need to modify upon using
	@Override
	public void onListItemClick(ListView l, final View v, int position, long id) {
		Fragment newContentFragment = null;
		switch (position) {
		case 0:
			Toast.makeText(v.getContext(), "hello", Toast.LENGTH_SHORT).show();
			break;
		case 1:
			//Different fragment for the menu
			break;
		case 2:
			//Different fragment for the menu
			break;
		case 3:
			if (!sharedPreferences.getBoolean(PREFS_LOGGEDIN, false)) {
				newContentFragment = new LoginFragment();
				MainActivity mainActivity = (MainActivity) v.getContext();
				mainActivity.switchContent(newContentFragment);
			} else if (sharedPreferences.getBoolean(PREFS_LOGGEDIN, false)) {
				MyAlertDialog.logoutDialog(v);
			}
			break;
		case 4:
			//Different fragment for the menu
			break;
		}
		if (newContentFragment != null){
			//play save here
			switchFragment(newContentFragment);
		}else{
			newContentFragment = new LoginFragment();
			switchFragment(newContentFragment);
		}
	}

	private void switchFragment(Fragment fragment) {
		if (getActivity() == null){
			return;
		}
		if (getActivity() instanceof MainActivity) {
			MainActivity mainActivity = (MainActivity) getActivity();
			mainActivity.switchContent(fragment);
		}
	}

}
