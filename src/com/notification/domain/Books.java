package com.notification.domain;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import com.notification.util.Util;

public class Books {

	private int id;
	private String title;
	private long release_date;
	private float price;
	private String publisher;
	private String authors;
	private long title_modified_date ;
	private long release_modified_date ;
	private long price_modified_date ;
	private long authors_modified_date ;
	private long publisher_modified_date ;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public long getRelease_date() {
		return release_date;
	}
	public void setRelease_date(long release_date) {
		this.release_date = release_date;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getAuthors() {
		return authors;
	}
	public void setAuthors(String authors) {
			this.authors = authors;
	}
	
	public long getTitle_modified_date() {
		return title_modified_date;
	}
	public void setTitle_modified_date(long title_modified_date) {
		this.title_modified_date = title_modified_date;
	}
	public long getRelease_modified_date() {
		return release_modified_date;
	}
	public void setRelease_modified_date(long release_modified_date) {
		this.release_modified_date = release_modified_date;
	}
	public long getPrice_modified_date() {
		return price_modified_date;
	}
	public void setPrice_modified_date(long price_modified_date) {
		this.price_modified_date = price_modified_date;
	}
	public long getAuthors_modified_date() {
		return authors_modified_date;
	}
	public void setAuthors_modified_date(long authors_modified_date) {
		this.authors_modified_date = authors_modified_date;
	}
	public long getPublisher_modified_date() {
		return publisher_modified_date;
	}
	public void setPublisher_modified_date(long publisher_modified_date) {
		this.publisher_modified_date = publisher_modified_date;
	}
	public static List<String> checkCondition(Books book, String attribute, String operator, String valueToBeChecked) {
		List <String> resultList = new LinkedList<String>();
		if(attribute.equals("price")) {
			int value = Integer.parseInt(valueToBeChecked);
			boolean result = Util.checkConditionInteger(book.getPrice(), value, operator);
			if(result) {
				resultList.add("Hi We thought you would know the price of "+book.getTitle() + " by "+book.getAuthors()+" is "+book.getPrice());
			}
		} else if(attribute.equals("publisher")) {
			boolean result = Util.checkConditionString(book.getPublisher(), valueToBeChecked, operator);
			if(result) {
				resultList.add("Hi We thought you would know the price of "+book.getTitle() + " by "+book.getAuthors()+"'s publisher is "+book.getPublisher());
			}
		} else if(attribute.equals("authors")) {
			boolean result = Util.checkConditionString(book.getAuthors(), valueToBeChecked, operator);
			if(result) {
				resultList.add("Hi The author of the book "+book.getTitle()+" is "+book.getAuthors());
			}
		} else if(attribute.equals("release_date")) {
			boolean result = Util.checkConditionDate(book.getRelease_date(), valueToBeChecked, operator);
			if(result) {
				resultList.add("Hi We are publishing the book "+book.getTitle()+" is releasing on "+book.getRelease_date());
			}
		}
		return resultList;
	}
	
	@Override
	public boolean equals(Object obj) {
		Books b = (Books) obj;
		return this.getId() == b.getId(); 
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Book Id - "+this.getId()+" || "+"price - "+this.getPrice()+"author "+this.getAuthors()+"publisher "+this.getPublisher();
	}
}
