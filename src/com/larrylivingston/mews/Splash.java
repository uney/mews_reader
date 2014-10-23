package com.larrylivingston.mews;

import java.util.Calendar;

import com.actionbarsherlock.app.SherlockActivity;
import com.larrylivingston.sliding_menu_framework.MainActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;

public class Splash extends SherlockActivity {
	boolean isFirstIn = false;

	private static final int GO_TO_MAIN = 1;
	private static final int GO_TO_GUIDE = 2;
	private static final long SPLASH_DELAY_MILLIS = 1000;
	private static Activity activity;

	private static Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_TO_MAIN:
				goMain();
				break;
			case GO_TO_GUIDE:
				goGuide();
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();
		setContentView(R.layout.splash_layout);
		init();
	}

	private void init() {
		activity = Splash.this;
		mHandler.sendEmptyMessageDelayed(GO_TO_GUIDE, SPLASH_DELAY_MILLIS);
	}

	private static void goMain() {

		Intent intent = new Intent(activity, MainActivity.class);
		activity.startActivity(intent);
		activity.finish();
	}

	private static void goGuide() {
		Intent intent = new Intent(activity, MainActivity.class);
		activity.startActivity(intent);
		activity.finish();
	}
	

}
