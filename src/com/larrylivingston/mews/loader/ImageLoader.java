package com.larrylivingston.mews.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.larrylivingston.cache.FileCache;
import com.larrylivingston.cache.ImageCache;
import com.larrylivingston.mews.MewsConstants;
import com.larrylivingston.mews.R;
import com.larrylivingston.utilities_module.Utilities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoader {
	final int stub_id = R.drawable.default_image;
	private final String TAG = "image_loader";
	private static final String APP_NAME = MewsConstants.APP_NAME;
	private static final String CACHE_FILE = MewsConstants.CACHE_FILE;
	private static final String OFFLINE_FILE = MewsConstants.OFFLINE_FILE;
	private static ImageLoader imageLoader;
	private static boolean large_image;
	ImageCache imageCache = new ImageCache();
	FileCache fileCache;
	FileCache offlineFile;
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	ExecutorService executorService;
	private int REQUIRED_IMAGE_SIZE = 70;

	private ImageLoader(Context context) {
		fileCache = new FileCache(context, APP_NAME, CACHE_FILE);
		offlineFile = new FileCache(context, APP_NAME, OFFLINE_FILE);
		executorService = Executors.newFixedThreadPool(5);
	}
	
	public static ImageLoader getImageLoader(Context context){
		if(imageLoader == null){
			synchronized(ImageLoader.class){
				if (imageLoader == null){
					imageLoader =new ImageLoader(context);
				}
			}					
		}
		return imageLoader;
	}
	


	public void DisplayImage(String url, ImageView imageView, int required_size, boolean largeImage) {
		large_image = largeImage;
		REQUIRED_IMAGE_SIZE = required_size;
		imageViews.put(imageView, url);
		Bitmap bitmap = imageCache.getBitmap(url);
		if (bitmap != null){
			imageView.setImageBitmap(bitmap);
			}
		else {
			queuePhoto(url, imageView);
			imageView.setImageResource(stub_id);
		}
	}

	private void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	private Bitmap getBitmap(String url) {
		url = url.replaceAll(" ", "%20");
		File cacheFile = fileCache.getFile(url);
		File offline = this.offlineFile.getFile(url);

		// from SD cache
		Bitmap b = decodeFile(cacheFile);
		if (b != null){
			return b;
		}
		//from offline file
		b = decodeFile(offline);
		if (b != null){
			return b;
		}

		// download from web directly
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			//After downloaded, store it to the the file cache of read again later
			OutputStream os = new FileOutputStream(cacheFile);
			Utilities.CopyStream(is, os);
			os.close();
			//After stored to the fileCache, read the bitmap from the cache
			bitmap = decodeFile(cacheFile);
			return bitmap;
		} catch (Throwable ex) {
			ex.printStackTrace();
			if (ex instanceof OutOfMemoryError){
				imageCache.clear();
			}
			return null;
		}
	}

	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			FileInputStream stream1 = new FileInputStream(f);
			BitmapFactory.decodeStream(stream1, null, o);
			stream1.close();

			// Find the correct scale value. It should be the power of 2.
			int REQUIRED_SIZE = REQUIRED_IMAGE_SIZE;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			FileInputStream stream2 = new FileInputStream(f);
			Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
			stream2.close();
			if(large_image){
				int originalWidth = bitmap.getWidth(); 
				int originalHeight = bitmap.getHeight();				
				bitmap = Bitmap.createScaledBitmap(bitmap, REQUIRED_SIZE, originalHeight*REQUIRED_SIZE/originalWidth, true);
			}
//			Bitmap yourBitmap;
//			Bitmap resized = Bitmap.createScaledBitmap(yourBitmap, newWidth, newHeight, true);
			return bitmap;
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			try {
				if (imageViewReused(photoToLoad)){
					return;
				}
				Bitmap bmp = getBitmap(photoToLoad.url);
				imageCache.putBitmap(photoToLoad.url, bmp);
				if (imageViewReused(photoToLoad)){
					return;
				}
				BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
				Activity a = (Activity) photoToLoad.imageView.getContext();
				a.runOnUiThread(bd);
			} catch (Throwable th) {
				th.printStackTrace();
			}
		}
	}
	
	class PhotosDownloader implements Runnable {
		String url;

		PhotosDownloader(String url) {
			this.url = url;
		}

		@Override
		public void run() {
			try {
				getBitmap(url);
			} catch (Throwable th) {
				th.printStackTrace();
			}
		}
	}

	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad)){
				return;
			}
			if (bitmap != null)
				photoToLoad.imageView.setImageBitmap(bitmap);
			else
				photoToLoad.imageView.setImageResource(stub_id);
		}
	}

	/**
	 * Load photo without displaying here
	 */
	public void downLoadPhoto(String url){
		Bitmap bitmap = imageCache.getBitmap(url);
		if (bitmap == null){
			executorService.submit(new PhotosDownloader(url));
		}
	}
	
	public void clearCache() {
		//TODO Changed have been made here
		imageCache.clear();
	}

}
