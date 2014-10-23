package com.larrylivingston.cache;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.graphics.Bitmap;

public class ImageCache extends CacheInterface{

    private static final String TAG = "MemoryCache";
    private Map<String, Bitmap> cache=Collections.synchronizedMap(
            new LinkedHashMap<String, Bitmap>(10,1.5f,true));//Last argument true for LRU ordering


    public ImageCache(){
    	super();
    }
    
    
	@Override
    public Bitmap getBitmap(String id){
        try{
            if(!cache.containsKey(id)){
            	return null;
            }
            //NullPointerException sometimes happen here http://code.google.com/p/osmdroid/issues/detail?id=78 
            return cache.get(id);
        }catch(NullPointerException ex){
            ex.printStackTrace();
            return null;
        }
    }
	
	@Override
    public void putBitmap(String id, Bitmap bitmap){
        try{
            if(cache.containsKey(id))
            	super.size-=getSizeInBytes(cache.get(id));
            cache.put(id, bitmap);
            super.size+=getSizeInBytes(bitmap);
            checkSize();
        }catch(Throwable th){
            th.printStackTrace();
        }
    }
    

	@Override
    public void clear() {
        try{
            //NullPointerException sometimes happen here http://code.google.com/p/osmdroid/issues/detail?id=78 
            cache.clear();
            super.size=0;
        }catch(NullPointerException ex){
            ex.printStackTrace();
        }
    }


	@Override
	public String getString(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putString(String id, String string) {
		// TODO Auto-generated method stub
		
	}

    public void checkSize() {
        if(super.size>super.limit){
            Iterator<Entry<String, Bitmap>> iter=cache.entrySet().iterator();//least recently accessed item will be the first one iterated  
            while(iter.hasNext()){
                Entry<String, Bitmap> entry=iter.next();
                super.size-=getSizeInBytes(entry.getValue());
                iter.remove();
                if(super.size<=super.limit)
                    break;
            }
        }
    }
    public long getSizeInBytes(Bitmap bitmap) {
        if(bitmap==null)
            return 0;
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

}