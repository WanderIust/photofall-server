package com.photofall.rest.service;

import java.nio.ByteBuffer;

public class Cache {
	int userId;
	int cacheId;
	int expiration;
	String message;
	ByteBuffer photo;
	
	Cache(int userId, int cacheId, int expiration, String message, ByteBuffer photo){
		this.userId= userId;
		this.cacheId= cacheId;
		this.expiration= expiration;
		this.message= message;
		this.photo = photo;
	}
	int getUserId(){
		return userId;
	}
	int getCacheId(){
		return cacheId;
	}
	int getExpiration(){
		return expiration;
	}
	String getMessage(){
		return message;
	}
	ByteBuffer getPhoto(){
		return photo;
	}
}
