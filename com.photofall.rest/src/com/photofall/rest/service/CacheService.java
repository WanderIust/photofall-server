package com.photofall.rest.service;

import java.nio.ByteBuffer;
import java.sql.SQLException;

public class CacheService {

	GeoStore geoStore= new GeoStore();
	
	String dropCache(String cacheId, String userId, ByteBuffer xml){
		try{
			geoStore.populateData(cacheId, userId, xml);
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		return "hello";
	}
	String getCache(String cacheId, String userId)
	{
		try{
			geoStore.getData(cacheId, userId);
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		return "done";
	}
}
