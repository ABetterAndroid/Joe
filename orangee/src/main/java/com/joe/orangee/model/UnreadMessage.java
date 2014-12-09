package com.joe.orangee.model;

public class UnreadMessage {

	private int status;
	private int follower;
	private int comment;
	private int letter;
	private int mention_status;
	private int metion_comment;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getFollower() {
		return follower;
	}
	public void setFollower(int follower) {
		this.follower = follower;
	}
	public int getComment() {
		return comment;
	}
	public void setComment(int comment) {
		this.comment = comment;
	}
	public int getLetter() {
		return letter;
	}
	public void setLetter(int letter) {
		this.letter = letter;
	}
	public int getMention_status() {
		return mention_status;
	}
	public void setMention_status(int mention_status) {
		this.mention_status = mention_status;
	}
	public int getMetion_comment() {
		return metion_comment;
	}
	public void setMetion_comment(int metion_comment) {
		this.metion_comment = metion_comment;
	}
	
}
