package com.photofall.rest.service;

import java.nio.ByteBuffer;
import java.sql.SQLException;

public class CacheService {

	GeoStore geoStore= new GeoStore();
	
	String dropCache(String cacheId, String userId, ByteBuffer xml){
		try{
			return geoStore.populateData(cacheId, userId, xml);
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	byte[] getCache(String cacheId, String userId)
	{
		byte[] image=null;
		try{
			image=  geoStore.getData(cacheId, userId);
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		return image;
		
	}
}
