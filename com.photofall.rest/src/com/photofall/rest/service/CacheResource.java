package com.photofall.rest.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.photofall.rest.db.CacheService;



@Path("/user/{userId}/cache/{cacheId}")
public class CacheResource {
	
	CacheService cacheService = new CacheService();
	
	@GET
	@Path("/drop")
	@Produces(MediaType.APPLICATION_JSON)
	public Response returnHello(@PathParam("cacheId")String cacheId,@PathParam("userId")String userId){ 
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
	@Produces(MediaType.APPLICATION_JSON)
	public Response returnTitle(@PathParam("cacheId")String cacheId,@PathParam("userId")String userId){
		File fi = null;
		byte[] fileContent= null;
		ByteBuffer buffer = null;
		String imageHTML= "<p><img src=\"/photofall/image.png\"></p>";
		return cacheService.getCache(cacheId,userId);
		/*try {

			String image =cacheService.getCache(cacheId,userId);
			byte[] imageWithoutCrap= Arrays.copyOfRange(image, 120, image.length);
	
			if(imageWithoutCrap != null) {
			    InputStream in = new ByteArrayInputStream(imageWithoutCrap);
			    BufferedImage bImageFromConvert = ImageIO.read(in);

			    if(bImageFromConvert != null) {
			        ImageIO.write(bImageFromConvert, "png", new File(
							"/Users/sgib0001/Documents/wanderlust/com.photofall.rest/WebContent/image.png"));
			    }
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}
	
}
