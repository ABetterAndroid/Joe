package com.joe.orangee.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import com.androidplus.net.HttpRequest;
import com.androidplus.net.NetworkUtil;
import com.androidplus.util.StringUtil;
import com.joe.orangee.callbacks.WeiboSendListener;
import com.joe.orangee.model.Comment;
import com.joe.orangee.model.WeiboStatus;
import com.joe.orangee.util.Constants;
import com.joe.orangee.util.JSONParseUtils;
import com.joe.orangee.util.PreferencesKeeper;
import com.joe.orangee.util.Utils;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;

@SuppressLint("SimpleDateFormat")
public class WeiboDownloader {

	private Context context;
	private Downloader downloader;
	
	public WeiboDownloader(Context context) {
		this.context = context;
		this.downloader = new Downloader(context);
	}

			
	public List<WeiboStatus> getWeiboList(String url, String screenName, long since_id, long max_id, int count, int page, int base_app, int featureType, int trim_user, int filter_by_author)
	{
		List<WeiboStatus> weiboList=new ArrayList<WeiboStatus>();
		if(NetworkUtil.getInstance(context).getNetworkType() == -1){
			return weiboList;
		}else{
			 HashMap<String, String> params = Utils.getParamMap(context);
			 if (url.equals(Constants.URL_PERSON_STATUSES)) {
				 params.put("screen_name", String.valueOf(screenName));
				 params.put("trim_user", String.valueOf(trim_user));
				 params.put("base_app", String.valueOf(base_app));
		         params.put("feature", String.valueOf(featureType));
			}else if(url.equals(Constants.URL_FRIENDS_TIMELINE)){
				params.put("trim_user", String.valueOf(trim_user));
				params.put("base_app", String.valueOf(base_app));
		         params.put("feature", String.valueOf(featureType));
			}else if (url.equals(Constants.URL_STATUS_MENTION)) {
				params.put("filter_by_author", String.valueOf(filter_by_author));
			}
			 
			 params.put("since_id", String.valueOf(since_id));
			 params.put("max_id", String.valueOf(max_id));
	         params.put("count", String.valueOf(count));
	         params.put("page", String.valueOf(page));
	         
	         String jsonStr = downloader.getJsonContent(url, params, HttpRequest.METHOD_GET);
	         
			if(StringUtil.isNullOrEmpty(jsonStr)){
				return weiboList;
			}
			try {
				JSONObject jsonObject=new JSONObject(jsonStr);
				JSONArray jsonArray=jsonObject.getJSONArray("statuses");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object=jsonArray.getJSONObject(i);
					WeiboStatus status=JSONParseUtils.parseStatusJSONObject(object);
					if (status!=null) {
						weiboList.add(status);
						
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return weiboList;
	}
	
	/**
	 * 获取附近微博
	 * @param lattitude
	 * @param longitude
	 * @param count
	 * @param page
	 * @return
	 */
	public List<WeiboStatus> getNearbyStatusList(double lattitude, double longitude, int count, int page)
	{
		List<WeiboStatus> weiboList=new ArrayList<WeiboStatus>();
		if(NetworkUtil.getInstance(context).getNetworkType() == -1){
			return weiboList;
		}else{
			 HashMap<String, String> params = Utils.getParamMap(context);
	         params.put("lat", String.valueOf(lattitude));
	         params.put("long", String.valueOf(longitude));
	         params.put("range", String.valueOf(2000));
	         params.put("sort", String.valueOf(0));
	         params.put("count", String.valueOf(count));
	         params.put("page", String.valueOf(page));
		        
			String jsonStr = downloader.getJsonContent(Constants.URL_STATUS_NEARBY, params, HttpRequest.METHOD_GET);
			Log.i("NearByStr", jsonStr);
			if(StringUtil.isNullOrEmpty(jsonStr)){
				return weiboList;
			}
			if (jsonStr.equals("[]")) {
				getNearbyStatusList(lattitude, longitude, count, page);
			}
			try {
				JSONObject jsonObject=new JSONObject(jsonStr);
				JSONArray jsonArray=jsonObject.getJSONArray("statuses");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object=jsonArray.getJSONObject(i);
					WeiboStatus status=JSONParseUtils.parseStatusJSONObject(object);
					weiboList.add(status);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return weiboList;
	}
	
	public List<Comment> getWeiboComment(String weibo_id, long since_id, long max_id, int count, int page, int filter_by_author)
	{
		List<Comment> commentList=new ArrayList<Comment>();
		if(NetworkUtil.getInstance(context).getNetworkType() == -1){
			return commentList;
		}else{
			 HashMap<String, String> params =Utils.getParamMap(context);
			 params.put("id", String.valueOf(weibo_id));
			 params.put("since_id", String.valueOf(since_id));
	         params.put("max_id", String.valueOf(max_id));
	         params.put("count", String.valueOf(count));
	         params.put("page", String.valueOf(page));
	         params.put("filter_by_author", String.valueOf(filter_by_author));
		        
			String jsonStr = downloader.getJsonContent(Constants.URL_COMMENT_SHOW, params, HttpRequest.METHOD_GET);
			if(StringUtil.isNullOrEmpty(jsonStr)){
				return commentList;
			}
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(jsonStr);
				JSONArray jsonArray=jsonObject.getJSONArray("comments");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object=jsonArray.getJSONObject(i);
					Comment comment=JSONParseUtils.parseCommentJSONObject(object);
					comment.setUser(JSONParseUtils.parseUserJSONObject(object.getJSONObject("user")));
					commentList.add(comment);
				}
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
		return commentList;
	}
	
	public List<WeiboStatus> getFavourites(int count, int page)
	{
		List<WeiboStatus> favouriteList=new ArrayList<WeiboStatus>();
		if(NetworkUtil.getInstance(context).getNetworkType() == -1){
			return favouriteList;
		}else{
			 HashMap<String, String> params =Utils.getParamMap(context);
			 params.put("count", String.valueOf(count));
			 params.put("page", String.valueOf(page));
		        
			String jsonStr = downloader.getJsonContent(Constants.URL_FAVOURITES, params, HttpRequest.METHOD_GET);
			if(StringUtil.isNullOrEmpty(jsonStr)){
				return favouriteList;
			}
			try {
				JSONObject jsonObject = new JSONObject(jsonStr);
				JSONArray jsonArray=jsonObject.getJSONArray("favorites");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object=jsonArray.getJSONObject(i).getJSONObject("status");
					WeiboStatus weiboStatus = JSONParseUtils.parseStatusJSONObject(object);
					if (weiboStatus==null) {
						continue;
					}
					favouriteList.add(weiboStatus);
				}
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
		return favouriteList;
	}
	
	
	/**
	 * 上传图片
	 * @user qiaorongzhu
	 * @since 2014-11-17
	 * @param path
	 */
	public void uploadPicture(String path)
	{
		if(NetworkUtil.getInstance(context).getNetworkType() == -1){
			return;
		}else{
			 HashMap<String, String> params =Utils.getParamMap(context);
			 params.put("pic", String.valueOf(path));
		        
			String jsonStr = downloader.getJsonContent(Constants.URL_UPLOAD_PICTURE, params, HttpRequest.METHOD_POST);
			if(StringUtil.isNullOrEmpty(jsonStr)){
				return ;
			}
				try {
					JSONObject jsonObject = new JSONObject(jsonStr);
				} catch (JSONException e) {
					e.printStackTrace();
				}
		}
		return ;
	}
	
	public void updateStatus(String status, Bitmap bitmap, final WeiboSendListener mListener)
	{
		if(NetworkUtil.getInstance(context).getNetworkType() == -1){
			mListener.onNoNetword();
		}else{
			String url=Constants.URL_UPDATE_STATUS;
			WeiboParameters params = new WeiboParameters();
	        params.put("status", status);
	        if (bitmap!=null) {
	        	params.put("pic", bitmap);
	        	url=Constants.URL_UPDATE_STATUS_WITH_ONE_PICTURE;
			}
	        params.put("access_token", PreferencesKeeper.readAccessToken(context).getToken());
	        AsyncWeiboRunner.requestAsync(url, params, "POST", new RequestListener() {
				
				@Override
				public void onWeiboException(WeiboException arg0) {
					mListener.onError();
				}
				
				@Override
				public void onComplete(String response) {
					if (response.startsWith("{\"created_at\"")) {
						mListener.onSuccess();
					}
				}
			});
		}
	}
	
}
	
