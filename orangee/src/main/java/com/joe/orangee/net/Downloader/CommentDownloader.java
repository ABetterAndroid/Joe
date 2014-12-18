package com.joe.orangee.net.Downloader;

import android.annotation.SuppressLint;
import android.content.Context;

import com.androidplus.net.HttpRequest;
import com.androidplus.net.NetworkUtil;
import com.androidplus.util.StringUtil;
import com.joe.orangee.model.Comment;
import com.joe.orangee.model.WeiboStatus;
import com.joe.orangee.net.Result;
import com.joe.orangee.util.Constants;
import com.joe.orangee.util.JSONParseUtils;
import com.joe.orangee.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@SuppressLint("SimpleDateFormat")
public class CommentDownloader {

	private Context context;
	private Downloader downloader;
	
	public CommentDownloader(Context context) {
		this.context = context;
		this.downloader = new Downloader(context);
	}

			
	public Result<Comment> postComment(String text, String id )
	{
		Comment comment = null;
		if(NetworkUtil.getInstance(context).getNetworkType() == -1){
			return new Result<Comment>(comment, Result.NETWORK_INVALID);
		}else{
			 HashMap<String, String> params = Utils.getParamMap(context);
			 params.put("comment", String.valueOf(text));
	         params.put("id", String.valueOf(id));
//	         params.put("comment_ori", String.valueOf(comment_ori));
		        
			String jsonStr = downloader.getJsonContent(Constants.URL_POST_COMMENT, params, HttpRequest.METHOD_POST);
			if(StringUtil.isNullOrEmpty(jsonStr)){
				return new Result<Comment>(comment, Result.JSON_DATA_ERROR);
			}
			
			SimpleDateFormat format=new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy", DateFormatSymbols.getInstance(Locale.US));
			SimpleDateFormat format2=new SimpleDateFormat("MM-dd HH:mm");
			
			try {
				JSONObject jsonObject=new JSONObject(jsonStr);
				comment=new Comment();
				Date date = null;
					date = format.parse(jsonObject.getString("created_at"));
					comment.setTime(format2.format(date));
				String[] sourceArray=jsonObject.getString("source").split(">");
				String[] source=sourceArray[1].split("<");
				comment.setSource(source[0].trim());
				comment.setText(jsonObject.getString("text"));
				comment.setComment_id(jsonObject.getString("mid"));
			} catch (JSONException e) {
				return new Result<Comment>(null, Result.JSON_PARSE_ERROR);
			} catch (ParseException e) {
                return new Result<Comment>(null, Result.JSON_PARSE_ERROR);
			}
		}
        return new Result<Comment>(comment, Result.JSON_PARSE_ERROR);
	}
	
	public WeiboStatus postRepost(String text, String id, int is_comment)
	{
		WeiboStatus status = null;
		if(NetworkUtil.getInstance(context).getNetworkType() == -1){
			return status;
		}else{
			 HashMap<String, String> params = Utils.getParamMap(context);
			 params.put("status", String.valueOf(text));
	         params.put("id", String.valueOf(id));
	         params.put("is_comment", String.valueOf(is_comment));
		        
			String jsonStr = downloader.getJsonContent(Constants.URL_POST_REPOST, params, HttpRequest.METHOD_POST);
			if(StringUtil.isNullOrEmpty(jsonStr)){
				return status;
			}
			
			try {
				JSONObject jsonObject=new JSONObject(jsonStr);
				status=new WeiboStatus();
				status.setComment_count(jsonObject.getInt("comments_count"));
				status.setRepost_count(jsonObject.getInt("reposts_count"));
			} catch (JSONException e) {
				return status;
			} 
		}
		return status;
	}
	
	/**
	 * 回复评论
	 * @param cid
	 * @param id
	 * @param comment
	 * @return
	 */
	public Integer postReply(String cid, String id, String comment)
	{
		int result=0;
		if(NetworkUtil.getInstance(context).getNetworkType() == -1){
			return result;
		}else{
			 HashMap<String, String> params = Utils.getParamMap(context);
			 params.put("cid", String.valueOf(cid));
	         params.put("id", String.valueOf(id));
	         params.put("comment", String.valueOf(comment));
		        
			String jsonStr = downloader.getJsonContent(Constants.URL_COMMENTS_REPLY, params, HttpRequest.METHOD_POST);
			if(StringUtil.isNullOrEmpty(jsonStr)){
				return result;
			}
			
			try {
				JSONObject jsonObject=new JSONObject(jsonStr);
				if (jsonObject.has("mid")) {
					result=1;
				}
			} catch (JSONException e) {
				return result;
			} 
		}
		return result;
	}
	
	/**
	 * 
	 * @user qiaorongzhu
	 * @since 2014-10-31
	 * @param since_id
	 * @param count
	 * @param page
	 * @param filter_by_author
	 * @return
	 */
	public List<Comment> getMyCommentList(String url, int since_id, int count, int page, Integer filter_by_author)
	{
		List<Comment> commentList = null;
		if(NetworkUtil.getInstance(context).getNetworkType() == -1){
			return commentList;
		}else{
			 HashMap<String, String> params = Utils.getParamMap(context);
			 params.put("since_id", String.valueOf(since_id));
	         params.put("count", String.valueOf(count));
	         params.put("page", String.valueOf(page));
	         if (filter_by_author!=null) {
	        	 params.put("filter_by_author", String.valueOf(filter_by_author));
			}
			String jsonStr = downloader.getJsonContent(url, params, HttpRequest.METHOD_GET);
			if(StringUtil.isNullOrEmpty(jsonStr)){
				return commentList;
			}
			
			try {
				JSONObject jsonObject=new JSONObject(jsonStr);
				JSONArray commentsArray=jsonObject.getJSONArray("comments");
				commentList=new ArrayList<Comment>();
				for (int i = 0; i < commentsArray.length(); i++) {
					JSONObject object=commentsArray.getJSONObject(i);
					Comment comment=JSONParseUtils.parseCommentJSONObject(object);
					comment.setUser(JSONParseUtils.parseUserJSONObject(object.getJSONObject("user")));
					if (object.has("status")) {
						comment.setStatus(JSONParseUtils.parseStatusJSONObject(object.getJSONObject("status")));
					}
					if (object.has("reply_comment")) {
						comment.setReplyComment(JSONParseUtils.parseCommentJSONObject(object.getJSONObject("reply_comment")));
					}
					commentList.add(comment);
				}
			} catch (JSONException e) {
				return commentList;
			} 
		}
		return commentList;
	}
	
}
