package com.notification.domain;

import java.util.List;

import com.notification.database.QueryExecutors;

public class Subscribers {

	private int subscriber_id;
	private String subscriber_emailid; 
	private String subscriber_phone;
	
	public int getSubscriber_id() {
		return subscriber_id;
	}
	public void setSubscriber_id(int subscriber_id) {
		this.subscriber_id = subscriber_id;
	}
	public String getSubscriber_emailid() {
		return subscriber_emailid;
	}
	public void setSubscriber_emailid(String subscriber_emailid) {
		this.subscriber_emailid = subscriber_emailid;
	}
	public String getSubscriber_phone() {
		return subscriber_phone;
	}
	public void setSubscriber_phone(String subscriber_phone) {
		this.subscriber_phone = subscriber_phone;
	}
	
	public static List<Subscribers> getSubscribers(int bookId) {
		return QueryExecutors.getInstance().getSubscribers(bookId);
	}
}
