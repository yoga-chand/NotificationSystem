package com.notification.domain;

public class Subscriber_Condition {

	private int id;
	private int subscriber_id;
	private int condition_id;
	private String valueToBeCompared;
	private int book_id;
	
	public int getBook_id() {
		return book_id;
	}
	
	public void setBook_id(int book_id) {
		this.book_id = book_id;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSubscriber_id() {
		return subscriber_id;
	}
	public void setSubscriber_id(int subscriber_id) {
		this.subscriber_id = subscriber_id;
	}
	public int getCondition_id() {
		return condition_id;
	}
	public void setCondition_id(int condition_id) {
		this.condition_id = condition_id;
	}
	
	public String getValueToBeCompared() {
		return valueToBeCompared;
	}
	
	public void setValueToBeCompared(String valueToBeCompared) {
		this.valueToBeCompared = valueToBeCompared;
	}
}
