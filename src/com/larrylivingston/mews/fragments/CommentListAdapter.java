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

public class CommentListAdapter extends BaseAdapter {
	private ImageLoader mImageLoader;
	private Context mContext;
	private CommentList list = null;
	private boolean mBusy = false;
	private int THUMB_NAIL_SIZE = 30;
	private int[] screenSize;

	public CommentListAdapter(Context context) {
		this.mContext = context;
		mImageLoader = ImageLoader.getImageLoader(context);
		screenSize = Utilities.getHeightWidth((Activity)context);
		THUMB_NAIL_SIZE = screenSize[1]/9;
	}

	// public void setData(List<Map<String, Object>> list) {
	// this.list = list;
	// }

	public ImageLoader getImageLoader() {
		return mImageLoader;
	}

	public void refreshData(CommentList list) {
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
					R.layout.comment_item_layout, null);

			viewHolder = new ViewHolder();
			viewHolder.comment_content_textview = (TextView) convertView.findViewById(R.id.comment_content_textview);
			viewHolder.comment_date_textview = (TextView) convertView.findViewById(R.id.content_textview);
			viewHolder.comment_up_textview = (TextView) convertView.findViewById(R.id.comment_up_textview);
			viewHolder.comment_down_textview = (TextView) convertView.findViewById(R.id.comment_down_textview);

			viewHolder.comment_author_imageview = (ImageView) convertView.findViewById(R.id.comment_author_imageview);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.comment_content_textview.setText(list.getItem(position).getContent());
		viewHolder.comment_date_textview.setText(list.getItem(position).getDate().toString());
		viewHolder.comment_up_textview.setText(list.getItem(position).getUpvote());
		viewHolder.comment_down_textview.setText(list.getItem(position).getDownvote());
		
		if (list.getItem(position).getAuthorId().trim() != null
				&& list.getItem(position).getAuthorId().length() > 0) {
			viewHolder.comment_author_imageview.setVisibility(View.VISIBLE);
			mImageLoader.DisplayImage(list.getItem(position).getAuthorPic(), viewHolder.comment_author_imageview,
					THUMB_NAIL_SIZE, false);
		} else {
			viewHolder.comment_author_imageview.setImageResource(R.drawable.default_author_image);
		}


		return convertView;
	}

	int lastX, curX;
	private int totalMove = 0;
	private boolean firstDown = true;// ����
	int duration = 150;

	static class ViewHolder {
		TextView comment_content_textview;
		TextView comment_date_textview;
		TextView comment_up_textview;
		TextView comment_down_textview;
		ImageView comment_author_imageview;
	}

}
