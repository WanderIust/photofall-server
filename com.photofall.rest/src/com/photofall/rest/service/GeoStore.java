package com.photofall.rest.service;

import java.nio.ByteBuffer;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.util.LinkedList;

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
	
	String table="\"Caches\"";
	
	public GeoStore() { 
		cluster = Cluster.builder().withPort(9042).addContactPoint("87.198.172.217").build();
		cluster.getConfiguration().getSocketOptions().setConnectTimeoutMillis(100000);
		session = cluster.connect("PhotoFall");
	
		System.out.println("Successfully added yee.....");
	}
     
    	public void populateData(String cacheId, String userId, ByteBuffer xml) throws SQLException {
	    	try{
		    	String data=
			     "BEGIN BATCH \n"+   
			     "APPLY BATCH;";
			    PreparedStatement ps = session.prepare("insert into "+table+" (cacheid, userid, metadata, photo, message) values (?,?,?,?,?);");
			    BoundStatement boundStatement = new BoundStatement(ps);
			    session.execute(  boundStatement.bind( cacheId, userId, null, xml, null));
	    	}catch(SQLException e){
	    		System.out.println("Error while populating data");
	    	}
    	}
	public void deleteData() throws SQLException {
		try{
		     String data= 
		      "BEGIN BATCH \n"+
			  "delete from "+table+" where key='user5' \n"+
			  "delete  category from "+table+" where key='user2' \n"+
			  "APPLY BATCH;";   
		     session.execute(data);
		}catch(SQLException e){
			System.out.println("Error while deleting data");	
		}
	}
    	public void updateData() throws SQLException {
    		try{
		     	String t = "update "+table+" set category='sports', linkcounts=1 where key='user5'";          
		     	session.execute(t);
    		}catch(SQLException e){
    			System.out.println("Error while updating data");	
    		}
    	}
	public void getData(String cacheId, String userId) throws SQLException {
		try{
		     	String t = "SELECT * FROM "+table+" WHERE cacheid='"+cacheId+"' AND userid='"+userId+"'";          
			ResultSet rs =session.execute(t);
		     	LinkedList<Row> rows = (LinkedList<Row>) rs.all();
		     	for(Row r: rows)
		     	{
		    		System.out.println(r.toString());
		     	}
		}catch(SQLException e){
			System.out.println("Error while fetching data");
		}
	}
}
