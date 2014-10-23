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
public class MewsList implements Serializable {

	private static final long serialVersionUID = 1L;
	private int itemCount = 0;
	private List<Mews> mewsList;
	private static final String TAG = "MewsList";
	public MewsList() {
		mewsList = new Vector<Mews>(0);
	}

	public void addItemToEnd(Mews mewsItem) {
		mewsList.add(mewsItem);
		itemCount++;
	}
	
	public void addItemFromTop(Mews mewsItem) {
		mewsList.add(0, mewsItem);
		itemCount++;
	}

	public Mews getItem(int location) {
		return mewsList.get(location);
	}
	
	public Mews revomeItem(int location) {
		itemCount --;
		return mewsList.remove(location);
	}
	
	public void addItemWithLocation(int location, Mews mewsItem) {
		itemCount --;
		mewsList.add(location, mewsItem);
	}

	public int getItemCount() {
		return itemCount;
	}
	public List<Mews> getList(){
		return mewsList;
	}

	public void addList(MewsList list){
		Log.i(TAG, TAG + " list check: "+list.getItemCount());
		Log.i(TAG, TAG + " before add: "+mewsList.size());
		for(int i=0;i<list.getItemCount();i++){
			Log.i(TAG, TAG + " addList here: "+list.getItem(i).getTitle());
			mewsList.add(list.getItem(i));
			itemCount ++;
		}
	}
}

