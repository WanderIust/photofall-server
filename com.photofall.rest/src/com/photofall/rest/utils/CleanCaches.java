package com.photofall.rest.service;

import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class CleanCaches {
	
	ScheduledFuture<?> scheduledFuture;
	ScheduledExecutorService scheduledExecutorService;
	GeoStore geoStore = new GeoStore();
	Cache nextCache;
	
	final Runnable cleaner = new Runnable(){
		public void run(){
			//delete cache
			try {
				geoStore.deleteData(nextCache);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("deleted cache");
			next();
		}
	};
	
	CleanCaches(Cache cache){
		this.nextCache=cache;
		scheduledExecutorService = Executors.newScheduledThreadPool(1);
		scheduledFuture = scheduledExecutorService.schedule(cleaner,cache.getExpiration(),TimeUnit.SECONDS);

	}
	void updateCache(Cache cache){
		scheduledFuture.cancel(false);
		this.nextCache= cache;
		scheduledFuture =scheduledExecutorService.schedule(cleaner,cache.getExpiration(),TimeUnit.SECONDS);
	}
	void next(){
		//get next cache from db & figure out expirationTime. Then start scheduler again.
	//	nextCache = db.getCache();
		//nextCache = Cache.toCache(geoStore.getFirst());
		//scheduledFuture = scheduledExecutorService.schedule(cleaner,cache.getExpiration(),TimeUnit.SECONDS);
	}
}
