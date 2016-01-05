/*
 * Created on Mar 23, 2005
 *
 */
package com.anacleto.sched;

import java.text.ParseException;
import java.util.*;
import java.util.Calendar;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import com.anacleto.base.ConfigurationException;

/**
 * This class is scheduled by the quartz scheduling engine to do do automatic indexing
 * Automatic indexing should be used when a content collection changes often
 * 
 * @author robi
 */
public class Scheduled {

	private final static  String SIMPLEJOB = "SIMPLEJOB";
	private final static  String CRONJOB   = "CRONJOB";
	
    public static void addCronIndexing(String bookName, 
    		String cronExpression) throws ConfigurationException {
        try {
            Scheduler sched = StdSchedulerFactory.getDefaultScheduler();
            if (sched.isShutdown())
                sched.start();

            //delete job if it's registred
            String jobs[] = sched.getJobNames(CRONJOB);
            for (int i = 0; i < jobs.length; i++) {
				String job = jobs[i];
				if (job.equals(bookName))
					sched.deleteJob(bookName,CRONJOB);
			}
            
            JobDetail jobDetail = new JobDetail(bookName,
            		CRONJOB, ScheduledIndexer.class);

            //set the bookColl attribute:
            Collection bookColl = new LinkedList();
            bookColl.add(bookName);
            jobDetail.getJobDataMap().put("bookColl", bookColl);
            
            CronTrigger trigger = new CronTrigger(bookName, CRONJOB);
            trigger.setCronExpression(cronExpression);
            sched.scheduleJob(jobDetail, trigger);
            
        } catch (SchedulerException e) {
            throw new ConfigurationException("Scheduler error", e);
        } catch (ParseException e) {
            throw new ConfigurationException("CronExpression: " + cronExpression
                    + " is not well formatted ", e);
        }
    }
    
    public static void addSimpleIndexing(Collection bookColl, 
    		Calendar cal) throws ConfigurationException {
        try {
            Scheduler sched = StdSchedulerFactory.getDefaultScheduler();

            if (sched.isShutdown())
                sched.start();

            JobDetail jobDetail = new JobDetail(cal.toString(),
                    SIMPLEJOB, ScheduledIndexer.class);

            jobDetail.getJobDataMap().put("bookColl", bookColl);
            
            Date startTime = cal.getTime();
            SimpleTrigger trigger = new SimpleTrigger(cal.toString(),
            		SIMPLEJOB, startTime, null, 0, 0L);
            
            sched.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            throw new ConfigurationException("Scheduler error", e);
        }
    }
    
    public static Collection getSimpleJobEntries() throws ConfigurationException{
    	return getJobEntries(SIMPLEJOB);
    }
    
    public static Collection getCronJobEntries() throws ConfigurationException{
    	return getJobEntries(CRONJOB);
    }

    private static Collection getJobEntries(String jobGrp) throws ConfigurationException{
    	
    	try {
	    	Collection retColl = new LinkedList();
	    	
		    Scheduler sched = StdSchedulerFactory.getDefaultScheduler();
		    
		    String triggers[] = sched.getTriggerNames(jobGrp);
		    for (int i = 0; i < triggers.length; i++) {
				Trigger trigger = sched.getTrigger(triggers[i], jobGrp);
				
				JobDetail jd = sched.getJobDetail(trigger.getJobName(),
												  trigger.getJobGroup());
				
				JobDataMap map = jd.getJobDataMap();
				
				Collection schedBooks = (Collection)map.get("bookColl");
				Iterator iter = schedBooks.iterator();
				while (iter.hasNext()) {
					JobView  jobView = new JobView();
					jobView.setNextFireTime(trigger.getNextFireTime());
					jobView.setBook((String) iter.next());

					if (trigger instanceof CronTrigger){
						CronTrigger tr = (CronTrigger)trigger;
						jobView.setCronExpression(tr.getCronExpression());
						
					}
					retColl.add(jobView);
				}
			}
		    return retColl;
    	} catch (SchedulerException e){
    		throw new ConfigurationException("Scheduler error", e);
    	}
    }
}


