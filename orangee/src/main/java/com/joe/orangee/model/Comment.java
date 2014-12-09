package com.joe.orangee.model;

public class Comment {

	private Comment replyComment; 
	private User user;
	private WeiboStatus status;
	private String comment_id;
	private String avatar;
	private String name;
	private String time;
	private String source;
	private String text;
	public Comment getReplyComment() {
		return replyComment;
	}
	public void setReplyComment(Comment replyComment) {
		this.replyComment = replyComment;
	}
	public WeiboStatus getStatus() {
		return status;
	}
	public void setStatus(WeiboStatus status) {
		this.status = status;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getComment_id() {
		return comment_id;
	}
	public void setComment_id(String comment_id) {
		this.comment_id = comment_id;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
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
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}
