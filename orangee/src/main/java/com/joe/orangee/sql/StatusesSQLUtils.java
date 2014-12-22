package com.joe.orangee.sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.joe.orangee.model.WeiboStatus;

import java.util.List;

public class StatusesSQLUtils {
	

	/**
	 * 往数据库里面插入数据 
	 */
	public static void insertStatusesData(List<WeiboStatus> dataList, SQLiteDatabase mSQLiteDatabase){		
		ContentValues values = new ContentValues();
		for (WeiboStatus status : dataList) {
			synchronized (mSQLiteDatabase) {
				try {
					mSQLiteDatabase.insert(StatusesSQLOpenHelper.DB_TABLE, StatusesSQLOpenHelper.KEY_ID, putContentValues(values, status)) ;
				} catch (Exception e) {
				}
				
			}
		}
	}

	/**
	 * 在数据库里面更新数据 
	 */
	public static void updateStatusesData(List<WeiboStatus> dataList, SQLiteDatabase mSQLiteDatabase){		
		ContentValues values = new ContentValues();
		for (int i = 0; i < dataList.size(); i++) {
			WeiboStatus status=dataList.get(i);
			mSQLiteDatabase.update(StatusesSQLOpenHelper.DB_TABLE, putContentValues(values, status), StatusesSQLOpenHelper.KEY_ID + " = " +(i+1), null);
		}
	}
	
	/**
	 * 查询数据 ， 当前的所有数据
	 * @return
	 */
	public static Cursor fetchAllData(SQLiteDatabase mSQLiteDatabase){		
		
		return mSQLiteDatabase.query(StatusesSQLOpenHelper.DB_TABLE, null	, null, null, null, null, null);
		
	}
	
	public static void deleteTableData(SQLiteDatabase mSQLiteDatabase){
		mSQLiteDatabase.delete(StatusesSQLOpenHelper.DB_TABLE, null, null);
	}
	
	/**
	 * ContentValues 放入数据
	 * @param values
	 * @param status
	 * @return
	 */
	private static ContentValues putContentValues(ContentValues values, WeiboStatus status){
		values.put(StatusesSQLOpenHelper.KEY_AVATAR, status.getUser().getAvatar());
		values.put(StatusesSQLOpenHelper.KEY_NAME, status.getUser().getName());
		values.put(StatusesSQLOpenHelper.KEY_UID, status.getUser().getUid());
		values.put(StatusesSQLOpenHelper.KEY_TIME, status.getTime());
		values.put(StatusesSQLOpenHelper.KEY_SOURCE, status.getSource());
		values.put(StatusesSQLOpenHelper.KEY_POSTTEXT, status.getPostText());
		String picUrls="";
		for (String string : status.getPicList()) {
			picUrls=picUrls+string+"_#_";
		}
		values.put(StatusesSQLOpenHelper.KEY_PICLIST,picUrls);
		values.put(StatusesSQLOpenHelper.KEY_WEIBOID,status.getWeiboId());
		values.put(StatusesSQLOpenHelper.KEY_COMMENT_COUNT, status.getComment_count());
		values.put(StatusesSQLOpenHelper.KEY_REPOST_COUNT, status.getRepost_count());
		values.put(StatusesSQLOpenHelper.KEY_LOCATION, status.getLocation());
		WeiboStatus retweetStatus=status.getWeiboStatus();
		if (retweetStatus!=null) {
			values.put(StatusesSQLOpenHelper.KEY_RETWEET_AVATAR,retweetStatus.getUser().getAvatar());
			values.put(StatusesSQLOpenHelper.KEY_RETWEET_NAME, retweetStatus.getUser().getName());
			values.put(StatusesSQLOpenHelper.KEY_RETWEET_UID, retweetStatus.getUser().getUid());
			values.put(StatusesSQLOpenHelper.KEY_RETWEET_TIME, retweetStatus.getTime());
			values.put(StatusesSQLOpenHelper.KEY_RETWEET_SOURCE, retweetStatus.getSource());
			values.put(StatusesSQLOpenHelper.KEY_RETWEET_POSTTEXT, retweetStatus.getPostText());
			String retweet_picUrls="";
			for (String string : retweetStatus.getPicList()) {
				retweet_picUrls=retweet_picUrls+string+"_#_";
			}
			values.put(StatusesSQLOpenHelper.KEY_RETWEET_PICLIST, retweet_picUrls);
			values.put(StatusesSQLOpenHelper.KEY_RETWEET_WEIBOID, retweetStatus.getWeiboId());
			values.put(StatusesSQLOpenHelper.KEY_RETWEET_COMMENT_COUNT, retweetStatus.getComment_count());
			values.put(StatusesSQLOpenHelper.KEY_RETWEET_REPOST_COUNT, retweetStatus.getRepost_count());
			
		}else {
			values.put(StatusesSQLOpenHelper.KEY_RETWEET_AVATAR, "");
			values.put(StatusesSQLOpenHelper.KEY_RETWEET_NAME, "");
			values.put(StatusesSQLOpenHelper.KEY_RETWEET_UID, "");
			values.put(StatusesSQLOpenHelper.KEY_RETWEET_TIME, "");
			values.put(StatusesSQLOpenHelper.KEY_RETWEET_SOURCE, "");
			values.put(StatusesSQLOpenHelper.KEY_RETWEET_POSTTEXT, "");
			values.put(StatusesSQLOpenHelper.KEY_RETWEET_PICLIST, "");
			values.put(StatusesSQLOpenHelper.KEY_RETWEET_WEIBOID, "");
			values.put(StatusesSQLOpenHelper.KEY_RETWEET_COMMENT_COUNT, -1);
			values.put(StatusesSQLOpenHelper.KEY_RETWEET_REPOST_COUNT, -1);
		}
		return values;
	}
	
	/*public static void SQLInsertExecute(List<Forum> data, SQLForumOpenHelper mOpenHelper) {
		
		for (Forum forum : data) {
			String[] insert=new String[8];
			insert[0]=forum.getAuthor();
			insert[1]=forum.getTime();
			insert[2]=forum.getMediaType()+"";
			insert[3]=forum.getTitle();
			insert[4]=forum.getClick();
			insert[5]=forum.getReply();
			insert[6]=forum.getNextPageUrl();
			insert[7]=forum.getUrl();
			mOpenHelper.insertData(insert);
		}
//		mOpenHelper.close();
	}
	
	public static void SQLUpdateExecute(List<Forum> data, SQLForumOpenHelper mOpenHelper){
		for (int i = 0; i < data.size(); i++) {
			String[] insert=new String[8];
			insert[0]=data.get(i).getAuthor();
			insert[1]=data.get(i).getTime();
			insert[2]=data.get(i).getMediaType()+"";
			insert[3]=data.get(i).getTitle();
			insert[4]=data.get(i).getClick();
			insert[5]=data.get(i).getReply();
			insert[6]=data.get(i).getNextPageUrl();
			insert[7]=data.get(i).getUrl();
			mOpenHelper.updateData(i+1, insert);
		}
//		mOpenHelper.close();
	}
	
	public static void SQLHeadlinesInsertExecute(List<RightHeadLines> data, SQLForumOpenHelper mOpenHelper) {
		
		for (RightHeadLines headLine : data) {
			String[] insert=new String[2];
			insert[0]=headLine.getTitle();
			insert[1]=headLine.getUrl();
			mOpenHelper.insertHeadlinesData(insert);
		}
//		mOpenHelper.close();
	}
	
	public static void SQLHeadlinesUpdateExecute(List<RightHeadLines> data, SQLForumOpenHelper mOpenHelper) {
		
		for (int i = 0; i < data.size(); i++) {
			String[] insert=new String[2];
			insert[0]=data.get(i).getTitle();
			insert[1]=data.get(i).getUrl();
			mOpenHelper.updateHeadlinesData(i+1, insert);
		}
//		mOpenHelper.close();
	}*/
	
}
