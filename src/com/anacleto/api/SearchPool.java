package com.anacleto.api;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.queryParser.ParseException;
import org.jboss.util.threadpool.BasicThreadPool;
import org.jboss.util.threadpool.Task;

import EDU.oswego.cs.dl.util.concurrent.TimeoutException;

public class SearchPool {
	
	private static int POOL_MIN_SIZE = 5;
	private static int POOL_MAX_SIZE = 10;
	
	private static int JOB_TIMEOUT_SECS = 5;
	
	BasicThreadPool tp = new BasicThreadPool("searchPool");
	
	public SearchPool() {
		tp.setMinimumPoolSize(POOL_MIN_SIZE);
		tp.setMaximumPoolSize(POOL_MAX_SIZE);
		//tp.setBlockingMode(BlockingMode.WAIT);
		
	}
	
	
	public void shutdown(){
		tp.stop(false);
	}
	

	public void executeQuery(String query, int offset, int length,
			QueryStats stats, List queryResults) throws IOException,
			ParseException {
//		Runnable task = new SearchTask(query, offset, length, 
//										stats, queryResults);
		Task task = new SearchTask(query, offset, length,
				stats, queryResults,
				JOB_TIMEOUT_SECS);
		tp.runTask(task);
		System.out.println("Poolsize:" + tp.getPoolSize());
		
		Task task2 = new SearchTask(query, offset, length,
				stats, queryResults,
				JOB_TIMEOUT_SECS);

		tp.runTask(task2);
		System.out.println("Poolsize:" + tp.getPoolSize());

		Task task3  = new SearchTask(query, offset, length,
				stats, queryResults,
				JOB_TIMEOUT_SECS);

		tp.runTask(task3);
		System.out.println("Poolsize:" + tp.getPoolSize());
		
		Task task4  = new SearchTask(query, offset, length,
				stats, queryResults,
				JOB_TIMEOUT_SECS);

		tp.runTask(task4);
		System.out.println("Poolsize:" + tp.getPoolSize());
		
		Task task5  = new SearchTask(query, offset, length,
				stats, queryResults,
				JOB_TIMEOUT_SECS);

		tp.runTask(task5);
		System.out.println("Poolsize:" + tp.getPoolSize());
		
		Task task6  = new SearchTask(query, offset, length,
				stats, queryResults,
				JOB_TIMEOUT_SECS);

		tp.runTask(task6);
		System.out.println("Poolsize:" + tp.getPoolSize());

	}
	
	public static void main(String[] args) {

		SearchPool sp = new SearchPool();
		try {
			sp.executeQuery("", 0, 10, new QueryStats(), new LinkedList());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class  SearchTask implements Task{

	private long accepedAt;
	private int timeout;
	String query;
	int offset;
	int length;
	
	QueryStats stats;
	List queryResults;
	
	Exception e;
	
	public SearchTask(String query, int offset, int length,
			QueryStats stats, List queryResults, int timeoutInSecs) {
		this.query = query;
		this.offset = offset;
		this.length = length;
		
		this.stats = stats;
		this.queryResults = queryResults;		
		this.timeout = timeoutInSecs * 1000 ;
	}
	
	public void accepted(long arg0) {
		accepedAt = System.currentTimeMillis();
		System.out.println("Accepted at:" + accepedAt);
		
	}

	public void completed(long arg0, Throwable t) {
		System.out.println("Completed at:" + 
				(System.currentTimeMillis() - accepedAt)
				+ t.getMessage());
		
	}

	public void execute() {
		try {
			Facade.executeQuery(query, offset, length, stats, queryResults);
		} catch (IOException e) {
			this.e = e;
		} catch (ParseException e) {
			this.e = e;
		}
		
	}

	public long getCompletionTimeout() {
		System.out.println("completion timeout req");
		return timeout;
	}

	public int getPriority() {
		return 5;
	}

	public long getStartTimeout() {
		return Long.MAX_VALUE;
	}

	public int getWaitType() {
		return Task.WAIT_NONE;
	}

	public void rejected(long time, Throwable t) {
		System.out.println("Rejected:" + time + 
				"::" +  t.getLocalizedMessage());
		
	}

	public void started(long arg0) {
		System.out.println("Started at: " + arg0);
		
	}

	public void stop() {
		System.out.println("Stopped at: "
				+ (System.currentTimeMillis() - accepedAt));
		e = new TimeoutException(timeout);
	}

	public SearchTask() {
		super();
	}
	
}
class  TestTask implements Runnable{

	
	protected void finalize() throws Throwable {
		super.finalize();
		System.out.println("finalize");
	}

	public void run() {
		System.out.println("Start");
		while (true) {
			;
		}
		
		
	}
}

class SearchTask2 implements Runnable{

	String query;
	int offset;
	int length;
	
	QueryStats stats;
	List queryResults;
	
	Exception e;
	
	public SearchTask2(String query, int offset, int length,
			QueryStats stats, List queryResults) {
		this.query = query;
		this.offset = offset;
		this.length = length;
		
		this.stats = stats;
		this.queryResults = queryResults;
		
	}
	
	public void run() {
		try {
			Facade.executeQuery(query, offset, length, stats, queryResults);
		} catch (IOException e) {
			this.e = e;
		} catch (ParseException e) {
			this.e = e;
		}
		
	}

	
}
