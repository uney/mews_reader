package com.larrylivingston.utilities_module;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import eu.janmuller.android.simplecropimage.CropImage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * Work flow of this fragment:
 * 1. User choose to take a picture or get a picture from gallery
 * 2. if they are taking a picture, store it in the default DCIM folder first
 * 3. Crop the selected picture with designated ratio
 * 4. Store the cropped picture into the application's folder (defined in Utilities class)
 * @author uney
 *
 */
public class PhotoTakingFragment extends Fragment {
	
	private final static String TAG = "photo_taking_fragment";
	
	//Different aspect ratio and size for cropping can be defined here
	private final static int ASPECT_X = 3;
	private final static int ASPECT_Y = 2;
	private final static int reqPhotoWidth = 400;
	private final static int reqPhotoHeight = 400;
	
	private final static int REQUEST_CAMERA = 1;
	private final static int SELECT_FILE = 2;
	private final static int CROP_IMAGE = 3;
	private final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
	private File tempFile;
	View view;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO View setting goes here, layout should be implemented upon using
		
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			tempFile = new File(Environment.getExternalStorageDirectory(),
					TEMP_PHOTO_FILE_NAME);
		} else {
			tempFile = new File(getActivity().getFilesDir(),
					TEMP_PHOTO_FILE_NAME);
		}
		return view;
	}

	private void selectImage() {
		final CharSequence[] items = { "Take Photo", "Choose from SDcard","Cancel" };
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Take Photo")) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(tempFile));
					startActivityForResult(intent, REQUEST_CAMERA);
				} else if (items[item].equals("Choose from SDcard")) {
					Intent intent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");
					startActivityForResult(
							Intent.createChooser(intent, "Select File"),
							SELECT_FILE);
				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		getActivity();
		if (resultCode == FragmentActivity.RESULT_OK) {
			if (requestCode == REQUEST_CAMERA) {

				try {

					//Camera photo goes to default file first
					String path = android.os.Environment
							.getExternalStorageDirectory()
							+ Utilities.defaultPhotoPath;
					File file = new File(path, String.valueOf(System
							.currentTimeMillis()) + ".jpg");
					Utilities.copyFile(tempFile, file);
					startCropImage(tempFile.getAbsolutePath());
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (requestCode == SELECT_FILE) {
				try {
					InputStream inputStream = getActivity()
							.getContentResolver().openInputStream(
									data.getData());
					FileOutputStream fileOutputStream = new FileOutputStream(
							tempFile);
					copyStream(inputStream, fileOutputStream);
					fileOutputStream.close();
					inputStream.close();
					startCropImage(tempFile.getAbsolutePath());
				} catch (Exception e) {
					Log.e(TAG, "Error while creating temp file", e);
				}
			} else if (requestCode == CROP_IMAGE) {
				String tempPath = data.getStringExtra(CropImage.IMAGE_PATH);
				// if nothing received
				if (tempPath == null) {
					tempFile.delete();
					return;
				} else {
					// cropped bitmap
					Bitmap bitmap = BitmapFactory.decodeFile(tempPath);
					
					String path = android.os.Environment
							.getExternalStorageDirectory()
							+ Utilities.appPhotoPath;
					File file = new File(path, String.valueOf(System
							.currentTimeMillis()) + ".jpg");

					try {
						Utilities.copyFile(tempFile, file);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					tempFile.delete();
					compressAndResize(file.getAbsolutePath(), reqPhotoWidth, reqPhotoHeight);
				}

			}
		}
	}

	public String getPath(Uri uri, Activity activity) {
		String[] projection = { MediaColumns.DATA };
		Cursor cursor = activity.getContentResolver().query(uri, projection,
				null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	/*
	 * Code for cropping image
	 */
	private void startCropImage(String filePath) {
		Intent intent = new Intent(getActivity(), CropImage.class);
		intent.putExtra(CropImage.IMAGE_PATH, filePath);
		intent.putExtra(CropImage.SCALE, true);
		intent.putExtra(CropImage.ASPECT_X, ASPECT_X);
		intent.putExtra(CropImage.ASPECT_Y, ASPECT_Y);
		startActivityForResult(intent, CROP_IMAGE);
	}

	/*
	 * Code for image compression, return a file path
	 */
	public String compressAndResize(String filePath, int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		Bitmap bm = BitmapFactory.decodeFile(filePath, options);
		if (bm == null) {
			return null;
		}

		String path = android.os.Environment.getExternalStorageDirectory()+ Utilities.appPhotoPath;		
		OutputStream fOut = null;
		File file = new File(path, String.valueOf(System
				.currentTimeMillis()) + ".jpg");
		try {
			fOut = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.JPEG, 30, fOut);
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file.getAbsolutePath();
	}

	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
		}
		return inSampleSize;
	}

	/*
	 * Others
	 */
	public static void copyStream(InputStream input, OutputStream output)
			throws IOException {
		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
	}
}
