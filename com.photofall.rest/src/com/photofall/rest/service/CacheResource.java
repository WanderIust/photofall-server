package com.photofall.rest.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


@Path("/user/{userId}/cache/{cacheId}")
public class CacheResource {
	
	CacheService cacheService = new CacheService();
	
	@GET
	@Path("/drop")
	@Produces(MediaType.TEXT_HTML)
	public String returnHello(@PathParam("cacheId")String cacheId,@PathParam("userId")String userId){ 
		//replace this String with a suitable data structure when we have the xml defined better
		File fi = null;
		byte[] fileContent= null;
		ByteBuffer buffer = null;
		try {
			fi = new File("/Users/sgib0001/blackbishop.png");
			fileContent = Files.readAllBytes(fi.toPath());
			buffer = ByteBuffer.wrap(fileContent);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cacheService.dropCache(cacheId,userId,buffer);
	}
	
	@GET
	@Path("/get")
	@Produces(MediaType.TEXT_HTML)
	public String returnTitle(@PathParam("cacheId")String cacheId,@PathParam("userId")String userId){
		return cacheService.getCache(cacheId,userId);
	}
	
}
