package com.larrylivingston.mews.objects;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.larrylivingston.mews.MewsConstants;

public class Comment implements Serializable {
	private static final long serialVersionUID = 2L;
	private int id;
	private String author;
	private String content;
	private Date date;
	private int upvote;
	private int downvote;
	private String authorId;

	/*
	 * setter methods
	 */

	public void setId(int id) {
		this.id = id;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setDate(String pubdate) {
		try {
			DateFormat formatter = new SimpleDateFormat(
					"EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH);
			Date date = formatter.parse(pubdate);
			this.date = date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setVote(int upvote, int downvote) {
		this.upvote = upvote;
		this.downvote = downvote;
	}

	/*
	 * getter methods
	 */
	public int getId() {
		return id;
	}

	public String getAuthor() {
		return author;
	}

	public Date getDate() {
		return date;
	}

	public String getContent() {
		return content;
	}

	public int getUpvote() {
		return upvote;
	}

	public int getDownvote() {
		return downvote;
	}

	public String getAuthorId() {
		return authorId;
	}
	public String getAuthorPic() {
		return  MewsConstants.USER_IMAGE_URL+authorId;
	}

}
