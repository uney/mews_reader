package com.larrylivingston.mews.objects;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.larrylivingston.mews.MewsConstants;

public class Mews implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String title;
	private String author;
	private Date date;
	private String content;
	private double  lat;
	private double  lng;
	private String link = null;
	private int viewCount;
	
	private String address;
	private String category;
	private int upvote;
	private int downvote;
	
	private String authorId;
	private String thumbnail;
	private String pic1;
	private String pic2;
	private String pic3;

	/*
	 * setter methods
	 */

	public void setId(int id){
		this.id=id;
	}	
	public void setLink(String link){
		this.link=link;
	}
	public void setTitle(String title){
		this.title=title;
	}
	public void setAuthor(String author){
		this.author=author;
	}
	public void setDate(String pubdate) {
		try {
			DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH);
			Date date = formatter.parse(pubdate);
			this.date = date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	public void setContent(String content){
		this.content=content;
	}
	public void setLatitude(double latitude){
		lat=latitude;
	}
	public void setLongitude(double longitude){
		lng=longitude;
	}
	public void setViewCount(int viewCount){
		this.viewCount=viewCount;
	}
	public void setAddress(String address){
		this.address=address;
	}
	public void setCategory(String category){
		this.category=category;
	}
	public void setVote(int upvote, int downvote){
		this.upvote=upvote;
		this.downvote=downvote;		
	}
	
	public void setAuthorId(String authorId){
		this.authorId=authorId;
	}
	public void setThumbnail(String thumbnail){
		this.thumbnail=thumbnail;
	}
	public void setPic1(String pic1){
		this.pic1=pic1;
	}
	public void setPic2(String pic2){
		this.pic2=pic2;
	}
	public void setPic3(String pic3){
		this.pic3=pic3;
	}
	/*
	 * getter methods
	 */
	public int getId(){
		return id;
	}
	public String getLink(){
		return link;
	}
	public String getTitle(){
		return title;
	}
	public String getAuthor(){
		return author;
	}
	public Date getDate() {
		return date;
	}
	public String getContent(){
		return content;
	}
	public double  getLatitude(){
		return lat;
	}
	public double  getLongitude(){
		return lng;
	}
	public int getViewCount(){
		return viewCount;
	}
	public String getCategory(){
		return category;
	}
	public String getAddress(){
		return address;
	}
	public int getUpvote(){
		return upvote;
	}
	public int getDownvote(){
		return downvote;
	}
	
	
	public String getAuthorId(){
		return authorId;
	}
	public String getAuthorPic(){
		return MewsConstants.USER_IMAGE_URL+authorId;
	}
	public String getThumbnail(){
		return MewsConstants.NEWS_IMAGE_URL+thumbnail;
	}
	public String getPic1(){
		return MewsConstants.NEWS_IMAGE_URL+pic1;
	}
	public String getPic2(){
		return MewsConstants.NEWS_IMAGE_URL+pic2;
	}
	public String getPic3(){
		return MewsConstants.NEWS_IMAGE_URL+pic3;
	}
	
}
