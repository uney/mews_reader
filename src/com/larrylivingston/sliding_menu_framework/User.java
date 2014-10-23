package com.larrylivingston.sliding_menu_framework;

import com.larrylivingston.loaction_module.GooglePlayLocationManager;

import android.content.Context;

public class User {
	// application context
	private Context context;
	private static User user;
	/**
	 * handle users local data, including:
	 * name
	 * status
	 * profile picture
	 * date of register
	 * 
	 * And the logged in user's data as well;
	 * Data will be saved as file instead if sqlite database. Local keeps only one copy and it will be destroyed upon logout
	 */
	
	private User (Context context){
		this.context = context;
	}
	
	public static User getUser(Context context) {
		if (user == null) {
			synchronized (User.class) {
				if (user == null) {
					user = new User(context);
				}
			}
		}
		return user;
	}
	
	public synchronized void updateUserInfo(){
		/*
		 * Check if user logged in
		 * Access server to get the user information from database
		 * update the sharedpreference
		 */
	}
	public void destroyUsersData(){
		 // update the sharedpreference
	}
	
}
