package com.larrylivingston.sliding_menu_framework;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.larrylivingston.loaction_module.GooglePlayLocationManager;
import com.larrylivingston.mews.R;
import com.larrylivingston.mews.fragments.NewsListFragment;
import com.larrylivingston.utilities_module.Utilities;


public class MainActivity extends SlidingFragmentActivity implements
OnClickListener {
	private static String TAG = "MainActivity";
	public static int [] heightWidth = new int[2];
	private ImageButton title_right_btn;
	private Fragment mContentFragment;
	GooglePlayLocationManager googlePlayLocationManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.content_frame_layout);
		googlePlayLocationManager = GooglePlayLocationManager
				.getGooglePlayLocationManager(this
						.getApplicationContext());
		
		ActionBar Bar = getSupportActionBar();
		Bar.setDisplayShowTitleEnabled(false);
		Bar.setDisplayShowHomeEnabled(false);
		LayoutInflater inflater = LayoutInflater.from(this);
		View customView = inflater.inflate(R.layout.main_title_layout, null);
		Bar.setCustomView(customView);
		Bar.setDisplayShowCustomEnabled(true);
//		Bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00853c")));
		
		title_right_btn = (ImageButton) customView.findViewById(R.id.title_right_btn);
		title_right_btn.setOnClickListener(this);
		
		initSlidingMenu(savedInstanceState);
	}
	
	private void initSlidingMenu(Bundle savedInstanceState) {

		SharedPreferences sharedPreferences = this.getSharedPreferences(
				Utilities.LOGIN_PREFS, Context.MODE_PRIVATE);
		String name = sharedPreferences.getString("name", "");
		String pswd = sharedPreferences.getString("password", "");


		if (savedInstanceState != null)
			mContentFragment = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContentFragment");
		if (mContentFragment == null) {
			mContentFragment = new NewsListFragment();
		}

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame_layout, mContentFragment).commit();
		
		setBehindContentView(R.layout.menu_frame_layout);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame_layout, new MySlidingMenu()).commit();
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		heightWidth = Utilities.getHeightWidth(this);
		getSlidingMenu().setBehindWidth(heightWidth[1]/3*2);
	}

	@Override
	public void onClick(View v) {
		// TODO THE button on header
		switch (v.getId()) {
		case R.id.title_left_btn:
			getSlidingMenu().showMenu();
			Toast.makeText(v.getContext(), "h1", Toast.LENGTH_SHORT).show();
			break;
		case R.id.title_right_btn:
			Toast.makeText(v.getContext(), "h2", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}

	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContentFragment", mContentFragment);
	}



	public void switchContent(Fragment fragment) {
		mContentFragment = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame_layout, fragment).commit();
		getSlidingMenu().showContent();
	}
	
	/**
	 * For Actionbarsherlock only
	 */
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate your menu.
//		getSupportMenuInflater().inflate(R.menu.dummy_actionbar_menu, menu);
//		menu.add(1, 1, 1, "Save").setIcon(R.drawable.favo_menu)
//		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
//
//
//		SubMenu subMenu1 = menu.addSubMenu(1, 1, 1, "");
//		subMenu1.add(1, 2, 1, "facebook").setIcon(R.drawable.facebook);
//		subMenu1.add(1, 3, 1, "other").setIcon(R.drawable.others);
//
//		MenuItem subMenu1Item = subMenu1.getItem();
//		subMenu1Item.setIcon(R.drawable.ic_title_share);
//		subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
//				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//
////		SubMenu subMenu2 = menu.addSubMenu(1, 4, 1, "Overflow Item");
////		subMenu2.add(1, 5, 1, "Clear Cache");
////		subMenu2.add(1, 6, 1, "Logout");
////		subMenu2.add(1, 7, 1, "Setting");
//
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		String menuTitle = item.getTitle().toString();
//		switch (item.getItemId()) {
//		case 0:
//			Toast.makeText(this, "0", Toast.LENGTH_SHORT).show();
//			break;
//		case 1:
//			Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
//			break;
//		case 2:
//			Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
//			break;
//		default:
//			Toast.makeText(this, "default", Toast.LENGTH_SHORT).show();
//			break;
//		}
//		return true;
//	}

}
