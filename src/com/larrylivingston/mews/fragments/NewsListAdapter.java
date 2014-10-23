package com.larrylivingston.mews.fragments;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.larrylivingston.mews.MewsConstants;
import com.larrylivingston.mews.R;
import com.larrylivingston.mews.loader.ImageLoader;
import com.larrylivingston.mews.objects.*;
import com.larrylivingston.utilities_module.Utilities;

public class NewsListAdapter extends BaseAdapter {
	private ImageLoader mImageLoader;
	private Context mContext;
	private MewsList list = null;
	private boolean mBusy = false;
	private int THUMB_NAIL_SIZE = 70;
	private int[] screenSize;

	public NewsListAdapter(Context context) {
		this.mContext = context;
		mImageLoader = ImageLoader.getImageLoader(context);
		screenSize = Utilities.getHeightWidth((Activity)context);
		THUMB_NAIL_SIZE = screenSize[0]/7;
	}

	// public void setData(List<Map<String, Object>> list) {
	// this.list = list;
	// }

	public ImageLoader getImageLoader() {
		return mImageLoader;
	}

	public void refreshData(MewsList list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (list == null) {
			return 0;
		}
		return list.getItemCount();
	}

	@Override
	public Object getItem(int position) {
		return list.getItem(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.news_item_layout, null);

			viewHolder = new ViewHolder();
			viewHolder.title_textview = (TextView) convertView.findViewById(R.id.title_textview);
			viewHolder.content_textview = (TextView) convertView.findViewById(R.id.content_textview);
			viewHolder.date_textview = (TextView) convertView.findViewById(R.id.date_textview);
			viewHolder.location_textview = (TextView) convertView.findViewById(R.id.location_textview);
			viewHolder.up_textview = (TextView) convertView.findViewById(R.id.up_textview);
			viewHolder.down_textview = (TextView) convertView.findViewById(R.id.down_textview);
			viewHolder.view_textview = (TextView) convertView.findViewById(R.id.view_textview);
			viewHolder.thumb_imageview = (ImageView) convertView.findViewById(R.id.thumb_imageview);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.title_textview.setText(list.getItem(position).getTitle());
		viewHolder.content_textview.setText(list.getItem(position).getContent());
		viewHolder.date_textview.setText(list.getItem(position).getDate().toString());
		viewHolder.location_textview.setText(list.getItem(position).getAddress());
		viewHolder.up_textview.setText(String.valueOf(list.getItem(position).getUpvote()));
		viewHolder.down_textview.setText(String.valueOf(list.getItem(position).getDownvote()));
		viewHolder.view_textview.setText(String.valueOf(list.getItem(position).getViewCount()));
		
		if (list.getItem(position).getThumbnail().trim() != null
				&& list.getItem(position).getThumbnail().length() > 0) {
			viewHolder.thumb_imageview.setVisibility(View.VISIBLE);
			mImageLoader.DisplayImage(list.getItem(position).getThumbnail(), viewHolder.thumb_imageview,
					THUMB_NAIL_SIZE, false);
		} else {
			viewHolder.thumb_imageview.setVisibility(View.GONE);
		}


		return convertView;
	}

	int lastX, curX;
	private int totalMove = 0;
	private boolean firstDown = true;// ����
	int duration = 150;

	static class ViewHolder {
		TextView title_textview;
		TextView content_textview;
		TextView date_textview;
		TextView location_textview;
		TextView up_textview;
		TextView down_textview;
		TextView view_textview;
		ImageView thumb_imageview;
	}

}
