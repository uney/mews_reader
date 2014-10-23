package com.larrylivingston.sliding_menu_framework;

import java.util.List;
import java.util.Map;

import com.larrylivingston.mews.R;
import com.larrylivingston.utilities_module.Utilities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MyAlertDialog {
	public static ProgressDialog p_dialog;
	public static SharedPreferences sharedPreferences;

	public static void loginDialog(final View v) {
		Context context = v.getContext();
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getResources().getString(R.string.login_string1));
		builder.setMessage(context.getResources().getString(R.string.login_string2));
		builder.setIcon(R.drawable.ic_launcher);
		builder.setPositiveButton(context.getResources().getString(R.string.okay), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Fragment newContent = new LoginFragment();
				MainActivity mainActivity = (MainActivity) v.getContext();
				mainActivity.switchContent(newContent);
			}
		});
		builder.setNegativeButton(context.getResources().getString(R.string.cancel), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Do nothing if user pressed cancel
			}
		});
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}

	public static void logoutDialog(final View v) {
		final Context context = v.getContext();
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getResources().getString(R.string.logout_string1));
		builder.setMessage(context.getResources().getString(R.string.logout_string2));
		builder.setIcon(R.drawable.ic_launcher);
		builder.setPositiveButton(context.getResources().getString(R.string.okay), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Utilities.updateFacebookToken(sharedPreferences, "", false, "", 0);
				Utilities.updateWeiboToken(sharedPreferences, "", false, "", 0);
				Toast.makeText(v.getContext(), context.getResources().getString(R.string.logout_success_string), Toast.LENGTH_SHORT)
						.show();
				User user = User.getUser(context);
				user.destroyUsersData();
				Fragment newContent = new LoginFragment();
				MainActivity mainActivity = (MainActivity) v.getContext();
				mainActivity.switchContent(newContent);

			}
		});
		builder.setNegativeButton(context.getResources().getString(R.string.cancel), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});
		AlertDialog alertDialog = builder.create();
		alertDialog.show();

	}


	public static void DlgGetPic(final View v) {

	}
}
