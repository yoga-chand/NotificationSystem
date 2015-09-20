package com.notification.domain;

import java.util.List;

public class Conditions {

	private int condition_id;
	private String attribute; 
	private String operator;
	
	public int getCondition_id() {
		return condition_id;
	}
	public void setCondition_id(int condition_id) {
		this.condition_id = condition_id;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public static List<Conditions> getConditions(int subscriber_Id, int book_id) {
		
		return null;
	}
}
