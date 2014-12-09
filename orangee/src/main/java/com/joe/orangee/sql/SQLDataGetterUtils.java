package com.joe.orangee.sql;

import java.util.ArrayList;
import java.util.List;
import android.database.Cursor;
import com.androidplus.util.StringUtil;
import com.joe.orangee.model.User;
import com.joe.orangee.model.WeiboStatus;

public class SQLDataGetterUtils {

	/**
	 * 从数据库获取首页数据
	 * @param mCursor
	 * @return
	 */
	public static List<WeiboStatus> getStatuses(Cursor mCursor){
		List<WeiboStatus> weiboList=new ArrayList<WeiboStatus>();
		mCursor.moveToFirst();
		for (int i = 0; i < mCursor.getCount(); i++) {
			WeiboStatus status=new WeiboStatus();
			User user=new User();
			user.setAvatar(mCursor.getString(1));
			user.setName(mCursor.getString(2));
			user.setUid(mCursor.getString(3));
			status.setUser(user);
			status.setTime(mCursor.getString(4));
			status.setSource(mCursor.getString(5));
			status.setPostText(mCursor.getString(6));
			if (StringUtil.isNullOrEmpty(mCursor.getString(7))) {
				status.setPicList(new ArrayList<String>());
			}else {
				String[] picUrlsArray=mCursor.getString(7).split("_#_");
				ArrayList<String> list=new ArrayList<String>();
				for (int j = 0; j < picUrlsArray.length; j++) {
					list.add(picUrlsArray[j]);
				}
				status.setPicList(list);
			}
			status.setWeiboId(mCursor.getString(8));
			status.setComment_count(mCursor.getInt(9));
			status.setRepost_count(mCursor.getInt(10));
			status.setLocation(mCursor.getString(11));
			
			if (!StringUtil.isNullOrEmpty(mCursor.getString(12))) {
				WeiboStatus retweetStatus=new WeiboStatus();
				User retweetUser=new User();
				retweetUser.setAvatar(mCursor.getString(12));
				retweetUser.setName(mCursor.getString(13));
				retweetUser.setUid(mCursor.getString(14));
				retweetStatus.setUser(retweetUser);
				retweetStatus.setTime(mCursor.getString(15));
				retweetStatus.setSource(mCursor.getString(16));
				retweetStatus.setPostText(mCursor.getString(17));
				
				if (StringUtil.isNullOrEmpty(mCursor.getString(18))) {
					retweetStatus.setPicList(new ArrayList<String>());
				}else {
					String[] retweetPicUrlsArray=mCursor.getString(18).split("_#_");
					ArrayList<String> list=new ArrayList<String>();
					for (int j = 0; j < retweetPicUrlsArray.length; j++) {
						list.add(retweetPicUrlsArray[j]);
					}
					retweetStatus.setPicList(list);
				}
				
				retweetStatus.setWeiboId(mCursor.getString(19));
				retweetStatus.setComment_count(mCursor.getInt(20));
				retweetStatus.setRepost_count(mCursor.getInt(21));
				status.setWeiboStatus(retweetStatus);
			}
			weiboList.add(status);
			mCursor.moveToNext();
		}
		return weiboList;
		
	}
	
}
