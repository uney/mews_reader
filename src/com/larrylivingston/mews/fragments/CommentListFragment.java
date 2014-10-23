package com.larrylivingston.mews.fragments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.larrylivingston.internet.FastJsonTools;
import com.larrylivingston.loaction_module.GooglePlayLocationManager;

import com.larrylivingston.mews.MewsConstants;
import com.larrylivingston.mews.R;
import com.larrylivingston.mews.objects.Mews;
import com.larrylivingston.mews.objects.MewsList;
import com.larrylivingston.ui.SwipeDismissListViewTouchListener;
import com.larrylivingston.ui.XListView;
import com.larrylivingston.ui.SwipeDismissListViewTouchListener.DismissCallbacks;
import com.larrylivingston.ui.XListView.IXListViewListener;
import com.larrylivingston.utilities_module.DbAdapter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CommentListFragment extends SherlockFragment implements
		IXListViewListener {
	public static NewsListAdapter newsListAdapter;
	public final String TAG = "NewsList";
	private XListView mListView;
	private ArrayList<String> items = new ArrayList<String>();
	private Handler mHandler;
	private int start = 0;
	private static int refreshCnt = 0;
	MewsList mewsList;
	String category;
	String mode;
	int page;

	private boolean _isRefreshing = true;
	private boolean _hasMoreData = false;
	private ProgressDialog dialog;
	ImageButton title_right_btn;
	// Handler, manager and adpter go here
	GooglePlayLocationManager googlePlayLocationManager;
	DbAdapter dbAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		title_right_btn = (ImageButton) getActivity().findViewById(
				R.id.title_right_btn);
		title_right_btn.setBackgroundResource(R.drawable.facebook);
		title_right_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d(TAG, TAG + "onClickListener overrided");
				Toast.makeText(getActivity().getApplicationContext(), "Test",
						Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.news_list_fragment_layout, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init();
		mewsList = new MewsList();
		try {
			category = getArguments().getString("category");
		} catch (NullPointerException e) {
			category = "error";
		}

		mListView = (XListView) getView().findViewById(R.id.news_listview);// for
																			// sherlockListFragment
		mListView.setPullLoadEnable(true);
		mListView.setAdapter(newsListAdapter);
		mListView.setXListViewListener(this);

		SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(
				mListView, new DismissCallbacks() {
					@Override
					public boolean canDismiss(int position) {
						return true;
					}

					@Override
					public void onDismiss(ListView listView,
							int[] reverseSortedPositions) {
						for (int position : reverseSortedPositions) {
							int realPosition = position - 1;
							saveArticate((Mews) newsListAdapter
									.getItem(realPosition), realPosition);
						}
						newsListAdapter.notifyDataSetChanged();
					}
				});
		mListView.setOnTouchListener(touchListener);
		// Setting this scroll listener is required to ensure that during
		// ListView scrolling,
		// we don't look for swipes.
		mListView.setOnScrollListener(touchListener.makeScrollListener());

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// actions to be performed when a list item clicked
				int pos = arg2;
				if (category.equalsIgnoreCase(MewsConstants.CATEGORY_LATER)) {
					dbAdapter.open();
					dbAdapter.afterRead(String.valueOf(mewsList
							.getItem(pos - 1).getId()));
					dbAdapter.close();
					// MewsConstants.updateUnread(dbAdapter, unread_textview);
				}
				// clickListFlurry(category, feed.getItem(pos - 1).getTitle());
				Bundle bundle = new Bundle();
				bundle.putSerializable("mews_list", mewsList);
				bundle.putInt("pos", pos - 1);
				bundle.putString("category", category);
				Intent intent = new Intent(getActivity(), DetailActivity.class);
				intent.putExtras(bundle);
				// TODO pass source as argument
				// intent.putExtra("source", source);
				// intent.putExtra("pos", pos - 1);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);

			}
		});

		mHandler = new Handler();
	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		Date currentLocalTime = cal.getTime();
		DateFormat date = new SimpleDateFormat("EEE, dd-MM-yyy HH:mm:ss",
				Locale.getDefault());
		date.setTimeZone(TimeZone.getTimeZone("GMT"));
		String localTime = date.format(currentLocalTime);
		mListView.setRefreshTime(localTime);
		Log.i(TAG, TAG + " :mewsList length" + mewsList.getItemCount());
	}

	@Override
	public void onRefresh() {
		new RefreshTask().execute(MewsConstants.NEWS_URL, "true", mode,
				category, "keyword");
	}

	@Override
	public void onLoadMore() {
		new LoadMore().execute(MewsConstants.NEWS_URL, "true", mode, category,
				"keyword");
	}

	// ===========================RefreshTask===============================================================================================
	public class RefreshTask extends AsyncTask<String, Integer, MewsList> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(MewsList result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			newsListAdapter.refreshData(result);
			if (_isRefreshing == true) {
				onLoad();
				_isRefreshing = false;
			} else {
			}
			onLoad();
		}

		@Override
		protected MewsList doInBackground(String... params) {
			/*
			 * params[0] = URL params[1] = is it searching params[2] = mode
			 * params[3] = category params[4] = keywords
			 */
			try {
				String _isSearching = params[1];
				String mode = params[2];
				String category = params[3];
				List<BasicNameValuePair> webparams = new ArrayList<BasicNameValuePair>();
				double[] latLng = googlePlayLocationManager.getLocation();
				webparams.add(new BasicNameValuePair(
						MewsConstants.SERVER_PARAM_LAT, Double
								.toString(latLng[0])));
				webparams.add(new BasicNameValuePair(
						MewsConstants.SERVER_PARAM_LNG, Double
								.toString(latLng[1])));
				webparams.add(new BasicNameValuePair(
						MewsConstants.SERVER_PARAM_CATEGORY, category));
				webparams.add(new BasicNameValuePair(
						MewsConstants.SERVER_PARAM_MODE, mode));
				if (_isSearching.equalsIgnoreCase("true")) {
					try {
						webparams.add(new BasicNameValuePair(
								MewsConstants.SERVER_PARAM_KEYWORD, params[3]));
					} catch (NullPointerException e) {
						Log.i(TAG, TAG + " Null Pointer: " + e);
					}
				}
				String param = URLEncodedUtils.format(webparams, "UTF-8");
				String requestURL = params[0] + "?" + param;
				Log.i(TAG, TAG + "url: " + requestURL);

				// HttpClient httpClient = new DefaultHttpClient();
				// HttpPost httpPost = new HttpPost(requestURL);
				// HttpResponse httpResponse = httpClient.execute(httpPost);
				// if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// String jsonString = EntityUtils.toString(
				// httpResponse.getEntity(), "utf-8");
				// MewsList tempList = new MewsList();
				// tempList = MewsConstants.convertFastJSON(FastJsonTools
				// .listKeyMaps(MewsConstants.TESTING_JSON));
				// mewsList.addList(tempList);
				//
				// }else{
				// //for http error handling
				//
				// }
			} catch (Exception e) {
				e.printStackTrace();
			}
			MewsList tempList = new MewsList();
			tempList = MewsConstants.convertFastJSON(FastJsonTools
					.listKeyMaps(MewsConstants.TESTING_JSON));
			mewsList = tempList;
			return mewsList;
		}

	}

	// ===========================LoadMore===============================================================================================
	public class LoadMore extends AsyncTask<String, Integer, MewsList> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(MewsList result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			newsListAdapter.refreshData(result);
			if (_isRefreshing == true) {
				onLoad();
				_isRefreshing = false;
			} else {
			}
			onLoad();
		}

		@Override
		protected MewsList doInBackground(String... params) {
			/*
			 * params[0] = URL params[1] = is it searching params[2] = mode
			 * params[3] = category params[4] = keywords
			 */
			try {
				String _isSearching = params[1];
				String mode = params[2];
				String category = params[3];
				List<BasicNameValuePair> webparams = new ArrayList<BasicNameValuePair>();
				double[] latLng = googlePlayLocationManager.getLocation();
				webparams.add(new BasicNameValuePair(
						MewsConstants.SERVER_PARAM_LAT, Double
								.toString(latLng[0])));
				webparams.add(new BasicNameValuePair(
						MewsConstants.SERVER_PARAM_LNG, Double
								.toString(latLng[1])));
				webparams.add(new BasicNameValuePair(
						MewsConstants.SERVER_PARAM_CATEGORY, category));
				webparams.add(new BasicNameValuePair(
						MewsConstants.SERVER_PARAM_MODE, mode));
				if (_isSearching.equalsIgnoreCase("true")) {
					try {
						webparams.add(new BasicNameValuePair(
								MewsConstants.SERVER_PARAM_KEYWORD, params[3]));
					} catch (NullPointerException e) {
						Log.i(TAG, TAG + " Null Pointer: " + e);
					}
				}
				String param = URLEncodedUtils.format(webparams, "UTF-8");
				String requestURL = params[0] + "?" + param;
				Log.i(TAG, TAG + "url: " + requestURL);

				// HttpClient httpClient = new DefaultHttpClient();
				// HttpPost httpPost = new HttpPost(requestURL);
				// HttpResponse httpResponse = httpClient.execute(httpPost);
				// if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// String jsonString = EntityUtils.toString(
				// httpResponse.getEntity(), "utf-8");
				// MewsList tempList = new MewsList();
				// tempList = MewsConstants.convertFastJSON(FastJsonTools
				// .listKeyMaps(jsonString));
				// mewsList.addList(tempList);
				//
				// }else{
				// //for http error handling
				//
				// }
			} catch (Exception e) {
				e.printStackTrace();
			}
			MewsList tempList = new MewsList();
			tempList = MewsConstants.convertFastJSON(FastJsonTools
					.listKeyMaps(MewsConstants.TESTING_JSON2));
			mewsList.addList(tempList);
			return mewsList;
		}

	}

	private void init() {
		newsListAdapter = new NewsListAdapter(getActivity());
		dbAdapter = new DbAdapter(getActivity());
		mewsList = new MewsList();
		_isRefreshing = false;
		_hasMoreData = true;
		page = 1;
		mode = "loaction";
		googlePlayLocationManager = GooglePlayLocationManager
				.getGooglePlayLocationManager(getActivity()
						.getApplicationContext());
		dialog = new ProgressDialog(getActivity());
		dialog.setMessage(getResources().getString(R.string.loading_text));
		dialog.setCancelable(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
		inflater.inflate(R.menu.news_list_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		String menuTitle = item.getTitle().toString();
		switch (item.getItemId()) {
		case R.id.news_list_by_distance:
			mode = "";
			Toast.makeText(getActivity(), menuTitle, Toast.LENGTH_SHORT).show();
			break;
		case R.id.news_list_by_date:
			mode = "";
			Toast.makeText(getActivity(), menuTitle, Toast.LENGTH_SHORT).show();
			break;
		case R.id.news_list_by_hotness:
			mode = "";
			Toast.makeText(getActivity(), menuTitle, Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}

		return true;
	}

	/*
	 * Save article here
	 */
	public void saveArticate(Mews mews, int realPosition) {

	}

}