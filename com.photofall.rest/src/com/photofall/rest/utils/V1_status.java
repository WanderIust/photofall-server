package com.photofall.rest.utils;

import com.photofall.rest.store.UserDataStore;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*---------------------------------------*/
//THIS PATH IS USED FOR METHOD TESTING PURPOSES//
/*----------TESTING ENVIRONMENT----------*/

@Path("/v1/test")
public class V1_status {
	
	private static final String api_version ="00.00.01";
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String returnTitle(){
		return "<p>Java Web Servicess</p>";
	}
	
	@Path("/addusertest")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String addusertest(){
		UserDataStore uds = new UserDataStore();
		try{
			uds.addUser("woodsa22","Aaron","Woods","password");
		}catch(SQLException e){
			System.out.println("OH NO!!");
		}
		return "<p>Add user test</p>";
	}
	
	@Path("/removeusertest")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String removeusertest(){
		UserDataStore uds = new UserDataStore();
		try{
			uds.removeUser("woodsa22");
		}catch(SQLException e){
			System.out.println("OH NO!!");
		}
		return "<p>remove user test</p>";
	}
	
	@Path("/changepasswordtest")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String changepass(){
		UserDataStore uds = new UserDataStore();
		try{
			uds.changePassword("password", "password2", "woodsa22");
		}catch(SQLException e){
			System.out.println("OH NO!!");
		}
		return "<p>change password test</p>";
	}
	
	@Path("/updateusertest")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String updateuser(){
		UserDataStore uds = new UserDataStore();
		try{
			uds.updateUserData("Aaron","Woods","woodsa22");
		}catch(SQLException e){
			System.out.println("OH NO!!");
		}
		return "<p>update user test</p>";
	}
	
	@Path("/fetchuserdatatest")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String fetchuser(){
		UserDataStore uds = new UserDataStore();
		List<String> userData = new ArrayList<>();
		try{
			userData = uds.retrieveUserData("woodsa22");
		}catch(SQLException e){
			System.out.println("OH NO!!");
		}
		return "<p>fetch user information test</p>";
	}
	
}
