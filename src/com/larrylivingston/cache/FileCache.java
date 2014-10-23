package com.larrylivingston.cache;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class FileCache {
    
    private File cacheDir;
    
    public FileCache(Context context, String appName, String cacheFileName){
        //Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
        	cacheDir=new File(android.os.Environment.getExternalStorageDirectory()+"/"+appName, cacheFileName);
        }
        else{
        	cacheDir=context.getCacheDir();
        }
        if(!cacheDir.exists()){
            cacheDir.mkdirs();
        }
    }
    
    public File getFile(String name){
    	//TODO
    	/**
    	 * add timestamp in front of the file for deletion
    	 */
        //Identify cached file by hashcode.
        String filename=String.valueOf(name.hashCode());
        File f = new File(cacheDir, filename);
        return f;
        
    }
    
    public void clear(){
        File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        for(File f:files){
            f.delete(); 
        }
    }

}