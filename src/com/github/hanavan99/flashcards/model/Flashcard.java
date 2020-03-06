package com.github.hanavan99.flashcards.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Flashcard {

	private UUID id;
	private String front;
	private String back;
	private List<Tag> tags;
	private Date lastViewed;
	private Date newViewDate;
	private int viewCount;

	public Flashcard(UUID id, String front, String back, List<Tag> tags, Date lastViewed, Date newViewDate, int viewCount) {
		this.id = id;
		this.front = front;
		this.back = back;
		this.tags = tags;
		this.lastViewed = lastViewed;
		this.newViewDate = newViewDate;
		this.viewCount = viewCount;
	}

	public UUID getID() {
		return id;
	}

	public void setID(UUID id) {
		this.id = id;
	}

	public String getFront() {
		return front;
	}

	public void setFront(String front) {
		this.front = front;
	}

	public String getBack() {
		return back;
	}

	public void setBack(String back) {
		this.back = back;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public Date getLastViewed() {
		return lastViewed;
	}

	public void setLastViewed(Date lastViewed) {
		this.lastViewed = lastViewed;
	}

	public Date getNewViewDate() {
		return newViewDate;
	}

	public void setNewViewDate(Date newViewDate) {
		this.newViewDate = newViewDate;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

}
