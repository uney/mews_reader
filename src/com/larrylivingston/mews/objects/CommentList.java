package com.larrylivingston.mews.objects;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

import android.util.Log;
/**
 * Work flow for list
 * this is only for off line reading
 * whenever there are updates, delete this and make a new list
 * @author uney
 *
 */
public class CommentList implements Serializable {

	private static final long serialVersionUID = 1L;
	private int itemCount = 0;
	private List<Comment> commentList;
	private static final String TAG = "CommentList";
	public CommentList() {
		commentList = new Vector<Comment>(0);
	}

	public void addItemToEnd(Comment commentItem) {
		commentList.add(commentItem);
		itemCount++;
	}
	
	public void addItemFromTop(Comment commentItem) {
		commentList.add(0, commentItem);
		itemCount++;
	}

	public Comment getItem(int location) {
		return commentList.get(location);
	}
	
	public Comment revomeItem(int location) {
		itemCount --;
		return commentList.remove(location);
	}
	
	public void addItemWithLocation(int location, Comment commentItem) {
		itemCount --;
		commentList.add(location, commentItem);
	}

	public int getItemCount() {
		return itemCount;
	}
	public List<Comment> getList(){
		return commentList;
	}

	public void addList(CommentList list){
		Log.i(TAG, TAG + " list check: "+list.getItemCount());
		Log.i(TAG, TAG + " before add: "+commentList.size());
		for(int i=0;i<list.getItemCount();i++){
			Log.i(TAG, TAG + " addList here: "+list.getItem(i).getContent());
			commentList.add(list.getItem(i));
			itemCount ++;
		}
	}
}

