package com.larrylivingston.mews;

import android.content.SharedPreferences;
import android.text.TextUtils;

public class LoginUtils {
	public static final String FACEBOOK = "facebook";
	
	//Store login information in local
	private final static String PREFERENCE_USERID = MewsConstants.PREFERENCE_USERID;
	private final static String PREFERENCE_USERNAME = MewsConstants.PREFERENCE_USERNAME;
	private static final String PREFERENCE_NAME_LOGIN = MewsConstants.PREFERENCE_NAME_LOGIN;
	private static final String PREFERENCE_LOGGED_IN = MewsConstants.PREFERENCE_LOGGED_IN;
	private static final String PREFERENCE_ACCESS_TOKEN = MewsConstants.PREFERENCE_ACCESS_TOKEN;
	private static final String PREFERENCE_LOGIN_EXPIRE_TIME = MewsConstants.PREFERENCE_LOGIN_EXPIRE_TIME;
	
	private static final String PREFERENCE_SORTING = MewsConstants.PREFERENCE_SORTING;



	/**
	 * Check if the local session valid using this method,
	 * for both facebook and weibo
	 * @param accessToken
	 * @param expiresIn
	 * @return
	 */
	public static boolean isSessionValid(String accessToken, long expiresIn) {
		return (!TextUtils.isEmpty(accessToken) && (expiresIn == 0 || (System
				.currentTimeMillis() < expiresIn)));
	}

	public static void updateFacebookUserName(SharedPreferences loginPrefs,
			String userId, String userName) {
		SharedPreferences.Editor editor = loginPrefs.edit();
		editor.putString(PREFERENCE_USERID, userId);
		editor.putString(PREFERENCE_USERNAME, userName);
		editor.commit();
	}
	
	
	public static String getFacebookUserId(SharedPreferences loginPrefs){
		return loginPrefs.getString(PREFERENCE_USERID, null);
	}
	
	/**
	 * Update the local session stored in Sharedpreferences
	 * 
	 * @param data
	 * @param uid
	 * @param localToken
	 * @param localExpiresIn
	 */
	public static void updateToken(SharedPreferences loginPrefs,
			String platform, boolean loggedIn, String accessToken,
			long expiresIn) {
		SharedPreferences.Editor editor = loginPrefs.edit();
		editor.putBoolean(PREFERENCE_LOGGED_IN, loggedIn);
		editor.putString(PREFERENCE_ACCESS_TOKEN, accessToken);
		editor.putLong(PREFERENCE_LOGIN_EXPIRE_TIME, expiresIn);
		editor.commit();
	}

}
