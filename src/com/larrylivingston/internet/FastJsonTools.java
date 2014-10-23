package com.larrylivingston.internet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class FastJsonTools {
	private static final String TAG = "FastJson";
	public FastJsonTools() {
		// TODO Auto-generated constructor stub
	}
	
	public static <T> T getPerson(String jsonString,Class<T> cls){
		T t=null;
		try {
			t=JSON.parseObject(jsonString,cls);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return t;
	}
	public static <T> List<T> getPersons(String jsonString,Class<T> cls){
		List<T> list=new ArrayList<T>();
		try {
			list=JSON.parseArray(jsonString,cls);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}
	

	
	public static List<Map<String, Object>> listKeyMaps(String jsonString){
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		try {
			list=JSON.parseObject(jsonString,new TypeReference<List<Map<String,Object>>>(){});
		} catch (Exception e) {
			Log.i(TAG, TAG+" Error on parsing JSON: "+e);
			Log.i(TAG, TAG+" JSON STRING: "+jsonString);
		}		
		return list;
	}
}
