package com.photofall.rest.service;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.codehaus.jettison.json.JSONArray;	
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.ResultSet;
import com.photofall.rest.service.ToJson;

public class GeoStore 
{
	private Cluster cluster;
	private Session session;

	String table="caches";
	
	public GeoStore() { 
		cluster = Cluster.builder().addContactPoints("127.0.0.1").build();
		cluster.getConfiguration().getSocketOptions().setConnectTimeoutMillis(100000);
		session = cluster.connect();
		session.execute("USE myks;");
		System.out.println("Successfully added yee.....");
	}
     
    public String populateData(String cacheId, String userId, ByteBuffer xml) throws SQLException {
		System.out.println("Attempting to add cache");
	    PreparedStatement ps = session.prepare("insert into "+table+" (cacheid, userid, metadata, photo, message) values (?,?,?,?,?);");
	    BoundStatement boundStatement = new BoundStatement(ps);
	    session.execute(boundStatement.bind( cacheId, userId, null, xml, null));
	    System.out.println("added cache");
	    return "<p>DONE</p>";
    }
	public void deleteData() throws SQLException {
	     String data= 
	      "BEGIN BATCH \n"+
		  "delete from "+table+" where key='user5' \n"+
		  "delete  category from "+table+" where key='user2' \n"+
		  "APPLY BATCH;";   
	     session.execute(data);
	}
    public void updateData() throws SQLException {
	     String t = "update "+table+" set category='sports', linkcounts=1 where key='user5'";          
	     session.execute(t);
    }
     public byte[] getData(String cacheId, String userId) throws SQLException {
	     String t = "SELECT * FROM "+table+" WHERE cacheid='"+cacheId+"' AND userid='"+userId+"'";          
	     ResultSet rs =session.execute(t);
	     //ToJson converter = new ToJson();
	     //JSONArray json = new JSONArray();
	     //json = converter.toJSONArray(rs);
	     ByteBuffer image= null;
	     for(Row r: rs.all())
	     {
	    	 System.out.println(r.toString());
	    	 image = (r.getBytes("photo"));// put this in to generate image locally
	    	 System.out.println("Image: "+image);
	     }
	     return(image.array());//put this in to generate image locally*/
	    // return null;
     }
     public void close(){
    	 session.close();
    	 cluster.close();
     }
}