package com.notification.util;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Util {

	public static boolean checkConditionInteger(double bookValue, double valueToBeChecked, String operator) {
		boolean result = false;
		switch(operator) {
			case ">":
				result = bookValue > valueToBeChecked;
				break;
			case "<":
				result = bookValue < valueToBeChecked;
				break;
			case "<=":
				result = bookValue <= valueToBeChecked;
				break;
			case ">=":
				result = bookValue >= valueToBeChecked;
				break;
			case "==":
				result = bookValue == valueToBeChecked;
				break;
			default: 
				result = false;
				break;
		}
		return result;
	}
	
	public static boolean checkConditionString(String bookValue, String valueToBeChecked, String operator) {
		boolean result = false;
		switch(operator) {
			case "==":
				result = bookValue.equals(valueToBeChecked);
				break;
			case "!=":
				result = !bookValue.equals(valueToBeChecked);
				break;
			default: 
				result = false;
				break;
		}
		return result;
	}
	
	public static boolean checkConditionDate(long bookValue, String valueToBeChecked, String operator) {
		long valueChecked = Long.parseLong(valueToBeChecked);
		boolean result = false;
		switch(operator) {
			case ">":
				result = bookValue>valueChecked;
				break;
			case "<":
				result = bookValue<valueChecked;
				break;
			case "==":
				result = bookValue==valueChecked;
				break;
			default: 
				result = false;
				break;
		}
		System.out.println("Util.checkConditionDate()"+result);
		return result;
	}
	
	public static java.sql.Date dateParser(String date) {
		java.sql.Date sqlDate = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
			java.util.Date parsed = format.parse(date);
			sqlDate = new java.sql.Date(parsed.getTime());
		} catch(ParseException e) {
			System.out.println("Date format is not correct");
		}
		return sqlDate;
	}
	
	public static Long convertDateToLong(String date) {
		Long formattedValue = 0L;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			formattedValue = format.parse(date).getTime();
		} catch(ParseException e) {
			System.out.println("Date format is not correct");
		}
		return formattedValue;
	}
	
}
