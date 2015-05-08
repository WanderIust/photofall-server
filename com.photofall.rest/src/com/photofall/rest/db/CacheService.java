package com.photofall.rest.db;

import java.nio.ByteBuffer;
import java.sql.SQLException;

import javax.ws.rs.core.Response;	

public class CacheService {

	GeoStore geoStore= new GeoStore();
	
	public Response dropCache(String cacheId, String userId, ByteBuffer xml){
		try{
			return(Response.ok(geoStore.populateData(cacheId, userId, xml)).build());
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	public Response getCache(String cacheId, String userId)
	{
		try{
			return(Response.ok(geoStore.getData(cacheId, userId)).build());
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
		
	}
}
