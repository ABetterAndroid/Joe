package com.joe.orangee.util;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import com.joe.orangee.model.Comment;
import com.joe.orangee.model.User;
import com.joe.orangee.model.WeiboStatus;

@SuppressLint("SimpleDateFormat")
public class JSONParseUtils {

	public static SimpleDateFormat format=new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy", DateFormatSymbols.getInstance(Locale.US));
	public static SimpleDateFormat format2=new SimpleDateFormat("MM-dd HH:mm");
	
	/**
	 * 解析comment字段
	 * @param jsonObject
	 * @param comment
	 */
	public static Comment parseCommentJSONObject(JSONObject jsonObject){
		
		Comment comment =new Comment();
		try {
			Date date = format.parse(jsonObject.getString("created_at"));
			comment.setTime(format2.format(date));
			String[] sourceArray=jsonObject.getString("source").split(">");
			if (sourceArray.length>1) {
				String[] source=sourceArray[1].split("<");
				comment.setSource(source[0].trim());
			}
			comment.setText(jsonObject.getString("text"));
			comment.setComment_id(jsonObject.getString("mid"));
		} catch (ParseException e) {
			return comment;
		} catch (JSONException e) {
			return comment;
		}
		return comment;
	}
	
	/**
	 * 解析User字段
	 * @user qiaorongzhu
	 * @since 2014-10-31
	 * @param jsonObject
	 * @param user
	 */
	public static User parseUserJSONObject(JSONObject jsonObject){
		
		User user=new User();
		try {
			user.setUid(jsonObject.getString("idstr"));
			user.setName(jsonObject.getString("screen_name"));
			user.setAvatar(jsonObject.getString("avatar_large"));
			user.setLocation(jsonObject.getString("location"));
			user.setDescription(jsonObject.getString("description"));
			user.setGender(jsonObject.getString("gender"));
			user.setFollowers_count(jsonObject.getInt("followers_count"));
			user.setFriends_count(jsonObject.getInt("friends_count"));
			user.setStatuses_count(jsonObject.getInt("statuses_count"));
			user.setFollowing(jsonObject.getBoolean("following"));
			user.setVerified(jsonObject.getBoolean("verified"));
			user.setVerified_reason(jsonObject.getString("verified_reason"));
			user.setFollow_me(jsonObject.getBoolean("follow_me"));
			user.setCover(jsonObject.getString("cover_image"));
		}  catch (JSONException e) {
			return user;
		}
		return user;
	}
	
	/**
	 * 解析Status
	 * @user qiaorongzhu
	 * @since 2014-10-31
	 * @param jsonObject
	 * @param user
	 * @return
	 */
	public static WeiboStatus parseStatusJSONObject(JSONObject jsonObject){
		
		WeiboStatus status=new WeiboStatus();
		try {
			status.setWeiboId(jsonObject.getString("idstr"));
			status.setRepost_count(jsonObject.getInt("reposts_count"));
			status.setComment_count(jsonObject.getInt("comments_count"));
			Date date = format.parse(jsonObject.getString("created_at"));
			status.setTime(format2.format(date));
			String[] sourceArray=jsonObject.getString("source").split(">");
			if (sourceArray.length > 1) {
				String[] source = sourceArray[1].split("<");
				status.setSource(source[0].trim());
			}
			status.setPostText(jsonObject.getString("text"));
			if (jsonObject.has("pic_urls")) {
				JSONArray array=jsonObject.getJSONArray("pic_urls");
				ArrayList<String> picList=new ArrayList<String>();
				for (int j = 0; j < array.length(); j++) {
					picList.add(((JSONObject)array.get(j)).getString("thumbnail_pic").replace("thumbnail", "bmiddle"));
				}
				status.setPicList(picList);
			}else if (jsonObject.has("pic_ids")) {
				JSONArray array=jsonObject.getJSONArray("pic_ids");
				ArrayList<String> picList=new ArrayList<String>();
				for (int j = 0; j < array.length(); j++) {
					
					picList.add("http://ww2.sinaimg.cn/bmiddle/"+array.get(j).toString());
				}
				status.setPicList(picList);
			}
			if (jsonObject.has("geo")) {
				JSONObject geoObject;
				try {
					geoObject = jsonObject.getJSONObject("geo");
					if (geoObject.has("coordinates")) {
						JSONArray array=geoObject.getJSONArray("coordinates");
						status.setLatitude(array.getDouble(0));
						status.setLongitude(array.getDouble(1));
						
					}
				} catch (Exception e) {
				}
			}
			status.setUser(parseUserJSONObject(jsonObject.getJSONObject("user")));
			if (jsonObject.has("retweeted_status")) {
				status.setWeiboStatus(parseStatusJSONObject(jsonObject.getJSONObject("retweeted_status")));
			}
			if (jsonObject.has("url_objects")) {
				JSONArray array=jsonObject.getJSONArray("url_objects");
				JSONObject o = null;
				for (int i = 0; i < array.length(); i++) {
					try {
						o = ((JSONObject)array.get(i)).getJSONObject("object").getJSONObject("object");
					} catch (Exception e) {
						continue;
					}
				}
				if (o!=null) {
//					String location=o.getString("summary").length()>25?o.getString("display_name"):o.getString("summary")+" "+o.getString("display_name");
					String location=o.getString("display_name");
					status.setLocation(location);
				}
			}
		}  catch (Exception e) {
			return null;
		} 
		return status;
	}
}
