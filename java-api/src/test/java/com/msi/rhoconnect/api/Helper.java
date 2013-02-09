package com.msi.rhoconnect.api;

import org.json.simple.JSONObject;

import com.msi.rhoconnect.api.SystemResource;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class Helper {
	static String getToken(String url) {
		ClientResponse response = SystemResource.login(url, "");
		return response.getEntity(String.class);
	}
	
	static void reset(String url, String token) {
		SystemResource.reset(url, token);
	}
	
	static void clientLogin(String url, String token, String userId, String password) {
		Client client = Client.create();
		String path = String.format("%s/rc/v1/app/login", url);
		WebResource webResource = client.resource(path);
		
		String credentials = 
				String.format("{\"login\":\"%s\",\"password\":\"%s\"}", userId, password);		
		ClientResponse response = webResource.type("application/json")
			.header("X-RhoConnect-API-TOKEN", token)
		    .post(ClientResponse.class, credentials);
		
//		if (response.getStatus() == 200)
//			System.out.println("Client " + userId + " logged in: " + response.getStatus());		
	}

}
