package com.larrylivingston.utilities_module;

import java.sql.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DbAdapter {

	/* 
	public static String JSON_TAG_ID = "id";
	public static String JSON_TAG_TITLE = "title";
	public static String JSON_TAG_AUTHOR = "author";
	public static String JSON_TAG_DATE = "date";
	public static String JSON_TAG_CONTENT = "content";
	public static String JSON_TAG_LAT = "lat";
	public static String JSON_TAG_LNG = "lng";
	public static String JSON_TAG_LINK = "link";
	public static String JSON_TAG_VIEWCOUNT = "view_count";
	public static String JSON_TAG_CATEGORY = "category";
	public static String JSON_TAG_ADDRESS = "address";
	public static String JSON_TAG_UPVOTE = "upvote";
	public static String JSON_TAG_DOWNVOTE = "downvote";
	public static String JSON_TAG_AUTHORID = "author_id";
	public static String JSON_TAG_THUMBNAIL = "thumbnail";
	public static String JSON_TAG_PIC1 = "pic_1";
	public static String JSON_TAG_PIC2 = "pic_2";
	public static String JSON_TAG_PIC3 = "pic_3";	 
	 */
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_NEWS_ID = "news_id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_AUTHOR = "author";
	public static final String KEY_DATE = "news_date";
	public static final String KEY_CONTENT = "content";
	public static final String KEY_LAT = "lat";
	public static final String KEY_LNG = "lng";
	public static final String KEY_LINK = "link";
	public static final String KEY_VIEWCOUNT = "view_count";
	public static final String KEY_CATEGORY = "category";
	public static final String KEY_UPVOTE = "upvote";
	public static final String KEY_DOWNVOTE = "downvote";
	public static final String KEY_AUTHORID = "author_id";
	public static final String KEY_THUMBNIAL = "thumbnail";
	public static final String KEY_PIC_1 = "pic_1";
	public static final String KEY_PIC_2 = "pic_2";
	public static final String KEY_PIC_3 = "pic_3";
	public static final String KEY_ADDED_DATE = "added_date";
	public static final String KEY_READ = "read";

	private static final String TAG = "DBAdapter";

	private static final String DATABASE_NAME = "news_database";
	private static final String DATABASE_TABLE = "saved_news";
	private static final String DATABASE_TABLE2 = "later_news";

	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE_SAVED_NEWS = "create table if not exists "
			+ DATABASE_TABLE
			+ " ("
			+ KEY_ROWID
			+ " integer primary key autoincrement, "
			+ KEY_NEWS_ID
			+ " text not null, "
			+ KEY_TITLE
			+ " text not null, "
			+ KEY_AUTHOR			
			+ " text not null, "
			+ KEY_DATE
			+ " text not null, "
			+ KEY_CONTENT
			+ " text not null, "
			+ KEY_LAT
			+ " double not null, "
			+ KEY_LNG
			+ " double not null, "
			+ KEY_LINK
			+ " text not null, "
			
			+ KEY_VIEWCOUNT
			+ " text not null, "
			+ KEY_CATEGORY
			+ " text not null, "
			+ KEY_UPVOTE
			+ " text not null, "
			+ KEY_DOWNVOTE
			+ " text not null, "
			
			+ KEY_AUTHORID
			+ " text not null, "
			+ KEY_THUMBNIAL
			+ " text not null, "
			+ KEY_PIC_1
			+ " text not null, "
			+ KEY_PIC_2
			+ " text not null, "
		    + KEY_PIC_3
			+ " text not null, "
			+ KEY_ADDED_DATE
			+ " text not null);";

	private static final String DATABASE_CREATE_READLATER_NEWS = "create table if not exists "
			+ DATABASE_TABLE2
			+ " ("
			+ KEY_ROWID
			+ " integer primary key autoincrement, "
			+ KEY_NEWS_ID
			+ " text not null, "
			+ KEY_TITLE
			+ " text not null, "
			+ KEY_AUTHOR			
			+ " text not null, "
			+ KEY_DATE
			+ " text not null, "
			+ KEY_CONTENT
			+ " text not null, "
			+ KEY_LAT
			+ " double not null, "
			+ KEY_LNG
			+ " double not null, "
			+ KEY_LINK
			+ " text not null, "
			
			+ KEY_VIEWCOUNT
			+ " text not null, "
			+ KEY_CATEGORY
			+ " text not null, "
			+ KEY_UPVOTE
			+ " text not null, "
			+ KEY_DOWNVOTE
			+ " text not null, "
			
			+ KEY_AUTHORID
			+ " text not null, "
			+ KEY_THUMBNIAL
			+ " text not null, "
			+ KEY_PIC_1
			+ " text not null, "
			+ KEY_PIC_2
			+ " text not null, "
		    + KEY_PIC_3
			+ " text not null, "
			+ KEY_ADDED_DATE
			+ " text not null, "
			+ KEY_READ
			+ " text not null);";
	/**
	 * KEY_READ = 0, means unread KEY_READ = 1, means read
	 * 
	 * KEY_READ_TIME = 0, any time KEY_READ_TIME = 1, lunch KEY_READ_TIME = 2,
	 * transport KEY_READ_TIME = 3, bed time
	 */

	private final Context context;

	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public DbAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.w(TAG, TAG+"In onCreate");
			// db.execSQL(DATABASE_CREATE);
			db.execSQL(DATABASE_CREATE_SAVED_NEWS);
			db.execSQL(DATABASE_CREATE_READLATER_NEWS);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, TAG+"Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS titles");
			onCreate(db);
		}
	}

	// ---opens the database---
	public DbAdapter open() throws SQLException {
		Log.w(TAG, "In open()");
		db = DBHelper.getWritableDatabase();
		setLockingEnabled();
		return this;
	}

	// ---closes the database---
	public void close() {
		DBHelper.close();
	}



	// ---insert a title into the database---
	public long insertSavedNews(String news_id, String title, String author, String date,
			String content, double lat, double lng, String link, String viewcount, String category,
			String upvote, String downvote, String author_id, String thumbnail, String pic_1, String pic_2, String pic_3,
			String added_date) {
		Log.w(TAG, TAG+" In insert");
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NEWS_ID, news_id);
		initialValues.put(KEY_TITLE, title);
		initialValues.put(KEY_AUTHOR, author);
		initialValues.put(KEY_DATE, date);
		initialValues.put(KEY_CONTENT, content);
		initialValues.put(KEY_LAT, lat);
		initialValues.put(KEY_LNG, lng);
		initialValues.put(KEY_LINK, link);
		initialValues.put(KEY_VIEWCOUNT, viewcount);
		initialValues.put(KEY_CATEGORY, category);
		initialValues.put(KEY_UPVOTE, upvote);
		initialValues.put(KEY_DOWNVOTE, downvote);
		initialValues.put(KEY_AUTHORID, author_id);
		initialValues.put(KEY_THUMBNIAL, thumbnail);
		initialValues.put(KEY_PIC_1, pic_1);
		initialValues.put(KEY_PIC_2, pic_2);
		initialValues.put(KEY_PIC_3, pic_3);
		initialValues.put(KEY_ADDED_DATE, added_date);
		return db.insert(DATABASE_TABLE, null, initialValues);
	}
	// ---deletes a particular title---
	public boolean deleteSavedNewsByDate(Date date) {
		return db.delete(DATABASE_TABLE, KEY_ADDED_DATE + "<" + date, null) > 0;
	}
	public boolean deleteSavedNewsTitle(String title) {
		return db.delete(DATABASE_TABLE, KEY_TITLE + "= \"" + title + "\"",
				null) > 0;
	}
	// ---deletes a all title---
	public boolean deleteSavedNewsAllTitle() {
		return db.delete(DATABASE_TABLE, null, null) > 0;
	}

	// ---retrieves all the titles---
	public Cursor getAllSavedNews() {
		return db.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NEWS_ID, KEY_TITLE, KEY_AUTHOR, KEY_DATE, 
				KEY_CONTENT, KEY_LAT, KEY_LNG, KEY_LINK, KEY_VIEWCOUNT, 
				KEY_CATEGORY, KEY_UPVOTE, KEY_DOWNVOTE, KEY_AUTHORID, KEY_THUMBNIAL, 
				KEY_PIC_1, KEY_PIC_2, KEY_PIC_3, KEY_ADDED_DATE }, null, null, null, null,
				KEY_ADDED_DATE + " DESC");
	}
//TODO
	public Cursor getTenSavedNews(String offset) {
		return db.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NEWS_ID, KEY_TITLE, KEY_AUTHOR, KEY_DATE, 
				KEY_CONTENT, KEY_LAT, KEY_LNG, KEY_LINK, KEY_VIEWCOUNT, 
				KEY_CATEGORY, KEY_UPVOTE, KEY_DOWNVOTE, KEY_AUTHORID, KEY_THUMBNIAL, 
				KEY_PIC_1, KEY_PIC_2, KEY_PIC_3, KEY_ADDED_DATE }, null, null, null, null,
				KEY_ADDED_DATE + " DESC", offset + ", 11");
	}

	public Cursor getSavedNewsByCat(String Cat, String offset) {
		Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID, KEY_NEWS_ID, KEY_TITLE, KEY_AUTHOR, KEY_DATE, 
				KEY_CONTENT, KEY_LAT, KEY_LNG, KEY_LINK, KEY_VIEWCOUNT, 
				KEY_CATEGORY, KEY_UPVOTE, KEY_DOWNVOTE, KEY_AUTHORID, KEY_THUMBNIAL, 
				KEY_PIC_1, KEY_PIC_2, KEY_PIC_3, KEY_ADDED_DATE},
				KEY_CATEGORY + "= \"" + Cat + "\"", null, null, null, KEY_ADDED_DATE + " DESC",
				offset + ", 11");

		if (mCursor != null) {
			// mCursor.moveToFirst();
		}
		return mCursor;
	}

	// ---retrieves a particular title---
	public Cursor getSavedNewsByTitle(String title) throws SQLException {
		Cursor mCursor = db
				.query(true, DATABASE_TABLE,
						new String[] { KEY_ROWID, KEY_NEWS_ID, KEY_TITLE, KEY_AUTHOR, KEY_DATE, 
						KEY_CONTENT, KEY_LAT, KEY_LNG, KEY_LINK, KEY_VIEWCOUNT, 
						KEY_CATEGORY, KEY_UPVOTE, KEY_DOWNVOTE, KEY_AUTHORID, KEY_THUMBNIAL, 
						KEY_PIC_1, KEY_PIC_2, KEY_PIC_3, KEY_ADDED_DATE},
						KEY_TITLE + "= \"" + title + "\"", null, null, null,
						null, null);

		if (mCursor != null) {
			// mCursor.moveToFirst();
		}
		return mCursor;
	}

	// ---updates a title---
	public boolean updateSavedNews(String news_id, String title, String author, String date,
			String content, double lat, double lng, String link, String viewcount, String category,
			String upvote, String downvote, String author_id, String thumbnail, String pic_1, String pic_2, String pic_3,
			String added_date) {
		ContentValues args = new ContentValues();
		args.put(KEY_NEWS_ID, news_id);
		args.put(KEY_TITLE, title);
		args.put(KEY_AUTHOR, author);
		args.put(KEY_DATE, date);
		args.put(KEY_CONTENT, content);
		args.put(KEY_LAT, lat);
		args.put(KEY_LNG, lng);
		args.put(KEY_LINK, link);
		args.put(KEY_VIEWCOUNT, viewcount);
		args.put(KEY_CATEGORY, category);
		args.put(KEY_UPVOTE, upvote);
		args.put(KEY_DOWNVOTE, downvote);
		args.put(KEY_AUTHORID, author_id);
		args.put(KEY_THUMBNIAL, thumbnail);
		args.put(KEY_PIC_1, pic_1);
		args.put(KEY_PIC_2, pic_2);
		args.put(KEY_PIC_3, pic_3);
		args.put(KEY_ADDED_DATE, added_date);
		return db.update(DATABASE_TABLE, args, KEY_NEWS_ID + "='" + news_id + "'",
				null) > 0;
	}

	/**
	 * Start from this part, this is the read later news Both read later and
	 * saved news share the same file directory but the index will be saved into
	 * two seperate table
	 * 
	 */
	public long insertLaterNews(String news_id, String title, String author, String date,
			String content, double lat, double lng, String link, String viewcount, String category,
			String upvote, String downvote, String author_id, String thumbnail, String pic_1, String pic_2, String pic_3,
			String added_date) {
		Log.w(TAG, TAG+" In insert");
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NEWS_ID, news_id);
		initialValues.put(KEY_TITLE, title);
		initialValues.put(KEY_AUTHOR, author);
		initialValues.put(KEY_DATE, date);
		initialValues.put(KEY_CONTENT, content);
		initialValues.put(KEY_LAT, lat);
		initialValues.put(KEY_LNG, lng);
		initialValues.put(KEY_LINK, link);
		initialValues.put(KEY_VIEWCOUNT, viewcount);
		initialValues.put(KEY_CATEGORY, category);
		initialValues.put(KEY_UPVOTE, upvote);
		initialValues.put(KEY_DOWNVOTE, downvote);
		initialValues.put(KEY_AUTHORID, author_id);
		initialValues.put(KEY_THUMBNIAL, thumbnail);
		initialValues.put(KEY_PIC_1, pic_1);
		initialValues.put(KEY_PIC_2, pic_2);
		initialValues.put(KEY_PIC_3, pic_3);
		initialValues.put(KEY_ADDED_DATE, added_date);
		initialValues.put(KEY_READ, 0);
		return db.insert(DATABASE_TABLE, null, initialValues);
	}

	// ---deletes a particular title---
	public boolean deleteLaterNewsByDate(Date date) {
		return db.delete(DATABASE_TABLE2, KEY_ADDED_DATE + "<" + date, null) > 0;
	}

	public boolean deleteLaterNewsTitle(String title) {
		return db.delete(DATABASE_TABLE2, KEY_TITLE + "= \"" + title + "\"",
				null) > 0;
	}

	// ---deletes a all title---
	public boolean deleteLaterNewsAllTitle() {
		return db.delete(DATABASE_TABLE2, null, null) > 0;
	}

	// ---retrieves all the titles---
	public Cursor getAllLaterNews() {
		return db.query(DATABASE_TABLE2, new String[] { KEY_ROWID, KEY_NEWS_ID, KEY_TITLE, KEY_AUTHOR, KEY_DATE, 
				KEY_CONTENT, KEY_LAT, KEY_LNG, KEY_LINK, KEY_VIEWCOUNT, 
				KEY_CATEGORY, KEY_UPVOTE, KEY_DOWNVOTE, KEY_AUTHORID, KEY_THUMBNIAL, 
				KEY_PIC_1, KEY_PIC_2, KEY_PIC_3, KEY_ADDED_DATE }, null, null, null, null,
				KEY_ADDED_DATE + " DESC");
	}

	public Cursor getTenLaterNews(String offset) {
		return db.query(DATABASE_TABLE2,  new String[] { KEY_ROWID, KEY_NEWS_ID, KEY_TITLE, KEY_AUTHOR, KEY_DATE, 
				KEY_CONTENT, KEY_LAT, KEY_LNG, KEY_LINK, KEY_VIEWCOUNT, 
				KEY_CATEGORY, KEY_UPVOTE, KEY_DOWNVOTE, KEY_AUTHORID, KEY_THUMBNIAL, 
				KEY_PIC_1, KEY_PIC_2, KEY_PIC_3, KEY_ADDED_DATE }, null, null, null, null,
				KEY_ADDED_DATE + " DESC", offset + ", 11");
	}

	public Cursor getLaterNewsByCat(String Cat, String offset) {
		Cursor mCursor = db.query(true, DATABASE_TABLE2, new String[] {
				KEY_ROWID, KEY_NEWS_ID, KEY_TITLE, KEY_AUTHOR, KEY_DATE, 
				KEY_CONTENT, KEY_LAT, KEY_LNG, KEY_LINK, KEY_VIEWCOUNT, 
				KEY_CATEGORY, KEY_UPVOTE, KEY_DOWNVOTE, KEY_AUTHORID, KEY_THUMBNIAL, 
				KEY_PIC_1, KEY_PIC_2, KEY_PIC_3, KEY_ADDED_DATE},
				KEY_CATEGORY + "= \"" + Cat + "\"", null, null, null, KEY_ADDED_DATE + " DESC",
				offset + ", 11");

		if (mCursor != null) {
			// mCursor.moveToFirst();
		}
		return mCursor;
	}

	// ---retrieves a particular title---
	public Cursor getLaterNewsByTitle(String news_id) throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE2, 						
				new String[] { KEY_ROWID, KEY_NEWS_ID, KEY_TITLE, KEY_AUTHOR, KEY_DATE, 
				KEY_CONTENT, KEY_LAT, KEY_LNG, KEY_LINK, KEY_VIEWCOUNT, 
				KEY_CATEGORY, KEY_UPVOTE, KEY_DOWNVOTE, KEY_AUTHORID, KEY_THUMBNIAL, 
				KEY_PIC_1, KEY_PIC_2, KEY_PIC_3, KEY_ADDED_DATE},
				KEY_NEWS_ID + "= \"" + news_id + "\"", null, null, null,
				null, null);

		if (mCursor != null) {
			// mCursor.moveToFirst();
		}
		return mCursor;
	}

	// ---updates a title---
	public boolean updateLaterNews(String news_id, String title, String author, String date,
			String content, double lat, double lng, String link, String viewcount, String category,
			String upvote, String downvote, String author_id, String thumbnail, String pic_1, String pic_2, String pic_3,
			String added_date) {
		ContentValues args = new ContentValues();
		args.put(KEY_NEWS_ID, news_id);
		args.put(KEY_TITLE, title);
		args.put(KEY_AUTHOR, author);
		args.put(KEY_DATE, date);
		args.put(KEY_CONTENT, content);
		args.put(KEY_LAT, lat);
		args.put(KEY_LNG, lng);
		args.put(KEY_LINK, link);
		args.put(KEY_VIEWCOUNT, viewcount);
		args.put(KEY_CATEGORY, category);
		args.put(KEY_UPVOTE, upvote);
		args.put(KEY_DOWNVOTE, downvote);
		args.put(KEY_AUTHORID, author_id);
		args.put(KEY_THUMBNIAL, thumbnail);
		args.put(KEY_PIC_1, pic_1);
		args.put(KEY_PIC_2, pic_2);
		args.put(KEY_PIC_3, pic_3);
		args.put(KEY_ADDED_DATE, added_date);
		return db.update(DATABASE_TABLE, args, KEY_NEWS_ID + "='" + news_id + "'",
				null) > 0;
	}

	// ---set after read---
	public boolean afterRead(String news_id) {
		// title = DatabaseUtils.sqlEscapeString(title);
		ContentValues args = new ContentValues();
		args.put(KEY_READ, 1);
		return db.update(DATABASE_TABLE2, args, KEY_NEWS_ID + "= \"" + news_id
				+ "\"", null) > 0;
	}

	/**
	 * KEY_READ = 0, means unread KEY_READ = 1, means read
	 * 
	 * KEY_READ_TIME = 0, any time KEY_READ_TIME = 1, lunch KEY_READ_TIME = 2,
	 * transport KEY_READ_TIME = 3, bed time
	 */
//	// ---set read time---
//	public boolean setReadTime(String title, int readTime) {
//		ContentValues args = new ContentValues();
//		args.put(KEY_READ_TIME, readTime);
//		return db.update(DATABASE_TABLE2, args, KEY_TITLE + "= \"" + title
//				+ "\"", null) > 0;
//	}

	// ---get number of unread---
	public int getNumberOfUnread() {
		Cursor mCursor = db.query(true, DATABASE_TABLE2, new String[] {
				KEY_ROWID, KEY_NEWS_ID, KEY_TITLE, KEY_AUTHOR, KEY_DATE, 
				KEY_CONTENT, KEY_LAT, KEY_LNG, KEY_LINK, KEY_VIEWCOUNT, 
				KEY_CATEGORY, KEY_UPVOTE, KEY_DOWNVOTE, KEY_AUTHORID, KEY_THUMBNIAL, 
				KEY_PIC_1, KEY_PIC_2, KEY_PIC_3, KEY_ADDED_DATE  },
				KEY_READ + "=0", null, null, null, null, null);
		if (mCursor != null) {
			// mCursor.moveToFirst();
		}
		return mCursor.getCount();
	}

	// ---get number of unread by reading time---
	public Cursor getNumberOfUnreadByCategory(String category) {
		Cursor mCursor = db.query(true, DATABASE_TABLE2, new String[] {
				KEY_ROWID, KEY_NEWS_ID, KEY_TITLE, KEY_AUTHOR, KEY_DATE, 
				KEY_CONTENT, KEY_LAT, KEY_LNG, KEY_LINK, KEY_VIEWCOUNT, 
				KEY_CATEGORY, KEY_UPVOTE, KEY_DOWNVOTE, KEY_AUTHORID, KEY_THUMBNIAL, 
				KEY_PIC_1, KEY_PIC_2, KEY_PIC_3, KEY_ADDED_DATE  },
				KEY_READ + " = 0 AND " + KEY_CATEGORY + " = " + category, null,
				null, null, KEY_ADDED_DATE + " DESC", null);

		if (mCursor != null) {
			// mCursor.moveToFirst();
		}
		return mCursor;
	}

	// ---get the unread news by reading time---
	public Cursor getLaterNewsByCategory(String category) {
		Cursor mCursor = null;
		try {
			mCursor = db.query(true, DATABASE_TABLE2, new String[] { 
					KEY_ROWID, KEY_NEWS_ID, KEY_TITLE, KEY_AUTHOR, KEY_DATE, 
					KEY_CONTENT, KEY_LAT, KEY_LNG, KEY_LINK, KEY_VIEWCOUNT, 
					KEY_CATEGORY, KEY_UPVOTE, KEY_DOWNVOTE, KEY_AUTHORID, KEY_THUMBNIAL, 
					KEY_PIC_1, KEY_PIC_2, KEY_PIC_3, KEY_ADDED_DATE  },
					KEY_READ + " = 0 AND " + KEY_CATEGORY + " = " + category, null,
					null, null, KEY_ADDED_DATE + " DESC", null);
		} catch (Exception e) {
		}

		if (mCursor != null) {
			// mCursor.moveToFirst();
		}
		return mCursor;
	}

	// ---check if it was read before---
	public boolean checkIfRead(String news_id) {
		// title = DatabaseUtils.sqlEscapeString(title);
		Cursor mCursor = db.query(true, DATABASE_TABLE2, 				
				new String[] { KEY_ROWID, KEY_NEWS_ID, KEY_TITLE, KEY_AUTHOR, KEY_DATE, 
				KEY_CONTENT, KEY_LAT, KEY_LNG, KEY_LINK, KEY_VIEWCOUNT, 
				KEY_CATEGORY, KEY_UPVOTE, KEY_DOWNVOTE, KEY_AUTHORID, KEY_THUMBNIAL, 
				KEY_PIC_1, KEY_PIC_2, KEY_PIC_3, KEY_ADDED_DATE},
				KEY_NEWS_ID + "= \"" + news_id + "\"", null, null, null,
				null, null);

		boolean read = false;
		if (mCursor.getCount() > 0) {
			read = true;
		}
		return read;
	}

	public String checkCategory(String news_id) {
		String category = "";
		// title = DatabaseUtils.sqlEscapeString(title);
		Cursor mCursor = db.query(true, DATABASE_TABLE2,
				new String[] { KEY_CATEGORY }, KEY_READ + " = 0 AND "
						+ KEY_NEWS_ID + "= \"" + news_id + "\"", null, null, null,
				null, null);

		if (mCursor != null) {
			// mCursor.moveToFirst();
		}
		while (mCursor.moveToNext()) {
			category = mCursor.getString(0);
		}
		return category;
	}

	/*
	 * For version handling
	 */
	public void setLockingEnabled() {
		// TODO Auto-generated method stub
		if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.HONEYCOMB
				&& db != null) {
			db.setLockingEnabled(false);
		}
	}
}