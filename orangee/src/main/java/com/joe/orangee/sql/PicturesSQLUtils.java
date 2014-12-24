package com.joe.orangee.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.joe.orangee.model.PictureCollection;
import com.joe.orangee.model.WeiboStatus;

public class PicturesSQLUtils {
	

	/**
	 * 往数据库里面插入数据 
	 */
	public static void insertStatusesData(PictureCollection collection, SQLiteDatabase mSQLiteDatabase){
		ContentValues values = new ContentValues();
        synchronized (mSQLiteDatabase) {
            try {
                mSQLiteDatabase.insert(PicturesSQLOpenHelper.DB_TABLE, PicturesSQLOpenHelper.KEY_ID, putContentValues(values, collection)) ;
            } catch (Exception e) {
            }

        }
	}


	/**
	 * 查询数据 ， 当前的所有数据
	 * @return
	 */
	public static Cursor fetchAllData(SQLiteDatabase mSQLiteDatabase){
		
		return mSQLiteDatabase.query(PicturesSQLOpenHelper.DB_TABLE, null	, null, null, null, null, null);
		
	}

    /**
     * 删除一条数据
     * @param id
     * @return
     */
    public static synchronized boolean deleteOneData(Context context, long id){
        PicturesSQLOpenHelper mOpenHelper = new PicturesSQLOpenHelper(context);
        SQLiteDatabase mSQLiteDatabase = mOpenHelper.getReadableDatabase();
        boolean result=mSQLiteDatabase.delete(PicturesSQLOpenHelper.DB_TABLE, PicturesSQLOpenHelper.KEY_ID + "=" + id, null) > 0 ;
        mOpenHelper.close();
        mSQLiteDatabase.close();
        return result;

    }

	public static synchronized void deleteTableData(SQLiteDatabase mSQLiteDatabase){
		mSQLiteDatabase.delete(PicturesSQLOpenHelper.DB_TABLE, null, null);
	}
	
	/**
	 * ContentValues 放入数据
	 * @param values
	 * @return
	 */
	private static ContentValues putContentValues(ContentValues values, PictureCollection collection){
        WeiboStatus status=collection.getStatus();
        values.put(PicturesSQLOpenHelper.KEY_COL_URL, collection.getUrl());
		values.put(PicturesSQLOpenHelper.KEY_AVATAR, status.getUser().getAvatar());
		values.put(PicturesSQLOpenHelper.KEY_NAME, status.getUser().getName());
		values.put(PicturesSQLOpenHelper.KEY_UID, status.getUser().getUid());
		values.put(PicturesSQLOpenHelper.KEY_TIME, status.getTime());
		values.put(PicturesSQLOpenHelper.KEY_SOURCE, status.getSource());
		values.put(PicturesSQLOpenHelper.KEY_POSTTEXT, status.getPostText());
		String picUrls="";
		for (String string : status.getPicList()) {
			picUrls=picUrls+string+"_#_";
		}
		values.put(PicturesSQLOpenHelper.KEY_PICLIST,picUrls);
		values.put(PicturesSQLOpenHelper.KEY_WEIBOID,status.getWeiboId());
		values.put(PicturesSQLOpenHelper.KEY_COMMENT_COUNT, status.getComment_count());
		values.put(PicturesSQLOpenHelper.KEY_REPOST_COUNT, status.getRepost_count());
		values.put(PicturesSQLOpenHelper.KEY_LOCATION, status.getLocation());
		WeiboStatus retweetStatus=status.getWeiboStatus();
		if (retweetStatus!=null) {
			values.put(PicturesSQLOpenHelper.KEY_RETWEET_AVATAR,retweetStatus.getUser().getAvatar());
			values.put(PicturesSQLOpenHelper.KEY_RETWEET_NAME, retweetStatus.getUser().getName());
			values.put(PicturesSQLOpenHelper.KEY_RETWEET_UID, retweetStatus.getUser().getUid());
			values.put(PicturesSQLOpenHelper.KEY_RETWEET_TIME, retweetStatus.getTime());
			values.put(PicturesSQLOpenHelper.KEY_RETWEET_SOURCE, retweetStatus.getSource());
			values.put(PicturesSQLOpenHelper.KEY_RETWEET_POSTTEXT, retweetStatus.getPostText());
			String retweet_picUrls="";
			for (String string : retweetStatus.getPicList()) {
				retweet_picUrls=retweet_picUrls+string+"_#_";
			}
			values.put(PicturesSQLOpenHelper.KEY_RETWEET_PICLIST, retweet_picUrls);
			values.put(PicturesSQLOpenHelper.KEY_RETWEET_WEIBOID, retweetStatus.getWeiboId());
			values.put(PicturesSQLOpenHelper.KEY_RETWEET_COMMENT_COUNT, retweetStatus.getComment_count());
			values.put(PicturesSQLOpenHelper.KEY_RETWEET_REPOST_COUNT, retweetStatus.getRepost_count());
			
		}else {
			values.put(PicturesSQLOpenHelper.KEY_RETWEET_AVATAR, "");
			values.put(PicturesSQLOpenHelper.KEY_RETWEET_NAME, "");
			values.put(PicturesSQLOpenHelper.KEY_RETWEET_UID, "");
			values.put(PicturesSQLOpenHelper.KEY_RETWEET_TIME, "");
			values.put(PicturesSQLOpenHelper.KEY_RETWEET_SOURCE, "");
			values.put(PicturesSQLOpenHelper.KEY_RETWEET_POSTTEXT, "");
			values.put(PicturesSQLOpenHelper.KEY_RETWEET_PICLIST, "");
			values.put(PicturesSQLOpenHelper.KEY_RETWEET_WEIBOID, "");
			values.put(PicturesSQLOpenHelper.KEY_RETWEET_COMMENT_COUNT, -1);
			values.put(PicturesSQLOpenHelper.KEY_RETWEET_REPOST_COUNT, -1);
		}
		return values;
	}
	
}
