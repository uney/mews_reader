package com.larrylivingston.cache;


import android.graphics.Bitmap;

public abstract class CacheInterface {
	//this is the abstract class for cache system
    private static final String TAG = "MemoryCache";
    protected long size=0;//current allocated size
    protected long limit=1000000;//max memory in bytes

    public CacheInterface(){
        //use 12.5% of available heap size
        setLimit(Runtime.getRuntime().maxMemory()/8);
    }
    
    private void setLimit(long new_limit){
        limit=new_limit;
    }

    public abstract String getString(String id);
    public abstract void putString(String id, String string);
    
    public abstract Bitmap getBitmap(String id);
    public abstract void putBitmap(String id, Bitmap bitmap);
    
    public abstract void clear();
}
