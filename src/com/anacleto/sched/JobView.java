/*
 * Created on May 3, 2005
 *
 */
package com.anacleto.sched;

import java.util.Date;

/**
 * Display scheduled job related fields
 * @author robi
 *
 */
public class JobView {

	private String    book;
	private Date      nextFireTime;
	private String    cronExpression;
	
	/**
	 * @return Returns the book.
	 */
	public String getBook() {
		return book;
	}
	/**
	 * @param book The book to set.
	 */
	public void setBook(String book) {
		this.book = book;
	}
	/**
	 * @return Returns the nextFireTime.
	 */
	public Date getNextFireTime() {
		return nextFireTime;
	}
	/**
	 * @param nextFireTime The nextFireTime to set.
	 */
	public void setNextFireTime(Date nextFireTime) {
		this.nextFireTime = nextFireTime;
	}


	/**
	 * @return Returns the cronExpression.
	 */
	public String getCronExpression() {
		return cronExpression;
	}
	/**
	 * @param cronExpression The cronExpression to set.
	 */
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
}
