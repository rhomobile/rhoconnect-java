package com.msi.rhoconnect.api;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class PluginResource {
	// Push new objects or object updates to RhoConnect
	public static ClientResponse pushObjects(String url, String token, String userId, String sourceName, JSONObject data) {
		Client client = Client.create();
		// POST /app/v1/:source_name/push_objects
		String path = String.format("%s/app/v1/%s/push_objects", url, sourceName);
		WebResource webResource = client.resource(path);
		
		JSONObject obj = new JSONObject();
		obj.put("objects", data);
		obj.put("user_id", userId);
		String content = JSONObject.toJSONString(obj);
		
		ClientResponse response = webResource.type("application/json")
			.header("X-RhoConnect-API-TOKEN", token)
			.post(ClientResponse.class, content);
		
		return response;
	}

	// Delete objects from RhoConnect
	public static ClientResponse deleteObjects(String url, String token, String userId, String sourceName, JSONArray objectIds) {
		Client client = Client.create();
		// POST /app/v1/:source_name/push_deletes
		String path = String.format("%s/app/v1/%s/push_deletes", url, sourceName);
		WebResource webResource = client.resource(path);
		
		JSONObject obj = new JSONObject();
		obj.put("objects", objectIds);
		obj.put("user_id", userId);
		String content = JSONObject.toJSONString(obj);
		
		ClientResponse response = webResource.type("application/json")
			.header("X-RhoConnect-API-TOKEN", token)
			.post(ClientResponse.class, content);
		
		return response;
	}
	
	// POST /app/v1/:source_name/fast_insert
	// POST /app/v1/:source_name/fast_update
	// POST /app/v1/:source_name/fast_delete
	
}
