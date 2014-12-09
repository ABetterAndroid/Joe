package com.joe.orangee.model;

import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;

public class WeiboStatus implements Parcelable {

	private User user;
	private String avatar;
	private String name;
	private String uid;
	private String time;
	private String source;
	private String postText;
	private ArrayList<String> picList;
	private String weiboId;
	private WeiboStatus weiboStatus;
	private int comment_count;
	private int repost_count;
	private String location;
	private double latitude;
	private double longitude;
	private long since_id;
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public long getSince_id() {
		return since_id;
	}
	public void setSince_id(long since_id) {
		this.since_id = since_id;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public int getComment_count() {
		return comment_count;
	}
	public void setComment_count(int comment_count) {
		this.comment_count = comment_count;
	}
	public int getRepost_count() {
		return repost_count;
	}
	public void setRepost_count(int repost_count) {
		this.repost_count = repost_count;
	}
	public String getWeiboId() {
		return weiboId;
	}
	public void setWeiboId(String weiboId) {
		this.weiboId = weiboId;
	}
	public WeiboStatus getWeiboStatus() {
		return weiboStatus;
	}
	public void setWeiboStatus(WeiboStatus weiboStatus) {
		this.weiboStatus = weiboStatus;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public ArrayList<String> getPicList() {
		return picList;
	}
	public void setPicList(ArrayList<String> picList) {
		this.picList = picList;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getPostText() {
		return postText;
	}
	public void setPostText(String postText) {
		this.postText = postText;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeParcelable(user, flags);
		dest.writeString(avatar);
		dest.writeString(name);
		dest.writeString(uid);
		dest.writeString(time);
		dest.writeString(source);
		dest.writeString(postText);
		dest.writeList(picList);
		dest.writeString(weiboId);
		dest.writeParcelable(weiboStatus, flags);
		dest.writeInt(comment_count);
		dest.writeInt(repost_count);
		dest.writeString(location);
		dest.writeLong(since_id);
		
	}
	
	 @SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Creator() {  
		   
	        @Override  
	        public WeiboStatus createFromParcel(Parcel source) {  
	   
	            // TODO Auto-generated method stub  
	        	WeiboStatus status = new WeiboStatus();  
	        	status.user=source.readParcelable(User.class.getClassLoader());
	        	status.avatar=source.readString();
	        	status.name=source.readString();
	        	status.uid=source.readString();
	        	status.time=source.readString();
	        	status.source=source.readString();
	        	status.postText=source.readString();
	        	status.picList=new ArrayList<String>();
	        	source.readList(status.picList, WeiboStatus.class.getClassLoader());
	        	status.weiboId=source.readString();
	        	status.weiboStatus=source.readParcelable(WeiboStatus.class.getClassLoader());
	        	status.comment_count=source.readInt();
	        	status.repost_count=source.readInt();
	        	status.location=source.readString();
	        	status.since_id=source.readLong();
	            return status;  
	   
	        }  
	   
	        @Override  
	        public WeiboStatus[] newArray(int size) {  
	   
	            // TODO Auto-generated method stub  
	            return new WeiboStatus[size];  
	   
	        }   
	   
	    };
	
}
