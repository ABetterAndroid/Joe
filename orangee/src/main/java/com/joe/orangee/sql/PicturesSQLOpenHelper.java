package com.joe.orangee.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.joe.orangee.util.Constants;

public class PicturesSQLOpenHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "oragnee.db";
	public static String DB_TABLE = Constants.TABLE_NAME_COLLECTED_PICS;
	private static final int DB_VERSION = 1;

	public static final String KEY_ID = "_id";
    public static final String KEY_COL_URL="col_url";
	public static final String KEY_AVATAR = "avatar";
	public static final String KEY_NAME = "name";
	public static final String KEY_UID = "uid";
	public static final String KEY_TIME = "time";
	public static final String KEY_SOURCE = "source";
	public static final String KEY_POSTTEXT = "post_text";
	public static final String KEY_PICLIST = "pic_list";
	public static final String KEY_WEIBOID= "weibo_id";
	public static final String KEY_COMMENT_COUNT = "comment_count";
	public static final String KEY_REPOST_COUNT = "repost_count";
	public static final String KEY_LOCATION = "location";

	public static final String KEY_RETWEET_AVATAR = "retweet_avatar";
	public static final String KEY_RETWEET_NAME = "retweet_name";
	public static final String KEY_RETWEET_UID = "retweet_uid";
	public static final String KEY_RETWEET_TIME = "retweet_time";
	public static final String KEY_RETWEET_SOURCE = "retweet_source";
	public static final String KEY_RETWEET_POSTTEXT = "retweet_post_text";
	public static final String KEY_RETWEET_PICLIST = "retweet_pic_list";
	public static final String KEY_RETWEET_WEIBOID= "retweet_weibo_id";
	public static final String KEY_RETWEET_COMMENT_COUNT = "retweet_comment_count";
	public static final String KEY_RETWEET_REPOST_COUNT = "retweet_repost_count";

	private String CREATE_DB;



	private SQLiteDatabase mSqLiteDatabase;

	public PicturesSQLOpenHelper(Context context ) {
		super(context, DB_NAME, null, DB_VERSION);
        CREATE_DB = " CREATE TABLE IF NOT EXISTS "
                + DB_TABLE + " ("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_COL_URL + " TEXT,"
                + KEY_AVATAR + " TEXT,"
                + KEY_NAME + " TEXT,"
                + KEY_UID + " TEXT,"
                + KEY_TIME + " TEXT,"
                + KEY_SOURCE + " TEXT,"
                + KEY_POSTTEXT + " TEXT,"
                + KEY_PICLIST + " TEXT,"
                + KEY_WEIBOID + " TEXT,"
                + KEY_COMMENT_COUNT + " INTEGER,"
                + KEY_REPOST_COUNT + " INTEGER,"
                + KEY_LOCATION + " TEXT,"
                + KEY_RETWEET_AVATAR + " TEXT,"
                + KEY_RETWEET_NAME + " TEXT,"
                + KEY_RETWEET_UID + " TEXT,"
                + KEY_RETWEET_TIME + " TEXT,"
                + KEY_RETWEET_SOURCE + " TEXT,"
                + KEY_RETWEET_POSTTEXT+ " TEXT,"
                + KEY_RETWEET_PICLIST + " TEXT,"
                + KEY_RETWEET_WEIBOID + " TEXT,"
                + KEY_RETWEET_COMMENT_COUNT + " INTEGER,"
                + KEY_RETWEET_REPOST_COUNT + " INTEGER"
                + ")";
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		db.execSQL(CREATE_DB);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_DB);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS notes");
		onCreate(db);

	}

	/**
	 * 根据 _id 来删除数据库中的某一条数据
	 * @param _id
	 * @return
	 */
	public boolean deleteData(long _id){
		return mSqLiteDatabase.delete(DB_TABLE, KEY_ID + "=" + _id, null) > 0 ;
	}
	
	/**
	 *通过_id 来更新某一条数据
	 * @param _id
	 * @param data
	 * @return
	 *//*
	public boolean updateData( long _id ,String data[]){
		getWritableDatabase();
		ContentValues updataValues = new ContentValues();
		updataValues.put(KEY_NAME, data[0]);
		updataValues.put(KEY_TIME, data[1]);
		updataValues.put(KEY_MEDIA, data[2]);
		updataValues.put(KEY_TITLE,data[3]);
		updataValues.put(KEY_READ,data[4]);
		updataValues.put(KEY_REPLY,data[5]);
		updataValues.put(KEY_NEXT_URL,data[6]);
		updataValues.put(KEY_URL,data[7]);
		return  mSqLiteDatabase.update(DB_TABLE, updataValues, KEY_ID + " = " + _id, null)>0;
	}*/
	/**
	 * 查询数据 ， 当前的所有数据
	 * @return
	 *//*
	public Cursor fetchAllData(){		
		return mSqLiteDatabase.query(DB_TABLE, null	, null, null, null, null, null);
	}
	
	*//**
	 * 通过Id来查询 当前id下的数据
	 * @param _id
	 * @return
	 *//*
	public Cursor fetchDataById(long _id){
		return mSqLiteDatabase.query(DB_TABLE, null	,KEY_ID + "=" + _id, null, null, null, null);
	}*/
}
