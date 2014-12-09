package com.joe.orangee.net;

import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import com.androidplus.net.HttpRequest;
import com.androidplus.net.NetworkUtil;
import com.androidplus.util.StringUtil;
import com.joe.orangee.model.UnreadMessage;
import com.joe.orangee.util.Constants;
import com.joe.orangee.util.PreferencesKeeper;
import com.joe.orangee.util.Utils;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

public class CommonDownloader {
	private Context context;
	private Downloader downloader;
	
	public CommonDownloader(Context context) {
		this.context = context;
		this.downloader = new Downloader(context);
	}

			
	public UnreadMessage getUnreadMessage()
	{
		UnreadMessage unreadMessage = null;
		if(NetworkUtil.getInstance(context).getNetworkType() == -1){
			return unreadMessage;
		}else{
			 HashMap<String, String> params = Utils.getParamMap(context);
			 Oauth2AccessToken mAccessToken = PreferencesKeeper.readAccessToken(context);
			 params.put("uid", mAccessToken.getUid());
		        
			String jsonStr = downloader.getJsonContent(Constants.URL_UNREAD_MESSAGE, params, HttpRequest.METHOD_GET);
			if(StringUtil.isNullOrEmpty(jsonStr)){
				return unreadMessage;
			}
			
			try {
				JSONObject jsonObject=new JSONObject(jsonStr);
				unreadMessage=new UnreadMessage();
				unreadMessage.setStatus(jsonObject.getInt("status"));
				unreadMessage.setFollower(jsonObject.getInt("follower"));
				unreadMessage.setComment(jsonObject.getInt("cmt"));
				unreadMessage.setLetter(jsonObject.getInt("dm"));
				unreadMessage.setMention_status(jsonObject.getInt("mention_status"));
				unreadMessage.setMetion_comment(jsonObject.getInt("mention_cmt"));
			} catch (JSONException e) {
				return null;
			}
		}
		return unreadMessage;
	}
	
	/**
	 * 退出登录
	 * @user qiaorongzhu
	 * @since 2014-12-8
	 * @return
	 */
	public Integer logout()
	{
		if(NetworkUtil.getInstance(context).getNetworkType() == -1){
			return 0;
		}else{
			HashMap<String, String> params = Utils.getParamMap(context);
			String jsonStr = downloader.getJsonContent(Constants.URL_LOGOUT, params, HttpRequest.METHOD_GET);
			if(StringUtil.isNullOrEmpty(jsonStr)){
				return 0;
			}
			return 1;
		}
	}
}
