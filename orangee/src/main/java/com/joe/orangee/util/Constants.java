package com.joe.orangee.util;

public class Constants {

	public static final String TABLE_NAME_FRIENDS_TIMELINE="friends_timeline";
	public static float DENSITY;
	public static final String PERSON_NAME = "person_name";
	public static final String PERSON_UID = "person_uid";
	public static final String URL = "url";
	
	public static final String APP_KEY = "470980811";
	public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
	public static final String SCOPE ="direct_messages_write"; 
	/*"email,direct_messages_read,direct_messages_write,"
	+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
	+ "follow_app_official_microblog," + "invitation_write";*/
	
	public static final String API_SERVER="https://api.weibo.com/";
	public static final String URL_FRIENDS_TIMELINE=API_SERVER+"2/statuses/friends_timeline.json";
	public static final String URL_COMMENT_SHOW=API_SERVER+"2/comments/show.json";
	public static final String URL_POST_COMMENT=API_SERVER+"2/comments/create.json";
	public static final String URL_POST_REPOST=API_SERVER+"2/statuses/repost.json";
	public static final String URL_FAVOURITES=API_SERVER+"2/favorites.json";
	public static final String URL_PERSON_INFO=API_SERVER+"2/users/show.json";
	public static final String URL_PERSON_STATUSES=API_SERVER+"2/statuses/user_timeline.json";
	public static final String URL_STATUSES_BATCH=API_SERVER+"2/statuses/timeline_batch.json";
	public static final String URL_FRIENDSHIP_CREATE=API_SERVER+"2/friendships/create.json";
	public static final String URL_FRIENDSHIP_DESTROY=API_SERVER+"2/friendships/destroy.json";
	public static final String URL_COMMENTS_TO_ME=API_SERVER+"2/comments/to_me.json";
	public static final String URL_COMMENTS_BY_ME=API_SERVER+"2/comments/by_me.json";
	public static final String URL_COMMENTS_REPLY=API_SERVER+"2/comments/reply.json";
	public static final String URL_STATUS_NEARBY=API_SERVER+"2/place/nearby_timeline.json";
	public static final String URL_STATUS_MENTION=API_SERVER+"2/statuses/mentions.json";
	public static final String URL_COMMENT_MENTION=API_SERVER+"2/comments/mentions.json";
	public static final String URL_UPLOAD_PICTURE=API_SERVER+"2/statuses/upload_pic.json";
	public static final String URL_UNREAD_MESSAGE="https://rm.api.weibo.com/2/remind/unread_count.json";
	public static final String URL_UPDATE_STATUS_WITH_ONE_PICTURE=API_SERVER+"2/statuses/upload.json";
	public static final String URL_UPDATE_STATUS=API_SERVER+"2/statuses/update.json";
	public static final String URL_LOGOUT=API_SERVER+"2/account/end_session.json";
	
}
