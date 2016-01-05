/*
 * Created on May 3, 2005
 *
 */
package com.anacleto.sched;

import java.util.Collection;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.anacleto.index.BookIndexQueue;

/**
 * @author robi
 *
 */
public class ScheduledIndexer implements Job{

		/**
	    * This method is called by the scheduler.
	    * The contect shoud contain the collectionType and URL
	    */
		public void execute(JobExecutionContext context) throws JobExecutionException {
	       
			JobDataMap dataMap = context.getJobDetail().getJobDataMap();
	       
	       	Collection bookColl = (Collection)dataMap.get("bookColl");
	       	BookIndexQueue.addBookToIndex(bookColl);
		}
	}

