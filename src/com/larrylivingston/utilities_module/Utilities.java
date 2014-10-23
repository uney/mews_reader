package com.larrylivingston.utilities_module;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;

public class Utilities {
	/**
	 * Login utility goes here
	 */
	private static final String DEFAULT_USERNAME="Guest";
	public static final String FACEBOOK = "facebook";
	public static final String WEIBO = "weibo";
	public static final String TWITTER = "twitter";
	private static final String WEIBO_KEY = "4026002034";
	private static final String WEIBO_APP_SECRET = "bb881f2fb9377b926c6b5b33d6b00fc5";
	private static final String WEIBO_REDIRECT_URL = "http://www.sina.com";
	// public static final String LOGIN_PREFS = "login_prefs";
	
	//Store login information in local
	public static final String LOGIN_PREFS = "login_prefs";
	public static final String PREFS_LOGGEDIN = "logged_in";
	public static final String PREFS_PLATFORM = "platform";
	public static final String PREFS_USER_ID = "user_id";

	
	public static final String PREFS_FACEBOOK_LOGGEDIN = "facebook_logged_in";
	public static final String PREFS_WEIBO_LOGGEDIN = "weibo_logged_in";
	public static final String PREFS_FACEBOOK_ID = "facebook_id";
	public static final String PREFS_WEIBO_ID = "weibo_id";
	
	//access token
	public static final String PREFS_FACEBOOK_ACCESS_TOKEN = "facebook_access_token";
	public static final String PREFS_WEIBO_ACCESS_TOKEN = "weibo_access_token";
	
	//expire time
	public static final String PREFS_FACEBOOK_EXPIRE_TIME = "facebook_expire_time";
	public static final String PREFS_WEIBO_EXPIRE_TIME = "weibo_expire_time";
	
	//user name
	public static final String PREFS_FACEBOOK_USERNAME = "facebook_user_name";
	public static final String PREFS_WEIBO_USERNAME = "weibo_user_name";
	
	//user id (for internal use only)
	public static final String PREFS_FACEBOOK_USERID = "facebook_user_id";
	public static final String PREFS_WEIBO_USERID = "weibo_user_id";
	
	/**
	 * Fields for file system,
	 * Should be modified upon using
	 * TODO
	 */
	public static final String appFilePath = "/YOUR_APP_NAME";
	public static final String defaultPhotoPath = "/DCIM";
	public static final String appCachePath = appFilePath+"/cached_file";
	public static final String appPhotoPath = appFilePath+"/image_file";

	/*
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

	/*
	 * methods for storing the post-login information to local
	 * @param loginPrefs
	 * @param userId
	 * @param userName
	 */
	public static void updateFacebookUserName(SharedPreferences loginPrefs,
			String userId, String userName) {
		SharedPreferences.Editor editor = loginPrefs.edit();
		editor.putString(PREFS_FACEBOOK_USERID, userId);
		editor.putString(PREFS_FACEBOOK_USERNAME, userName);
		editor.commit();
	}
	
	public static void updateWeiboUserName(SharedPreferences loginPrefs,
			String userId, String userName) {
		SharedPreferences.Editor editor = loginPrefs.edit();
		editor.putString(PREFS_WEIBO_USERID, userId);
		editor.putString(PREFS_WEIBO_USERNAME, userName);
		editor.commit();
	}
	
	public static String getWeiboUserId(Context context, SharedPreferences loginPrefs){
		return loginPrefs.getString(PREFS_WEIBO_USERID, DEFAULT_USERNAME);
	}	
	public static String getFacebookUserId(Context context, SharedPreferences loginPrefs){
		return loginPrefs.getString(PREFS_FACEBOOK_USERID, DEFAULT_USERNAME);
	}
	
	/*
	 * Update the local session stored in Sharedpreferences
	 * 
	 * @param data
	 * @param uid
	 * @param localToken
	 * @param localExpiresIn
	 */
	public static void updateFacebookToken(SharedPreferences loginPrefs,
			String platform, boolean loggedIn, String accessToken,
			long expiresIn) {
		SharedPreferences.Editor editor = loginPrefs.edit();
		editor.putBoolean(PREFS_FACEBOOK_LOGGEDIN, loggedIn);
		editor.putBoolean(PREFS_LOGGEDIN, loggedIn);
		editor.putString(PREFS_PLATFORM, platform);
		editor.putString(PREFS_FACEBOOK_ACCESS_TOKEN, accessToken);
		editor.putLong(PREFS_FACEBOOK_EXPIRE_TIME, expiresIn);
		editor.commit();
	}
	
	public static void updateWeiboToken(SharedPreferences loginPrefs,
			String platform, boolean loggedIn, String accessToken,
			long expiresIn) {
		SharedPreferences.Editor editor = loginPrefs.edit();
		editor.putBoolean(PREFS_WEIBO_LOGGEDIN, loggedIn);
		editor.putBoolean(PREFS_LOGGEDIN, loggedIn);
		editor.putString(PREFS_PLATFORM, platform);
		editor.putString(PREFS_WEIBO_ACCESS_TOKEN, accessToken);
		editor.putLong(PREFS_WEIBO_EXPIRE_TIME, expiresIn);
		editor.commit();
	}
	public static String getWeiboKey() {
		return WEIBO_KEY;
	}
	public static String getWeiboSecret() {
		return WEIBO_APP_SECRET;
	}
	
	/**
	 * Screen Size Utility goes here
	 */
	@SuppressLint("NewApi")
	public static int[] getHeightWidth(Activity activity) {
		
		int[] heightWidth = new int[2];
		int height, width;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
			Display display = activity.getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			width = size.x;
			height = size.y;
		}else{
			DisplayMetrics displaymetrics = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay()
					.getMetrics(displaymetrics);
			height = displaymetrics.heightPixels;
			width = displaymetrics.widthPixels;
		}
		heightWidth[0] = height;
		heightWidth[1] = width;
		return heightWidth;
	}
	
	/**
	 * Method for copy stream from web
	 */
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
    
    /**
     * Method for copying file
     */
    public static void copyFile(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
}
