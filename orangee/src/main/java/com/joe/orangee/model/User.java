package com.joe.orangee.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User  implements Parcelable{

	private String uid;
	private String name;
	private String location;
	private String description;
	private String avatar;
	private String cover;
	private String gender;
	private int friends_count;
	private int followers_count;
	private int statuses_count;
	private boolean following;
	private boolean verified;
	private String verified_reason;
	private boolean follow_me;
	public int getFriends_count() {
		return friends_count;
	}
	public void setFriends_count(int friends_count) {
		this.friends_count = friends_count;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getFollowers_count() {
		return followers_count;
	}
	public void setFollowers_count(int followers_count) {
		this.followers_count = followers_count;
	}
	public int getStatuses_count() {
		return statuses_count;
	}
	public void setStatuses_count(int statuses_count) {
		this.statuses_count = statuses_count;
	}
	public boolean isFollowing() {
		return following;
	}
	public void setFollowing(boolean following) {
		this.following = following;
	}
	public boolean isVerified() {
		return verified;
	}
	public void setVerified(boolean verified) {
		this.verified = verified;
	}
	public String getVerified_reason() {
		return verified_reason;
	}
	public void setVerified_reason(String verified_reason) {
		this.verified_reason = verified_reason;
	}
	public boolean isFollow_me() {
		return follow_me;
	}
	public void setFollow_me(boolean follow_me) {
		this.follow_me = follow_me;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeString(uid);
		dest.writeString(name);
		dest.writeString(location);
		dest.writeString(description);
		dest.writeString(avatar);
		dest.writeString(cover);
		dest.writeString(gender);
		dest.writeInt(friends_count);
		dest.writeInt(followers_count);
		dest.writeInt(statuses_count);
		dest.writeBooleanArray(new boolean[]{following});
		dest.writeBooleanArray(new boolean[]{verified});
		dest.writeString(verified_reason);
		dest.writeBooleanArray(new boolean[]{follow_me});
		
	}
	
	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Creator() {  
		   
	        @Override  
	        public User createFromParcel(Parcel source) {  
	            // TODO Auto-generated method stub  
	        	User user = new User();  
	        	user.uid=source.readString();
	        	user.name=source.readString();
	        	user.location=source.readString();
	        	user.description=source.readString();
	        	user.avatar=source.readString();
	        	user.cover=source.readString();
	        	user.gender=source.readString();
	        	user.friends_count=source.readInt();
	        	user.followers_count=source.readInt();
	        	user.statuses_count=source.readInt();
	        	boolean[] followingArray=new boolean[1];
	        	source.readBooleanArray(followingArray);
	        	user.following=followingArray[0];
	        	boolean[] verifiedArray=new boolean[1];
	        	source.readBooleanArray(verifiedArray);
	        	user.verified=verifiedArray[0];
	        	user.verified_reason=source.readString();
	        	boolean[] follow_meArray=new boolean[1];
	        	source.readBooleanArray(follow_meArray);
	        	user.verified=follow_meArray[0];
	            return user;  
	   
	        }  
	   
	        @Override  
	        public User[] newArray(int size) {  
	   
	            // TODO Auto-generated method stub  
	            return new User[size];  
	   
	        }   
	   
	    };
	
}
