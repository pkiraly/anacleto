package com.anacleto.base;

import java.io.Serializable;

import org.apache.log4j.Logger;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class GlobalCache {
	
	private static Logger logger = Logging.getAdminLogger();
	
	public static final String FastDocumentCache = "fastDocument";
	
	public static final String HitCollectorCache = "hitCollectorCache";
	
	public static final String FilteredDocTreeCache = "filteredDocTreeCache";
	
	private Cache  cache;  
	/**
	 * Start cacheing services
	 *
	 */
	synchronized public static void startUp(){
		
		logger.info("Start caching services");
		
		try {
			CacheManager manager = CacheManager.getInstance();
			 //Create a Cache specifying its configuration.
			Cache fastDocumentCache = new Cache(
					GlobalCache.FastDocumentCache, 
					100000,   //MAX size
					false, 	  //overfolow to disk
					false,     //eternal
					900, 	  //time to live
					300);	  //time to idle
			manager.addCache(fastDocumentCache);
			/*
			Cache hitCollectorCache = new Cache(
					GlobalCache.HitCollectorCache, 
					100,      //MAX size
					false, 	  //overfolow to disk
					true,     //eternal
					60, 	  //time to live
					30);	  //time to idle
			manager.addCache(hitCollectorCache);
			*/
			Cache filteredDocTreeCache = new Cache(
					GlobalCache.FilteredDocTreeCache, 
					100,      //MAX size
					false, 	  //overfolow to disk
					false,     //eternal
					900, 	  //time to live
					300);	  //time to idle
			manager.addCache(filteredDocTreeCache);

		} catch (CacheException e) {
			//cahce configuration exception;
			logger.error("Cache couldn't be started. Root exception: " + e);
		}
	}
	
	/**
	 * Stop cacheing srvices
	 *
	 */
	synchronized public static void shutdown(){
		try {
			logger.info("Shutting down caching services...");
			CacheManager man = CacheManager.getInstance();
			String cache[] = man.getCacheNames();
			for (int i = 0; i < cache.length; i++) {
				man.removeCache(cache[i]);
			}
			man.shutdown();
		} catch (CacheException e) {
			logger.error("Cache couldn't be stopped. Root exception: " + e);
		}
	}
	
	public GlobalCache(String name){
		try {
			CacheManager manager = CacheManager.getInstance();
			cache = manager.getCache(name);
		} catch (CacheException e) {
			logger.warn("Couldn't get cache for name: " + name + 
					". Root exception: " + e);
		}
		
	}
	
	public Serializable retrieve(Serializable key){
		
		if (cache == null)
			return null;
		
		try {
			Element el = cache.get(key);
			if (el != null){
				return el.getValue();
			}
		} catch (CacheException e) {
			logger.warn("Cache error occured. Root exception: " + e);
		}
		
		return null;
		
	}

	public void store(Serializable key, Serializable value){
		Element el = new Element(key, value);
		cache.put(el);
	}
}

