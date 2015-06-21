package com.photofall.rest.service;

import com.sun.jersey.api.core.InjectParam;

import javax.ws.rs.core.Response;
import java.nio.ByteBuffer;
import java.sql.SQLException;

public class CacheService {

    @InjectParam
	GeoStore geoStore;

    @InjectParam
    CleanCaches cleanCaches;

    private boolean started = false;

	public Response dropCache(String cacheId, String userId, int expiration, ByteBuffer xml){
		System.out.println("println");
        if(!started) {
            cleanCaches.start();
            started = true;
        }
		try{
             if(System.currentTimeMillis()/1000+expiration<cleanCaches.getExpiration())
                cleanCaches.cancel();
			return(Response.ok(geoStore.populateData(cacheId, userId, expiration, xml)).build());
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	public Response getCache(String cacheId, String userId){
		try{
			return(Response.ok(geoStore.getData(cacheId, userId)).build());
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
		
	}
    public Response deleteCache(String cacheId, String userId){
        return(Response.ok(geoStore.deleteCache(cacheId, userId)).build());
    }
    public void setGeoStore(GeoStore geoStore){
        this.geoStore = geoStore;
    }
    public void setCleanCaches(CleanCaches cleanCaches){
        this.cleanCaches = cleanCaches;
    }
    public Response getFirst(String cacheId, String userId){
        String response = "hello";
        System.out.println("THE RESPONSE: " + response);
        return(Response.ok(response).build());
    }

}
