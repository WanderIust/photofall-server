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

public class GeoStore 
{
	private Cluster cluster;
	private Session session;

	String table="caches";
	
	public GeoStore() { 
		cluster = Cluster.builder().addContactPoints("127.0.0.1").build();
		cluster.getConfiguration().getSocketOptions().setConnectTimeoutMillis(100000);
		session = cluster.connect();
		session.execute("USE photofall;");
		System.out.println("Successfully added yee.....");
	}
     
    public String populateData(String cacheId, String userId, ByteBuffer xml) throws SQLException {
		
    	System.out.println("Attempting to add cache");
	    PreparedStatement ps = session.prepare("insert into "+table+" (cacheid, userid, expiration, message, photo) values (?,?,?,?,?);");
	    BoundStatement boundStatement = new BoundStatement(ps);
	    ResultSet rs =session.execute(boundStatement.bind( cacheId, userId, (long)10000, "BeboStunazxoxoxo",xml));
	    close();
	    return toJSON(rs);
    }
	public void deleteData(Cache cache) throws SQLException {
		PreparedStatement ps = session.prepare(
				"delete from "+table+" where cacheid='?' and userid='?';" 
				);
	    BoundStatement boundStatement = new BoundStatement(ps);
	    ResultSet rs =session.execute(boundStatement.bind( cache.getCacheId(), cache.getUserId()));
	    close();
	}
	public void updateData() throws SQLException {  
	    String t = "update "+table+" set category='sports', linkcounts=1 where key='user5'";          
	    session.execute(t);
	}
	public String getFirst() throws SQLException { 
		PreparedStatement ps = session.prepare(
				"select * from "+table+" LIMIT 1;" 
				);
	    BoundStatement boundStatement = new BoundStatement(ps);
	    ResultSet rs =session.execute(boundStatement);
	    close();
	    return(toJSON(rs));
	}
	public String getData(String cacheId, String userId) throws SQLException {
	    String t = "SELECT * FROM "+table+" WHERE cacheid='"+cacheId+"' AND userid='"+userId+"'";          
	    ResultSet rs =session.execute(t);
	
	     /*ByteBuffer image= null;
	     for(Row r: rs.all())
	     {
	    	 System.out.println(r.toString());
	    	 image = (r.getBytes("photo"));// put this in to generate image locally
	    	 System.out.println("Image: "+image);
	     }*/
	    close();
	    return(toJSON(rs));
	}
	public void close(){
		session.close();
		cluster.close();
	}
	public String toJSON(ResultSet rs){
	    ToJson converter = new ToJson();
	    JSONArray json = new JSONArray();
	    json = converter.toJSONArray(rs);
	    return(json.toString());
	}

}