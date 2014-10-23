package com.larrylivingston.mews.fragments;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;

import com.flurry.android.FlurryAgent;
import com.larrylivingston.loaction_module.GooglePlayLocationManager;
import com.larrylivingston.mews.MewsConstants;
import com.larrylivingston.mews.R;
import com.larrylivingston.mews.loader.ImageLoader;
import com.larrylivingston.mews.objects.MewsList;
import com.larrylivingston.ui.XListView;
import com.larrylivingston.utilities_module.Utilities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class DetailFragment extends Fragment {
	private int fPos;
	MewsList mewsList;
	private String link;
	private static int width;
	private static int height;
	private final String TAG = "deatil_fragment";

	private XListView mListView;
	private int[] heightWidth = new int[2];
	GooglePlayLocationManager googlePlayLocationManager;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			mewsList = (MewsList) getArguments().getSerializable("mews_list");
			fPos = getArguments().getInt("pos");
			link = mewsList.getItem(fPos).getLink();
		} catch (NullPointerException e) {
			// TODO
			Log.i(TAG, TAG + "Null POinter Exception: " + e);
		}
		googlePlayLocationManager = GooglePlayLocationManager
				.getGooglePlayLocationManager(this.getActivity()
						.getApplicationContext());

		heightWidth = Utilities.getHeightWidth(this.getActivity());

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Initialize views
		View view = inflater
				.inflate(R.layout.news_detail_fragment_layout, null);

		TextView newsdetail_title_textview = (TextView) view
				.findViewById(R.id.newsdetail_title_textview);

		// the first block
		TextView newsdetail_up_textview = (TextView) view
				.findViewById(R.id.newsdetail_up_textview);
		TextView newsdetail_down_textview = (TextView) view
				.findViewById(R.id.newsdetail_down_textview);
		TextView newsdetail_view_textview = (TextView) view
				.findViewById(R.id.newsdetail_view_textview);

		// the second block
		TextView newsdetail_distance_textview = (TextView) view
				.findViewById(R.id.newsdetail_distance_textview);
		TextView newsdetail_address_textview = (TextView) view
				.findViewById(R.id.newsdetail_address_textview);

		// the third block
		ImageView newsdetail_author_imageview = (ImageView) view
				.findViewById(R.id.newsdetail_author_imageview);
		TextView newsdetail_author_textview = (TextView) view
				.findViewById(R.id.newsdetail_author_textview);
		TextView newsdetail_date_textview = (TextView) view
				.findViewById(R.id.newsdetail_date_textview);

		WebView newsdetail_preview_webview = (WebView) view
				.findViewById(R.id.newsdetail_preview_webview);
		TextView newsdeatil_content_textview = (TextView) view
				.findViewById(R.id.newsdetail_content_textview);

		newsdetail_preview_webview.setVerticalScrollBarEnabled(false);
		newsdetail_preview_webview.setFocusable(false);
		// To disabled the horizontal and vertical scrolling:
		newsdetail_preview_webview
				.setOnTouchListener(new View.OnTouchListener() {
					public boolean onTouch(View v, MotionEvent event) {
						return (event.getAction() == MotionEvent.ACTION_MOVE);
					}
				});
		// Enable the vertical fading edge (by default it is disabled)

		// Set webview properties
		// WebSettings ws = newsdetail_content_webview.getSettings();
		// newsdetail_content_webview.setHorizontalScrollBarEnabled(false);
		// newsdetail_content_webview.setVerticalScrollBarEnabled(false);
		// newsdetail_content_webview.setFocusable(false);
		// ws.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		// ws.setLightTouchEnabled(false);
		// ws.setPluginState(PluginState.ON);

		WebSettings ws2 = newsdetail_preview_webview.getSettings();
		ws2.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		ws2.setLightTouchEnabled(false);
		ws2.setPluginState(PluginState.ON);
		// new getNewsFromYahoo().execute(desc);
		// Set the views
		newsdetail_title_textview.setText(mewsList.getItem(fPos).getTitle());
		newsdetail_preview_webview.loadDataWithBaseURL(
				"http://www.androidcentral.com/", mewsList.getItem(fPos)
						.getContent().substring(0, 30), "text/html", "UTF-8",
				null);
		/**
		 * start handling the Large image here
		 */
		// set width here
		width = heightWidth[1] / 8 * 7;
		int authorWidth = heightWidth[1] / 9;
		ImageView newsdetail_large_imageview = (ImageView) view
				.findViewById(R.id.newsdetail_large_imageview);
		ImageView newsdetail_large_imageview2 = (ImageView) view
				.findViewById(R.id.newsdetail_large_imageview2);
		ImageView newsdetail_large_imageview3 = (ImageView) view
				.findViewById(R.id.newsdetail_large_imageview3);
		if (mewsList.getItem(fPos).getPic1() != null && mewsList.getItem(fPos).getPic1() != "") {
			ImageLoader imageLoader = ImageLoader.getImageLoader(getActivity());
			imageLoader.DisplayImage(mewsList.getItem(fPos).getPic1(),
					newsdetail_large_imageview, width, true);
		} else {
			newsdetail_large_imageview.setVisibility(View.GONE);
		}
		if (mewsList.getItem(fPos).getPic2() != null && mewsList.getItem(fPos).getPic2() != "") {
			ImageLoader imageLoader = ImageLoader.getImageLoader(getActivity());
			imageLoader.DisplayImage(mewsList.getItem(fPos).getPic2(),
					newsdetail_large_imageview2, width, true);
			Log.i(TAG, TAG+"pic_2 is not null"+mewsList.getItem(fPos).getPic2());

		}else{
			newsdetail_large_imageview2.setVisibility(View.GONE);
		}
		if (mewsList.getItem(fPos).getPic3() != null && mewsList.getItem(fPos).getPic3() != "") {
			Log.i(TAG, TAG+"pic_3 is not null"+mewsList.getItem(fPos).getPic3());
			ImageLoader imageLoader = ImageLoader.getImageLoader(getActivity());
			imageLoader.DisplayImage(mewsList.getItem(fPos).getPic3(),
					newsdetail_large_imageview3, width, true);
		}else{
			newsdetail_large_imageview3.setVisibility(View.GONE);
		}

		ImageLoader imageLoader = ImageLoader.getImageLoader(getActivity());
		imageLoader.DisplayImage(MewsConstants.USER_IMAGE_URL
				+ mewsList.getItem(fPos).getAuthorId(),
				newsdetail_author_imageview, authorWidth, true);

		// the first block
		newsdetail_up_textview.setText(String.valueOf(mewsList.getItem(fPos)
				.getUpvote()));
		newsdetail_down_textview.setText(String.valueOf(mewsList.getItem(fPos)
				.getDownvote()));
		newsdetail_view_textview.setText(String.valueOf(mewsList.getItem(fPos)
				.getViewCount()));

		// the second block
		double[] latLng = googlePlayLocationManager.getLocation();
		double lat = 0, lng = 0;
		if (latLng.length > 1) {
			lat = latLng[0];
			lng = latLng[1];
		}
		double distance = GooglePlayLocationManager.getDistatce(lat, lng,
				mewsList.getItem(fPos).getLatitude(), mewsList.getItem(fPos)
						.getLongitude());
		DecimalFormat df = new DecimalFormat("#.###");
		newsdetail_distance_textview.setText(df.format(distance) + " km");
		newsdetail_address_textview.setText(mewsList.getItem(fPos).getAuthor());

		// the third block
		newsdetail_date_textview.setText(mewsList.getItem(fPos).getDate()
				.toString());
		newsdetail_author_textview.setText(mewsList.getItem(fPos).getAuthor());
		/**
		 * 
		 * Get pending View with animation here
		 * 
		 */
		// if (source.equalsIgnoreCase(MewsConstants.EDITORIAL)) {
		// desc.loadDataWithBaseURL("http://www.androidcentral.com/", fFeed
		// .getItem(fPos).getContent(), "text/html", "UTF-8", null);
		// } else if (source.equalsIgnoreCase(MewsConstants.FIVEOOPX)) {
		// desc.setVisibility(View.GONE);
		// preview.setVisibility(View.GONE);
		// } else {
		// PendingView pendingView = new PendingView(getActivity());
		// myPendingView = pendingView.getPendingView();
		// RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
		// RelativeLayout.LayoutParams.WRAP_CONTENT,
		// RelativeLayout.LayoutParams.WRAP_CONTENT);
		//
		// p.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		// p.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		// myPendingView.setLayoutParams(p);
		// ((ViewGroup) view).addView(myPendingView);
		//
		// // new GetNewsFromYahoo().execute(desc, myPendingView);
		// NewsLoader myNewsLoader = NewsLoader.getNewsLoader(getActivity());
		// myNewsLoader.DisplayContent(fFeed.getItem(fPos).getLink(), desc,
		// myPendingView, source);
		// // myNewsLoader.DisplayContent(fFeed, fPos, desc, myPendingView);
		// }
		newsdeatil_content_textview
				.setText(mewsList.getItem(fPos).getContent());
		mListView = (XListView) view
				.findViewById(R.id.newsdetail_comment_listview);
		Log.i(TAG, TAG + " views initialized!");
		return view;
	}
	
	static class ViewHolder {
		TextView newsdetail_title_textview;

		// the first block
		TextView newsdetail_up_textview;
		TextView newsdetail_down_textview;
		TextView newsdetail_view_textview;

		// the second block
		TextView newsdetail_distance_textview;
		TextView newsdetail_address_textview;

		// the third block
		ImageView newsdetail_author_imageview;
		TextView newsdetail_author_textview;
		TextView newsdetail_date_textview;

		WebView newsdetail_preview_webview;
		TextView newsdeatil_content_textview;
	}


}