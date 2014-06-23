package com.apps.twitter.client.model;

public class AppUser {

	private static User appUser;
	
	public static User getAppUser(){
		return appUser;
	}
	
	public static void setAppUser(User user){
		if(appUser == null){
			appUser = user;
		}
	}
	
}
