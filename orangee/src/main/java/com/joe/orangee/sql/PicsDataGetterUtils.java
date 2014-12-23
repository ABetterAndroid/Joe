package com.joe.orangee.sql;

import android.database.Cursor;

import com.androidplus.util.StringUtil;
import com.joe.orangee.model.PictureCollection;
import com.joe.orangee.model.User;
import com.joe.orangee.model.WeiboStatus;

import java.util.ArrayList;
import java.util.List;

public class PicsDataGetterUtils {

	/**
	 * 从数据库获取首页数据
	 * @param mCursor
	 * @return
	 */
	public static List<PictureCollection> getStatuses(Cursor mCursor){
		List<PictureCollection> collectionList=new ArrayList<PictureCollection>();
        mCursor.moveToFirst();
        for (int i = 0; i < mCursor.getCount(); i++) {
            PictureCollection collection=new PictureCollection();
            collection.setId(mCursor.getLong(0));
            collection.setUrl(mCursor.getString(1));
			WeiboStatus status=new WeiboStatus();
			User user=new User();
			user.setAvatar(mCursor.getString(2));
			user.setName(mCursor.getString(3));
			user.setUid(mCursor.getString(4));
			status.setUser(user);
			status.setTime(mCursor.getString(5));
			status.setSource(mCursor.getString(6));
			status.setPostText(mCursor.getString(7));
			if (StringUtil.isNullOrEmpty(mCursor.getString(8))) {
				status.setPicList(new ArrayList<String>());
			}else {
				String[] picUrlsArray=mCursor.getString(8).split("_#_");
				ArrayList<String> list=new ArrayList<String>();
				for (int j = 0; j < picUrlsArray.length; j++) {
					list.add(picUrlsArray[j]);
				}
				status.setPicList(list);
			}
			status.setWeiboId(mCursor.getString(9));
			status.setComment_count(mCursor.getInt(10));
			status.setRepost_count(mCursor.getInt(11));
			status.setLocation(mCursor.getString(12));
			
			if (!StringUtil.isNullOrEmpty(mCursor.getString(13))) {
				WeiboStatus retweetStatus=new WeiboStatus();
				User retweetUser=new User();
				retweetUser.setAvatar(mCursor.getString(13));
				retweetUser.setName(mCursor.getString(14));
				retweetUser.setUid(mCursor.getString(15));
				retweetStatus.setUser(retweetUser);
				retweetStatus.setTime(mCursor.getString(16));
				retweetStatus.setSource(mCursor.getString(17));
				retweetStatus.setPostText(mCursor.getString(18));
				
				if (StringUtil.isNullOrEmpty(mCursor.getString(19))) {
					retweetStatus.setPicList(new ArrayList<String>());
				}else {
					String[] retweetPicUrlsArray=mCursor.getString(19).split("_#_");
					ArrayList<String> list=new ArrayList<String>();
					for (int j = 0; j < retweetPicUrlsArray.length; j++) {
						list.add(retweetPicUrlsArray[j]);
					}
					retweetStatus.setPicList(list);
				}
				
				retweetStatus.setWeiboId(mCursor.getString(20));
				retweetStatus.setComment_count(mCursor.getInt(21));
				retweetStatus.setRepost_count(mCursor.getInt(22));
				status.setWeiboStatus(retweetStatus);
			}
			collection.setStatus(status);
            collectionList.add(collection);
			mCursor.moveToNext();
		}
		return collectionList;
		
	}
	
}
