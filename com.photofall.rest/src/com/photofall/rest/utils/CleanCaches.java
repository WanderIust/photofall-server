package com.photofall.rest.service;

import com.sun.jersey.api.core.InjectParam;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Component
public class CleanCaches {

    @InjectParam
    private GeoStore geoStore;

    private Cache current;

    private ScheduledFuture<?> result;

    private boolean started = false;

    public void start() {
        started = true;
        System.out.println("Welcome to clean caches");
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
        while(true) {
            Runnable command = getNextCache();
            System.out.println("rows: "+ geoStore.getRows()+ " cacheid: "+ current.getCacheId()+ " expiration: "+ current.getExpiration());
            result = scheduler.schedule(command, current.getExpiration(), TimeUnit.SECONDS);
            while (!result.isDone()) {
                try {
                    System.out.println("not done..");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("rows: "+ geoStore.getRows()+" done");
        }
    }
    public void cancel(){
        if(result != null)
            result.cancel(false);
    }
    public long getExpiration(){
        if(current == null)
            return Long.MAX_VALUE;
        return System.currentTimeMillis()/1000+current.getExpiration();
    }
    public Runnable getNextCache(){
        try {
            while(current == null) {
                System.out.println("Searching for cache");
                Thread.sleep(1000);
                current = geoStore.getFirst();
            }
        }catch(InterruptedException | SQLException e){
            e.printStackTrace();
        }
        final Cache finalCurrent = current;
        return () -> {
            try {
                geoStore.deleteData(finalCurrent);
                current = null;
            }catch(SQLException e){
                e.printStackTrace();
            }
        };
    }
    public boolean getStarted(){
        return started;
    }
    public void setGeoStore(GeoStore geoStore) {
        this.geoStore = geoStore;
    }
}
