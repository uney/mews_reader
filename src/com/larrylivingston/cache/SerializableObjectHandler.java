package com.larrylivingston.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;

import com.larrylivingston.mews.objects.MewsList;

public class SerializableObjectHandler {
	
	// Method to read the object from the File
	public static Object readFromFile(String fName, Context context) {

		FileInputStream fIn = null;
		ObjectInputStream isr = null;

		MewsList _list = new MewsList();
		File listFile = context.getFileStreamPath(fName);
		if (!listFile.exists()) {
			return null;
		}
		try {
			fIn = context.openFileInput(fName);
			isr = new ObjectInputStream(fIn);
			_list = (MewsList) isr.readObject();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			try {
				fIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return _list;

	}

	// Method to write the object to the File
	public static void writeToFile(Object data, String filePath, Context context) {

		FileOutputStream fOut = null;
		ObjectOutputStream osw = null;

		try {
			fOut = context.openFileOutput(filePath, Context.MODE_PRIVATE);
			osw = new ObjectOutputStream(fOut);
			osw.writeObject(data);
			osw.flush();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			try {
				fOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
