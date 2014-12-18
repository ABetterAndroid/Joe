package com.joe.orangee.net.Downloader;

import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import com.androidplus.net.HttpRequest;
import com.androidplus.net.NetworkUtil;
import com.androidplus.util.StringUtil;
import com.joe.orangee.model.User;
import com.joe.orangee.net.Downloader.Downloader;
import com.joe.orangee.util.Constants;
import com.joe.orangee.util.Utils;

import android.content.Context;

public class PersonDownloader {

	private Context context;
	private Downloader downloader;
	
	public PersonDownloader(Context context) {
		this.context = context;
		this.downloader = new Downloader(context);
	}
	
	/**
	 * 获取个人主页信息
	 * 
	 * @param text
	 * @param id
	 * @return
	 */
	public User getPersonInfo( String name, String uid )
	{
		User person = null;
		if(NetworkUtil.getInstance(context).getNetworkType() == -1){
			return person;
		}else{
			 HashMap<String, String> params = Utils.getParamMap(context);
			 if (name!=null) {
				 params.put("screen_name", String.valueOf(name));
			}
			 if (uid!=null) {
			 params.put("uid", String.valueOf(uid));
				
			}
		        
			String jsonStr = downloader.getJsonContent(Constants.URL_PERSON_INFO, params, HttpRequest.METHOD_GET);
			if(StringUtil.isNullOrEmpty(jsonStr)){
				return person;
			}
			try {
				JSONObject jsonObject=new JSONObject(jsonStr);
				person=new User();
				person.setUid(jsonObject.getString("idstr"));
				person.setName(jsonObject.getString("screen_name"));
				person.setAvatar(jsonObject.getString("avatar_large"));
				if (jsonObject.has("cover_image")) {
					person.setCover(jsonObject.getString(""));
				}
				person.setLocation(jsonObject.getString("location"));
				person.setDescription(jsonObject.getString("description"));
				person.setGender(jsonObject.getString("gender"));
				person.setFollowers_count(jsonObject.getInt("followers_count"));
				person.setFriends_count(jsonObject.getInt("friends_count"));
				person.setStatuses_count(jsonObject.getInt("statuses_count"));
				person.setFollowing(jsonObject.getBoolean("following"));
				person.setVerified(jsonObject.getBoolean("verified"));
				person.setVerified_reason(jsonObject.getString("verified_reason"));
				person.setFollow_me(jsonObject.getBoolean("follow_me"));
			} catch (JSONException e) {
				return person;
			}
		}
		return person;
	}
	
	public Integer friendshipDeal(String name, boolean doFollow)
	{
		if(NetworkUtil.getInstance(context).getNetworkType() == -1){
			return 0;
		}else{
			 HashMap<String, String> params = Utils.getParamMap(context);
	         params.put("screen_name", String.valueOf(name));
		     String url;
	         if (doFollow) {
				url=Constants.URL_FRIENDSHIP_CREATE;
			}else {
				url=Constants.URL_FRIENDSHIP_DESTROY;
			}
			String jsonStr = downloader.getJsonContent(url, params, HttpRequest.METHOD_POST);
			if(StringUtil.isNullOrEmpty(jsonStr)){
				return 0;
			}
			/*try {
				JSONObject jsonObject=new JSONObject(jsonStr);
				
			} catch (JSONException e) {
				return 0;
			}*/
		}
		return 1;
	}
	
}
