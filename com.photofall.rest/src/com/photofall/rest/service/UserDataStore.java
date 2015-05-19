package com.photofall.rest.service;

import java.util.*;


import org.codehaus.jettison.json.JSONArray;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class UserDataStore {
	private Cluster cluster;
	private Session session;
	String keyspace = "userdata";
	String table = "UserDataTable";
	
	public UserDataStore() { 
		cluster = Cluster.builder().addContactPoints("127.0.0.1").build();
		cluster.getConfiguration().getSocketOptions().setConnectTimeoutMillis(100000);
		session = cluster.connect(keyspace);
		System.out.println("Successfully added yee.....");
	}
	
	public String displayTableData() { //Visualise 
		String query = "SELECT * FROM UserDataTable;";          
	    ResultSet rs =session.execute(query);
	    Row r = rs.one();
	    while(r != null){ //work on this for testing, alternative select * in cqlsh
	    	System.out.println("Listing id's... "+ r.getString("userId"));
	    	r= rs.one();
	    }
	    close();
	    return(toJSON(rs));
	}
     
    public String populateData() { //testing purposes, adding users
		
    	System.out.println("Attempting to add user");
	    PreparedStatement ps = session.prepare("INSERT INTO "+table+" (userId) VALUES (?);");
	    BoundStatement boundStatement = new BoundStatement(ps);
	    ResultSet rs =session.execute(boundStatement.bind(2));
	    close();
	    System.out.println("Added user with id 1");
	    return toJSON(rs);
    }
    
    public int addUser(String username, String fName, String lName, String password){ //String username, String fName, String lName, String password

    	System.out.println("attempting to check username uniqueness");
    	PreparedStatement ps = session.prepare("SELECT * FROM UserDataTable WHERE username = ?;");
    	BoundStatement bs = new BoundStatement(ps);
    	ResultSet rs = session.execute(bs.bind(username));
    	Row r =rs.one();
    	if(r != null){
    		System.out.println("Found occurence of username");
    		close();
    		return 0; //error code
    	}
    	else{ //add user to UserDataTable
    		System.out.println("No occurence of username - Attempting to add new user");
    		SecurityHandler sh = new SecurityHandler();
    		String encPass = sh.encrypt(password);
    		PreparedStatement ps2 = session.prepare("INSERT INTO "+table+" (username, fName, lName, password) VALUES (?,?,?,?);");
    		BoundStatement bs2 = new BoundStatement(ps2);
    		ResultSet rs2 =session.execute(bs2.bind(username, fName, lName, encPass));
    		close();
    		System.out.println("Added user with user: "+ username);
    		System.out.println("User's encrypted pass is: "+ encPass);
    		return 1; //success code
    	}
    }
    
    public void removeUser(String username){
    	System.out.println("attempting remove user");
    	PreparedStatement ps = session.prepare("DELETE FROM "+table+" WHERE username = ?;");
    	BoundStatement bs = new BoundStatement(ps);
    	ResultSet rs = session.execute(bs.bind(username));
    }
    
    public void changePassword(String currentPass, String newPass, int userId){
    	System.out.println("attempting to overwrite password");
    	PreparedStatement ps = session.prepare("SELECT * FROM UserDataTable WHERE userId = ?;");
    	BoundStatement bs = new BoundStatement(ps);
    	ResultSet rs = session.execute(bs.bind(userId));
    	Row r =rs.one();
    	if(r != null){
    		SecurityHandler sh = new SecurityHandler();
    		String storedPass = r.getString("userPass");
    		String encCurrentPass = sh.encrypt(currentPass);
    		if(storedPass.equals(encCurrentPass)){ //passwords matched. Override password
    			String newEncPass = sh.encrypt(newPass);
    			System.out.println("Passwords matched. Overriding password.");
    	    	PreparedStatement ps2 = session.prepare("UPDATE "+table+" SET password = ? WHERE userId = ?;");
    	    	BoundStatement bs2 = new BoundStatement(ps2);
    	    	ResultSet rs2 = session.execute(bs2.bind(newEncPass, userId));
    	    	System.out.println("Password updated");
    		}
    		else{
    			//passwords didn't match. Do not override stored pass
    			System.out.println("Passwords did not match, aborting password change, try input correct pass");
    		}
    		close();
    	}
    	//encrypt current pass and compare to stored encrypted pass
    	//if same, overide old password with new encrypted pass
    }
    
    /*
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
	
	     //ByteBuffer image= null;
	     //for(Row r: rs.all())
	     //{
	    //	 System.out.println(r.toString());
	    //	 image = (r.getBytes("photo"));// put this in to generate image locally
	    //	 System.out.println("Image: "+image);
	    // }
	    close();
	    return(toJSON(rs));
	}*/
	
    public void updateUserData(String firstN, String lastN, int userId){
    	System.out.println("Updating user information");
    	PreparedStatement ps2 = session.prepare("UPDATE "+table+" SET fName = ?, lName = ? WHERE userId = ?;");
    	BoundStatement bs2 = new BoundStatement(ps2);
    	ResultSet rs2 = session.execute(bs2.bind(firstN, lastN, userId));
    }
    
    //or have methods to update specific fields in UserDataTable
    
    public List<Object> retrieveUserData(String username){
    	List<Object> userData = new ArrayList<>();
    	PreparedStatement ps = session.prepare("SELECT * FROM UserDataTable WHERE username = ?;");
    	BoundStatement bs = new BoundStatement(ps);
    	ResultSet rs = session.execute(bs.bind(username));
	    Row r = rs.one();
	    if(r != null){ //work on this for testing, alternative select * in cqlsh
	    	userData.add(r.getString("username")); //username
	    	userData.add(r.getString("fName")); //firstName
	    	userData.add(r.getString("lName")); //lastName
	    }
	    else{
	    	System.out.println("No user present with username: "+ username);
	    }
	    close();
	    return userData;
	    
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
