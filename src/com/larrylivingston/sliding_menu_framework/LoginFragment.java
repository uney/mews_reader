package com.larrylivingston.sliding_menu_framework;


import java.util.Arrays;
import java.util.Calendar;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.larrylivingston.mews.R;
import com.larrylivingston.mews.fragments.NewsListFragment;
import com.larrylivingston.utilities_module.Utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
/**
 * This is the facebook login fragment, it has to be used with the Utilities Class and the Facebook Android Library,
 * You may change the background or other UI elements as you want in the layout file
 * This class provides two functions:
 * 1. Facebook Authentication
 * 2. Facebook user name request (makeMeRequest method)
 * @author uney
 *
 */
public class LoginFragment extends Fragment {

	private LoginButton facebook_login_btn;
	private Context context;
	private ProgressDialog progressDialog;
	//Flag for lifecycle activity
	private boolean isResumed = false;
	SharedPreferences sharedPreferences;

	/*
	 * Facebook fields
	 * 1. UiLifecycleHandler
	 * 2. userName
	 * 3. unserId
	 * 4. Session
	 */
	private UiLifecycleHelper uiHelper;
	private String userName, userId;
	private Session.StatusCallback callback_facebook = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};
	//Debugging Flags
	private final static String TAG = "FACEBOOK";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		sharedPreferences = getActivity().getSharedPreferences(Utilities.LOGIN_PREFS,
				Context.MODE_PRIVATE);
		uiHelper = new UiLifecycleHelper(getActivity(), callback_facebook);
		uiHelper.onCreate(savedInstanceState);
		View v = inflater.inflate(R.layout.login_layout, null);
		context=getActivity();
		facebook_login_btn = (LoginButton) v.findViewById(R.id.fb_login_btn);
		facebook_login_btn.setReadPermissions(Arrays.asList("user_likes",
				"user_status"));

		boolean logged_in = sharedPreferences.getBoolean(Utilities.PREFS_LOGGEDIN, false);
		String accessToken = sharedPreferences.getString(Utilities.PREFS_FACEBOOK_ACCESS_TOKEN, null);
		long expiresIn = sharedPreferences.getLong(Utilities.PREFS_FACEBOOK_EXPIRE_TIME, 0);
		if (Utilities.isSessionValid(accessToken, expiresIn)) {
			loginDone();
		} 
		else {
			accessToken = sharedPreferences.getString(Utilities.PREFS_WEIBO_ACCESS_TOKEN,
					null);
			expiresIn = sharedPreferences.getLong(Utilities.PREFS_WEIBO_EXPIRE_TIME, 0);
			if (Utilities.isSessionValid(accessToken, expiresIn)) {
				loginDone();
			} else {
				Toast.makeText(getActivity(), "login please", Toast.LENGTH_LONG).show();
			}
		}
//		facebook_login_btn.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//			}
//		});

		return v;
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		loginDone();
	}
	
	/*
	 * Facebook Authentication
	 * onSessionStateChangeMethod
	 */
	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (isResumed) {
			Log.i("", "facebook exception: " + exception);
			if (state.equals(SessionState.OPENED)) {
				// TODO do something if users are successfully logged in
				SharedPreferences pref = context.getSharedPreferences(Utilities.LOGIN_PREFS,
						context.MODE_PRIVATE);
				Calendar c = Calendar.getInstance();
				c.setTime(session.getExpirationDate());
				Utilities.updateFacebookToken(pref, Utilities.FACEBOOK, true,
						session.getAccessToken(), c.getTimeInMillis());
				String id, name;
				id = pref.getString(Utilities.PREFS_FACEBOOK_USERID, null);
				name = pref.getString(Utilities.PREFS_FACEBOOK_USERNAME,
						null);
				if (name == null) {
					makeMeRequest(session);
					progressDialog = ProgressDialog.show(context,
							"Logging in to Facebook", "Logging in...", true,
							false);
				}

			} else if (state.equals(SessionState.CLOSED)) {
				// TODO do something if users are successfully logged in
				SharedPreferences pref = context.getSharedPreferences(Utilities.LOGIN_PREFS,
						context.MODE_PRIVATE);
				Utilities.updateFacebookToken(pref, "", false, null, 0);
			} else if (state.isClosed()) {
				// TODO do something if users aren't successfully logged in
			}
		}
	}
	/*
	 * Getting user's facebook user name
	 */
	private void makeMeRequest(final Session session) {
		// Make an API call to get user data and define a
		// new callback to handle the response.
		Request request = Request.newMeRequest(session,
				new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						// If the response is successful
						if (session == Session.getActiveSession()) {
							if (user != null) {
								// Retrieve the ProfilePicture using userId
								// view that in turn displays the profile
								// picture.
								userId = user.getId();
								userName = user.getName();
								SharedPreferences pref = context.getSharedPreferences(
										Utilities.LOGIN_PREFS, context.MODE_PRIVATE);
								Utilities.updateFacebookUserName(pref, userId,
										userName);
								Toast.makeText(context,
										context.getResources().getString(R.string.login_success_string)+userName,
										Toast.LENGTH_LONG).show();
								loginDone();
								if (progressDialog != null) {
									progressDialog.dismiss();
								}
								// TODO switch to another activity
							}
						}
						if (response.getError() != null) {
							// Handle errors, will do so later.
							Log.i(TAG, TAG+"facebook, is on error now");

						}
					}
				});
		request.executeAsync();
	}
	/*
	 * After Login handling
	 */
	private void loginDone() {
		//TODO Need to modify upon using
		Log.i(TAG, TAG+"---- Login.class, login done");
		Fragment newContent = new NewsListFragment();
		MainActivity mainActivity = (MainActivity) getActivity();
		mainActivity.switchContent(newContent);
	}

	/*
	 * Lifecycle overriding
	 */
	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
		isResumed = true;
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
		isResumed = false;
	}
}
