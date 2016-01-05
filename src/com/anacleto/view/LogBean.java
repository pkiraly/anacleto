package com.anacleto.view;

public class LogBean {
	
	String eventTime;
	
	String eventClass;
	
	String eventMessage;

	String eventType;

	public LogBean(String time, String className, String message, String type) {
		eventClass = className;
		eventMessage = message;
		eventTime = time;
		eventType = type;
	}

	public String getEventClass() {
		return eventClass;
	}

	public void setEventClass(String eventClass) {
		this.eventClass = eventClass;
	}

	public String getEventMessage() {
		return eventMessage;
	}

	public void setEventMessage(String eventMessage) {
		this.eventMessage = eventMessage;
	}

	public String getEventTime() {
		return eventTime;
	}

	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}
	
	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}


	
	

}
