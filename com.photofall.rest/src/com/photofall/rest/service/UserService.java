package com.photofall.rest.service;

import java.sql.SQLException;

import javax.ws.rs.core.Response;	

public class UserService {
	UserDataStore userStore = new UserDataStore();
	
	public Response newUser(String username, String fName, String lName, String password){
		System.out.println("UserService adding user");
		try{
			return(Response.ok(userStore.addUser(username, fName, lName, password)).build());
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	public Response deleteUser(String username){
		System.out.println("UserService removing user");
		try{
			return(Response.ok(userStore.removeUser(username)).build());
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	public Response modifyPassword(String currentPass, String newPass, String username){
		System.out.println("UserService modifying pass");
		try{
			return(Response.ok(userStore.changePassword(currentPass, newPass, username)).build());
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	public Response updateUser(String firstN, String lastN, String username){
		System.out.println("UserService updating user");
		try{
			return(Response.ok(userStore.updateUserData(firstN, lastN, username)).build());
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	public Response retrieveUser(String username){
		System.out.println("UserService fetching user data");
		try{
			return(Response.ok(userStore.retrieveUserData(username)).build());
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
}
