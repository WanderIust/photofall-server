package com.photofall.rest.status;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
@Path("/user/{userId}/cache")
public class CacheService {
	
	GeoStore geoStore = new GeoStore();
	
	@POST
	@Path("/drop")
	@Consumes(MediaType.APPLICATION_XML)
	public String returnHello(@PathParam("userId")String userId, String xml){ 
//replace this String with a suitable data structure when we have the xml defined better
		return geoStore.dropCache(userId,xml);
	}
	
	@GET
	@Path("/search")
	@Produces(MediaType.TEXT_HTML)
	public String returnTitle(){
		return "<p>Java Web Service</p>";
	}
}
