package com.larrylivingston.mews.fragments;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.larrylivingston.cache.FileCache;
import com.larrylivingston.mews.LoginUtils;
import com.larrylivingston.mews.MewsConstants;
import com.larrylivingston.mews.R;
import com.larrylivingston.mews.objects.Mews;
import com.larrylivingston.mews.objects.MewsList;
import com.larrylivingston.sliding_menu_framework.User;
import com.larrylivingston.ui.MarqueeTextView;
import com.larrylivingston.utilities_module.DbAdapter;
import com.actionbarsherlock.view.Menu;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookException;
import com.facebook.Session;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.flurry.android.FlurryAgent;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//TODO
/**
 * FragmentActivity changed to SherlockFragmentActivity
 * 
 * @author uney
 * 
 */
public class DetailActivity extends SherlockFragmentActivity {
	private static int current_page;
	/*
	 * UI elements go here: TextView, ViewPager and buttons
	 */
	private MarqueeTextView detail_title_marqueetextview;
	private ImageView detail_title_imageview;
	private TextView detail_title_unread_textview;
	private final String TAG = "detail_activity";
	private ViewPager news_viewpager;

	MewsList mewsList;
	int pos;

	private String link;
	private String category;
	static FileCache fileCache;
	private DbAdapter myDbAdapter;
	private PageAdapter pageAdapter;
	private String USER_ID;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myDbAdapter = new DbAdapter(this);
		ActionBar Bar = getSupportActionBar();
		Bar.setDisplayShowTitleEnabled(false);
		Bar.setDisplayShowHomeEnabled(false);
		LayoutInflater inflater = LayoutInflater.from(this);
		View customView = inflater.inflate(R.layout.detail_title_layout, null);
		detail_title_marqueetextview = (MarqueeTextView) customView
				.findViewById(R.id.detail_title_marqueetextview);
		detail_title_marqueetextview.setSelected(true);
		Bar.setCustomView(customView);
		Bar.setDisplayShowCustomEnabled(true);
		detail_title_marqueetextview.setText("22");
		detail_title_marqueetextview.setEnabled(true);
		detail_title_unread_textview = (TextView) customView
				.findViewById(R.id.detail_title_unread_textview);
		detail_title_unread_textview.setVisibility(View.GONE);
		detail_title_imageview = (ImageView) customView
				.findViewById(R.id.detail_title_imageview);

		detail_title_imageview.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onBackPressed();
			}
		});
		MewsConstants.updateUnread(myDbAdapter, detail_title_unread_textview);
		setContentView(R.layout.news_detail_activity_layout);

		try {
			// Get the MewsList object and the position from the Intent
			mewsList = (MewsList) getIntent().getExtras().get("mews_list");
			pos = getIntent().getExtras().getInt("pos");
			category = getIntent().getExtras().getString("category");
			current_page = pos;
			link = mewsList.getItem(pos).getLink();
			Log.i(TAG, TAG + "source: " + link);
		} catch (NullPointerException e) {
			Log.i(TAG, TAG + ", Null pointer: " + e);
		}
		fileCache = new FileCache(this, MewsConstants.APP_NAME,
				MewsConstants.IMAGE_FILE);

		// Initialize the views
		pageAdapter = new PageAdapter(getSupportFragmentManager());
		news_viewpager = (ViewPager) findViewById(R.id.news_viewpager);

		// Set Adapter to pager:
		news_viewpager.setAdapter(pageAdapter);
		news_viewpager.setCurrentItem(pos);
		news_viewpager.setOnPageChangeListener(new OnPageChangeListener() {
			public void onPageScrollStateChanged(int state) {
			}

			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			}

			public void onPageSelected(int position) {
				// Check if this is the page you want.
				//TODO remove it from the UI thread
				if (category.equalsIgnoreCase(MewsConstants.CATEGORY_LATER)) {
					myDbAdapter.open();
					myDbAdapter.afterRead(Integer.toString(mewsList.getItem(
							position).getId()));
					myDbAdapter.close();
					MewsConstants.updateUnread(myDbAdapter,
							detail_title_unread_textview);
				}
			}
		});
	}

	public class PageAdapter extends FragmentStatePagerAdapter {
		public PageAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return mewsList.getItemCount();
		}

		@Override
		public Fragment getItem(int position) {
			DetailFragment detailFragment = new DetailFragment();
			current_page = position;
			Bundle bundle = new Bundle();
			bundle.putSerializable("mews_list", mewsList);
			bundle.putInt("pos", position);
			bundle.putString("link", link);
			detailFragment.setArguments(bundle);
			return detailFragment;
		}

	}

	/**
	 * For Actionbarsherlock only
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate your menu.
		getSupportMenuInflater().inflate(R.menu.dummy_actionbar_menu, menu);

		// Set file with share history to the provider and set the share intent.
		menu.add("Save").setIcon(R.drawable.favo_menu)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		// MenuItem actionItem =
		// menu.findItem(R.id.menu_item_share_action_provider_action_bar);
		// ShareActionProvider actionProvider = (ShareActionProvider)
		// actionItem.getActionProvider();
		// actionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
		// Note that you can set/change the intent any time,
		// say when the user has selected an image.

		SubMenu subMenu1 = menu.addSubMenu(1, 1, 1, "Action Item");
		subMenu1.add(1, 2, 1, "facebook").setIcon(R.drawable.facebook);
		subMenu1.add(1, 3, 1, "other").setIcon(R.drawable.others);

		MenuItem subMenu1Item = subMenu1.getItem();
		subMenu1Item.setIcon(R.drawable.ic_title_share);
		subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		// SubMenu subMenu2 = menu.addSubMenu("Overflow Item");
		// subMenu2.add(1, 5, 1, "Clear Cache");
		// subMenu2.add(1, 6, 1, "Logout");
		// subMenu2.add(1, 7, 1, "Setting");
		// MenuItem subMenu2Item = subMenu2.getItem();
		// subMenu2Item.setIcon(R.drawable.ic_compose);

		return true;
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// MenuInflater inflater = getMenuInflater();
	// inflater.inflate(R.menu.main_menu, menu);
	// return true;
	// }
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		String menuTitle = item.getTitle().toString();
		switch (item.getItemId()) {
		case 0:
//			saveArticate(mewsList.getItem(news_viewpager.getCurrentItem()));
			break;
		case 1:
			Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
			break;
		case 2:
			shareToFacebook();
			break;
		case 3:
			shareToOthers();
			break;
		default:
			break;
		}

		return true;
	}

	private Intent createShareIntent() {

		String title = mewsList.getItem(news_viewpager.getCurrentItem())
				.getTitle();
		String link = mewsList.getItem(news_viewpager.getCurrentItem())
				.getLink();
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		// shareIntent.setType("image/*");
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
		shareIntent.putExtra(Intent.EXTRA_TEXT, link);
		// startActivity(Intent.createChooser(shareIntent, "Share URL"));
		// Uri uri = Uri.fromFile(getFileStreamPath("shared.png"));
		// shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
		return shareIntent;
	}

	private void clearCache() {
		fileCache.clear();
	}

	@Override
	public void onAttachFragment(Fragment fragment) {
		// TODO Auto-generated method stub
		super.onAttachFragment(fragment);
	}

	/**
	 * Facebook
	 */
	private void shareToFacebook() {
		final SharedPreferences pref = getSharedPreferences(MewsConstants.PREFERENCE_NAME_LOGIN,
				MODE_PRIVATE);
		boolean logged_in = pref.getBoolean(MewsConstants.PREFERENCE_LOGGED_IN, false);
		String accessToken = pref.getString(
				MewsConstants.PREFERENCE_ACCESS_TOKEN, null);
		long expiresIn = pref.getLong(MewsConstants.PREFERENCE_LOGIN_EXPIRE_TIME, 0);
		if (LoginUtils.isSessionValid(accessToken, expiresIn)) {
			Bundle params = new Bundle();
			params.putString("name",
					mewsList.getItem(news_viewpager.getCurrentItem())
							.getTitle());
			params.putString("caption", "News From Mews!");
			params.putString("description",
					mewsList.getItem(news_viewpager.getCurrentItem())
							.getContent().substring(0, 50));
			params.putString("link",
					mewsList.getItem(news_viewpager.getCurrentItem()).getLink());

			WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(this,
					Session.getActiveSession(), params)).setOnCompleteListener(
					new OnCompleteListener() {

						@Override
						public void onComplete(Bundle values,
								FacebookException error) {
							if (error == null) {
								// When the story is posted, echo the success
								// and the post Id.
								final String postId = values
										.getString("post_id");
								if (postId != null) {
									// Toast.makeText(DetailActivity.this,
									// "Posted story, id: "+postId,
									// Toast.LENGTH_SHORT).show();
									shareDetailFlurry(
											category,
											mewsList.getItem(
													news_viewpager
															.getCurrentItem())
													.getTitle(), "facebook",
											postId);
								} else {
									// User clicked the Cancel button

								}
							} else if (error instanceof FacebookOperationCanceledException) {
								// User clicked the "x" button
							} else {
								// Generic, ex: network error
								runOnUiThread(new Runnable() {
									public void run() {
										Toast.makeText(DetailActivity.this,
												"Error", Toast.LENGTH_SHORT)
												.show();
									}
								});
							}
						}

					}).build();
			feedDialog.show();
		} else {
			Toast.makeText(this, this.getString(R.string.login_toast), Toast.LENGTH_SHORT).show();
			Bundle bundle = new Bundle();
			bundle.putInt("pos", news_viewpager.getCurrentItem());
			Intent intent = new Intent(this, Login.class);
			startActivity(intent);
		}

	}

	/**
	 * Other share
	 */
	public void shareToOthers() {
		final Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT,
				mewsList.getItem(news_viewpager.getCurrentItem()).getTitle());
		intent.putExtra(Intent.EXTRA_TEXT,
				mewsList.getItem(news_viewpager.getCurrentItem()).getLink());
		shareDetailFlurry(category,
				mewsList.getItem(news_viewpager.getCurrentItem()).getTitle(),
				"others", "others");
		startActivity(Intent.createChooser(intent, this.getResources().getText(R.string.share_string)));
	}

//	private void saveArticate(Mews itemToSave) {
//		DbAdapter myDbAdapter = new DbAdapter(this);
//		String user_id = itemToSave.getAuthor();
//
//		String news_date = itemToSave.getDate().toString();
//		Log.i(TAG, TAG+", date news_date1: " + news_date);
//		String title = itemToSave.getTitle();
//		Log.i(TAG, TAG+", date title: " + title);
//		// Toast.makeText(this, "title = " + title, Toast.LENGTH_SHORT).show();
//		myDbAdapter.open();
//
//		Cursor cursor = myDbAdapter.getSavedNewsTitle(title);
//		if (cursor.moveToFirst()) {
//			Toast.makeText(this, "added before!!", Toast.LENGTH_SHORT).show();
//		} else {
//			String content = itemToSave.getDescription();
//			String share_link = itemToSave.getLink();
//			String image = itemToSave.getImage();
//			Log.i("Testing ", "image: " + image);
//			Log.i("Testing ", "title: " + title);
//			Toast.makeText(this, title + " saved!", Toast.LENGTH_SHORT).show();
//
//			Date date = new Date();
//			SimpleDateFormat dateFormat = new SimpleDateFormat(
//					"yyyy-MM-dd HH:mm:ss");
//			String added_date = dateFormat.format(date);
//			NewsLoader myNewsLoader = NewsLoader.getNewsLoader(this);
//			if (!itemToSave.getSource()
//					.equalsIgnoreCase(MewsConstants.ENGADGET)) {
//				ImageLoader imageLoader = ImageLoader.getImageLoader(this);
//				if (image.length() > 10) {
//					String[] temp = image.split("http://");
//					Log.i("Debug", "image link:" + temp[1]);
//					imageLoader.downLoadPhoto(temp[1]);
//				}
//			}
//			myNewsLoader.downloadContent(share_link, source);
//			myDbAdapter.insertLaterNews(user_id, news_date,
//					MewsConstants.READLATER, title, content, share_link, image,
//					added_date, MewsConstants.DB_KEY);
//		}
//
//		myDbAdapter.close();
//
//	}

	@Override
	public void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, MewsConstants.FLURRY_API_KEY);
		FlurryAgent.setUserId(USER_ID);
	}

	@Override
	protected void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

	public void shareDetailFlurry(String cat, String title, String platform,
			String post_id) {
		// Log an event with params, where param1=value1 and param2=value2
		// Map<String, String>
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("main_cat", cat);
		params.put("title", title);
		params.put("platform", platform);
		params.put("post_id", platform);
		FlurryAgent.logEvent(MewsConstants.FLURRY_EVENT_DETAIL, params);
	}

}
